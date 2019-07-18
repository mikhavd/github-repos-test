package m13.retrofittest.main.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import m13.retrofittest.main.repos.AbstractDao;

public abstract class AbstractRepository<T, TDao extends AbstractDao<T>> {

    protected final RepoDatabase db;
    protected TDao tDao;
    protected LiveData<List<T>> allItems;

    AbstractRepository(Application application) {
        db = RepoDatabase.getDatabase(application);
        tDao = getTDao(db);
        allItems = tDao.getAllItems();
    }

    public LiveData<List<T>> getAllItems() {
        return allItems;
    }

    /*
        этот метод возвращает Dao,
        как RepoDatabase возвращает repositoryDao сейчас
         */
    protected abstract TDao getTDao(RepoDatabase db);

    public void insert(T item){
        insertAsyncTask<T> insertAsyncTask = getInsertAsyncTask(tDao);
        insertAsyncTask.execute(item);
    }

    private insertAsyncTask<T> getInsertAsyncTask(TDao tDao){
        return new insertAsyncTask<>(tDao);
    };


    //в оригинале private static class
    protected static class insertAsyncTask<T> extends AsyncTask<T, Void, Void> {

        AbstractDao<T> asyncTaskDao;

        insertAsyncTask(AbstractDao<T> dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final T... items) {
            asyncTaskDao.insert(items[0]);
            return null;
        }
    }

}
