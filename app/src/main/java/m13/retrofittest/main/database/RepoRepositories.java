package m13.retrofittest.main.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import m13.retrofittest.main.repos.AbstractDao;
import m13.retrofittest.main.repos.Repository;

public class RepoRepositories extends AbstractRepository<Repository, RepoRepositories.RepoDao> {

    public RepoRepositories(Application application) {
        super(application);
    }

    @Override
    protected RepoRepositories.RepoDao getTDao(RepoDatabase db) {
        return db.repoDao();
    }

    @Dao
    public interface RepoDao extends AbstractDao<Repository> {

        @Override
        @Query("SELECT * FROM repositories")
        LiveData<List<Repository>> getAllItems();

        @Override
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Repository... item);

        @Override
        @Delete
        void delete(Repository item);

        @Query("DELETE FROM repositories")
        void deleteAll();
    }

}
