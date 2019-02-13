package m13.retrofittest.main.api;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.processors.BehaviorProcessor;

/**
 * Created by Mikhail Avdeev on 13.02.2019.
 */
public class PaginationExample {

    // Fetch all pages and return the items contained in those pages, using the provided page fetcher function
    public static <T> Flowable<T> fetchItems(Function<Integer, Single<Page<T>>> fetchPage) {
        // Processor issues page indices
        BehaviorProcessor<Integer> processor = BehaviorProcessor.createDefault(0);
        // When an index number is issued, fetch the corresponding page
        return processor.concatMap(index -> fetchPage.apply(index).toFlowable())
                // when returning the page, update the processor to get the next page (or stop)
                .doOnNext(page -> {
                    if (page.hasNext()) {
                        processor.onNext(page.getNextPageIndex());
                    } else {
                        processor.onComplete();
                    }
                })
                .concatMapIterable(Page::getElements);
    }

    public static void main(String[] args) {
        fetchItems(PaginationExample::examplePageFetcher)
                .subscribe(System.out::println);
    }

    // A function to fetch a page of our paged data
    private static Single<Page<String>> examplePageFetcher(int index) {
        return Single.just(pages.get(index));
    }

    // Create some paged data
    private static ArrayList<Page<String>> pages = new ArrayList<>(3);

    static {
        pages.add(new Page<>(Arrays.asList("one", "two"), Optional.of(1)));
        pages.add(new Page<>(Arrays.asList("three", "four"), Optional.of(2)));
        pages.add(new Page<>(Arrays.asList("five"), Optional.empty()));
    }

    static class Page<T> {
        private List<T> elements;
        private Optional<Integer> nextPageIndex;

        public Page(List<T> elements, Optional<Integer> nextPageIndex) {
            this.elements = elements;
            this.nextPageIndex = nextPageIndex;
        }

        public List<T> getElements() {
            return elements;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public int getNextPageIndex() {
            return nextPageIndex.get();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public boolean hasNext() {
            return nextPageIndex.isPresent();
        }
    }
}