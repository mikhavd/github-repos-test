package m13.retrofittest.main.repos;

/**
 * Created by Mikhail Avdeev on 08.02.2019.
 */
public enum RepoType {
    all("all"),
    publicType("public"),
    privateType("private"),
    forksType("forks"),
    sourcesType("sources"),
    memberType("member");

    private String repoTypeName = "all";

    RepoType(String repoTypeName) {
        this.repoTypeName = repoTypeName;
    }

    public String getRepoTypeName(){
        return this.repoTypeName;
    }

}
