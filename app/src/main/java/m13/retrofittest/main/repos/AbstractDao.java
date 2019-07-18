package m13.retrofittest.main.repos;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface AbstractDao<T> {

    LiveData<List<T>> getAllItems();

    //List<T> loadAllByIds(int[] repositoryIds);

    void insert(T... item);

    void delete(T item);

    void deleteAll();
}
