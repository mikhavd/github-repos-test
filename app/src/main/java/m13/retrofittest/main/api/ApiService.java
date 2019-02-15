package m13.retrofittest.main.api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Mikhail Avdeev on 09.02.2019.
 */
public class ApiService<Api> {
    protected Api api;


    public ApiService(GithubRetorfitClient githubRetorfitClient) {
        this.api = githubRetorfitClient.getRetrofit().create(getManagedClass());
    }

    private Class<Api> getManagedClass() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] actualTypeArguments = superclass.getActualTypeArguments();
        Type type = actualTypeArguments[0];
        return (Class<Api>) type;
    }

    public Api getApi() {
        return api;
    }
}
