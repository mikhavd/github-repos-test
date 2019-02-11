package m13.retrofittest.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import m13.retrofittest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 18.12.2018.
 */
public class UmoriliMainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<PostModel> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        posts = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.posts_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        PostsAdapter adapter = new PostsAdapter(posts);
        recyclerView.setAdapter(adapter);

        try {
            Call<List<PostModel>> call = UmoriliApp.getApi().getData("bash", 10);
            call.enqueue(new Callback<List<PostModel>>() {
                @Override
                public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                    List<PostModel> items = response.body();
                    posts.addAll(items);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<PostModel>> call, Throwable t) {
                    Toast.makeText(UmoriliMainActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("exception", "exception: " + e.toString());
        }


    }
}
