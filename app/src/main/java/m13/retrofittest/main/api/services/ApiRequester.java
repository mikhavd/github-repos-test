package m13.retrofittest.main.api.services;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 14.02.2019.
 */
public interface ApiRequester<T> {

    Observable<Response<T>> firstRequest();
}
