package m13.retrofittest.main.api.repos;

import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import m13.retrofittest.main.api.HeaderParser;
import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.generated.repos.Repo;
import m13.retrofittest.main.githubUI.ReposActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class ReposCallback implements Callback {

    private final WeakReference<ReposActivity> activity;

    public ReposCallback(WeakReference<ReposActivity> activityWeakReference) {
        this.activity = activityWeakReference;

    }

    @Override
    public void onResponse(Call call, Response response) {
        List<Repo> obtainedRepos = handleAsyncGetRepos(response);
        for (Repo repo: obtainedRepos) {
            //Integer numberOfContributors = activity.get().loadRepoContributors(repo);
            RepoWithContributors repoWithContrubutors =
                    new RepoWithContributors(repo, new ArrayList<Contributor>());
        }
        //activity.get().addRepos(obtainedRepos);
       // String nextLink = HeaderParser.parseHeaderLink(response);
       // if (!nextLink.isEmpty()) {
         //   activity.get().loadAdditionalRepos(nextLink);
        //}
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Toast.makeText(
                activity.get(),
                "An error occurred during networking", Toast.LENGTH_SHORT).show();
        //todo: использовать эту функцию
        //ErrorHandler.parseServerErrorCode(call.e.code());

    }


    private List<Repo> handleAsyncGetRepos(Response repoResponce) {
        List<Repo> origRepos = (List<Repo>) repoResponce.body();
        List<Repo> repos = new ArrayList<>();
        repos.addAll(origRepos);
        return repos;
    }
}
