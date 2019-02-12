package m13.retrofittest;

import org.junit.Test;

import m13.retrofittest.main.api.GithubRetorfitClient;
import m13.retrofittest.main.api.generated.contributors.Contributor;
import m13.retrofittest.main.api.repos.RepoWithContributors;
import m13.retrofittest.main.api.repos.RxReposInterface;
import m13.retrofittest.main.api.repos.RxReposService;
import m13.retrofittest.main.githubUI.ReposActivity;
import retrofit2.HttpException;

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

    private Integer reposCount = 1;
    private Integer contribsCount = 0;

    @Test
    public void rxAPITest(){
        RxReposService rxService = new RxReposService(new GithubRetorfitClient());
        RxReposInterface rxRepoApi = rxService.getApi();
        ReposActivity.loadReposWithContributors(rxRepoApi, this::print, this::printExInfo);
    }

    private void printExInfo(HttpException ex){
        System.out.println("-------------");
        System.out.println("ex: " + ex.toString());
        System.out.println("    message: " + ex.message());
        System.out.println("-------------");
    }

    private void print(RepoWithContributors repo){
        if (repo.getContributors() == null){
            System.out.println("-------------");
            System.out.println("ERROR");
            System.out.println("-------------");
            return;
        }
        System.out.println("-------------");
        System.out.println(reposCount + ". " + repo.getName());
        System.out.println("contributors:" );
        for (Contributor contributor : repo.getContributors()){
            System.out.println("    " + contribsCount +". " + contributor.getLogin());
            contribsCount++;
        }
        contribsCount = 0;
        reposCount++;
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

