package m13.retrofittest.main.api;

import java.util.List;

import m13.retrofittest.main.repos.Repo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mikhail Avdeev on 08.02.2019.
 */
public interface ReposEndpointInterface {
    //example
    //@GET("group/{id}/users")
    //Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    //example
    //@POST("users/new")
    //Call<User> createUser(@Body User user);

    //real thing
    @GET("orgs/{org}/repos")
    Call<List<Repo>> organizationRepoList(
            @Path("org") String organization,
            @Query("type") String repoType,
            @Query("sort") String orderingParameter,
            @Query("direction") Integer order);

}
