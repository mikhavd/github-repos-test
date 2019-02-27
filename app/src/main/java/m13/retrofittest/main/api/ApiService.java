package m13.retrofittest.main.api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Mikhail Avdeev on 09.02.2019.
 */
public class ApiService<API> {
    protected API api;
    //private final Class<API> type;


    //для наследников
    public ApiService(GithubRetrofitClient githubRetrofitClient){
        this.api = githubRetrofitClient.getRetrofit().create(getManagedClass());
    }


    protected Class<API> getManagedClass() {
        ParameterizedType superclass = (ParameterizedType)
                getClass().getGenericSuperclass();
        Type[] actualTypeArguments = superclass.getActualTypeArguments();
        Type type = actualTypeArguments[0];
        return (Class<API>) type;
    }

    public API getApi() {
        return api;
    }
}
