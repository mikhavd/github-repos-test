package m13.retrofittest.main.githubUI;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import m13.retrofittest.R;
import m13.retrofittest.main.api.repos.RepoWithContributors;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
class ReposAdapter extends RecyclerView.Adapter<BasicViewHolder> {


    private final RecyclerViewClickListener itemListener;
    private List<RepoWithContributors> repos;

    ReposAdapter(RecyclerViewClickListener itemListener,
                 List<RepoWithContributors> repos) {
        this.itemListener = itemListener;
        this.repos = repos;
    }

    @NonNull
    @Override
    public BasicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item, parent, false);
        return new BasicViewHolder(v, this.itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BasicViewHolder holder, int position) {
        RepoWithContributors repo = repos.get(position);
        holder.setMainText(repo.getName());
        holder.setBottomRightText("★: " +
                String.valueOf(repo.getStargazersCount()));
        holder.setBottomLeftText(String.valueOf(position + 1));
        holder.setTopRightText("Contibutors: " +
                String.valueOf(repo.getContributorsSize()));

    }

    @Override
    public int getItemCount() {
        if (repos == null)
            return 0;
        return repos.size();
    }
}
