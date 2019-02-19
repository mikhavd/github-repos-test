package m13.retrofittest.main.api.services;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Response;

import static m13.retrofittest.main.api.HeaderParser.getLastPageNumber;

public class PagesCounter<T> {

    private final SinglePageRequester<T> singlePageRequester;

    public PagesCounter(SinglePageRequester<T> singlePageRequester) {
        this.singlePageRequester = (SinglePageRequester) singlePageRequester;
    }



    public Observable<Integer> getObservableCount(){
        return
                singlePageRequester.singlePageRequest()
                        .concatMap((Function<Response<T>, Observable<Integer>>) response -> {
                            try {
                                return Observable.just(getLastPageNumber(response));
                            } catch (Exception e) {
                                //если нет следующей страницы, у нас 1 контрибутор
                                return Observable.just(1);
                            }
                        });
    }

    public interface SinglePageRequester<T>{
        io.reactivex.Observable<Response<T>> singlePageRequest();
    }
}
