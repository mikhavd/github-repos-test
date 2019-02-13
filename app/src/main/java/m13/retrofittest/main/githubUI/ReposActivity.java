package m13.retrofittest.main.githubUI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import m13.retrofittest.main.api.repos.RepoWithContributors;
import m13.retrofittest.main.api.repos.ReposInterface;
import m13.retrofittest.main.api.repos.RxReposInterface;
import m13.retrofittest.main.api.repos.RxReposService;
import retrofit2.HttpException;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class ReposActivity extends AppCompatActivity
        implements RecyclerViewClickListener{
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
        ReposAdapter adapter = new ReposAdapter(this, this, extendedRepos);
        recyclerView.setAdapter(adapter);
        setRecyclerView();
        try {
            //this.repoApi = new ReposService(new GithubRetorfitClient()).getApi();
            RxReposService rxService = new RxReposService(new GithubRetorfitClient());
            this.rxRepoApi = rxService.getApi();
            loadReposWithContributors(rxRepoApi, this::saveRepo, this::printExInfo);
        } catch (Exception e) {
            emptyView.setText("Ошибка при загрузке данных: " + e.toString());
            Toast.makeText(
                    this,
                    "exception: " + e.toString(), Toast.LENGTH_SHORT).show(); //Log.d("exception", );
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


    @SuppressLint("CheckResult")
    public void loadReposWithContributors(RxReposInterface rxRepoApi, ILoader loader, IErrorHandler errorHandler) {
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
                    if (ex instanceof HttpException) errorHandler.handleError((HttpException) ex);
                    else
                        this.emptyView.setText(ex.toString());
            return new RepoWithContributors(null, null); //empty object of the datatype
        })
        .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(loader::save, Throwable::printStackTrace);
    }

    private void printExInfo(HttpException ex) {
        emptyView.setText(ex.toString());
        Toast.makeText(
                this,
                ex.toString(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, string);
    }

    private void saveRepo(RepoWithContributors repoWithContributors) {
        if (repoWithContributors.getContributors() == null) {
         //todo
            return;
        }
        extendedRepos.add(repoWithContributors);
        recyclerView.getAdapter().notifyDataSetChanged();
        setRecyclerView();
    }

    @Override
    public void recycleViewListClicked(View v, int position) {
        RepoWithContributors selectedRepo = extendedRepos.get(position);
        Toast.makeText(
                this,
                selectedRepo.getName(), Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this, ElementActivity.class);
        //this.startActivity(intent);
    }
}
