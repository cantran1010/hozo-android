package vn.tonish.hozo.rest;

import java.util.List;
import java.util.Map;

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
import retrofit2.http.QueryMap;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.rest.responseRes.BidResponse;
import vn.tonish.hozo.rest.responseRes.CancelOfferResponse;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.rest.responseRes.OtpReponse;
import vn.tonish.hozo.rest.responseRes.RateResponse;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.rest.responseRes.Token;

/**
 * Created by LongBui on 09/05/2017.
 */
public interface ApiInterface {

    @POST("auth/otp_code")
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

    @PUT("users")
    Call<User> updateUser(@Header("Authorization") String token, @Body RequestBody body);

    @GET("tasks")
    Call<List<TaskResponse>> getDetailTask(@Header("Authorization") String token, @QueryMap Map<String, String> params);

    @GET("tasks/{taskId}")
    Call<TaskResponse> getDetailTask(@Header("Authorization") String token, @Path("taskId") int taskId);

    @GET("users")
    Call<UserEntity> getMyAccountInfor(@Header("Authorization") String token);

    @POST("user/logout")
    Call<Void> logOut(@Header("Authorization") String token);

    @GET("users/{user_id}/reviews")
    Call<List<ReviewEntity>> getUserReviews(@Header("Authorization") String token, @Path("user_id") int id, @QueryMap Map<String, String> option);

    @POST("tasks/{taskId}/comments")
    Call<Comment> commentTask(@Header("Authorization") String token, @Path("taskId") int taskId, @Body RequestBody body);

    @POST("comments/{commentId}/report")
    Call<Void> report(@Header("Authorization") String token, @Path("commentId") int commentId, @Body RequestBody body);

    @POST("tasks/{taskId}/bids")
    Call<BidResponse> bidsTask(@Header("Authorization") String token, @Path("taskId") int taskId);

//    @POST("bids/{bidId}")
//    Call<AcceptOfferResponse> acceptOffer(@Header("Authorization") String token, @Path("bidId") int bidId, @Body RequestBody body);

    @POST("tasks/{taskId}/accept")
    Call<TaskResponse> acceptOffer(@Header("Authorization") String token, @Path("taskId") int taskId, @Body RequestBody body);

    @PUT("tasks/{taskId}")
    Call<TaskResponse> updateTask(@Header("Authorization") String token, @Path("taskId") int taskId, @Body RequestBody body);

    @POST("tasks/{taskId}/reviews")
    Call<RateResponse> rateTask(@Header("Authorization") String token, @Path("taskId") int taskId, @Body RequestBody body);

    @POST("bids/{bidId}")
    Call<CancelOfferResponse> cancelOffer(@Header("Authorization") String token, @Path("bidId") int bidId, @Body RequestBody body);

    @GET("notifications")
    Call<List<Notification>> getMyNotifications(@Header("Authorization") String token, @QueryMap Map<String, String> option);

    @GET("users/tasks")
    Call<List<TaskResponse>> getMyTask(@Header("Authorization") String token, @QueryMap Map<String, String> option);

    @GET("tasks/{taskId}/comments")
    Call<List<Comment>> getCommens(@Header("Authorization") String token, @Path("taskId") int taskId, @QueryMap Map<String, String> params);

}
