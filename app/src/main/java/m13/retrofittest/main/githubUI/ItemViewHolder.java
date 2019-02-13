package m13.retrofittest.main.githubUI;

import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import m13.retrofittest.R;
import m13.retrofittest.main.githubUI.RecyclerViewClickListener;

/**
 * Created by Mikhail Avdeev on 18.12.2018.
 */
public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final RecyclerViewClickListener itemListener;
    private TextView commits;
    private TextView index;
    private TextView post;
    private TextView site;


    public ItemViewHolder(View itemView,
                          RecyclerViewClickListener itemListener) {
        super(itemView);
        this.itemListener = itemListener;
        post = (TextView) itemView.findViewById(R.id.repoitem);
        site = (TextView) itemView.findViewById(R.id.postitem_site);
        index = (TextView) itemView.findViewById(R.id.index);
        commits = (TextView) itemView.findViewById(R.id.commits);
        itemView.setOnClickListener(this);
    }

    public void setPostText(String text) {
        this.post.setText(text);
    }

    public void setPostSpanned(Spanned text) {
        this.post.setText(text);
    }

    public void setSiteText(String text) {
        this.site.setText(text);
    }

    public void setIndex(String index){
        this.index.setText(index);
    }

    public void setCommitsText(String commits) {
        this.commits.setText(commits);
    }

    @Override
    public void onClick(View v) {
        itemListener.recycleViewListClicked(
                v, this.getLayoutPosition());
    }
}

