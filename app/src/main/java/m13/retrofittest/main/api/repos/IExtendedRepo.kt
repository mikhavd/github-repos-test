package m13.retrofittest.main.api.repos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
interface IExtendedRepo {

    val uid: Int

    val contributorsNumber: Int?

    val name: String

    val stargazersCount: Int

    val repoInfo: String
}
