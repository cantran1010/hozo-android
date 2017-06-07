package vn.tonish.hozo.database.manager;

import java.util.List;

import io.realm.Realm;
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

    private static void insertDateSincereviewEntities(List<ReviewEntity> reviewEntities) {
        LogUtils.d(TAG, "insertComments start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        for (int i = 0; i < reviewEntities.size(); i++) {
            reviewEntities.get(i).setCraeatedDateAt(getDateFromStringIso(reviewEntities.get(i).getCreatedAt()));
            realm.insertOrUpdate(reviewEntities.get(i));
            LogUtils.d(TAG, "getCreatedDateAt  " + reviewEntities.get(i).getCraeatedDateAt().toString());
        }

        realm.commitTransaction();
    }

}