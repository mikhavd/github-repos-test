package m13.retrofittest.main.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import m13.retrofittest.main.repos.Repository;
import m13.retrofittest.main.repos.RepositoryDao;

@Database(entities = {Repository.class}, version =3)
public abstract class RepoDatabase extends RoomDatabase {
    public abstract RepositoryDao repositoryDao();

    private static volatile RepoDatabase INSTANCE;

    static RepoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RepoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RepoDatabase.class, "repository_database")
                            .addMigrations(
                                    MIGRATION_1_2,
                                    MIGRATION_2_3)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final RepositoryDao repositoryDao;

        PopulateDbAsync(RepoDatabase db) {
            repositoryDao = db.repositoryDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //repositoryDao.deleteAll();
            //Repository repository = null; //new Repository("Hello");
            //repositoryDao.insert(repository);
            return null;
        }
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Repository " +
                    "RENAME TO Repositories");
            database.execSQL("ALTER TABLE Repositories "+ " ADD COLUMN server_id INTEGER");
            //database.execSQL("CREATE INDEX index_d_course_id_studio ON  d_course(id, studio)")
            database.execSQL("CREATE UNIQUE INDEX index_repositories_uid_server_id on Repositories(uid, server_id)");
            //database.execSQL("ALTER TABLE Repos "+ " ADD COLUMN pub_year INTEGER");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //https://stackoverflow.com/questions/3620828/sqlite-select-where-empty
            database.execSQL("DELETE from Repositories where server_Id is null or server_Id = ''");
        }
    };

}
