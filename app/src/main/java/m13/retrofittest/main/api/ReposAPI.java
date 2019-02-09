package m13.retrofittest.main.api;

import java.util.ArrayList;
import java.util.List;

import m13.retrofittest.main.repos.Repo;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 09.02.2019.
 */
public class ReposAPI extends ApiService<ReposEndpointInterface, List<Repo>> {


    public ReposAPI(RetrofitClient retrofitClient) {
        super(retrofitClient);
    }

    public List<Repo> getRepos(String organizationName, Integer maxNumberOfRepos) throws Exception {
        RepoType repoType = RepoType.all;
        List<Repo> repos = new ArrayList<>();
        Response<List<Repo>> repoResponce = secureExecute(api.organizationRepoList(organizationName,
                repoType.getRepoTypeName(),
                //todo: здесь добавить Enum, и загуглить, как его связать с Retrofit query
                null,
                null,
                maxNumberOfRepos));
        List<Repo> origRepos = repoResponce.body();
        repos.addAll(origRepos);
        String nextLink = super.parseHeaderLink(repoResponce);
        System.out.println("ReposeAPI: getRepos: ORIG nextLink: " + nextLink);
        while (!nextLink.isEmpty()) {
            Response additionalResponce = secureExecute(
                    api.organizationRepoListByLink(nextLink));
            repos.addAll((List<Repo>) additionalResponce.body());
            nextLink = super.parseHeaderLink(additionalResponce);
            System.out.println("ReposeAPI: getRepos: NEW nextLink: " + nextLink);
        }
        return repos;
    }
}
