package m13.retrofittest.main.api.repos;

import java.util.List;

import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.generated.repos.Repo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public interface RxReposInterface {

    //public interface IGetDetailsService {
        //@GET(BuildConfig.GET_DETAILS_ENDPOINT)
        //Observable<Detail> getDetails(@Query("q") String someDetail);

        @GET("orgs/{org}/repos")
        Observable<List<Repo>> organizationRepoList(
                @Path("org") String organization,
                @Query("type") String repoType,
                @Query("sort") String orderingParameter,
                @Query("direction") Integer order,
                @Query("per_page") Integer maxNumberOfRepos);

    //https://api.github.com/repos/square/wire/contributors
    @GET("repos/{org}/{repo}/contributors")
    Observable<List<Contributor>> getContributorsList(
            @Path("org") String organization,
            @Path("repo") String repo,
            @Query("per_page") Integer maxNumberOfContributors
    );
}
