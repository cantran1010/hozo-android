package vn.tonish.hozo.database.manager;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui.
 */
public class ReviewManager {

    private static final String TAG = ReviewManager.class.getName();
    public static Context context;

    public ReviewManager(Context context) {
        ReviewManager.context = context;
    }

    public static ReviewManager reviewManager;

    public static void insertReviews(List<ReviewEntity> reviews){
        LogUtils.d(TAG, "insertReviews start ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        realm.beginTransaction();

        for (int i=0;i<reviews.size();i++){
            realm.insertOrUpdate(reviews.get(i));
        }

        realm.commitTransaction();
    }

    public static List<ReviewEntity> getAllReview(){
        LogUtils.d(TAG, "List<ReviewEntity> start ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        // get last update
        return realm.where(ReviewEntity.class).findAll();
    }

    public static List<ReviewEntity> getReviewByType(String type){
        LogUtils.d(TAG, "getUserLogin start ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        // get last update
        return realm.where(ReviewEntity.class).equalTo("type", type).findAll();
    }
    public static void deleteAll() {
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        realm.beginTransaction();
        realm.where(ReviewEntity.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

}