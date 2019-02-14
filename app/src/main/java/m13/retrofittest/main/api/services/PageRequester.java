package m13.retrofittest.main.api.services;

import java.util.List;

import io.reactivex.Observable;
import m13.retrofittest.main.api.generated.repos.Repo;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 14.02.2019.
 */
public interface PageRequester<T> {

    Observable<Response<T>> pageRequest(String url);
}
