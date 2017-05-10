package vn.tonish.hozo.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.ReviewManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Review;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.rest.responseRes.Token;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by Can Tran on 24/04/2017.
 */

public class DataParse {

    //inser data UserEntity

    public static void insertUsertoDb(User user, Token token, Context context) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setAddress(user.getAddress());
        userEntity.setAvatar(user.getAvatar());
        userEntity.setDateOfBirth(user.getDateOfBirth());
        userEntity.setDescription(user.getDescription());
        userEntity.setEmail(user.getEmail());
        userEntity.setFacebookId(user.getFacebookId());
        userEntity.setFullName(user.getFullName());
        userEntity.setPhoneNumber(user.getPhone());
        userEntity.setVerified(user.getVerified());
        userEntity.setPosterAverageRating(user.getPosterAverageRating());
        userEntity.setPosterReviewCount(user.getPosterReviewCount());
        userEntity.setTaskerAverageRating(user.getTaskerAverageRating());
        userEntity.setTaskerReviewCount(user.getTaskerReviewCount());

        userEntity.setAccessToken(token.getAccessToken());
        userEntity.setRefreshToken(token.getRefreshToken());
        userEntity.setTokenExp(token.getTokenExpires());

        UserManager.insertUserLogin(userEntity, context);
        LogUtils.d("inser data UserEntity : ", userEntity.toString());
    }

    //update User to database


    public static void updateUser(User user, Context context) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setAddress(user.getAddress());
        userEntity.setAvatar(user.getAvatar());
        userEntity.setDateOfBirth(user.getDateOfBirth());
        userEntity.setDescription(user.getDescription());
        userEntity.setEmail(user.getEmail());
        userEntity.setFacebookId(user.getFacebookId());
        userEntity.setFullName(user.getFullName());
        userEntity.setPhoneNumber(user.getPhone());
        userEntity.setVerified(user.getVerified());
        userEntity.setPosterAverageRating(user.getPosterAverageRating());
        userEntity.setPosterReviewCount(user.getPosterReviewCount());
        userEntity.setTaskerAverageRating(user.getTaskerAverageRating());
        userEntity.setTaskerReviewCount(user.getTaskerReviewCount());

        UserManager.insertUserLogin(userEntity, context);
        LogUtils.d("update User to database: ", userEntity.toString());
    }


    //update data reviewEntities

    public static void insertReviewtoDb(User user) {
        List<Review> reviewsPoster = user.getPosterReviews();
        List<Review> reviewsTasker = user.getTaskerReviews();
        List<Review> reviews = new ArrayList();
        reviews.addAll(reviewsPoster);
        reviews.addAll(reviewsTasker);
        List<ReviewEntity> reviewEntities = new ArrayList();
        for (int i = 0; i < reviews.size(); i++) {
            ReviewEntity reviewEntity = new ReviewEntity();
            Review review = reviews.get(i);
            reviewEntity.setAuthorId(review.getAuthorId());
            reviewEntity.setAuthorAvatar(review.getAuthorAvatar());
            reviewEntity.setAuthorName(review.getAuthorName());
            reviewEntity.setBody(review.getBody());
            reviewEntity.setCreatedAt(review.getCreatedAt());
            reviewEntity.setRating(review.getRating());
            reviewEntity.setTaskName(review.getTaskName());
            reviewEntity.setType(review.getType());
            reviewEntities.add(reviewEntity);
        }

        ReviewManager.insertReviews(reviewEntities);
        LogUtils.d("update data reviewEntities: ", reviewEntities.toString());
    }

    public static int getAvatarTempId(String response) {
        Integer result = 0;
        try {
            JSONObject jsonResponse = new JSONObject(response);
            result = jsonResponse.getJSONObject("data").getInt("tmp_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
