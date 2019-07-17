package m13.retrofittest.main.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import m13.retrofittest.main.repos.Repository;
import m13.retrofittest.main.repos.RepositoryDao;

public class RepoRepository {

    private RepositoryDao repositoryDao;
    private LiveData<List<Repository>> allRepositories;

    public RepoRepository(Application application) {
        RepoDatabase db = RepoDatabase.getDatabase(application);
        repositoryDao = db.repositoryDao();
        allRepositories = repositoryDao.getAllRepositories();
    }

    public LiveData<List<Repository>> getAllRepositories(){
        return allRepositories;
    }

    public void insert(Repository repository){
        new insertAsyncTask(repositoryDao).execute(repository);
    }

    private static class insertAsyncTask extends AsyncTask<Repository, Void, Void> {

        private RepositoryDao asyncTaskDao;

        insertAsyncTask(RepositoryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Repository... repositories) {
            asyncTaskDao.insert(repositories[0]);
            return null;
        }
    }
}
