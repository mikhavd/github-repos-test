package m13.retrofittest.main.githubUI;

/**
 * Created by Mikhail Avdeev on 19.02.2019.
 */
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import m13.retrofittest.R;
import m13.retrofittest.main.api.generated.commits.Commit;


/**
 * Created by Mikhail Avdeev on 13.02.2019.
 */
public class CommitsAdapter extends RecyclerView.Adapter<BasicViewHolder> {


    private final RecyclerViewClickListener itemListener;
    private List<Commit> commits;


    public CommitsAdapter(RecyclerViewClickListener itemListener,
                               List<Commit> commits) {
        this.itemListener = itemListener;
        this.commits = commits;
    }

    @NonNull
    @Override
    public BasicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item, parent, false);
        return new BasicViewHolder(v, this.itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BasicViewHolder holder, int position) {
        Commit commit = commits.get(position);
        holder.setMainText(commit.getMessage());//.getLogin());
        holder.setBottomLeftText(String.valueOf(position + 1));
        holder.setTopRightText("Committer: " +
                commit.getCommitter().getName());//contributor.getContributions()));
        holder.setBottomRightText("");
    }

    @Override
    public int getItemCount() {
        if (commits == null)
            return 0;
        return commits.size();
    }

}

