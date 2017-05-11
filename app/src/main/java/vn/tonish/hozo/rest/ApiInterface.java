package vn.tonish.hozo.rest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.rest.responseRes.OtpReponse;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.rest.responseRes.Token;

/**
 * Created by LongBui on 09/05/2017.
 */
public interface ApiInterface {

    @POST("auth/otpcode")
    Call<Void> getOtpCode(@Header("X-Hozo-API-Key") String apiKey, @Body RequestBody body);

    @PUT("auth/refresh_token")
    Call<Token> refreshToken(@Header("Authorization") String token, @Body RequestBody body);

    @GET("tasks/categories")
    Call<List<Category>> getCategories(@Header("Authorization") String token);

    @Multipart
    @POST("upload/image")
    Call<ImageResponse> uploadImage(@Header("Authorization") String token, @Part MultipartBody.Part filePart);

    @POST("tasks")
    Call<TaskResponse> createNewTask(@Header("Authorization") String token, @Body RequestBody body);

    @POST("auth/login")
    Call<OtpReponse> senOtp(@Body RequestBody body);

    @PUT("user")
    Call<User> updateUser(@Header("Authorization") String token, @Body RequestBody body);

    @GET("tasks/{id}")
    Call<TaskResponse> getDetailTask(@Header("Authorization") String token, @Path("id") int id);


}
