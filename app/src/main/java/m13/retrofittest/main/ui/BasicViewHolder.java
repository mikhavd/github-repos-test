package m13.retrofittest.main.ui;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import m13.retrofittest.R;
import m13.retrofittest.main.githubUI.RecyclerViewClickListener;

/**
 * Created by Mikhail Avdeev on 18.12.2018.
 */
public class BasicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final RecyclerViewClickListener itemListener;
    private TextView topRightText;
    private TextView bottomLeftText;
    private TextView mainText;
    private TextView bottomRightText;


    public BasicViewHolder(View itemView,
                           RecyclerViewClickListener itemListener) {
        super(itemView);
        this.itemListener = itemListener;
        mainText = (TextView) itemView.findViewById(R.id.main_text);
        bottomRightText = (TextView) itemView.findViewById(R.id.bottom_rigth_item);
        bottomLeftText = (TextView) itemView.findViewById(R.id.bottom_left_item);
        topRightText = (TextView) itemView.findViewById(R.id.top_right_item);
        itemView.setOnClickListener(this);
    }

    public void setMainText(String text) {
        this.mainText.setText(text);
    }

    public void setBottomRightText(String text) {
        this.bottomRightText.setText(text);
    }

    public void setBottomLeftText(String bottomLeftText){
        this.bottomLeftText.setText(bottomLeftText);
    }

    public void setTopRightText(String commits) {
        this.topRightText.setText(commits);
    }

    @Override
    public void onClick(View v) {
        itemListener.recycleViewListClicked(
                v, this.getLayoutPosition());
    }
}

