package m13.retrofittest;

import org.junit.Test;

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

    //@Test
    /*public void SyncRepoTest() {
        List<Repo> repos = null;
        //получаем список репозиториев
        ReposService reposService = new ReposService(new GithubRetorfitClient());
        try {
            Integer maxNumberOfRepos = 1000;
            repos = reposService.getRepos("square", maxNumberOfRepos);
            for (Repo repo: repos) {
                System.out.println("repo.name: " + repo.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

         assertTrue(repos.size() > 0);
    }*/


}

