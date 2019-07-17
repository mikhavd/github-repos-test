package m13.retrofittest.main.application;

import android.app.Application;

import androidx.room.Room;

import m13.retrofittest.BuildConfig;
import m13.retrofittest.main.api.GithubRetrofitClient;
import m13.retrofittest.main.repos.IExtendedRepo;
import m13.retrofittest.main.database.AppDatabase;

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
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
        this.githubClient = new GithubRetrofitClient();
    }



    public IExtendedRepo getSelectedRepo() {
        return selectedRepo;
    }

    public void setSelectedRepo(IExtendedRepo selectedRepo) {
        this.selectedRepo = selectedRepo;
    }

    public <T> T getApiInterface(Class<T> tClass) {
        return githubClient.getRetrofit().create(tClass);
    }
}
