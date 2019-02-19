package m13.retrofittest.main.githubUI.activities;

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
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import m13.retrofittest.R;
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
        setContentView(R.layout.activity_main);
        //create and populate adapter
        extendedRepos = new ArrayList<>();
        recyclerView = findViewById(R.id.posts_recycle_view);
        emptyView = findViewById(R.id.empty_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ReposAdapter adapter = new ReposAdapter(this, extendedRepos);
        recyclerView.setAdapter(adapter);
        setRecyclerView();
        try {
            APIInterface rxRepoApi = app.getRxRepoApi();
            loadExtendedReposContributorsCount(rxRepoApi)
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
        //совмещаем информацию об контирбуторах от двух разных Repo (из-за того, что они разбиваются на страницы?)
        /*if (repoToSave instanceof ExtendedRepo) {
            if (((ExtendedRepo)repoToSave).getContributors() == null) return;
            for (int i = 0; i < extendedRepos.size(); i++) {
                IExtendedRepo savedRepo = extendedRepos.get(i);
                if (!(savedRepo instanceof ExtendedRepo)) return;;
                if (savedRepo.getName().equals(repoToSave.getName())) {
                    List<Contributor> newContributorsList =
                            new ArrayList<>(((ExtendedRepo)savedRepo).getContributors());
                    newContributorsList.addAll(((ExtendedRepo)repoToSave).getContributors());
                    ((ExtendedRepo)repoToSave).setContributors(newContributorsList);
                    extendedRepos.set(i, repoToSave);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    setRecyclerView();
                    return;
                }
            }
        }*/
        extendedRepos.add(repoToSave);
        //сортируем репозитории по названиям, в алфавитном порядке
        //Collections.sort(extendedRepos, (lhs, rhs) -> lhs.getName().compareTo(rhs.getName()));
        recyclerView.getAdapter().notifyDataSetChanged();
        setRecyclerView();
    }

    @Override
    public void recycleViewListClicked(View v, int position) {
        IExtendedRepo selectedRepo = extendedRepos.get(position);
        if (selectedRepo != null) {
            //Toast.makeText(this, selectedRepo.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ContributorsListActivity.class);

            app.setSelectedRepo(selectedRepo);
            this.startActivity(intent);
        }
    }

    public static Observable<ExtendedRepoLite> loadExtendedReposContributorsCount(
            APIInterface rxRepoApi) {
        //объект, который склеит все страницы с репозиториями
        Observable<List<Repo>> repoList = new PagesConcatinator<>(
                () -> rxRepoApi.getRepoList(CLIENT_ID, CLIENT_SECRET),
                url -> rxRepoApi.getReposListByLink(url + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET))
                .getObservableT();
        Log.wtf("GithubAPI", "repoList:" + repoList.toString());
        return repoList
                .flatMap(Observable::fromIterable)//разбираем Observable<List<Repo>> на перебор Repo
                .flatMap( //в этом flatMap используется сигнатура с двумя функциями:
                        //первая возвращает число контрибуторов проекта...
                        (Function<Repo, Observable<Integer>>) repo -> {
                            try{
                                return new PagesCounter<>(
                                        () -> rxRepoApi.getContributorsSinglePage(
                                                repo.getName(), CLIENT_ID, CLIENT_SECRET))
                                        .getObservableCount();
                            }catch (Exception e){
                                return Observable.just(1);
                            }
                        },
                        //...вторая использует результат первой:
                        //создаём объект (repo1, contributorsNumber) -> new ExtendedRepoLite(repo1, contributorsNUmber));
                        ExtendedRepoLite::new);
    }

}
