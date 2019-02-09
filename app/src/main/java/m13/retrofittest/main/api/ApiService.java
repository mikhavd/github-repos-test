package m13.retrofittest.main.api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import m13.retrofittest.main.api.errors.ErrorHandler;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Mikhail Avdeev on 09.02.2019.
 */
class ApiService<Api, T> {
    protected RetrofitClient apiClient;
    protected Api api;
    private static final String SEPARATOR = ","; //$NON-NLS-1$
    private static final String DOUBLE_SEPARATOR = ";"; //$NON-NLS-1$

    ApiService(RetrofitClient retrofitClient) {
        this.api =
                retrofitClient.getRetrofit().create(getManagedClass());
    }

    Response<T> secureExecute(Call<T> call) throws Exception {
        Response<T> response = call.execute();
        if (!response.isSuccessful()) {
            ErrorHandler.parseServerErrorCode(response.code());
        }
        return response;
    }


    //protected <T> T getSecureResponseBody(Call<T> call, Class<T> type) throws Exception {
        //Response<T> response = secureExecute(call);
        //return type.cast(response.body());
    //}

    private Class<Api> getManagedClass() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] actualTypeArguments = superclass.getActualTypeArguments();
        Type type = actualTypeArguments[0];
        return (Class<Api>) type;
    }

    protected String parseHeaderLink(Response response) {
        Headers headers = response.headers();
        String linkHeader = headers.get("link");
        String next = "";
        if (linkHeader != null) {
            String[] links = linkHeader.split(SEPARATOR);
            links: for (String link : links) {
                String[] segments = link.split(DOUBLE_SEPARATOR);
                if (segments.length < 2)
                    continue;
                String linkPart = segments[0].trim();
                if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) //$NON-NLS-1$ //$NON-NLS-2$
                    continue;
                linkPart = linkPart.substring(1, linkPart.length() - 1);

                linkElements: for (int i = 1; i < segments.length; i++) {
                    String[] rel = segments[i].trim().split("="); //$NON-NLS-1$
                    if (rel.length < 2) //|| !META_REL.equals(rel[0]))
                        continue;

                    String relValue = rel[1];
                    if (relValue.startsWith("\"") && relValue.endsWith("\"")) //$NON-NLS-1$ //$NON-NLS-2$
                        relValue = relValue.substring(1, relValue.length() - 1);
                    if (relValue.equals("next")) {
                        next = linkPart;
                        break links;
                    }
                    //if (META_FIRST.equals(relValue)) first = linkPart;
                    //else if (META_LAST.equals(relValue)) last = linkPart;
                    //else if (META_NEXT.equals(relValue)) next = linkPart;
                    //else if (META_PREV.equals(relValue)) prev = linkPart;
                }
            }
        }
        //} else {
        //next = response.getHeader(HEADER_NEXT);
        //last = response.getHeader(HEADER_LAST);
        //}
        return next;
    }

}
