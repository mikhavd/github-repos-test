package m13.retrofittest.main.githubUI;

import m13.retrofittest.main.api.repos.RepoWithContributors;

/**
 * Created by Mikhail Avdeev on 12.02.2019.
 */
public interface ILoader {
    void save(RepoWithContributors repoWithContributors);
}
