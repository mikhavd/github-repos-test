package m13.retrofittest.main.api.repos;

import java.util.ArrayList;
import java.util.List;

import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.generated.repos.Repo;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class RepoWithContributors {
    Repo repo;
    List<Contributor> contributors;

    public RepoWithContributors(Repo repo, List<Contributor> contributors){
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

    public Integer getContributorsSize() {
        return this.contributors.size();
    }

    public List<Contributor> getContributors() {
        return contributors;
    }
}
