package m13.retrofittest.main.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import m13.retrofittest.R;
import m13.retrofittest.main.repos.Repozitory;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class RepozitoriesListAdapter extends RecyclerView.Adapter<RepozitoriesListAdapter.RepozitoryViewHolder> {

    class RepozitoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView repositoryTextView;

        private RepozitoryViewHolder(View repositoryView) {
            super(repositoryView);
            repositoryTextView = itemView.findViewById(R.id.textView);//repositoryView;
        }
    }

    private final LayoutInflater layoutInflater;
    private List<Repozitory> repozitoryList; // Cached copy of words


    public RepozitoriesListAdapter(Context context) { layoutInflater = LayoutInflater.from(context); }

    @Override
    public RepozitoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new RepozitoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RepozitoryViewHolder holder, int position) {
        if (repozitoryList != null) {
            Repozitory currentRepozitory = repozitoryList.get(position);
            holder.repositoryTextView.setText(currentRepozitory.getFullName());
        } else {
            // Covers the case of data not being ready yet.
            holder.repositoryTextView.setText("No repositories");
        }
    }

    public void setRepositories(List<Repozitory> repositories){
        repozitoryList = repositories;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // repositoriesList has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (repozitoryList != null)
            return repozitoryList.size();
        else return 0;
    }
}
