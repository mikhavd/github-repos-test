package m13.retrofittest.main.api.repos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RepositoryDao{
    @Query("Select * from Repository")
    fun getAll(): List<Repository>

    @Query("Select * From Repository where uid in (:userIds)")
    fun loadAllByIds(userIds: IntArray):List<Repository>

    /*
     @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
           "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User
     */

    @Insert
    fun insertAll(vararg repositories : Repository)

    @Delete
    fun delete(repository: Repository)
}