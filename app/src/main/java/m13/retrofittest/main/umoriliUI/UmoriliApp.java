package m13.retrofittest.main.umoriliUI;

import android.app.Application;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mikhail Avdeev on 18.12.2018.
 */
public class UmoriliApp extends Application {
    private static UmoriliApi umoriliApi;
    private Retrofit retrofit;

    @Override
    public void onCreate(){
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://umorili.herokuapp.com") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        umoriliApi = retrofit.create(UmoriliApi.class); //Создаем объект, при помощи которого будем выполнять запросы

    }
    public static UmoriliApi getApi() {
        return umoriliApi;
    }

    public void asyncCall(){
        //асинхронный вызов
        UmoriliApp.getApi().getData("bash", 50).enqueue(
            new Callback<List<PostModel>>() {
                @Override
                public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                    //Данные успешно пришли, но надо проверить response.body() на null
                }
                @Override
                public void onFailure(Call<List<PostModel>> call, Throwable t) {
                    Log.d("exception", "t: " + t.toString());
        //Произошла ошибка
                }
        });
    }

    public void syncCall(){
        //синхронный вызов
        try {
            Response syncResponse = umoriliApi.getData("bash", 50).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
