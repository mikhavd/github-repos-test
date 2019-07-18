package m13.retrofittest.main.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import m13.retrofittest.main.database.RepoRepositories;
import m13.retrofittest.main.repos.Repository;

public class RepositoriesViewModel extends AndroidViewModel {

    //private RepositoriesRepository repositoriesRepository;
    private RepoRepositories repoRepositories;
    private LiveData<List<Repository>> allRepositories;

    public RepositoriesViewModel(@NonNull Application application) {
        super(application);
        repoRepositories = new RepoRepositories(application);
        allRepositories = repoRepositories.getAllItems();
    }

    public LiveData<List<Repository>> getAllRepositories(){
        return allRepositories;
    }

    public void insert(Repository repository){
        repoRepositories.insert(repository);
    }
}
