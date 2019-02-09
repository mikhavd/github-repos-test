package m13.retrofittest;

import android.util.Log;

import org.junit.Test;

import java.util.List;

import m13.retrofittest.main.api.GithubService;
import m13.retrofittest.main.api.ReposAPI;
import m13.retrofittest.main.api.ReposEndpointInterface;
import m13.retrofittest.main.api.RetrofitClient;
import m13.retrofittest.main.repos.Repo;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RepoUnitTest {

    @Test
    public void OldReposTest(){
        //List<Repo> reposResponse = new GithubService().getRepoList();
        //assertTrue("true",reposResponse.size() > 0);
    }

    @Test
    public void NewRepoTest() {
        List<Repo> repos = null;
        //получаем список репозиториев
        ReposAPI reposAPI = new ReposAPI(new RetrofitClient());
        try {
            Integer maxNumberOfRepos = 1000;
            repos = reposAPI.getRepos("square", maxNumberOfRepos);
            for (Repo repo: repos) {
                System.out.println("repo.name: " + repo.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

         assertTrue(repos.size() > 0);
    }
}

