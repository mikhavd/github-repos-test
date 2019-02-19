package m13.retrofittest.main.githubUI;

import android.app.Application;
import android.util.Log;

import m13.retrofittest.BuildConfig;
import m13.retrofittest.main.api.GithubRetorfitClient;
import m13.retrofittest.main.api.repos.IExtendedRepo;
import m13.retrofittest.main.api.services.APIInterface;
import m13.retrofittest.main.api.services.RxReposService;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class GithubApp extends Application {
    public static final String CLIENT_ID = m13.retrofittest.BuildConfig.CLIENT_ID;
    public static final String CLIENT_SECRET = BuildConfig.CLIENT_SECRET;
    private APIInterface rxRepoApi;
    private IExtendedRepo selectedRepo;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("githubAPP", " CLIENT_ID: " + CLIENT_ID);
        Log.d("githubAPP", " CLIENT_SECRET: " + CLIENT_SECRET);
        RxReposService rxService = new RxReposService(new GithubRetorfitClient());
        this.rxRepoApi = rxService.getApi();

    }



    public IExtendedRepo getSelectedRepo() {
        return selectedRepo;
    }

    public void setSelectedRepo(IExtendedRepo selectedRepo) {
        this.selectedRepo = selectedRepo;
    }

    public APIInterface getRxRepoApi() {
        return rxRepoApi;
    }
}
