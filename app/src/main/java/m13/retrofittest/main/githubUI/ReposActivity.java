package m13.retrofittest.main.githubUI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import m13.retrofittest.R;
import m13.retrofittest.main.api.GithubRetorfitClient;
import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.generated.repos.Repo;
import m13.retrofittest.main.api.repos.RepoType;
import m13.retrofittest.main.api.repos.RepoWithContributors;
import m13.retrofittest.main.api.repos.ReposCallback;
import m13.retrofittest.main.api.repos.ReposInterface;
import m13.retrofittest.main.api.repos.ReposService;
import m13.retrofittest.main.api.repos.RxReposInterface;
import m13.retrofittest.main.api.repos.RxReposService;
import retrofit2.Call;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.util.functions.Func1;

import static rx.Observable.*;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class ReposActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Repo> repos;
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
        repos = new ArrayList<>();
        recyclerView = findViewById(R.id.posts_recycle_view);
        emptyView = (TextView) findViewById(R.id.empty_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ReposAdapter adapter = new ReposAdapter(repos);
        recyclerView.setAdapter(adapter);
        setRecyclerView();
        try {
            this.repoApi = new ReposService(new GithubRetorfitClient()).getApi();
            RxReposService rxService = new RxReposService(new GithubRetorfitClient());
            this.rxRepoApi = rxService.getApi();
            loadRepos();
        } catch (Exception e) {
            Log.d("exception", "exception: " + e.toString());
        }
    }

    private void setRecyclerView() {
        if (repos.isEmpty()){
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

    public void addRepos(List<Repo> repos) {
        this.repos.addAll(repos);
        recyclerView.getAdapter().notifyDataSetChanged();
        setRecyclerView();
    }

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


    //не получается, потому что у меня в Observable LISt!
    public Observable<RepoWithContributors> getRepoWithContributors() throws IOException {
        RepoType repoType = RepoType.all;
        Observable<List<Repo>> orgsList = rxRepoApi.organizationRepoList(
                organizationName,
                repoType.getRepoTypeName(),
                null,
                null,
                maxNumberPerPage);



                        new Func1<RepoWithContributors,
                        Observable<RepoWithContributors>>() {
                    @Override
                    public Observable<RepoWithContributors> call(
                            final RepoWithContributors repoWithContributors) {
                        return rxRepoApi.getContributorsList(
                                organizationName,
                                repoWithContributors.getRepoName(),
                                maxNumberPerPage)
                                .flatMap(new Func1<ArrayList<Contributor>,
                                        Observable<RepoWithContributors>>() {
                                    @Override
                                    public Observable<RepoWithContributors> call(ArrayList<Contributor> contributors) {
                                        repoWithContributors.setContributors(contributors);
                                        return just(repoWithContributors);
                                    }
                                });
                    }
                });
    }


    void test(){
        RepoType repoType = RepoType.all;
        List<RepoWithContributors> reposExtended = rxRepoApi.organizationRepoList(
                organizationName,
                repoType.getRepoTypeName(),
                null,
                null,
                maxNumberPerPage)
                .concatMap(Observable::from)
                .flatMap(item -> rxRepoApi.getContributorsList(
                                organizationName,
                                item.getName(),
                                maxNumberPerPage),
                        (item, detail) -> new RepoWithContributors(item, detail))
                .toList();

    }








    /*public Integer loadRepoContributors(Repo repo) {
        Call<List<Contributor>> call = this.repoApi.getContributorsList(
                organizationName,
                repo.getName(),
                1
        );
        call.enqueue(new ContributorsCallback(getWeakReference()));
        return
    }*/
}
