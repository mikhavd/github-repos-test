package m13.retrofittest;

import org.junit.Test;

import m13.retrofittest.main.api.GithubRetorfitClient;
import m13.retrofittest.main.api.services.APIInterface;
import m13.retrofittest.main.api.services.RxReposService;
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

    @Test
    public void rxAPITestPages(){
        RxReposService rxService = new RxReposService(new GithubRetorfitClient());
        APIInterface rxRepoApi = rxService.getApi();
        //OrganizationReposActivity.loadExtendedReposFromPages(rxRepoApi, this::print, this::printExInfo);
    }


    private void printExInfo(Exception ex){
        System.out.println("-------------");
        System.out.println("ex: " + ex.toString());
        if (ex instanceof HttpException)
            System.out.println("    message: " + ((HttpException)ex).message());
        System.out.println("-------------");
    }

}

