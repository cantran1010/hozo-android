package vn.tonish.hozo.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.entity.TaskEntity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.ReviewManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.model.Review;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
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
        userEntity.setPhoneNumber(user.getPhoneNumber());
        userEntity.setVerified(user.getVerified());
        userEntity.setPosterAverageRating(user.getPosterAverageRating());
        userEntity.setPosterReviewCount(user.getPosterReviewCount());
        userEntity.setTaskerAverageRating(user.getTaskerAverageRating());
        userEntity.setTaskerReviewCount(user.getTaskerReviewCount());

        userEntity.setAccessToken(token.getAccessToken());
        userEntity.setRefreshToken(token.getRefreshToken());
        userEntity.setTokenExp(token.getTokenExpires());

        UserManager.insertUserLogin(userEntity);
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
        userEntity.setPhoneNumber(user.getPhoneNumber());
        userEntity.setVerified(user.getVerified());
        userEntity.setPosterAverageRating(user.getPosterAverageRating());
        userEntity.setPosterReviewCount(user.getPosterReviewCount());
        userEntity.setTaskerAverageRating(user.getTaskerAverageRating());
        userEntity.setTaskerReviewCount(user.getTaskerReviewCount());

        UserManager.insertUserLogin(userEntity);
        LogUtils.d("update User to database: ", userEntity.toString());
    }

    public static User convertUserEntityToUser(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setAddress(userEntity.getAddress());
        user.setAvatar(userEntity.getAvatar());
        user.setDateOfBirth(userEntity.getDateOfBirth());
        user.setDescription(userEntity.getDescription());
        user.setEmail(userEntity.getEmail());
        user.setFacebookId(userEntity.getFacebookId());
        user.setFullName(userEntity.getFullName());
        user.setPhoneNumber(userEntity.getPhoneNumber());
        user.setVerified(userEntity.getVerified());
        user.setPosterAverageRating(userEntity.getPosterAverageRating());
        user.setPosterReviewCount(userEntity.getPosterReviewCount());
        user.setTaskerAverageRating(userEntity.getTaskerAverageRating());
        user.setTaskerReviewCount(userEntity.getTaskerReviewCount());
        return user;
    }

    public static Category convertCatogoryEntityToCategory(CategoryEntity categoryEntity) {
        Category category = new Category();
        category.setId(categoryEntity.getId());
        category.setName(categoryEntity.getName());
        category.setDescription(categoryEntity.getDescription());
        category.setSuggestTitle(categoryEntity.getSuggestTitle());
        category.setSuggestDescription(categoryEntity.getSuggestDescription());
        category.setPresentPath(categoryEntity.getPresentPath());
        return category;
    }

    public static List<CategoryEntity> convertListCategoryToListCategoryEntity(List<Category> categories) {
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            categoryEntities.add(convertCatogoryToCategoryEntity(categories.get(i)));
        }
        return categoryEntities;
    }

    public static CategoryEntity convertCatogoryToCategoryEntity(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(category.getId());
        categoryEntity.setName(category.getName());
        categoryEntity.setDescription(category.getDescription());
        categoryEntity.setSuggestTitle(category.getSuggestTitle());
        categoryEntity.setSuggestDescription(category.getSuggestDescription());
        categoryEntity.setPresentPath(category.getPresentPath());
        return categoryEntity;
    }

    public static UserEntity convertUserToUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setAddress(user.getAddress());
        userEntity.setAvatar(user.getAvatar());
        userEntity.setDateOfBirth(user.getDateOfBirth());
        userEntity.setDescription(user.getDescription());
        userEntity.setEmail(user.getEmail());
        userEntity.setFacebookId(user.getFacebookId());
        userEntity.setFullName(user.getFullName());
        userEntity.setPhoneNumber(user.getPhoneNumber());
        userEntity.setVerified(user.getVerified());
        userEntity.setPosterAverageRating(user.getPosterAverageRating());
        userEntity.setPosterReviewCount(user.getPosterReviewCount());
        userEntity.setTaskerAverageRating(user.getTaskerAverageRating());
        userEntity.setTaskerReviewCount(user.getTaskerReviewCount());
        return userEntity;
    }


    //update data reviewEntities

    public static void insertReviewtoDb(User user) {
        List<Review> reviews = new ArrayList();
        reviews.addAll(user.getReviews());
        List<ReviewEntity> reviewEntities = new ArrayList();
        for (int i = 0; i < reviews.size(); i++) {
            ReviewEntity reviewEntity = new ReviewEntity();
            Review review = reviews.get(i);
            reviewEntity.setId(review.getId());
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

    public static TaskResponse converTaskEntityToTaskReponse(TaskEntity taskEntity) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(taskEntity.getId());
        taskResponse.setCategoryId(taskEntity.getCategoryId());
        taskResponse.setTitle(taskEntity.getTitle());
        taskResponse.setDescription(taskEntity.getDescription());
        taskResponse.setStartTime(taskEntity.getStartTime());
        taskResponse.setEndTime(taskEntity.getEndTime());
        taskResponse.setStatus(taskEntity.getStatus());
        taskResponse.setCommentsCount(taskEntity.getCommentsCount());
        taskResponse.setGender(taskEntity.getGender());
        taskResponse.setMinAge(taskEntity.getMinAge());
        taskResponse.setMaxAge(taskEntity.getMaxAge());
        taskResponse.setLatitude(taskEntity.getLatitude());
        taskResponse.setLongitude(taskEntity.getLongitude());
        taskResponse.setCity(taskEntity.getCity());
        taskResponse.setDistrict(taskEntity.getDistrict());
        taskResponse.setAddress(taskEntity.getAddress());
        taskResponse.setWorkerRate(taskEntity.getWorkerRate());
        taskResponse.setWorkerCount(taskEntity.getWorkerCount());
        taskResponse.setCurrency(taskEntity.getCurrency());
        taskResponse.setPoster(taskEntity.getPoster());

        String[] arrAtachments = taskEntity.getAttachments().split(",");
        List<String> listAttachments = new ArrayList<>();
        for (int i = 0; i < arrAtachments.length; i++)
            if (!arrAtachments[i].equals(""))
                listAttachments.add(arrAtachments[i]);
//        List<String> listAttachments = new ArrayList<String>(Arrays.asList(arrAtachments));
        taskResponse.setAttachments(listAttachments);

        List<Bidder> bidders = new ArrayList<>();
        for (int i = 0; i < taskEntity.getBidders().size(); i++)
            bidders.add(taskEntity.getBidders().get(i));
        taskResponse.setBidders(bidders);

        List<Assigner> assigners = new ArrayList<>();
        for (int i = 0; i < taskEntity.getAssignees().size(); i++)
            assigners.add(taskEntity.getAssignees().get(i));
        taskResponse.setAssignees(assigners);

        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < taskEntity.getComments().size(); i++)
            comments.add(taskEntity.getComments().get(i));
        taskResponse.setComments(comments);

        return taskResponse;
    }

    public static TaskEntity converTaskReponseToTaskEntity(TaskResponse taskResponse) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskResponse.getId());
        taskEntity.setCategoryId(taskResponse.getCategoryId());
        taskEntity.setTitle(taskResponse.getTitle());
        taskEntity.setDescription(taskResponse.getDescription());
        taskEntity.setStartTime(taskResponse.getStartTime());
        taskEntity.setEndTime(taskResponse.getEndTime());
        taskEntity.setStatus(taskResponse.getStatus());
        taskEntity.setCommentsCount(taskResponse.getCommentsCount());
        taskEntity.setGender(taskResponse.getGender());
        taskEntity.setMinAge(taskResponse.getMinAge());
        taskEntity.setMaxAge(taskResponse.getMaxAge());
        taskEntity.setLatitude(taskResponse.getLatitude());
        taskEntity.setLongitude(taskResponse.getLongitude());
        taskEntity.setCity(taskResponse.getCity());
        taskEntity.setDistrict(taskResponse.getDistrict());
        taskEntity.setAddress(taskResponse.getAddress());
        taskEntity.setWorkerRate(taskResponse.getWorkerRate());
        taskEntity.setWorkerCount(taskResponse.getWorkerCount());
        taskEntity.setCurrency(taskResponse.getCurrency());
        taskEntity.setPoster(taskResponse.getPoster());

        List<String> atachments = taskResponse.getAttachments();
        String strAtachments = "";
        for (int i = 0; i < atachments.size(); i++) {
            strAtachments = strAtachments + "," + atachments.get(i);
        }
        taskEntity.setAttachments(strAtachments);

        RealmList<Bidder> bidders = new RealmList<Bidder>();
        for (int i = 0; i < taskResponse.getBidders().size(); i++)
            bidders.add(taskResponse.getBidders().get(i));
        taskEntity.setBidders(bidders);

        RealmList<Assigner> assigners = new RealmList<>();
        for (int i = 0; i < taskResponse.getAssignees().size(); i++)
            assigners.add(taskResponse.getAssignees().get(i));
        taskEntity.setAssignees(assigners);

        RealmList<Comment> comments = new RealmList<>();
        for (int i = 0; i < taskResponse.getComments().size(); i++)
            comments.add(taskResponse.getComments().get(i));
        taskEntity.setComments(comments);

        return taskEntity;
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
