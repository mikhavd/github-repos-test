package m13.retrofittest.main.githubUI;

import android.app.Application;
import android.util.Log;

import m13.retrofittest.main.api.repos.RepoWithContributors;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class GithubApp extends Application {
    //private static UmoriliApi umoriliApi;
    //private Retrofit retrofit;
    private RepoWithContributors selectedRepo;

    @Override
    public void onCreate(){
        super.onCreate();

    }

    public RepoWithContributors getSelectedRepo() {
        return selectedRepo;
    }

    public void setSelectedRepo(RepoWithContributors selectedRepo) {
        this.selectedRepo = selectedRepo;
    }
    //public static UmoriliApi getApi() {
        //return umoriliApi;



}
