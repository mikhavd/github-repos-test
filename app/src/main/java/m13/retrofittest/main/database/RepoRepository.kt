package m13.retrofittest.main.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import m13.retrofittest.main.repos.Repository
import m13.retrofittest.main.repos.RepositoryDao

class RepoRepository(private val repositoryDao: RepositoryDao){
    val allRepositories: LiveData<List<Repository>> = repositoryDao.getAll();

    @WorkerThread
    suspend fun insert(repository: Repository){
        repositoryDao.insertAll(repository)
    }
}
