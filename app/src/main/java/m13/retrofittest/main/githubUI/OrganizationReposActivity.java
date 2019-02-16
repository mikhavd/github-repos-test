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
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import m13.retrofittest.R;
import m13.retrofittest.main.api.GithubRetorfitClient;
import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.generated.repos.Repo;
import m13.retrofittest.main.api.repos.RepoWithContributors;
import m13.retrofittest.main.api.services.PagesConcatinator;
import m13.retrofittest.main.api.services.RxReposInterface;
import m13.retrofittest.main.api.services.RxReposService;
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
    List<RepoWithContributors> extendedRepos;
    private final static String organizationName = "square";
    private final static Integer maxNumberPerPage = 1000;
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
        emptyView = findViewById(R.id.empty_view);
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

    private void handleException(Exception ex) {
        String exInfo = ex.toString() +
                ((ex instanceof HttpException)
                        ? ". " + ((HttpException) ex).message()
                        : "");
        emptyView.setText(exInfo);
        Toast.makeText(this,
                exInfo, Toast.LENGTH_SHORT).show();
    }

    private void saveRepo(RepoWithContributors repoToSave) {
        //todo
        if (repoToSave.getContributors() == null) return;
        for (int i = 0; i < extendedRepos.size(); i++) {
            RepoWithContributors savedRepo = extendedRepos.get(i);
            if (savedRepo.getName().equals(repoToSave.getName())) {
                List<Contributor> newList = new ArrayList<>(savedRepo.getContributors());
                newList.addAll(repoToSave.getContributors());
                repoToSave.setContributors(newList);
                extendedRepos.set(i, repoToSave);
                recyclerView.getAdapter().notifyDataSetChanged();
                setRecyclerView();
                return;
            }
        }
        extendedRepos.add(repoToSave);
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


    public static Observable<RepoWithContributors> loadExtendedReposWithPages(
            RxReposInterface rxRepoApi) {
        //объект, который склеит все страницы с репозиториями
        Observable<List<Repo>> repoList = new PagesConcatinator<>(
                () -> rxRepoApi.getRepoList(CLIENT_ID, CLIENT_SECRET),
                url -> rxRepoApi.getReposListByLink(url+"&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET))
            .getObservableT();
        Log.d("GithubAPI", "repoList:" + repoList.toString());
        return repoList
                .flatMap(Observable::fromIterable)//разбираем Observable<List<Repo>> на перебор Repo
                .flatMap( //в этом flatMap используется сигнатура с двумя функциями:
                        //первая возвращает список контрибуторов проекта...
                        repo -> new PagesConcatinator<>(
                                //вместо ссылки на метод лямбда, т.к. нужно передать параметр RepoName
                                () -> rxRepoApi.getСontributorsList(repo.getName(), CLIENT_ID, CLIENT_SECRET),
                                url -> rxRepoApi.getContributorsListByLink(url+"&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET))
                                .getObservableT(),
                        //...вторая использует результат первой:
                        //создаём объект (repo1, contributors) -> new RepoWithContributors(repo1, contributors));
                        RepoWithContributors::new);
    }

}
