package m13.retrofittest.main.api.services;

import java.util.List;

import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.generated.repos.Repo;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

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



    @GET("orgs/square/repos")
    Observable<List<Repo>> getRepoList();


    @GET("repos/square/{repo}/contributors")
    Observable<List<Contributor>> getContribsList(
            @Path("repo") String repo
    );

    @GET("orgs/square/repos?per_page=1000")
    Observable<Response<List<Repo>>> getPageWithRepoList();

    @GET
    Observable<Response<List<Repo>>> responceWithRepoListByLink(
            @Url String url);


    @GET("repos/square/{repo}/contributors?per_page=1000")
    Observable<Response<List<Contributor>>> getPageWithContributorsList(
            @Path("repo") String repo
    );

    @GET
    Observable<Response<List<Contributor>>> responceWithContributorsListByLink(
            @Url String url);

}
