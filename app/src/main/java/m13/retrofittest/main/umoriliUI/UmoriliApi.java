package m13.retrofittest.main.umoriliUI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mikhail Avdeev on 18.12.2018.
 */public interface UmoriliApi {
    @GET("/api/get")
    Call<List<PostModel>> getData(
            //Аннотация @Query("name") String resourceName показывает Retrofit'у,
            // что в качестве параметра запроса нужно поставить пару name=<Значение строки resourceName>.
            @Query("name") String resourceName,
            @Query("num") int count);

}
