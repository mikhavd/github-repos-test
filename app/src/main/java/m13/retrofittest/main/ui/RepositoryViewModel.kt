package m13.retrofittest.main.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import m13.retrofittest.main.database.RepoDatabase
import m13.retrofittest.main.database.RepoRepository
import m13.retrofittest.main.repos.Repository

class RepositoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repoRepository: RepoRepository

    val allRepos: LiveData<List<Repository>>

    init {
        val repositoryDao = RepoDatabase.getDatabase(application).repositoryDao();
        repoRepository = RepoRepository(repositoryDao)
        allRepos = repoRepository.allRepositories
    }

    //todo using Kotlin Coroutines
    // https://codelabs.developers.google.com/codelabs/kotlin-coroutines/index.html?index=..%2F..index#4
    fun insert(repository: Repository) = viewScopeModel.launch(Dispatchers.IO) {
        repoRepository.insert(repository)
    }

}