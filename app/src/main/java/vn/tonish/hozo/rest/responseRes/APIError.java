package vn.tonish.hozo.rest.responseRes;

/**
 * Created by CanTran on 5/23/17.
 */

public class APIError {

    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}
