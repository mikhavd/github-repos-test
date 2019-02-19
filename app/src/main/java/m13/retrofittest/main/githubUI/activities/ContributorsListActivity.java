package m13.retrofittest.main.githubUI.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import m13.retrofittest.R;
import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.repos.IExtendedRepo;
import m13.retrofittest.main.api.services.PagesConcatinator;
import m13.retrofittest.main.api.services.APIInterface;
import m13.retrofittest.main.githubUI.ContributorsAdapter;
import m13.retrofittest.main.githubUI.GithubApp;
import m13.retrofittest.main.githubUI.RecyclerViewClickListener;

import static m13.retrofittest.main.githubUI.GithubApp.CLIENT_ID;
import static m13.retrofittest.main.githubUI.GithubApp.CLIENT_SECRET;

/**
 * Created by Mikhail Avdeev on 13.02.2019.
 */
public class ContributorsListActivity extends AppCompatActivity implements RecyclerViewClickListener {
    RecyclerView recyclerView;
    IExtendedRepo selectedRepo;
    //List<Contributor> contributorList;
    private TextView emptyView;
    ArrayList<Contributor> contributorsFullList = new ArrayList<>();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        GithubApp app = (GithubApp) getApplicationContext();
        this.selectedRepo = app.getSelectedRepo();
        setTitle("Contributors of " + selectedRepo.getName());
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.posts_recycle_view);
        emptyView = findViewById(R.id.empty_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        ContributorsAdapter adapter = new ContributorsAdapter(this,
                contributorsFullList);
        recyclerView.setAdapter(adapter);
        setRecyclerView();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        try {
            APIInterface rxRepoApi = app.getRxRepoApi();

            loadRepoContributorsList(rxRepoApi)
                    //loadExtendedReposWithPages(rxRepoApi)
                    .onErrorReturn((Throwable ex) -> {
                        handleException(ex);
                        //empty object of the datatype
                        return new ArrayList<>();
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::saveContributors, this::handleException);
        } catch (Exception e) {
            handleException(e);
        }

    }

    private void saveContributors(List<Contributor> contributors) {
        contributorsFullList.addAll(contributors);
        recyclerView.getAdapter().notifyDataSetChanged();
        setRecyclerView();
    }

    private void handleException(Throwable throwable) {
        //todo
    }

    private Observable<List<Contributor>> loadRepoContributorsList(APIInterface rxApi) {
        return new PagesConcatinator<>(
                () -> rxApi.getContributorsList(selectedRepo.getName(), CLIENT_ID, CLIENT_SECRET),
                url -> rxApi.getContributorsListByLink(url + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET))
                .getObservableT();
    }

    private void setRecyclerView() {
        if (contributorsFullList.isEmpty()){
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
