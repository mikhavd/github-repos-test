package m13.retrofittest.main.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import m13.retrofittest.main.database.GenericRepository;
import m13.retrofittest.main.database.TDaoProvider;

public abstract class GenericViewModel<T> extends AndroidViewModel {

    GenericRepository<T> genericRepository;
    private LiveData<List<T>> allItems;

    GenericViewModel(@NonNull Application application) {
        super(application);
        genericRepository = new GenericRepository<T>(application, getDaoProvider());
        allItems = genericRepository.getAllItems();
        }

    abstract TDaoProvider<T> getDaoProvider();


    public final LiveData<List<T>> getAllItems() {
        return allItems;
    }

    public abstract void insert(T item);


}

