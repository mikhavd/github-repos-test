package m13.retrofittest.main.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import m13.retrofittest.main.database.GenericRepository;
import m13.retrofittest.main.database.RepoDatabase;
import m13.retrofittest.main.repos.Repository;

public class RepositoriesViewModel extends AndroidViewModel {

    //private RepositoriesRepository repositoriesRepository;
    private GenericRepository<Repository> repoRepository;
    private LiveData<List<Repository>> allRepositories;

    public RepositoriesViewModel(@NonNull Application application) {
        super(application);
        repoRepository = new GenericRepository<>(application, RepoDatabase::repoDao);
        allRepositories = repoRepository.getAllItems();
    }

    public LiveData<List<Repository>> getAllRepositories(){
        return allRepositories;
    }

    public void insert(Repository repository){
        repoRepository.insert(repository);
    }
}
