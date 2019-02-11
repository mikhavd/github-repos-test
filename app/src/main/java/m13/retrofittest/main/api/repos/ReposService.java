package m13.retrofittest.main.api.repos;

import m13.retrofittest.main.api.ApiService;
import m13.retrofittest.main.api.GithubRetorfitClient;

/**
 * Created by Mikhail Avdeev on 09.02.2019.
 */
public class ReposService extends ApiService<ReposInterface> {


    public ReposService(GithubRetorfitClient githubRetorfitClient) {
        super(githubRetorfitClient);
    }


    /*public List<Repo> getRepos(String organizationName, Integer maxNumberOfRepos) throws Exception {
        //по умлочанию запрашиваем все репозитории
        RepoType repoType = RepoType.all;
        List<Repo> repos = new ArrayList<>();
        //todo: если добавлять асинхронный callback или handler, то лучше здесь...
        Response<List<Repo>> repoResponce = secureExecute(api.organizationRepoList(organizationName,
                repoType.getRepoTypeName(),
                //todo: здесь добавить Enum, и загуглить, как его связать с Retrofit query
                null,
                null,
                maxNumberOfRepos));
        List<Repo> origRepos = repoResponce.body();
        repos.addAll(origRepos);
        //next page with elements obtained from link from responce
        String nextLink = HeaderParser.parseHeaderLink(repoResponce);
        System.out.println("ReposeAPI: getRepos: ORIG nextLink: " + nextLink);
        while (!nextLink.isEmpty()) {
            Response additionalResponce = secureExecute(
                    api.organizationRepoListByLink(nextLink));
            repos.addAll((List<Repo>) additionalResponce.body());
            nextLink = HeaderParser.parseHeaderLink(additionalResponce);
            System.out.println("ReposeAPI: getRepos: NEW nextLink: " + nextLink);
        }
        return repos;
    }*/
}
