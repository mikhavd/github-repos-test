package m13.retrofittest.main.githubUI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import m13.retrofittest.R;
import m13.retrofittest.main.api.GithubRetorfitClient;
import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.generated.repos.Repo;
import m13.retrofittest.main.api.repos.RepoType;
import m13.retrofittest.main.api.repos.RepoWithContributors;
import m13.retrofittest.main.api.repos.ReposCallback;
import m13.retrofittest.main.api.repos.ReposInterface;
import m13.retrofittest.main.api.repos.RxReposInterface;
import m13.retrofittest.main.api.repos.RxReposService;
import retrofit2.Call;
import retrofit2.HttpException;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class ReposActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    //List<Repo> repos;
    List<RepoWithContributors> extendedRepos;
    private final static String organizationName = "square";
    private final static Integer maxNumberPerPage = 1000;
    private ReposInterface repoApi;
    private RxReposInterface rxRepoApi;
    private TextView emptyView;

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
        ReposAdapter adapter = new ReposAdapter(extendedRepos);
        recyclerView.setAdapter(adapter);
        setRecyclerView();
        try {
            //this.repoApi = new ReposService(new GithubRetorfitClient()).getApi();
            RxReposService rxService = new RxReposService(new GithubRetorfitClient());
            this.rxRepoApi = rxService.getApi();
            //loadRepos();
            loadReposWithContributors(rxRepoApi, this::saveRepo, this::printExInfo);
        } catch (Exception e) {
            Log.d("exception", "exception: " + e.toString());
        }
    }

    private void setRecyclerView() {
        if (extendedRepos.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else{
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    //"оригинальная" загрузка списка репозиториев
    void loadRepos() {
        RepoType repoType = RepoType.all;
        Call<List<Repo>> call = this.repoApi.organizationRepoList(
                organizationName,
                repoType.getRepoTypeName(),
                null,
                null,
                maxNumberPerPage);
        call.enqueue(new ReposCallback(getWeakReference()));
    }

    /*public void addRepos(List<Repo> repos) {
        this.extendedRepos.addAll(repos);
        recyclerView.getAdapter().notifyDataSetChanged();
        setRecyclerView();
    }*/

    //загрузка дополнительных репозиториев, по прямой ссылке
    public void loadAdditionalRepos(String nextLink) {
        Toast.makeText(
                this,
                "loadingAdditionalRepos, link: " + nextLink, Toast.LENGTH_SHORT).show();
        Call<List<Repo>> call = this.repoApi.organizationRepoListByLink(nextLink);
        call.enqueue(new ReposCallback(getWeakReference()));
    }

    WeakReference<ReposActivity> getWeakReference(){
        return new WeakReference<>(this);
    }


    @SuppressLint("CheckResult")
    public static void loadReposWithContributors(RxReposInterface rxRepoApi, ILoader loader, IErrorHandler errorHandler) {
        Function<Repo, Observable<List<Contributor>>> contibsList = new Function<Repo, Observable<List<Contributor>>>() {
            @Override
            public Observable<List<Contributor>> apply(Repo repo){
                return rxRepoApi.getContribsList(repo.getName());
            }
        };
        rxRepoApi.getRepoList()
                //функция разбирает Observable<List<Repo>> на перебор Repo
                .flatMap(Observable::fromIterable)
                //функция получает список всех контрибуторов проекта
                .flatMap(contibsList,
                        RepoWithContributors::new)
                .onErrorReturn((Throwable ex) -> {
                    if (ex instanceof HttpException) errorHandler.handleError((HttpException) ex);
            //printExInfo(ex.toString()); //examine error here
            return new RepoWithContributors(null, null); //empty object of the datatype
        }).subscribe(loader::save, Throwable::printStackTrace);
    }

    private void printExInfo(HttpException ex) {
        Toast.makeText(
                this,
                ex.toString(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, string);
    }

    private void saveRepo(RepoWithContributors repoWithContributors) {
        if (repoWithContributors.getContributors() == null){
         //todo
        }
        extendedRepos.add(repoWithContributors);
        recyclerView.getAdapter().notifyDataSetChanged();
        setRecyclerView();
    }
}
