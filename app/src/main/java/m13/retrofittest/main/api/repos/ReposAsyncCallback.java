package m13.retrofittest.main.api.repos;

import android.widget.Toast;

import java.lang.ref.WeakReference;

import m13.retrofittest.main.api.HeaderParser;
import m13.retrofittest.main.githubUI.ReposActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class ReposAsyncCallback implements Callback {

    private final WeakReference<ReposActivity> activity;

    public ReposAsyncCallback(WeakReference<ReposActivity> activityWeakReference){
        this.activity = activityWeakReference;

    }
    @Override
    public void onResponse(Call call, Response response) {
        String nextLink = HeaderParser.parseHeaderLink(response);
        System.out.println("ReposeAPI: getRepos: nextLink: " + nextLink);
        activity.get().refreshRepos(ReposService.handleAsyncGetRepos(response));
        if (!nextLink.isEmpty()) activity.get().loadAdditionalRepos(nextLink);
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Toast.makeText(
                activity.get(),
                "An error occurred during networking", Toast.LENGTH_SHORT).show();
        //todo: использовать эту функцию
        //ErrorHandler.parseServerErrorCode(call.e.code());

    }
}
