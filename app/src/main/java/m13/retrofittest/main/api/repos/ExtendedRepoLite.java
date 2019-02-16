package m13.retrofittest.main.api.repos;

import m13.retrofittest.main.api.generated.repos.Repo;

public class ExtendedRepoLite implements IExtendedRepo{

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
}
