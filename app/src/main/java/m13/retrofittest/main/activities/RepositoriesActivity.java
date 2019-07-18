package m13.retrofittest.main.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import m13.retrofittest.R;
import m13.retrofittest.main.api.generated.repos.Repo;
import m13.retrofittest.main.api.services.APIInterface;
import m13.retrofittest.main.api.services.PagesConcatinator;
import m13.retrofittest.main.api.services.PagesCounter;
import m13.retrofittest.main.application.GithubApp;
import m13.retrofittest.main.repos.Repository;
import m13.retrofittest.main.ui.RepositoriesListAdapter;
import m13.retrofittest.main.viewmodels.RepositoriesViewModel;
import retrofit2.HttpException;

import static m13.retrofittest.main.application.GithubApp.CLIENT_ID;
import static m13.retrofittest.main.application.GithubApp.CLIENT_SECRET;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class RepositoriesActivity extends AppCompatActivity {
    private final static String organizationName = "square";
    private final static Integer maxNumberPerPage = 1000;
    private TextView emptyView;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GithubApp app = (GithubApp) getApplicationContext();

        //create and populate adapter
        setTitle("Github repositories of Square");
        setContentView(R.layout.repos_activity);

        //new code
        RecyclerView recyclerView = findViewById(R.id.viewmodel_recyclerview);
        RepositoriesListAdapter adapter = new RepositoriesListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //new new code
        //private RepositoriesListAdapter adapter;
        RepositoriesViewModel repositoriesViewModel = ViewModelProviders.of(this).get(RepositoriesViewModel.class);
        // Update the cached copy of the words in the adapter.
        repositoriesViewModel.getAllRepositories().observe(this, adapter::setRepositories);
        emptyView = findViewById(R.id.empty_view);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        try {
            APIInterface rxRepoApi = app.getApiInterface(APIInterface.class);
            loadRepos(rxRepoApi)
            .onErrorReturn((Throwable throwable1) -> {
                handleException(throwable1);
                //empty object of the datatype
                return null;
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(repositoriesViewModel::insert,
                    this::handleException);
        } catch (Exception e) {
            handleException(e);
        }
    }


    private void handleException(Throwable ex) {
        String exInfo = ex.toString() +
                ((ex instanceof HttpException)
                        ? ". " + ((HttpException) ex).message()
                        : "");
        emptyView.setText(exInfo);
        Toast.makeText(this,
                exInfo, Toast.LENGTH_SHORT).show();
    }


    public static Observable<Repository> loadRepos(
            APIInterface rxRepoApi) {
        //объект, который склеит все страницы с репозиториями
        Observable<List<Repo>> repoList = new PagesConcatinator<>(
                () -> rxRepoApi.getRepoList(CLIENT_ID, CLIENT_SECRET),
                url -> rxRepoApi.getReposListByLink(url + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET))
                .getObservableT();
        //new Repository(repo1, contributorsNumber);
        return repoList
                .flatMap(Observable::fromIterable)//разбираем Observable<List<Repo>> на перебор Repo
                .flatMap( //в этом flatMap используется сигнатура с двумя функциями:
                        //первая возвращает число контрибуторов проекта...
                        (Function<Repo, Observable<Integer>>) repo -> {
                            try {
                                return new PagesCounter<>(
                                        () -> rxRepoApi.getContributorsSinglePage(
                                                repo.getName(), CLIENT_ID, CLIENT_SECRET))
                                        .getObservableCount();
                            } catch (Exception e) {
                                return Observable.just(1);
                            }
                        },
                        //...вторая использует результат первой:
                        //создаём объект (repo1, contributorsNumber) -> new Repository(repo1, contributorsNUmber));
                        Repository.RepositoryFabric::getRepository);
    }
}
