package vn.tonish.hozo.database.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;
import vn.tonish.hozo.fragment.InboxFragment;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui.
 */
class NotificationManager {

    private static final String TAG = NotificationManager.class.getName();

    public static void insertNotifications(List<Notification> notifications) {
        LogUtils.d(TAG, "insertNotifications start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(notifications);
        realm.commitTransaction();
        realm.close();
    }

    public static void insertNotification(Notification notification) {
        LogUtils.d(TAG, "insertNotification start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(notification);
        realm.commitTransaction();
        realm.close();
    }

    public static List<Notification> getNotificationsSince(Date sinceDate) {
        LogUtils.d(TAG, "getAllNotifications start , sinceDate : " + sinceDate);
        List<Notification> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();

        RealmQuery<Notification> notificationEntityRealmQuery = realm.where(Notification.class);
        if (sinceDate != null)
            notificationEntityRealmQuery = notificationEntityRealmQuery.lessThan("createdDateAt", sinceDate);
        List<Notification> notificationEntities = notificationEntityRealmQuery.findAll().sort("createdDateAt", Sort.DESCENDING);
        if (notificationEntities.size() > 0) {
            if (notificationEntities.size() >= InboxFragment.LIMIT)
                result = notificationEntities.subList(0, InboxFragment.LIMIT);
            else result = notificationEntities;

        }
        realm.close();
        return result;
    }

}