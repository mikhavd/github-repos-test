package m13.retrofittest.main.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import m13.retrofittest.main.repos.AbstractDao;

public abstract class AbstractRepository<T, TDao extends AbstractDao<T>> {

    private TDao tDao;
    private LiveData<List<T>> allItems;

    public AbstractRepository(Application application) {
        RepoDatabase db = RepoDatabase.getDatabase(application);
        tDao = getTDao(db);
        allItems = tDao.getAllItems();
    }

    /*
    этот метод возвращает Dao,
    как RepoDatabase возвращает repositoryDao сейчас
     */
    protected abstract TDao getTDao(RepoDatabase db);

    public void insert(T item){
        insertAsyncTask<T, TDao> insertAsyncTask = getInsertAsyncTask();
        insertAsyncTask.execute(item);
    }

    abstract insertAsyncTask<T,TDao> getInsertAsyncTask();


    //в оригинале private static class
    private static class insertAsyncTask<T, TDao extends AbstractDao<T>> extends AsyncTask<T, Void, Void> {

        TDao asyncTaskDao;

        insertAsyncTask(TDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final T... items) {
            asyncTaskDao.insert(items[0]);
            return null;
        }
    }

}
