package m13.retrofittest.main.api.repos;

import java.util.List;

import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.generated.repos.Repo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Mikhail Avdeev on 08.02.2019.
 */
public interface ReposInterface {

    //https://api.github.com/orgs/square/repos
    @GET("orgs/{org}/repos")
    Call<List<Repo>> organizationRepoList(
            @Path("org") String organization,
            @Query("type") String repoType,
            @Query("sort") String orderingParameter,
            @Query("direction") Integer order,
            @Query("per_page") Integer maxNumberOfRepos);

    @GET
    Call<List<Repo>> organizationRepoListByLink(
            @Url String url);

    //https://api.github.com/repos/square/wire/contributors
    @GET("repos/{org}/{repo}/contributors")
    Call<List<Contributor>> getContributorsList(
            @Path("org") String organization,
            @Path("repo") String repo,
            @Query("per_page") Integer maxNumberOfContributors
    );

}
