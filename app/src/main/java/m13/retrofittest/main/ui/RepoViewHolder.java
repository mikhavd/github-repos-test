package m13.retrofittest.main.ui;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import m13.retrofittest.R;

/**
 * Created by Mikhail Avdeev on 19.02.2019.
 */
public class RepoViewHolder extends
        RecyclerView.ViewHolder implements View.OnClickListener {
    private final RecyclerViewClickListener itemListener;
    private TextView repoName;
    private TextView stargazersNumber;
    private TextView contributorsNumber;

    RepoViewHolder(View itemView,
                   RecyclerViewClickListener itemListener) {
        super(itemView);
        this.itemListener = itemListener;
        repoName = itemView.findViewById(R.id.main_text);
        stargazersNumber = itemView.findViewById(R.id.stargazers_text);
        contributorsNumber = itemView.findViewById(R.id.contributors_text);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemListener.recycleViewListClicked(
                v, this.getLayoutPosition());
    }

    public void setRepoName(String repoName) {
        this.repoName.setText(repoName);
    }

    public void setStargazersNumber(String stargazersNumber) {
        this.stargazersNumber.setText(stargazersNumber);
    }

    public void setContributorsNumber(String contributorsNumber) {
        this.contributorsNumber.setText(contributorsNumber);
    }
}
