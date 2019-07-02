package m13.retrofittest.main.api.repos

interface IExtendedRepo {

    val contributorsNumber: Int?

    val name: String

    val stargazersCount: Int

    val repoInfo: String
}
