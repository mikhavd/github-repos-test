package m13.retrofittest.main.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import m13.retrofittest.main.repos.Repozitory;

@Dao
public interface RepoDao extends GenericDao<Repozitory> {

    @Override
    @Query("SELECT * FROM repositories")
    LiveData<List<Repozitory>> getAllItems();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Repozitory... item);

    @Override
    @Delete
    void delete(Repozitory item);

    @Query("DELETE FROM repositories")
    void deleteAll();
}
