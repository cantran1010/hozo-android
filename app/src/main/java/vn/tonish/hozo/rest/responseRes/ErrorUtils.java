package vn.tonish.hozo.rest.responseRes;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

import static vn.tonish.hozo.rest.ApiClient.retrofit;

/**
 * Created by CanTran on 5/23/17.
 */

public class ErrorUtils {
    public static APIError parseError(Response<?> response) {
        Converter<ResponseBody, APIError> errorConverter = retrofit.responseBodyConverter(APIError.class, new Annotation[0]);

        @SuppressWarnings("UnusedAssignment") APIError error = new APIError();

        try {
            error = errorConverter.convert(response.errorBody());
        } catch (Exception e) {
            return new APIError();
        }

        return error;
    }
}
