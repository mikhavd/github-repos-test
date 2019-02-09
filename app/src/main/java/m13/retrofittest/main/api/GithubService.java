package m13.retrofittest.main.api;

import java.util.List;

import m13.retrofittest.main.repos.Repo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Mikhail Avdeev on 08.02.2019.
 */
public class GithubService {

    private static String organizationName = "square";
    //todo: использовать нашу API
    private List<Repo> repoList;

    public GithubService(){
        Retrofit retrofit = ApiProvider.getRetrofit();
        ReposEndpointInterface service = retrofit.create(ReposEndpointInterface.class);
        RepoType repoType = RepoType.all;
        Call<List<Repo>> call = service.organizationRepoList(
                organizationName,
                repoType.getRepoTypeName(),
                //todo: здесь добавить Enum, и загуглить, как его связать с Retrofit query
                null,
                null);

        call.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                // handle response here
                repoList = response.body();
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable throwable) {

            }

        });
    }

    public List<Repo> getRepoList() {
        return repoList;
    }
}
