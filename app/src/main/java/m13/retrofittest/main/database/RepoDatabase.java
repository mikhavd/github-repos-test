package m13.retrofittest.main.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import m13.retrofittest.main.repos.Repository;
import m13.retrofittest.main.repos.RepositoryDao;

@Database(entities = {Repository.class}, version = 1)
public abstract class RepoDatabase extends RoomDatabase {
    public abstract RepositoryDao repositoryDao();

    private static volatile RepoDatabase INSTANCE;

    static RepoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RepoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RepoDatabase.class, "repository_database")
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
}
