package m13.retrofittest.main.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import m13.retrofittest.main.repos.Repository;
import m13.retrofittest.main.repos.RepositoryDao;


@Database(entities = {Repository.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RepositoryDao repositoryDao();
}

