package m13.retrofittest.main.api.errors;

import m13.retrofittest.R;

/**
 * Created by Mikhail Avdeev on 09.02.2019.
 */
public class ErrorHandler {
    public final static int UNAUTHORIZED = 401;
    public final static int BAD_REQUEST = 400;
    public final static int ACCESS_DENIED = 403;
    public final static int INTERNAL_SERVER_ERROR = 500;
    public final static int CONFLICT = 409;
    //ошибка добавлена для обработки исключения IOException при выполнении запроса по http
    public final static int UNKNOWN_ERROR = 520;



    public static void parseServerErrorCode(int code) throws Exception {
        int msgId;
        switch (code) {
            case UNAUTHORIZED:
                msgId = R.string.str_auth_error;
                break;
            case ACCESS_DENIED:
                msgId = R.string.has_no_rights_for_archive;
                break;
            case CONFLICT:
            case BAD_REQUEST:
            case INTERNAL_SERVER_ERROR:
            default:
                msgId = R.string.str_server_communication_error;
                break;
        }
        //ApiError apiError = new ApiError(code, msgId);
        throw new Exception("Ошибка при загрузке данных"); //CommunicationException("message");
    }
}
