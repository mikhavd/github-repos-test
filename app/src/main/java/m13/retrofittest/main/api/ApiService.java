package m13.retrofittest.main.api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import m13.retrofittest.main.api.errors.ErrorHandler;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 09.02.2019.
 */
public class ApiService<Api, T> {
    protected RetrofitClient apiClient;
    protected Api api;


    protected ApiService(RetrofitClient retrofitClient) {
        this.api =
                retrofitClient.getRetrofit().create(getManagedClass());
    }

    protected Response<T> secureExecute(Call<T> call) throws Exception {
        Response<T> response = call.execute();
        if (!response.isSuccessful()) {
            ErrorHandler.parseServerErrorCode(response.code());
        }
        return response;
    }

    //protected <T> T getSecureResponseBody(Call<T> call, Class<T> type) throws Exception {
        //Response<T> response = secureExecute(call);
        //return type.cast(response.body());
    //}

    private Class<Api> getManagedClass() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] actualTypeArguments = superclass.getActualTypeArguments();
        Type type = actualTypeArguments[0];
        return (Class<Api>) type;
    }

}
