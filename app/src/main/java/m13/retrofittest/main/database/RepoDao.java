package m13.retrofittest.main.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import m13.retrofittest.main.repos.GenericDao;
import m13.retrofittest.main.repos.Repository;

@Dao
public interface RepoDao extends GenericDao<Repository> {

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
