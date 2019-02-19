package m13.retrofittest.main.api.services;

import m13.retrofittest.main.api.ApiService;
import m13.retrofittest.main.api.GithubRetorfitClient;

/**
 * Created by Mikhail Avdeev on 11.02.2019.
 */
public class RxReposService extends ApiService<APIInterface> {

    public RxReposService(GithubRetorfitClient githubRetorfitClient) {
        super(githubRetorfitClient);
    }
}
