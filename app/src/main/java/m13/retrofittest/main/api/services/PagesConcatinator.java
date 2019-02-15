package m13.retrofittest.main.api.services;

import io.reactivex.Observable;
import m13.retrofittest.main.api.HeaderParser;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 14.02.2019.
 */
//класс, "склеивающий" ответы из нескольких страниц
public class PagesConcatinator<T> {

    private final ApiRequester<T> apiRequester;
    private final PageRequester<T> pageRequester;

    public PagesConcatinator(ApiRequester<T> apiRequester, PageRequester<T> pageRequester){
        this.apiRequester = apiRequester;
        this.pageRequester = pageRequester;

    }


    private Observable<Response<T>> getObservableResponses(){
        return
            apiRequester.firstRequest()
                    .concatMap(response -> {
                        String linkToNextPage = HeaderParser.getLinkToNextPage(response);
                        if ((linkToNextPage == null) || linkToNextPage.isEmpty())
                            return Observable.just(response);
                        else
                            return Observable.just(response)
                                    .concatWith(pageRequester.pageRequest(linkToNextPage));
                    });
    }

    public Observable<T> getObservableT(){
        return getObservableResponses()
                .concatMap(response -> Observable.just(response.body()));
    }

    /**
     * Created by Mikhail Avdeev on 14.02.2019.
     */
    public interface ApiRequester<T> {

        Observable<Response<T>> firstRequest();
    }

    /**
     * Created by Mikhail Avdeev on 14.02.2019.
     */
    public interface PageRequester<T> {

        Observable<Response<T>> pageRequest(String url);
    }
}