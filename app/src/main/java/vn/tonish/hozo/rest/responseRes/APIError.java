package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CanTran on 5/23/17.
 */

public class APIError {

    @SerializedName("code")
    private String statusCode;
    private String message;

    public APIError() {
    }

    public String status() {
        return statusCode;
    }

    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return "APIError{" +
                "statusCode='" + statusCode + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}
