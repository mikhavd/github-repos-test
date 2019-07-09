package m13.retrofittest.main.githubUI.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import m13.retrofittest.R;
import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.generated.repos.Repo;
import m13.retrofittest.main.api.repos.ExtendedRepoLite;
import m13.retrofittest.main.api.repos.IExtendedRepo;
import m13.retrofittest.main.api.services.PagesConcatinator;
import m13.retrofittest.main.api.services.PagesCounter;
import m13.retrofittest.main.api.services.APIInterface;
import m13.retrofittest.main.githubUI.GithubApp;
import m13.retrofittest.main.githubUI.RecyclerViewClickListener;
import m13.retrofittest.main.githubUI.ReposAdapter;
import retrofit2.HttpException;
import retrofit2.Response;

import static m13.retrofittest.main.githubUI.GithubApp.CLIENT_ID;
import static m13.retrofittest.main.githubUI.GithubApp.CLIENT_SECRET;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class OrganizationReposActivity extends AppCompatActivity
        implements RecyclerViewClickListener {
    RecyclerView recyclerView;
    //List<Repo> repos;
    List<IExtendedRepo> extendedRepos;
    private final static String organizationName = "square";
    private final static Integer maxNumberPerPage = 1000;

    private TextView emptyView;
    private GithubApp app;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (GithubApp) getApplicationContext();
        setContentView(R.layout.basic_activity);
        //create and populate adapter
        setTitle("Github repositories of Square");
        extendedRepos = new ArrayList<>();
        recyclerView = findViewById(R.id.posts_recycle_view);
        emptyView = findViewById(R.id.empty_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        ReposAdapter adapter = new ReposAdapter(this, extendedRepos);
        recyclerView.setAdapter(adapter);
        setRecyclerView();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        try {
            APIInterface rxRepoApi = app.getApiInterface(APIInterface.class);
            loadExtendedRepos(rxRepoApi)
            //loadExtendedReposWithPages(rxRepoApi)
            .onErrorReturn((Throwable ex) -> {
                handleException((Exception) ex);
                //empty object of the datatype
                return new ExtendedRepoLite(null, null);
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

    private void handleException(Exception ex) {
        String exInfo = ex.toString() +
                ((ex instanceof HttpException)
                        ? ". " + ((HttpException) ex).message()
                        : "");
        emptyView.setText(exInfo);
        Toast.makeText(this,
                exInfo, Toast.LENGTH_SHORT).show();
    }

    private void saveRepo(IExtendedRepo repoToSave) {
        extendedRepos.add(repoToSave);
        recyclerView.getAdapter().notifyDataSetChanged();
        setRecyclerView();
    }


    public static Observable<ExtendedRepoLite> loadExtendedRepos(
            APIInterface rxRepoApi) {
        //объект, который склеит все страницы с репозиториями
        Observable<List<Repo>> repoList = new PagesConcatinator<>(
                () -> rxRepoApi.getRepoList(CLIENT_ID, CLIENT_SECRET),
                url -> rxRepoApi.getReposListByLink(url + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET))
                .getObservableT();
        return repoList
                .flatMap(Observable::fromIterable)//разбираем Observable<List<Repo>> на перебор Repo
                .flatMap( //в этом flatMap используется сигнатура с двумя функциями:
                        //первая возвращает число контрибуторов проекта...
                        new Function<Repo, Observable<Integer>>() {
                            @Override
                            public Observable<Integer> apply(Repo repo) throws Exception {
                                try {
                                    return new PagesCounter<>(
                                            new PagesCounter.SinglePageRequester<List<Contributor>>() {
                                                @Override
                                                public Observable<Response<List<Contributor>>> singlePageRequest() {
                                                    return rxRepoApi.getContributorsSinglePage(
                                                            repo.getName(), CLIENT_ID, CLIENT_SECRET);
                                                }
                                            })
                                            .getObservableCount();
                                } catch (Exception e) {
                                    return Observable.just(1);
                                }
                            }
                        },
                        //...вторая использует результат первой:
                        //создаём объект (repo1, contributorsNumber) -> new ExtendedRepoLite(repo1, contributorsNUmber));
                        new BiFunction<Repo, Integer, ExtendedRepoLite>() {
                            @Override
                            public ExtendedRepoLite apply(Repo repo1, Integer contributorsNumber) throws Exception {
                                return new ExtendedRepoLite(repo1, contributorsNumber);
                            }
                        });
    }


    @Override
    public void recycleViewListClicked(View v, int position) {
        IExtendedRepo selectedRepo = extendedRepos.get(position);
        if (selectedRepo != null) {
            //Toast.makeText(this, selectedRepo.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RepoActivity.class); //ContributorsListActivity.class);

            app.setSelectedRepo(selectedRepo);
            this.startActivity(intent);
        }
    }

}
