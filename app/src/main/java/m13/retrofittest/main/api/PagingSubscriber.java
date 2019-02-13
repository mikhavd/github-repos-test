package m13.retrofittest.main.api;

/**
 * Created by Mikhail Avdeev on 13.02.2019.
 */

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/***
 * This is a helper wrapper Subscriber that helps you lazily defer
 * continuous paging of a result set from some API.
 * Through the use of a {@link Subject}, it helps notify the original {@link Observable}
 * when to perform an additional fetch.
 * The notification is sent when a certain count of items has been reached.
 * Generally this count represents the page.
 * @param <T> The event type
 */

public class PagingSubscriber<T> implements Subscriber<T> {

    private final Subject<Void> nextPageTrigger =
            PublishSubject.create();
    private final long pageSize;
    private long count = 0;
    private final Subscriber<T> delegate;

    /***
     * Creates the {@link PagingSubscriber}
     * @param pageSize
     * @param delegate
     */
    public PagingSubscriber(long pageSize, Subscriber<T> delegate) {
        this.pageSize = pageSize;
        this.delegate = delegate;
    }

    public Observable<Void> getNextPageTrigger() {
        return nextPageTrigger;
    }

    //@Override
    //public void onStart() {
        //delegate.onStart();
    //}

    //@Override
    //public void onCompleted() {
        //delegate.onCompleted();
    //}

    @Override
    public void onError(Throwable e) {
        delegate.onError(e);
    }

    @Override
    public void onComplete() {
        delegate.onComplete();
    }

    @Override
    public void onSubscribe(Subscription s) {
        delegate.onSubscribe(s);
    }

    @Override
    public void onNext(T t) {
        count+=1;
        if (count == pageSize) {
            nextPageTrigger.onNext(null);
            count= 0;
        }
        delegate.onNext(t);
    }

}
