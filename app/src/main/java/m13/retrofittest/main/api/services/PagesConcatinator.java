package m13.retrofittest.main.api.services;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
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
            apiRequester.request()
                    .concatMap( response -> {
                        String linkToNextPage = HeaderParser.getNextPageURL(response);
                        if ((linkToNextPage == null) || linkToNextPage.isEmpty())
                            return Observable.just(response);
                        else
                            return Observable.just(response)
                                    .concatWith(pageRequester.request(linkToNextPage));
                    });
    }

    private Observable<Response<T>> getPages(PageRequester<T> pageRequester, String pageURL){
        return pageRequester.request(pageURL)
                .concatMap(response -> {
                    String linkToNextPage = HeaderParser.getNextPageURL(response);
                    if ((linkToNextPage == null) || linkToNextPage.isEmpty())
                        return Observable.just(response);
                    else
                        return Observable.just(response)
                                .concatWith(getPages(pageRequester, linkToNextPage));
                });
    }

    private Observable<Response<T>> getObservableResponsesPages(){
        return
            apiRequester.request()
                    .concatMap(response -> {
                        String linkToNextPage = HeaderParser.getNextPageURL(response);
                        if ((linkToNextPage == null) || linkToNextPage.isEmpty())
                            return Observable.just(response);
                        else
                            return Observable.just(response)
                                    .concatWith(getPages(pageRequester, linkToNextPage));
                    }
                    );
    }

    Observable<Response<T>> getPageAndNext(String nextPageUrl) {
        return ((nextPageUrl.isEmpty())
                        ? apiRequester.request()
                        : pageRequester.request(nextPageUrl))
                .concatMap(response -> {
                            String linkToNextPage = HeaderParser.getNextPageURL(response);
                            if ((linkToNextPage == null) || linkToNextPage.isEmpty())
                                return Observable.just(response);
                            else
                                return Observable.just(response)
                                        .concatWith(getPageAndNext(linkToNextPage));
                        }
                );
    }


    public Observable<T> getObservableT(){
        return getPageAndNext("")//getObservableResponsesPages() //getObservableResponses()
                .concatMap(response -> Observable.just(response.body()));
    }

    /**
     * Created by Mikhail Avdeev on 14.02.2019.
     */
    public interface ApiRequester<T> {

        Observable<Response<T>> request();
    }

    /**
     * Created by Mikhail Avdeev on 14.02.2019.
     */
    public interface PageRequester<T> {

        Observable<Response<T>> request(String url);
    }
}
