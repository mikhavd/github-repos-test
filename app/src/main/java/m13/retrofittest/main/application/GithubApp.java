package m13.retrofittest.main.application;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import m13.retrofittest.BuildConfig;
import m13.retrofittest.main.api.GithubRetrofitClient;
import m13.retrofittest.main.repos.IExtendedRepo;

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
        initStetho(this);
        this.githubClient = new GithubRetrofitClient();
    }

    /*
     * инициализация параметров для Stetho - библиотеки для отладки приложения
     * @param context - контекст
     */

    public  void initStetho(Context context)
    {

        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        // Enable command line interface
        initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(context));
        // Use the InitializerBuilder to generate an Initializer
        com.facebook.stetho.Stetho.Initializer initializer = initializerBuilder.build();
        // Initialize Stetho with the Initializer
        com.facebook.stetho.Stetho.initialize(initializer);
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
