package m13.retrofittest;

import org.junit.Test;

import java.util.List;

import m13.retrofittest.main.api.GithubService;
import m13.retrofittest.main.repos.Repo;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RepoUnitTest {

    @Test
    public void ReposTest(){
        List<Repo> reposResponse = new GithubService().getRepoList();
        //assertTrue("true",reposResponse.size() > 0);
    }
}

