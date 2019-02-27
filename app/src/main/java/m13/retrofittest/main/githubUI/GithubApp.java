package m13.retrofittest.main.githubUI;

import android.app.Application;

import m13.retrofittest.BuildConfig;
import m13.retrofittest.main.api.GithubRetrofitClient;
import m13.retrofittest.main.api.repos.IExtendedRepo;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class GithubApp extends Application {
    public static final String CLIENT_ID = m13.retrofittest.BuildConfig.CLIENT_ID;
    public static final String CLIENT_SECRET = BuildConfig.CLIENT_SECRET;
    private IExtendedRepo selectedRepo;
    private GithubRetrofitClient githubClient;

    @Override
    public void onCreate(){
        super.onCreate();
        this.githubClient = new GithubRetrofitClient();
        //ApiService<APIInterface> apiService = new ApiService<>(githubClient);
        //apiInterface = apiService.getApi();
    }



    public IExtendedRepo getSelectedRepo() {
        return selectedRepo;
    }

    public void setSelectedRepo(IExtendedRepo selectedRepo) {
        this.selectedRepo = selectedRepo;
    }

    public <T> T getApiInterface(Class<T> tClass) {
        //return (new ApiService<>(githubClient, tClass)).getApi();
        return githubClient.getRetrofit().create(tClass);
    }


    //public APIInterface getRxRepoApi() {
        //return apiInterface;
    //}
}
