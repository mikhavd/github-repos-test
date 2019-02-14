package m13.retrofittest.main.githubUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import m13.retrofittest.R;
import m13.retrofittest.main.api.GithubRetorfitClient;
import m13.retrofittest.main.api.HeaderParser;
import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.generated.repos.Repo;
import m13.retrofittest.main.api.repos.RepoWithContributors;
import m13.retrofittest.main.api.repos.ReposInterface;
import m13.retrofittest.main.api.services.PagesConcatinator;
import m13.retrofittest.main.api.services.RxReposInterface;
import m13.retrofittest.main.api.services.RxReposService;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class OrganizationReposActivity extends AppCompatActivity
        implements RecyclerViewClickListener {
    RecyclerView recyclerView;
    //List<Repo> repos;
    List<RepoWithContributors> extendedRepos;
    private final static String organizationName = "square";
    private final static Integer maxNumberPerPage = 1000;
    private ReposInterface repoApi;
    private RxReposInterface rxRepoApi;
    private TextView emptyView;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create and populate adapter
        extendedRepos = new ArrayList<>();
        recyclerView = findViewById(R.id.posts_recycle_view);
        emptyView = (TextView) findViewById(R.id.empty_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ReposAdapter adapter = new ReposAdapter(this, extendedRepos);
        recyclerView.setAdapter(adapter);
        setRecyclerView();
        try {
            RxReposService rxService = new RxReposService(new GithubRetorfitClient());
            this.rxRepoApi = rxService.getApi();
            loadExtendedReposWithPages(rxRepoApi)
            .onErrorReturn((Throwable ex) -> {
                handleException((Exception) ex);
                //empty object of the datatype
                return new RepoWithContributors(null, null);
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::saveRepo, this::handleException);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void handleException(Throwable throwable) {
        handleException((Exception) throwable);
    }

    private void setRecyclerView() {
        if (extendedRepos.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    /*@SuppressLint("CheckResult")
    public static void loadReposWithContributors(RxReposInterface rxRepoApi, ILoader loader,
                                                 IErrorHandler errorHandler) {
        rxRepoApi.getRepoList()
                //разбираем Observable<List<Repo>> на перебор Repo
                .flatMap(Observable::fromIterable)
                //в этом flatMap используется сигнатура с двумя функциями:
                //вторая (создание RepoWithContributors) использует результат первой (getContribsList)
                //
                .flatMap(
                        //получаем список всех контрибуторов проекта
                        repo -> rxRepoApi.getContribsList(repo.getName()),
                        //создаём объект new RepoWithContributors
                        (repo1, contributors) -> new RepoWithContributors(repo1, contributors))
                .onErrorReturn((Throwable ex) ->
                {
                    errorHandler.handleError((Exception) ex);
                    //empty object of the datatype
                    return new RepoWithContributors(null, null);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(loader::save, Throwable::printStackTrace);
    }
    */

    private void handleException(Exception ex) {
        String exInfo = ex.toString() +
                ((ex instanceof HttpException)
                        ? ". " + ((HttpException) ex).message()
                        : "");
        emptyView.setText(exInfo);
        Toast.makeText(this,
                exInfo, Toast.LENGTH_SHORT).show();
    }

    private void saveRepo(RepoWithContributors repoWithContributors) {
        //todo
        if (repoWithContributors.getContributors() == null) return;
        extendedRepos.add(repoWithContributors);
        recyclerView.getAdapter().notifyDataSetChanged();
        setRecyclerView();
    }

    @Override
    public void recycleViewListClicked(View v, int position) {
        RepoWithContributors selectedRepo = extendedRepos.get(position);
        if (selectedRepo != null) {
            //Toast.makeText(this, selectedRepo.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RepoActivity.class);
            GithubApp app = (GithubApp) getApplicationContext();
            app.setSelectedRepo(selectedRepo);
            this.startActivity(intent);
        }
    }

    static Observable<Response<List<Repo>>> getRepoPageAndAllNext(RxReposInterface rxRepoApi) {
        return rxRepoApi.getPageWithRepoList()
                .concatMap(
                        response -> {
                            String linkToNextPage = HeaderParser.getLinkToNextPage(response); //response.headers().get("next");
                            if ((linkToNextPage == null) || linkToNextPage.isEmpty())
                                return Observable.just(response);
                            else
                                return Observable.just(response)
                                        .concatWith(rxRepoApi.responceWithRepoListByLink(linkToNextPage));
                        });
    }

    static Observable<List<Repo>> getObservableRepos(RxReposInterface rxRepoApi) {
        return getRepoPageAndAllNext(rxRepoApi)
        .concatMap(response -> Observable.just(response.body()));
    }

    static Observable<Response<List<Contributor>>> getContributorsPageAndAllNext(RxReposInterface rxRepoApi, String repoName) {
        return rxRepoApi.getPageWithContributorsList(repoName)
        .concatMap(
        response -> {
            String linkToNextPage = HeaderParser.getLinkToNextPage(response); //response.headers().get("next");
            if ((linkToNextPage == null) || linkToNextPage.isEmpty())
                return Observable.just(response);
            else
                return Observable.just(response)
                        .concatWith(rxRepoApi.responceWithContributorsListByLink(linkToNextPage));
        });
    }

    static Observable<List<Contributor>> getObservableContributors(RxReposInterface rxRepoApi, String repoName) {
        return getContributorsPageAndAllNext(rxRepoApi, repoName)
                .concatMap(response -> Observable.just(response.body()));
    }


    @SuppressLint("CheckResult")
    public static void loadExtendedReposFromPages(RxReposInterface rxRepoApi,
                                                  ILoader loader,
                                                 IErrorHandler errorHandler) {
        getObservableRepos(rxRepoApi)
                //разбираем Observable<List<Repo>> на перебор Repo
                .flatMap(Observable::fromIterable)
                //в этом flatMap используется сигнатура с двумя функциями:
                //вторая (создание RepoWithContributors) использует результат первой (getContribsList)
                //
                .flatMap(
                        //получаем список всех контрибуторов проекта
                        repo -> getObservableContributors(rxRepoApi, repo.getName()),
                        //создаём объект new RepoWithContributors
                        (repo1, contributors) -> new RepoWithContributors(repo1, contributors))
                .onErrorReturn((Throwable ex) ->
                {
                    errorHandler.handleError((Exception) ex);
                    //empty object of the datatype
                    return new RepoWithContributors(null, null);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(loader::save, Throwable::printStackTrace);
    }

    interface IRepoProvider{
        String getRepoName();

        void setRepoName(String name);
    }

    public static Observable<RepoWithContributors> loadExtendedReposWithPages(
            RxReposInterface rxRepoApi) {
        //объект, который склеит все страницы с репозиториями
        Observable<List<Repo>> repoList = new PagesConcatinator<>(
                rxRepoApi::getPageWithRepoList,
                rxRepoApi::responceWithRepoListByLink)
            .getObservableT();

        Log.d("GithubAPI", "repoList:" + repoList.toString());

        //Observable<List<Contributor>> contribsList =
        return repoList
                .flatMap(Observable::fromIterable)//разбираем Observable<List<Repo>> на перебор Repo
                .flatMap( //в этом flatMap используется сигнатура с двумя функциями:
                        //первая возвращает список контрибуторов проекта...
                        repo -> new PagesConcatinator<>(
                                //вместо ссылки на метод лямбда, т.к. нужно передать параметр RepoName
                                () -> rxRepoApi.getPageWithContributorsList(repo.getName()),
                                rxRepoApi::responceWithContributorsListByLink)
                                .getObservableT(),
                        //...вторая использует результат первой:
                        //создаём объект (repo1, contributors) -> new RepoWithContributors(repo1, contributors));
                        RepoWithContributors::new);
    }

}
