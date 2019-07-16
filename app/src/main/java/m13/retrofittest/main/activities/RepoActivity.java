package m13.retrofittest.main.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import m13.retrofittest.R;
import m13.retrofittest.main.api.repos.IExtendedRepo;
import m13.retrofittest.main.ui.GithubApp;

/**
 * Created by Mikhail Avdeev on 19.02.2019.
 */
public class RepoActivity extends AppCompatActivity {
    IExtendedRepo selectedRepo;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GithubApp app = (GithubApp) getApplicationContext();
        this.selectedRepo = app.getSelectedRepo();
        setTitle("Repo: " + selectedRepo.getName());
        setContentView(R.layout.repo_activity);
        TextView infoView = findViewById(R.id.repo_info);
        infoView.setText(selectedRepo.getRepoInfo());
        Button commitsButton = findViewById(R.id.button_commits);
        commitsButton.setOnClickListener(click -> runCommitsActivity());
        Button contributorsButton = findViewById(R.id.button_contributors);
        contributorsButton.setOnClickListener(click -> runContributorsActivity());

    }

    private void runContributorsActivity() {
        if (selectedRepo != null) {
            Intent intent = new Intent(this,
                    ContributorsListActivity.class);
            this.startActivity(intent);
        }
    }

    private void runCommitsActivity() {
        if (selectedRepo != null) {
            Intent intent = new Intent(this,
                    CommitsListActivity.class);

            this.startActivity(intent);
        }
    }

}
