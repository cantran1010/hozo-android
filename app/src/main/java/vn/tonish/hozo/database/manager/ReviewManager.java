package vn.tonish.hozo.database.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import vn.tonish.hozo.activity.CommentsActivity;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.utils.LogUtils;

import static vn.tonish.hozo.utils.DateTimeUtils.getDateFromStringIso;

/**
 * Created by LongBui.
 */
public class ReviewManager {
    private static final String TAG = ReviewManager.class.getName();

    public static void insertReviews(List<ReviewEntity> reviews) {
        LogUtils.d(TAG, "insertReviews start ");
        insertDateSincereviewEntities(reviews);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (int i = 0; i < reviews.size(); i++) {
            reviews.get(i).setCraeatedDateAt(getDateFromStringIso(reviews.get(i).getCreatedAt()));
        }
        realm.insertOrUpdate(reviews);
        realm.commitTransaction();
    }


    public static List<ReviewEntity> getFirstPage() {
        LogUtils.d(TAG, "getFirstPageComment start ");
        List<ReviewEntity> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        List<ReviewEntity> reviewEntities = realm.where(ReviewEntity.class).findAll().sort("createdAt");
        if (reviewEntities.size() > 0) {
            if (reviewEntities.size() >= CommentsActivity.LIMIT)
                result = reviewEntities.subList(0, CommentsActivity.LIMIT);
            else
                result = reviewEntities;

        }
        return result;
    }

    public static List<ReviewEntity> getReviewsSince(Date sinceDate) {

        LogUtils.d(TAG, "getCommentsSince start ");
        List<ReviewEntity> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        List<ReviewEntity> reviewEntities = realm.where(ReviewEntity.class).lessThan("craeatedDateAt", sinceDate).findAll().sort("createdAt");
        if (reviewEntities.size() > 0) {

            if (reviewEntities.size() >= CommentsActivity.LIMIT)
                result = reviewEntities.subList(0, CommentsActivity.LIMIT);
            else result = reviewEntities;

        }
        return result;
    }


    public static List<ReviewEntity> getAllReview() {
        LogUtils.d(TAG, "getMyUser start ");
        Realm realm = Realm.getDefaultInstance();
        // get last update
        return realm.where(ReviewEntity.class).findAll();
    }

    public static List<ReviewEntity> getReviewByType(String type) {
        LogUtils.d(TAG, "getMyUser start ");
        Realm realm = Realm.getDefaultInstance();
        // get last update
        return realm.where(ReviewEntity.class).equalTo("type", type).findAll();
    }

    public static void deleteAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(ReviewEntity.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    public static void insertDateSincereviewEntities(List<ReviewEntity> reviewEntities) {
        LogUtils.d(TAG, "insertComments start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        for (int i = 0; i < reviewEntities.size(); i++) {
            reviewEntities.get(i).setCraeatedDateAt(getDateFromStringIso(reviewEntities.get(i).getCreatedAt()));
            realm.insertOrUpdate(reviewEntities.get(i));
            LogUtils.d(TAG, "getCraeatedDateAt  " + reviewEntities.get(i).getCraeatedDateAt().toString());
        }

        realm.commitTransaction();
    }

}