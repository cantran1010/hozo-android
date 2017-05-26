package vn.tonish.hozo.database.manager;

import java.util.List;

import io.realm.Realm;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui.
 */
public class CategoryManager {

    private static final String TAG = CategoryManager.class.getName();

    public static void insertCategories(List<CategoryEntity> categories) {
        LogUtils.d(TAG, "insertCategories start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(categories);
        realm.commitTransaction();
    }

    public static void insertCategory(CategoryEntity category) {
        LogUtils.d(TAG, "insertCategories start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(category);
        realm.commitTransaction();
    }

    public static void insertIsSelected(CategoryEntity categoryEntity, boolean isSelected) {
        LogUtils.d(TAG, "insertCategories start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        categoryEntity.setSelected(isSelected);
        realm.commitTransaction();
    }

    public static List<CategoryEntity> getAllCategories() {
        LogUtils.d(TAG, "getAllCategories start ");
        Realm realm = Realm.getDefaultInstance();
        return realm.where(CategoryEntity.class).findAll();
    }


    public static CategoryEntity getCategoryById(int id) {
        LogUtils.d(TAG, "getAllCategories start ");
        Realm realm = Realm.getDefaultInstance();
        return realm.where(CategoryEntity.class).equalTo("id", id).findFirst();
    }


    public static void deleteAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(CategoryEntity.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

}