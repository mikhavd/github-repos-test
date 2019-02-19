package m13.retrofittest.main.api.services;

import java.util.List;

import io.reactivex.Observable;
import m13.retrofittest.main.api.generated.commits.CommitData;
import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.generated.repos.Repo;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public interface APIInterface {

        /*@GET("orgs/{org}/repos")
        Observable<List<Repo>> organizationRepoList(
                @Path("org") String organization,
                @Query("type") String repoType,
                @Query("sort") String orderingParameter,
                @Query("direction") Integer order,
                @Query("per_page") Integer maxNumberOfRepos);*/


    @GET("orgs/square/repos?per_page=1000")
    Observable<Response<List<Repo>>> getRepoList(
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret);

    @GET
    Observable<Response<List<Repo>>> getReposListByLink(
            @Url String url);


    @GET("repos/square/{repo}/contributors?per_page=1000")
    Observable<Response<List<Contributor>>> getContributorsList(
            @Path("repo") String repo,
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret
    );

    @GET("repos/square/{repo}/contributors?per_page=1")
    Observable<Response<List<Contributor>>> getContributorsSinglePage(
            @Path("repo") String repo,
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret
    );

    @GET
    Observable<Response<List<Contributor>>> getContributorsListByLink(
            @Url String url);

    @GET("repos/square/{repo}/commits?per_page=1000")
    Observable<Response<List<CommitData>>> getCommitDataList(
            @Path("repo") String repo,
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret
    );

    @GET
    Observable<Response<List<CommitData>>> getCommitDataListByLink(
            @Url String s);
}