package vn.tonish.hozo.database.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
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
    }

    public static List<NotificationEntity> getAllNotifications() {
        LogUtils.d(TAG, "getAllNotifications start ");
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NotificationEntity.class).findAll();
    }

    public static List<NotificationEntity> getFirstPage() {
        LogUtils.d(TAG, "getAllNotifications start ");
        List<NotificationEntity> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        List<NotificationEntity> notifications = realm.where(NotificationEntity.class).findAll().sort("createdAt");
        if (notifications.size() > 0) {

            if (notifications.size() >= InboxFragment.LIMIT)
                result = notifications.subList(0, InboxFragment.LIMIT);
            else
                result = notifications;

        }
        return result;
    }

    public static List<NotificationEntity> getNotificationsSince(Date sinceDate) {
        LogUtils.d(TAG, "getAllNotifications start ");
        List<NotificationEntity> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        List<NotificationEntity> notificationEntities = realm.where(NotificationEntity.class).lessThan("createdAt", sinceDate).findAll().sort("createdAt");
        if (notificationEntities.size() > 0) {

            if (notificationEntities.size() >= InboxFragment.LIMIT)
                result = notificationEntities.subList(0, InboxFragment.LIMIT);
            else result = notificationEntities;

        }
        return result;
    }

    public static void deleteAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(NotificationEntity.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }


}