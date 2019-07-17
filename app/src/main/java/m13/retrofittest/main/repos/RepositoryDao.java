package m13.retrofittest.main.repos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RepositoryDao {
    @Query("SELECT * FROM repository")
    LiveData<List<Repository>> getAll();

    @Query("SELECT * FROM repository WHERE uid IN (:repositoryIds)")
    List<Repository> loadAllByIds(int[] repositoryIds);

    @Query("SELECT * FROM repository WHERE full_name LIKE :fullName LIMIT 1")
    Repository findByName(String fullName);

    @Insert
    void insertAll(Repository... repositories);

    @Delete
    void delete(Repository repository);
}

