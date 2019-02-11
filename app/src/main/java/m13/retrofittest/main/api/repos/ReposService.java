package m13.retrofittest.main.api.repos;

import java.util.ArrayList;
import java.util.List;

import m13.retrofittest.main.api.ApiService;
import m13.retrofittest.main.api.HeaderParser;
import m13.retrofittest.main.api.GithubRetorfitClient;
import m13.retrofittest.main.api.generated.repos.Repo;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 09.02.2019.
 */
public class ReposService extends ApiService<ReposEndpointInterface, List<Repo>> {


    public ReposService(GithubRetorfitClient githubRetorfitClient) {
        super(githubRetorfitClient);
    }


    public List<Repo> getRepos(String organizationName, Integer maxNumberOfRepos) throws Exception {
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
    }

    static public List<Repo> handleAsyncGetRepos(Response repoResponce){
        List<Repo> origRepos = (List<Repo>) repoResponce.body();
        List<Repo> repos = new ArrayList<>();
        repos.addAll(origRepos);
        //next page with elements obtained from link from responce
        String nextLink = HeaderParser.parseHeaderLink(repoResponce);
        System.out.println("ReposeAPI: getRepos: ORIG nextLink: " + nextLink);
        /*while (!nextLink.isEmpty()) {
            //todo: asyncLoading
            Response additionalResponce = secureExecute(
                    this.api.organizationRepoListByLink(nextLink));
            repos.addAll((List<Repo>) additionalResponce.body());
            nextLink = HeaderParser.parseHeaderLink(additionalResponce);
            System.out.println("ReposeAPI: getRepos: NEW nextLink: " + nextLink);
        }*/
        return repos;
    }
}
