package m13.retrofittest.main.database

import androidx.room.Database
import androidx.room.RoomDatabase

import m13.retrofittest.main.api.repos.Repository
import m13.retrofittest.main.api.repos.RepositoryDao

@Database(entities = [Repository::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): RepositoryDao
}