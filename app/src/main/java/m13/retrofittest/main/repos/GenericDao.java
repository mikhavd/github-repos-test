package m13.retrofittest.main.repos;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

public interface GenericDao<T> {

    LiveData<List<T>> getAllItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T... item);

    @Update
    void update(T entity);

    void delete(T item);

    void deleteAll();
}
