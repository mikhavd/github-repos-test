package m13.retrofittest.main.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mikhail Avdeev on 08.02.2019.
 */
public class GithubRetorfitClient {

    //todo: https://developer.github.com/v3/media/#request-specific-version
    public static final String BASE_URL = "https://api.github.com";
    private final Retrofit retrofit;

    public GithubRetorfitClient(){
        this.retrofit = initRetrofit();
    }

    Retrofit getRetrofit() {
        return this.retrofit;
    }

    static private Retrofit initRetrofit(){
        // Add the interceptor to OkHttpClient
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        //httpClient.interceptors().add(requestInterceptor);

        //custom Gson parser instance
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit;
    }
}
