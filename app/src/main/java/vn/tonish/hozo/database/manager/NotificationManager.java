package vn.tonish.hozo.database.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;
import vn.tonish.hozo.database.entity.NotificationEntity;
import vn.tonish.hozo.fragment.InboxFragment;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui.
 */
public class NotificationManager {

    private static final String TAG = NotificationManager.class.getName();

    public static void insertNotifications(List<NotificationEntity> notifications) {
        LogUtils.d(TAG, "insertNotifications start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(notifications);
        realm.commitTransaction();
        realm.close();
    }

    public static List<NotificationEntity> getNotificationsSince(Date sinceDate) {
        LogUtils.d(TAG, "getAllNotifications start , sinceDate : " + sinceDate);
        List<NotificationEntity> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();

        RealmQuery<NotificationEntity> notificationEntityRealmQuery = realm.where(NotificationEntity.class);
        if (sinceDate != null)
            notificationEntityRealmQuery = notificationEntityRealmQuery.lessThan("createdAt", sinceDate);
        List<NotificationEntity> notificationEntities = notificationEntityRealmQuery.findAll().sort("createdAt", Sort.DESCENDING);
        if (notificationEntities.size() > 0) {
            if (notificationEntities.size() >= InboxFragment.LIMIT)
                result = notificationEntities.subList(0, InboxFragment.LIMIT);
            else result = notificationEntities;

        }
        realm.close();
        return result;
    }

}