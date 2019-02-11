package m13.retrofittest.main.githubUI;

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

import m13.retrofittest.R;
import m13.retrofittest.main.api.GithubRetorfitClient;
import m13.retrofittest.main.api.generated.repos.Repo;
import m13.retrofittest.main.api.repos.RepoType;
import m13.retrofittest.main.api.repos.ReposAsyncCallback;
import m13.retrofittest.main.api.repos.ReposEndpointInterface;
import m13.retrofittest.main.api.repos.ReposService;
import retrofit2.Call;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class ReposActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Repo> repos;
    private final static String organizationName = "square";
    private final static Integer maxNumberOfRepos = 1000;
    private ReposEndpointInterface repoApi;
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
                maxNumberOfRepos);
        call.enqueue(new ReposAsyncCallback(getWeakReference()));
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
        call.enqueue(new ReposAsyncCallback(getWeakReference()));
    }

    WeakReference<ReposActivity> getWeakReference(){
        return new WeakReference<>(this);
    }
}
