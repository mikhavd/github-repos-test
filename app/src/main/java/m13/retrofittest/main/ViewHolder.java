package m13.retrofittest.main;

import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import m13.retrofittest.R;

/**
 * Created by Mikhail Avdeev on 18.12.2018.
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView post;
    private TextView site;

    public ViewHolder(View itemView) {
        super(itemView);
        post = (TextView) itemView.findViewById(R.id.postitem_post);
        site = (TextView) itemView.findViewById(R.id.postitem_site);
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
}

