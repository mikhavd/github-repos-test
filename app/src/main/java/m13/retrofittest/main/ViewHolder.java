package m13.retrofittest.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import m13.retrofittest.R;

/**
 * Created by Mikhail Avdeev on 18.12.2018.
 */
class ViewHolder extends RecyclerView.ViewHolder {
    TextView post;
    TextView site;

    ViewHolder(View itemView) {
        super(itemView);
        post = (TextView) itemView.findViewById(R.id.postitem_post);
        site = (TextView) itemView.findViewById(R.id.postitem_site);
    }
}

