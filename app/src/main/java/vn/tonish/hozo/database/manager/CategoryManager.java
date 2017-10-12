package vn.tonish.hozo.database.manager;

import java.util.List;

import io.realm.Realm;
import vn.tonish.hozo.common.Constants;
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

    public static void insertIsSelected(CategoryEntity categoryEntity) {
        LogUtils.d(TAG, "insertCategories start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        categoryEntity.setSelected(false);
        realm.commitTransaction();
    }

    public static List<CategoryEntity> getAllCategories() {
        LogUtils.d(TAG, "getAllCategories start ");
        Realm realm = Realm.getDefaultInstance();
        return realm.where(CategoryEntity.class).equalTo("status", Constants.CATEGORY_ACTIVE).findAll();
    }


    public static CategoryEntity getCategoryById(int id) {
        LogUtils.d(TAG, "getCategoryById start ");
        Realm realm = Realm.getDefaultInstance();
        return realm.where(CategoryEntity.class).equalTo("id", id).findFirst();
    }


    public static boolean checkCategoryById(int id) {
        boolean ck;
        LogUtils.d(TAG, "checkCategoryById start ");
        Realm realm = Realm.getDefaultInstance();
        ck = realm.where(CategoryEntity.class).equalTo("id", id).findFirst() != null && realm.where(CategoryEntity.class).equalTo("id", id).findFirst().isSelected();
        return ck;
    }

}