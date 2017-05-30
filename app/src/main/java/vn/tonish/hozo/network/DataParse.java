package vn.tonish.hozo.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.entity.NotificationEntity;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.entity.SettingEntiny;
import vn.tonish.hozo.database.entity.TaskEntity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.ReviewManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.AddvanceSetting;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.model.Review;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.rest.responseRes.Token;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by Can Tran on 24/04/2017.
 */

public class DataParse {
    private final static String TAG = DataParse.class.getSimpleName();

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
        userEntity.setPhone(user.getPhoneNumber());
//        userEntity.setVerified(user.getVerified());
        userEntity.setPosterAverageRating(user.getPosterAverageRating());
//        userEntity.setPosterReviewCount(user.getPosterReviewCount());
//        userEntity.setTaskerAverageRating(user.getTaskerAverageRating());
//        userEntity.setTaskerReviewCount(user.getTaskerReviewCount());

        userEntity.setAccessToken(token.getAccessToken());
        userEntity.setRefreshToken(token.getRefreshToken());
        userEntity.setTokenExp(token.getTokenExpires());

        UserManager.insertUser(userEntity, true);
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
        userEntity.setPhone(user.getPhoneNumber());
//        userEntity.setVerified(user.getVerified());

        userEntity.setPosterAverageRating(user.getPosterAverageRating());
//        userEntity.setPosterReviewCount(user.getPosterReviewCount());
//        userEntity.setTaskerAverageRating(user.getTaskerAverageRating());
//        userEntity.setTaskerReviewCount(user.getTaskerReviewCount());

        UserManager.insertUser(userEntity, true);

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
        user.setPhoneNumber(userEntity.getPhone());
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
        category.setSelected(categoryEntity.isSelected());
        return category;
    }


    public static CategoryEntity convertCatogoryToCategoryEntity(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(category.getId());
        categoryEntity.setName(category.getName());
        categoryEntity.setDescription(category.getDescription());
        categoryEntity.setSuggestTitle(category.getSuggestTitle());
        categoryEntity.setSuggestDescription(category.getSuggestDescription());
        categoryEntity.setPresentPath(category.getPresentPath());
        categoryEntity.setSelected(category.isSelected());
        return categoryEntity;
    }

    public static List<CategoryEntity> convertListCategoryToListCategoryEntity(List<Category> categories) {
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            categoryEntities.add(convertCatogoryToCategoryEntity(categories.get(i)));
        }
        return categoryEntities;
    }

    public static List<Category> convertListCategoryEntityToListCategory(List<CategoryEntity> categories) {
        List<Category> categories1 = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            categories1.add(convertCatogoryEntityToCategory(categories.get(i)));
        }
        return categories1;
    }


    public static SettingEntiny convertSettingToSettingEntiny(AddvanceSetting addvanceSetting) {
        RealmList<CategoryEntity> categoryEntities = new RealmList<>();
        SettingEntiny entiny = new SettingEntiny();
        entiny.setUserId(addvanceSetting.getUserId());
        entiny.setMinWorkerRate(addvanceSetting.getMinWorkerRate());
        entiny.setMaxWorkerRate(addvanceSetting.getMaxWorkerRate());
        entiny.setLatitude(addvanceSetting.getLatitude());
        entiny.setLongitude(addvanceSetting.getLongitude());
        entiny.setRadius(addvanceSetting.getRadius());
        entiny.setGender(addvanceSetting.getGender());
        return entiny;
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
        taskResponse.setAssigneeCount(taskEntity.getAssigneeCount());
        taskResponse.setBidderCount(taskEntity.getBidderCount());
        taskResponse.setCurrency(taskEntity.getCurrency());
        taskResponse.setPoster(taskEntity.getPoster());
        taskResponse.setRole(taskEntity.getRole());
        taskResponse.setCreatedAt(DateTimeUtils.fromDateIso(taskEntity.getCreatedAt()));

        List<String> listAttachments = new ArrayList<>();

        if (taskEntity.getAttachments() != null) {
            String[] arrAtachments = taskEntity.getAttachments().split(",");
            for (int i = 0; i < arrAtachments.length; i++)
                if (!arrAtachments[i].equals(""))
                    listAttachments.add(arrAtachments[i]);
        }

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
        taskEntity.setAssigneeCount(taskResponse.getAssigneeCount());
        taskEntity.setBidderCount(taskResponse.getBidderCount());
        taskEntity.setCurrency(taskResponse.getCurrency());
        taskEntity.setPoster(taskResponse.getPoster());
        taskEntity.setRole(taskResponse.getRole());
        taskEntity.setCreatedAt(DateTimeUtils.getDateFromStringIso(taskResponse.getCreatedAt()));

        List<String> atachments = taskResponse.getAttachments();
        String strAtachments = "";
        for (int i = 0; i < atachments.size(); i++) {
            strAtachments = strAtachments + "," + atachments.get(i);
        }
        taskEntity.setAttachments(strAtachments);

        RealmList<Bidder> bidders = new RealmList<Bidder>();
        if (taskResponse.getBidders() != null)
            for (int i = 0; i < taskResponse.getBidders().size(); i++)
                bidders.add(taskResponse.getBidders().get(i));
        taskEntity.setBidders(bidders);


        RealmList<Assigner> assigners = new RealmList<>();
        if (taskResponse.getAssignees() != null)
            for (int i = 0; i < taskResponse.getAssignees().size(); i++)
                assigners.add(taskResponse.getAssignees().get(i));
        taskEntity.setAssignees(assigners);

        RealmList<Comment> comments = new RealmList<>();
        if (taskResponse.getComments() != null)
            for (int i = 0; i < taskResponse.getComments().size(); i++)
                comments.add(taskResponse.getComments().get(i));
        taskEntity.setComments(comments);

        return taskEntity;
    }

    public static List<TaskResponse> converListTaskEntityToTaskResponse(List<TaskEntity> taskEntities) {
        List<TaskResponse> taskResponses = new ArrayList<>();
        for (int i = 0; i < taskEntities.size(); i++)
            taskResponses.add(converTaskEntityToTaskReponse(taskEntities.get(i)));
        return taskResponses;
    }

    public static List<TaskEntity> convertListTaskResponseToTaskEntity(List<TaskResponse> taskResponses) {
        List<TaskEntity> taskEntities = new ArrayList<>();
        for (int i = 0; i < taskResponses.size(); i++)
            taskEntities.add(converTaskReponseToTaskEntity(taskResponses.get(i)));
        return taskEntities;
    }

    public static Notification convertNotificationEntityToNotification(NotificationEntity notificationEntity) {
        Notification notification = new Notification();
        notification.setId(notificationEntity.getId());
        notification.setUserId(notificationEntity.getUserId());
        notification.setFullName(notificationEntity.getFullName());
        notification.setAvatar(notificationEntity.getAvatar());
        notification.setTaskId(notificationEntity.getTaskId());
        notification.setTaskName(notificationEntity.getTaskName());
        notification.setEvent(notificationEntity.getEvent());
        notification.setRead(notificationEntity.getRead());
        notification.setCreatedAt(DateTimeUtils.fromDateIso(notificationEntity.getCreatedAt()));
        return notification;
    }

    public static NotificationEntity convertNotificationToNotificationEntity(Notification notification) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(notification.getId());
        notificationEntity.setUserId(notification.getUserId());
        notificationEntity.setFullName(notification.getFullName());
        notificationEntity.setAvatar(notification.getAvatar());
        notificationEntity.setTaskId(notification.getTaskId());
        notificationEntity.setTaskName(notification.getTaskName());
        notificationEntity.setEvent(notification.getEvent());
        notificationEntity.setRead(notification.getRead());
        notificationEntity.setCreatedAt(DateTimeUtils.getDateFromStringIso(notification.getCreatedAt()));
        return notificationEntity;
    }

    public static List<Notification> converListNotificationEntity(List<NotificationEntity> notificationEntities) {
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < notificationEntities.size(); i++)
            notifications.add(convertNotificationEntityToNotification(notificationEntities.get(i)));
        return notifications;
    }

    public static List<NotificationEntity> convertListNotification(List<Notification> notifications) {
        List<NotificationEntity> notificationEntities = new ArrayList<>();
        for (int i = 0; i < notifications.size(); i++)
            notificationEntities.add(convertNotificationToNotificationEntity(notifications.get(i)));
        return notificationEntities;
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

    public static Map<String, String> setParameterGetTasks(SettingEntiny addvanceSetting, String sortBy, String limit, String since, String query) {
        Map<String, String> option = new HashMap<>();
        option.put("category_id", getIds2(CategoryManager.getAllCategories()));
        option.put("min_worker_rate", String.valueOf(addvanceSetting.getMinWorkerRate()));
        option.put("min_worker_rate", String.valueOf(addvanceSetting.getMaxWorkerRate()));
        option.put("latitude", String.valueOf(addvanceSetting.getLatitude()));
        option.put("longitude", String.valueOf(addvanceSetting.getLongitude()));
        option.put("distance", String.valueOf(addvanceSetting.getRadius()));
        option.put("sort_by", sortBy);
        option.put("limit", limit);
        option.put("since", since);
        option.put("query", query);
        return option;
    }

    public static String getIds(RealmList<CategoryEntity> entityRealmList) {
        String ids = "";
        for (CategoryEntity categoryEntity : entityRealmList) {
            ids = "ids[]=" + categoryEntity.getId() + "&";
        }
        return ids.substring(0, ids.length() - 2);

    }

    public static String getIds2(List<CategoryEntity> entityRealmList) {
        String ids = "";
        for (CategoryEntity categoryEntity : entityRealmList) {
            ids = "ids[]=" + categoryEntity.getId() + "&";
        }
        return ids.substring(0, ids.length() - 2);

    }


}
