package m13.retrofittest.main.api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Mikhail Avdeev on 09.02.2019.
 */
public class ApiService<Api> {
    protected Api api;
    //private final Class<Api> type;


    //для наследников
    public ApiService(GithubRetrofitClient githubRetrofitClient){
        //todo ченкуть, нужно ли это вообще
        //тут был error при компиляции с -Xlint:unchecked...
        // this.api = githubRetrofitClient.getRetrofit().create(getManagedClass());
    }


    /*private Class<Api> getManagedClass() {
        ParameterizedType superclass = (ParameterizedType)
                getClass().getGenericSuperclass();
        Type[] actualTypeArguments = superclass.getActualTypeArguments();
        Type type = actualTypeArguments[0];
        return (Class<Api>) type;
    }*/

    public Api getApi() {
        return api;
    }
}
