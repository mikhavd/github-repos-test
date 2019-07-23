package m13.retrofittest.main.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class GenericRepository<T> {

    private GenericDao<T> tDao;
    private LiveData<List<T>> allItems;

    public GenericRepository(Application application, TDaoProvider<T> tDaoProvider) {
        RepoDatabase db = RepoDatabase.getDatabase(application);
        tDao = tDaoProvider.getTDao(db);
        allItems = tDao.getAllItems();
    }

    public LiveData<List<T>> getAllItems() {
        return allItems;
    }

    @SafeVarargs
    public final void insert(T... items){
        Thread insertThread = new Thread(new InsertRunnable(items));
        insertThread.start();
    }

    private final class InsertRunnable implements Runnable {

        private final T[] items;

        @SafeVarargs
        InsertRunnable(T... items){
            this.items = items;
        }

        @Override
        final public void run() {
            tDao.insert(items);
        }
    }
}
