package m13.retrofittest.main.api.repos;

import m13.retrofittest.main.api.generated.repos.Repo;

public class ExtendedRepoLite implements IExtendedRepo{

    public static final String NEW_LINE = "\n";
    private Repo repo;
    private Integer contributorsNumber;

    public ExtendedRepoLite(Repo repo, Integer contributorsNumber) {
        this.repo = repo;
        this.contributorsNumber = contributorsNumber;
    }

    public Repo getRepo() {
        return repo;
    }

    public Integer getContributorsNumber() {
        return contributorsNumber;
    }

    @Override
    public String getName() {
        return repo.getName();
    }

    @Override
    public int getStargazersCount() {
        return repo.getStargazersCount();
    }

    @Override
    public String getRepoInfo() {
        return
                toLineIfNotEmpty("Repo: ", repo.getFullName()) +
                toLineIfNotEmpty("Description: ", repo.getDescription()) +
                toLineIfNotEmpty("Owner: ", repo.getOwner().getLogin()) +
                toLineIfNotEmpty("Created: : ", repo.getCreatedAt()) +
                toLineIfNotEmpty("GitURL: ", repo.getGitUrl()) ;


    }

    String toLineIfNotEmpty(String title, String line){
        return (line.isEmpty()) ? "" : title + " " + line + NEW_LINE;
    }
}
