package util;

import java.util.HashMap;

public class CommonUtils {

    private static final HashMap<Integer, String> errorCodeAndMessageMap = new HashMap<>();
    static {
        errorCodeAndMessageMap.put(404, "the user or role can not be found in db");
        errorCodeAndMessageMap.put(409, "the user has already existed");
        errorCodeAndMessageMap.put(400, "the user or password can not be empty");
        errorCodeAndMessageMap.put(401, "authenticate error: the user or password is incorrect");
        errorCodeAndMessageMap.put(403, "token is incorrect or expired, please login");
        errorCodeAndMessageMap.put(416, "the user does not have the role");
    }

    public static BaseResult constructBaseResult(boolean isSuccess, int code, Object... data){
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(code);
        if(isSuccess){
            baseResult.setSuccess(isSuccess);
            if(data != null && data.length > 0){
                baseResult.setData(data[0]);
            }
        }
        else baseResult.setErrorMessage(errorCodeAndMessageMap.get(code));
        return baseResult;
    }
}
