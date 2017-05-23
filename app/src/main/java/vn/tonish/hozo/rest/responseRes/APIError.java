package vn.tonish.hozo.rest.responseRes;

/**
 * Created by CanTran on 5/23/17.
 */

public class APIError {

    private String code;
    private String message;

    public APIError() {
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "APIError{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
