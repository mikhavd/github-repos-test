package m13.retrofittest.main.githubUI.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import m13.retrofittest.R;
import m13.retrofittest.main.api.generated.commits.Commit;
import m13.retrofittest.main.api.generated.commits.CommitData;
import m13.retrofittest.main.api.repos.IExtendedRepo;
import m13.retrofittest.main.api.services.APIInterface;
import m13.retrofittest.main.api.services.PagesConcatinator;
import m13.retrofittest.main.githubUI.CommitsAdapter;
import m13.retrofittest.main.githubUI.GithubApp;
import m13.retrofittest.main.githubUI.RecyclerViewClickListener;

import static m13.retrofittest.main.githubUI.GithubApp.CLIENT_ID;
import static m13.retrofittest.main.githubUI.GithubApp.CLIENT_SECRET;

/**
 * Created by Mikhail Avdeev on 19.02.2019.
 */
public class CommitsListActivity extends AppCompatActivity implements RecyclerViewClickListener {
    RecyclerView recyclerView;
    IExtendedRepo selectedRepo;
    private TextView emptyView;
    ArrayList<Commit> commitsFullList = new ArrayList<>();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        GithubApp app = (GithubApp) getApplicationContext();
        this.selectedRepo = app.getSelectedRepo();
        setTitle("Commits of " + selectedRepo.getName());
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.posts_recycle_view);
        emptyView = findViewById(R.id.empty_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        try {
            APIInterface rxRepoApi = app.getRxRepoApi();
            CommitsAdapter adapter = new CommitsAdapter(this, commitsFullList);
            recyclerView.setAdapter(adapter);
            setRecyclerView(commitsFullList);
            loadRepoCommitsList(rxRepoApi)
                    //loadExtendedReposWithPages(rxRepoApi)
                    .onErrorReturn((Throwable ex) -> {
                        handleException(ex);
                        //empty object of the datatype
                        return new ArrayList<>();
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::saveCommits, this::handleException);
        } catch (Exception e) {
            handleException(e);
        }

    }

    private void saveCommits(List<CommitData> commitDataList) {
        ArrayList<Commit> commits = new ArrayList<>();
            for (CommitData commitData : commitDataList)
                commits.add(commitData.getCommit());
            commitsFullList.addAll(commits);
        recyclerView.getAdapter().notifyDataSetChanged();
        setRecyclerView();
    }

    private void setRecyclerView() {
        if (commitsFullList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    private void handleException(Throwable throwable) {
        //todo
    }

    private Observable<List<CommitData>> loadRepoCommitsList(APIInterface rxApi) {
        return new PagesConcatinator<>(
                () -> rxApi.getCommitDataList(selectedRepo.getName(), CLIENT_ID, CLIENT_SECRET),
                url -> rxApi.getCommitDataListByLink(url + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET))
                .getObservableT();
    }

    private void setRecyclerView(List<Commit> commitList) {
        if (commitList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else{
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void recycleViewListClicked(View v, int position) {

    }
}

