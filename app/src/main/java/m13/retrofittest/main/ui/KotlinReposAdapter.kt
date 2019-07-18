package m13.retrofittest.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import m13.retrofittest.R
import m13.retrofittest.main.githubUI.RecyclerViewClickListener
import m13.retrofittest.main.repos.IExtendedRepo

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
private class KotlinReposAdapter(private val itemListener: RecyclerViewClickListener,
                         private val repos: List<IExtendedRepo>?) : RecyclerView.Adapter<RepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.repo_list_item, parent, false)
        return RepoViewHolder(v, this.itemListener)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = repos!![position]
        holder.setRepoName(repo.fullName)
        holder.setStargazersNumber(repo.stargazersCount.toString())
        holder.setContributorsNumber(repo.contributorsNumber.toString())
    }

    override fun getItemCount(): Int {
        return repos?.size ?: 0
    }
}