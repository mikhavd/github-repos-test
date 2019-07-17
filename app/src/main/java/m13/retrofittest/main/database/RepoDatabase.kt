package m13.retrofittest.main.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import m13.retrofittest.main.repos.Repository
import m13.retrofittest.main.repos.RepositoryDao


@Database(entities = [Repository::class], version = 1)
public abstract class RepoDatabase : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao

    //Make the WordRoomDatabase a singleton
    // to prevent having multiple instances of the database opened at the same time.
    companion object {
        @Volatile
        private var INSTANCE: RepoDatabase? = null

        fun getDatabase(context: Context): RepoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                //Add the code to get a database.
                // This code uses Room's database builder
                // to create a RoomDatabase object
                // in the application context
                // from the WordRoomDatabase class and names it "word_database".
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        RepoDatabase::class.java,
                        "repo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

