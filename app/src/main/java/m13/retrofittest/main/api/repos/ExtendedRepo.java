package m13.retrofittest.main.api.repos;

import java.util.ArrayList;
import java.util.List;

import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.generated.repos.Repo;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class ExtendedRepo implements IExtendedRepo{
    Repo repo;
    List<Contributor> contributors;

    public ExtendedRepo(Repo repo, List<Contributor> contributors){
        this.repo = repo;
        this.contributors = contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

    public String getRepoName() {
        return repo.getName();
    }

    public String getName() {
        return this.repo.getName();
    }

    public int getStargazersCount() {
        return this.repo.getStargazersCount();
    }

    public Integer getContributorsNumber() {
        return this.contributors.size();
    }

    public List<Contributor> getContributors() {
        return contributors;
    }
}
