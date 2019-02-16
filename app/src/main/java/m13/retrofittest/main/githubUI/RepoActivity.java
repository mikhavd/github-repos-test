package m13.retrofittest.main.githubUI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import m13.retrofittest.R;
import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.repos.ExtendedRepo;
import m13.retrofittest.main.api.repos.IExtendedRepo;

/**
 * Created by Mikhail Avdeev on 13.02.2019.
 */
public class RepoActivity extends AppCompatActivity implements RecyclerViewClickListener {
    RecyclerView recyclerView;
    IExtendedRepo repo;
    List<Contributor> contributorList;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        GithubApp app = (GithubApp) getApplicationContext();
        this.repo = app.getSelectedRepo();
        setContentView(R.layout.activity_main);
        //todo для тех IExtendedRepo, у которых при передаче в эту activity нет своего
        //контрибутор-листа, нужно создать загрузку контрибуторов
        //contributorList = repo.getContributors();
        recyclerView = findViewById(R.id.posts_recycle_view);
        emptyView = (TextView) findViewById(R.id.empty_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ContributorsAdapter adapter = new ContributorsAdapter(this, contributorList);
        recyclerView.setAdapter(adapter);
        setRecyclerView();

    }

    private void setRecyclerView() {
        if (contributorList.isEmpty()){
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
