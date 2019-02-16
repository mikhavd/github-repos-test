package m13.retrofittest.main.api.services;

import java.util.List;
import java.util.Observable;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import m13.retrofittest.main.api.HeaderParser;
import m13.retrofittest.main.api.generated.contributors.Contributor;
import retrofit2.Response;

import static m13.retrofittest.main.api.HeaderParser.getLastPageNumber;

public class PagesCounter<T> {

    private final SinglePageRequester singlePageRequester;

    public PagesCounter(io.reactivex.Observable<Response<T>> singlePageRequester) {
        this.singlePageRequester = (SinglePageRequester) singlePageRequester;
    }

    public io.reactivex.Observable<Integer> getPagesCount(){
        return singlePageRequester.singlePageRequest()
                .flatMap(tResponse -> {
                    return io.reactivex.Observable.just(getLastPageNumber((Response) tResponse));
                });

    }

    public interface SinglePageRequester<T>{
        io.reactivex.Observable<Response<T>> singlePageRequest();
    }
}
