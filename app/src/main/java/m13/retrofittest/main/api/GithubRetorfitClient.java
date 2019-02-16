package m13.retrofittest.main.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static m13.retrofittest.main.githubUI.GithubApp.CLIENT_ID;
import static m13.retrofittest.main.githubUI.GithubApp.CLIENT_SECRET;

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
        HttpLoggingInterceptor httpBodyLogging = new HttpLoggingInterceptor();
        httpBodyLogging
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        HttpLoggingInterceptor httpHeaderLogging = new HttpLoggingInterceptor();
        httpHeaderLogging
                .setLevel(HttpLoggingInterceptor.Level.HEADERS);
                //.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        class LoggingInterceptor implements Interceptor {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                long t1 = System.nanoTime();
                Log.d("OkHttp", String.format("Sending request %s on %s%n%s",
                        request.url(), chain.connection(), request.headers()));

                Response response = chain.proceed(request);

                long t2 = System.nanoTime();
                Log.d("OkHttp", String.format("Received response for %s in %.1fms%n%s",
                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));

                return response;
            }
        }

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("client_id", CLIENT_ID)
                            .addHeader("client_secret", CLIENT_SECRET)
                            .build();
                    //Log.d("GithubAPI", "-------------------------------");
                    //Log.d("GithubAPI", "Request headers: " + request.headers());
                    //Log.d("GithubAPI", "Request body: " + request.body());
                    //Log.d("GithubAPI", "-------------------------------");
                    return chain.proceed(request);
                })
                .addInterceptor(httpHeaderLogging)
                .addInterceptor(httpBodyLogging)
        .addInterceptor(new LoggingInterceptor());
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
