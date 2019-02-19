package m13.retrofittest.main.githubUI;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import m13.retrofittest.R;
import m13.retrofittest.main.api.repos.ExtendedRepo;
import m13.retrofittest.main.api.repos.IExtendedRepo;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class ReposAdapter extends RecyclerView.Adapter<BasicViewHolder> {


    private final RecyclerViewClickListener itemListener;
    private List<IExtendedRepo> repos;

    public ReposAdapter(RecyclerViewClickListener itemListener,
                        List<IExtendedRepo> repos) {
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
        IExtendedRepo repo = repos.get(position);
        holder.setMainText(repo.getName());
        holder.setBottomRightText("★: " +
                String.valueOf(repo.getStargazersCount()));
        holder.setBottomLeftText(String.valueOf(position + 1));
        holder.setTopRightText("Contibutors: " +
                String.valueOf(repo.getContributorsNumber()));

    }

    @Override
    public int getItemCount() {
        if (repos == null)
            return 0;
        return repos.size();
    }
}
