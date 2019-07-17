package m13.retrofittest.main.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import m13.retrofittest.R;
import m13.retrofittest.main.api.generated.repos.Repo;
import m13.retrofittest.main.repos.IExtendedRepo;
import m13.retrofittest.main.repos.Repository;
import m13.retrofittest.main.api.services.APIInterface;
import m13.retrofittest.main.api.services.PagesConcatinator;
import m13.retrofittest.main.api.services.PagesCounter;
import m13.retrofittest.main.application.GithubApp;
import m13.retrofittest.main.githubUI.RecyclerViewClickListener;
import m13.retrofittest.main.ui.ReposAdapter;
import retrofit2.HttpException;

import static m13.retrofittest.main.application.GithubApp.CLIENT_ID;
import static m13.retrofittest.main.application.GithubApp.CLIENT_SECRET;

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
            .onErrorReturn((Throwable throwable1) -> {
                handleException((Exception) throwable1);
                //empty object of the datatype
                return null; //Repository.RepositoryFabric.getRepository(null, null);
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer<Repository>() {
                @Override
                public void accept(Repository repoToSave) throws Exception {
                    OrganizationReposActivity.this.saveRepo(repoToSave);
                }
            }, throwable -> handleException((Exception) throwable));
        } catch (Exception e) {
            handleException(e);
        }
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


    public static Observable<Repository> loadExtendedRepos(
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


    @Override
    public void recycleViewListClicked(View v, int position) {
        IExtendedRepo selectedRepo = extendedRepos.get(position);
        if (selectedRepo != null) {
            //Toast.makeText(this, selectedRepo.getFullName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RepoActivity.class); //ContributorsListActivity.class);

            app.setSelectedRepo(selectedRepo);
            this.startActivity(intent);
        }
    }

}
