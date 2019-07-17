package m13.retrofittest.main.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import m13.retrofittest.main.database.RepoRepository;
import m13.retrofittest.main.repos.Repository;

public class RepositoriesViewModel extends AndroidViewModel {

    private RepoRepository repoRepository;
    private LiveData<List<Repository>> allRepositories;

    public RepositoriesViewModel(@NonNull Application application) {
        super(application);
        repoRepository = new RepoRepository(application);
        allRepositories = repoRepository.getAllRepositories();
    }

    public LiveData<List<Repository>> getAllRepositories(){
        return allRepositories;
    }

    public void insert(Repository repository){
        repoRepository.insert(repository);
    }
}
