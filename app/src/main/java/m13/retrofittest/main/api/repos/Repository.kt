package m13.retrofittest.main.api.repos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import m13.retrofittest.main.api.retrofitgenerated.repos.Repo

@Entity
class Repository(repo: Repo,
        //private final Repo repo;

                 @field:ColumnInfo(name = "contributors_number")
                 override val contributorsNumber: Int?) : IExtendedRepo {

    @PrimaryKey
    override val uid: Int

    @ColumnInfo(name = "name")
    override val name: String

    @ColumnInfo(name = "stargazersCount")
    override val stargazersCount: Int

    @ColumnInfo(name = "fullName")
    private val fullName: String

    @ColumnInfo(name = "description")
    private val description: String

    @ColumnInfo(name = "ownerLogin")
    private val ownerLogin: String

    @ColumnInfo(name = "createdAt")
    private val createdAt: String

    @ColumnInfo(name = "gitURL")
    private val gitURL: String

    override val repoInfo: String
        get() = toLineIfNotEmpty("Repo: ", fullName) +
                toLineIfNotEmpty("Contributors: ", contributorsNumber.toString()) +
                toLineIfNotEmpty("Stargazers: ", stargazersCount.toString()) +
                toLineIfNotEmpty("Description: ", description) +
                toLineIfNotEmpty("Owner: ", ownerLogin) +
                toLineIfNotEmpty("Created: ", createdAt) +
                toLineIfNotEmpty("GitURL: ", gitURL)

    init {
        uid = repo.id!!
        name = repo.name
        stargazersCount = repo.stargazersCount!!
        fullName = repo.fullName
        description = repo.description
        ownerLogin = repo.owner.login
        createdAt = repo.createdAt
        gitURL = repo.gitUrl
    }//this.repo = repo;

    private fun toLineIfNotEmpty(title: String, line: String?): String {
        return if (line != null) {
            if (line.isEmpty()) "" else "$title $line$NEW_LINE"
        } else {
            ""
        }
    }

    companion object {

        private val NEW_LINE = "\n"
    }
}
