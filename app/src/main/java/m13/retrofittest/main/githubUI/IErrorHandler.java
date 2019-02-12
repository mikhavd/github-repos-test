package m13.retrofittest.main.githubUI;

import retrofit2.HttpException;

/**
 * Created by Mikhail Avdeev on 12.02.2019.
 */
public interface IErrorHandler {
    void handleError(HttpException ex);
}
