package m13.retrofittest.main.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import m13.retrofittest.R;
import m13.retrofittest.main.repos.Repository;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class RepositoriesListAdapter extends RecyclerView.Adapter<RepositoriesListAdapter.RepositoryViewHolder> {

    class RepositoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView repositoryTextView;

        private RepositoryViewHolder(View repositoryView) {
            super(repositoryView);
            repositoryTextView = itemView.findViewById(R.id.textView);//repositoryView;
        }
    }

    private final LayoutInflater layoutInflater;
    private List<Repository> repositoryList; // Cached copy of words


    public RepositoriesListAdapter(Context context) { layoutInflater = LayoutInflater.from(context); }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new RepositoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        if (repositoryList != null) {
            Repository currentRepository = repositoryList.get(position);
            holder.repositoryTextView.setText(currentRepository.getFullName());
        } else {
            // Covers the case of data not being ready yet.
            holder.repositoryTextView.setText("No repositories");
        }
    }

    public void setRepositories(List<Repository> repositories){
        repositoryList = repositories;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // repositoriesList has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (repositoryList != null)
            return repositoryList.size();
        else return 0;
    }
}
