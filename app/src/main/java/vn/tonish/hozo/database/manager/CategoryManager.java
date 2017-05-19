package vn.tonish.hozo.database.manager;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui.
 */
public class CategoryManager {

    private static final String TAG = CategoryManager.class.getName();

    public static void insertCategories(Context context, List<CategoryEntity> categories) {
        LogUtils.d(TAG, "insertCategories start ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        realm.beginTransaction();

        for (int i = 0; i < categories.size(); i++) {
            realm.insertOrUpdate(categories.get(i));
        }

        realm.commitTransaction();
    }

    public static void insertCategory(Context context, CategoryEntity category) {
        LogUtils.d(TAG, "insertCategories start ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        realm.beginTransaction();
        realm.insertOrUpdate(category);
        realm.commitTransaction();
    }

    public static void insertIsSelected(Context context, CategoryEntity categoryEntity, boolean isSelected) {
        LogUtils.d(TAG, "insertCategories start ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        realm.beginTransaction();
        categoryEntity.setSelected(isSelected);
        realm.commitTransaction();
    }

    public static List<CategoryEntity> getAllCategories(Context context) {
        LogUtils.d(TAG, "getAllCategories start ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        return realm.where(CategoryEntity.class).findAll();
    }


    public static CategoryEntity getCategoryById(Context context, int id) {
        LogUtils.d(TAG, "getAllCategories start ");
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        return realm.where(CategoryEntity.class).equalTo("id", id).findFirst();
    }


    public static void deleteAll(Context context) {
        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
        realm.beginTransaction();
        realm.where(CategoryEntity.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

}