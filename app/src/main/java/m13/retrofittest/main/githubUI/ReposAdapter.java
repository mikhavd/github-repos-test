package m13.retrofittest.main.githubUI;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import m13.retrofittest.R;
import m13.retrofittest.main.ViewHolder;
import m13.retrofittest.main.api.generated.repos.Repo;
import m13.retrofittest.main.api.repos.RepoWithContributors;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
class ReposAdapter extends RecyclerView.Adapter<ViewHolder> {


    private List<RepoWithContributors> repos;

    ReposAdapter(List<RepoWithContributors> repos) {
        this.repos = repos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RepoWithContributors repo = repos.get(position);
        holder.setPostText(repo.getName());
        holder.setSiteText("★: " +
                String.valueOf(repo.getStargazersCount()));
        holder.setIndex(String.valueOf(position + 1));
        holder.setCommitsText("Contibutors: " +
                String.valueOf(repo.getContributorsSize()));

    }

    @Override
    public int getItemCount() {
        if (repos == null)
            return 0;
        return repos.size();
    }
}
