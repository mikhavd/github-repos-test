package m13.retrofittest.main.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import m13.retrofittest.R;
import m13.retrofittest.main.api.retrofitgenerated.contributors.Contributor;

/**
 * Created by Mikhail Avdeev on 13.02.2019.
 */
public class ContributorsAdapter extends RecyclerView.Adapter<BasicViewHolder> {


    private final RecyclerViewClickListener itemListener;
    private List<Contributor> contributorList;


    public ContributorsAdapter(RecyclerViewClickListener itemListener,
                               List<Contributor> contributorList) {
        this.itemListener = itemListener;
        this.contributorList = contributorList;
    }

    @NonNull
    @Override
    public BasicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new BasicViewHolder(v, this.itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BasicViewHolder holder, int position) {
        Contributor contributor = contributorList.get(position);
        holder.setMainText(contributor.getLogin());
        holder.setBottomLeftText(String.valueOf(position + 1));
        holder.setTopRightText("Contributions: " +
                String.valueOf(contributor.getContributions()));
        holder.setBottomRightText("");
    }

    @Override
    public int getItemCount() {
        if (contributorList == null)
            return 0;
        return contributorList.size();
    }

}
