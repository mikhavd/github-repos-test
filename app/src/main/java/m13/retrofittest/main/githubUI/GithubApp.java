package m13.retrofittest.main.githubUI;

import android.app.Application;
import android.util.Log;

import m13.retrofittest.BuildConfig;
import m13.retrofittest.main.api.repos.RepoWithContributors;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class GithubApp extends Application {
    public static final String CLIENT_ID = m13.retrofittest.BuildConfig.CLIENT_ID;
    public static final String CLIENT_SECRET = BuildConfig.CLIENT_SECRET;

    private RepoWithContributors selectedRepo;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("githubAPP", " CLIENT_ID: " + CLIENT_ID);
        Log.d("githubAPP", " CLIENT_SECRET: " + CLIENT_SECRET);

    }

    public RepoWithContributors getSelectedRepo() {
        return selectedRepo;
    }

    public void setSelectedRepo(RepoWithContributors selectedRepo) {
        this.selectedRepo = selectedRepo;
    }
}
