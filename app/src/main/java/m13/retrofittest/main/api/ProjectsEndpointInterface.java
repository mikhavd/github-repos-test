package m13.retrofittest.main.api;

/**
 * Created by Mikhail Avdeev on 08.02.2019.
 */
public interface ProjectsEndpointInterface {
    // Request method and URL specified in the annotation


    //NOT AN EXAMPLE
    //
    // оригинальный GET c https://developer.github.com/v3/projects/
    // @GET("repos/:owner/:repo/projects")
    //Call<List<Project>> groupList(
            //@Path("id") int groupId,
            //@Query("sort") String sort);


    //example
    //@GET("group/{id}/users")
    //Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    //example
    //@POST("users/new")
    //Call<User> createUser(@Body User user);

}
