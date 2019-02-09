package m13.retrofittest.main.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mikhail Avdeev on 08.02.2019.
 */
public class ApiProvider {

    //todo: https://developer.github.com/v3/media/#request-specific-version
    public static final String BASE_URL = "https://api.github.com";

    static Retrofit getRetrofit(){
        // Add the interceptor to OkHttpClient
        OkHttpClient client = new OkHttpClient();

        //client.interceptors().add(requestInterceptor);

        //custom Gson parser instance
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }
}
