package vn.tonish.hozo.rest;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.model.SettingAdvance;
import vn.tonish.hozo.rest.responseRes.BlockResponse;
import vn.tonish.hozo.rest.responseRes.DepositResponse;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.rest.responseRes.NewTaskResponse;
import vn.tonish.hozo.rest.responseRes.NofifySystemResponse;
import vn.tonish.hozo.rest.responseRes.NotifyChatRoomResponse;
import vn.tonish.hozo.rest.responseRes.OtpReponse;
import vn.tonish.hozo.rest.responseRes.PromotionResponse;
import vn.tonish.hozo.rest.responseRes.RateResponse;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.rest.responseRes.Token;
import vn.tonish.hozo.rest.responseRes.TransactionResponse;
import vn.tonish.hozo.rest.responseRes.UpdateResponse;
import vn.tonish.hozo.rest.responseRes.WalletResponse;

/**
 * Created by LongBui on 09/05/2017.
 */
public interface ApiInterface {

    @POST("v1/auth/otp_code")
    Call<BlockResponse> getOtpCode(@Header("X-Hozo-API-Key") String apiKey, @Body RequestBody body);

    @PUT("v1/auth/refresh_token")
    Call<Token> refreshToken(@Body RequestBody body);

    @GET("v1/tasks/categories")
    Call<List<Category>> getCategories(@Header("Authorization") String token);

    @Multipart
    @POST("v1/upload/image")
    Call<ImageResponse> uploadImage(@Header("Authorization") String token, @Part MultipartBody.Part filePart);

    @POST("v1/tasks")
    Call<TaskResponse> createNewTask(@Header("Authorization") String token, @Body RequestBody body);

    @POST("v1/auth/login")
    Call<OtpReponse> senOtp(@Body RequestBody body);

    @PUT("v1/users")
    Call<UserEntity> updateUser(@Header("Authorization") String token, @Body RequestBody body);

    @GET("v1/tasks")
    Call<List<TaskResponse>> getTasks(@Header("Authorization") String token, @QueryMap Map<String, String> params);

    @GET("v1/tasks/new_number")
    Call<NewTaskResponse> getCountNewTasks(@Header("Authorization") String token, @QueryMap Map<String, String> params);

    @GET("v1/tasks/{taskId}")
    Call<TaskResponse> getDetailTask(@Header("Authorization") String token, @Path("taskId") int taskId, @QueryMap Map<String, Boolean> option);

    @GET("v1/users/{user_id}")
    Call<UserEntity> getUser(@Header("Authorization") String token, @Path("user_id") int id);

    @POST("v1/users/logout")
    Call<Void> logOut(@Header("Authorization") String token);

    @GET("v1/users/{user_id}/reviews")
    Call<List<ReviewEntity>> getUserReviews(@Header("Authorization") String token, @Path("user_id") int id, @QueryMap Map<String, String> option);

    @POST("v1/tasks/{taskId}/comments")
    Call<Comment> commentTask(@Header("Authorization") String token, @Path("taskId") int taskId, @Body RequestBody body);

    @POST("v1/comments/{commentId}/report")
    Call<Void> report(@Header("Authorization") String token, @Path("commentId") int commentId, @Body RequestBody body);

    @POST("v1/tasks/{taskId}/bids")
    Call<TaskResponse> bidsTask(@Header("Authorization") String token, @Path("taskId") int taskId);

    @POST("v1/tasks/{taskId}/accept")
    Call<TaskResponse> acceptOffer(@Header("Authorization") String token, @Path("taskId") int taskId, @Body RequestBody body);

    @PUT("v1/tasks/{taskId}/cancel")
    Call<TaskResponse> cancelTask(@Header("Authorization") String token, @Path("taskId") int taskId);

    @PUT("v1/tasks/{taskId}/reviews")
    Call<RateResponse> rateTask(@Header("Authorization") String token, @Path("taskId") int taskId, @Body RequestBody body);

    @GET("v1/notifications/group")
    Call<List<Notification>> getMyNotificationsGroup(@Header("Authorization") String token, @QueryMap Map<String, String> option);

    @GET("v1/users/tasks")
    Call<List<TaskResponse>> getMyTask(@Header("Authorization") String token, @QueryMap Map<String, String> option, @Query("status[]") List<String> statuses);

    @GET("v1/tasks/{taskId}/comments")
    Call<List<Comment>> getComments(@Header("Authorization") String token, @Path("taskId") int taskId, @QueryMap Map<String, String> params);

    @GET("v1/tasks/comments/{commentId}/replies")
    Call<List<Comment>> getCommentsInComments(@Header("Authorization") String token, @Path("commentId") int commentId, @QueryMap Map<String, String> params);

    @PUT("v1/notifications/device")
    Call<Void> updateDeviceToken(@Header("Authorization") String token, @Body RequestBody body);

    @POST("v1/auth/check_block_user")
    Call<BlockResponse> checkBlockUser(@Body RequestBody body);

    @POST("v1/applications/check_update")
    Call<UpdateResponse> apdateVersion(@Body RequestBody body);

    @GET("v1/settings/alert_tasks")
    Call<List<Integer>> getSettingAlert(@Header("Authorization") String token);

    @POST("v1/settings/alert_tasks")
    Call<Void> postSettingAlert(@Header("Authorization") String token, @Body RequestBody body);

    @DELETE("v1/users/tasks/{taskId}")
    Call<Void> deleteTask(@Header("Authorization") String token, @Path("taskId") int taskId);

    @POST("v1/tasks/{taskId}/cancel_bid")
    Call<TaskResponse> cancelBid(@Header("Authorization") String token, @Path("taskId") int taskId, @Body RequestBody body);

    @PUT("v1/notifications/{notificationId}")
    Call<Void> updateReadNotification(@Header("Authorization") String token, @Path("notificationId") int notificationId, @Body RequestBody body);

    @POST("v1/tasks/{taskId}/report")
    Call<Void> reportTask(@Header("Authorization") String token, @Path("taskId") int taskId, @Body RequestBody body);

    @PUT("v1/tasks/{taskId}")
    Call<TaskResponse> editTask(@Header("Authorization") String token, @Path("taskId") int taskId, @Body RequestBody body);

    @POST("v1/tasks/{taskId}/follow")
    Call<Void> followTask(@Header("Authorization") String token, @Path("taskId") int taskId);

    @DELETE("v1/tasks/{taskId}/follow")
    Call<Void> unFollowTask(@Header("Authorization") String token, @Path("taskId") int taskId);

    @GET("v1/users/chat-rooms")
    Call<List<TaskResponse>> getChatRooms(@Header("Authorization") String token);

    @GET("v1/users/setting/notification_chat_group")
    Call<NotifyChatRoomResponse> getChatRoomsNotify(@Header("Authorization") String token);

    @POST("v1/users/settings")
    Call<NotifyChatRoomResponse> updateChatRoomsNotify(@Header("Authorization") String token, @Body RequestBody body);

    @GET("v1/users/setting/notification_system")
    Call<NofifySystemResponse> getNotifySystem(@Header("Authorization") String token);

    @POST("v1/users/settings")
    Call<NofifySystemResponse> updateNotifySystem(@Header("Authorization") String token, @Body RequestBody body);

    @POST("v1/auth/account-kit")
    Call<OtpReponse> loginAccountKit(@Body RequestBody body);

    @GET("v1/users/settings")
    Call<SettingAdvance> getSettingAdvance(@Header("Authorization") String token);

    @POST("v1/users/settings")
    Call<SettingAdvance> postSettingAdvance(@Header("Authorization") String token, @Body RequestBody body);

    @POST("v1/wallet/redeem")
    Call<PromotionResponse> promotionCode(@Header("Authorization") String token, @Body RequestBody body);

    @GET("v1/wallet")
    Call<WalletResponse> getWalletInfo(@Header("Authorization") String token);

    @GET("v1/wallet/transactions")
    Call<List<TransactionResponse>> getTransactionsHistory(@Header("Authorization") String token, @QueryMap Map<String, String> option);

    @POST("v1/support/feedback")
    Call<Void> sendFeedback(@Header("Authorization") String token, @Body RequestBody body);

    @POST("v1/support/message")
    Call<Void> sendMailSupport(@Header("Authorization") String token, @Body RequestBody body);

    @POST("v1/users/{userId}/follow")
    Call<Void> follow(@Header("Authorization") String token, @Path("userId") int userId, @Body RequestBody body);

    @POST("v1/wallet/deposit")
    Call<DepositResponse> deposit(@Header("Authorization") String token, @Body RequestBody body);

}
