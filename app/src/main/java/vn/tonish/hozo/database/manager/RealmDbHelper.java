package vn.tonish.hozo.database.manager;

import android.content.Context;
import android.util.Base64;

import java.util.Random;

import io.realm.RealmConfiguration;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;

/**
 * Created by LongBui.
 */
public class RealmDbHelper {

    private static final String TAG = RealmDbHelper.class.getName();
    //    private static RealmDbHelper instance;
    //    private Realm realm;
    private static RealmConfiguration realmConfiguration;
    private static String key = null;


    public static RealmConfiguration getRealmConfig(Context context) {
        if (key == null) {
            if (PreferUtils.getKeyEncryption(context).equals(Constants.KEY_ENCRYPTION_DEFAULT)) {
                byte[] b = new byte[64];
                new Random().nextBytes(b);

                key = Base64.encodeToString(b, Base64.DEFAULT);

                LogUtils.d(TAG, "getRealm , key constructor : " + key);
                PreferUtils.setKeyEncryption(context, key);
            } else {
                key = PreferUtils.getKeyEncryption(context);
                LogUtils.d(TAG, "getRealm , key : " + key);
            }
        }

        if (realmConfiguration == null)
            realmConfiguration = new RealmConfiguration.Builder()
                    .name(Constants.DB_NAME)
                    .deleteRealmIfMigrationNeeded()
                    .encryptionKey(Base64.decode(key, Base64.DEFAULT))
                    .build();

        return realmConfiguration;
    }

//    public static RealmDbHelper getInstance() {
//
//        if (instance == null) {
//            instance = new RealmDbHelper();
//        }
//
//        return instance;
//    }

//    public Realm getRealm(Context context) {
//        Realm.init(context);
//        if (key == null) {
//            if (PreferUtils.getKeyEncryption(context).equals(Constants.KEY_ENCRYPTION_DEFAULT)) {
//                byte[] b = new byte[64];
//                new Random().nextBytes(b);
//
//                key = Base64.encodeToString(b, Base64.DEFAULT);
//
//                LogUtils.d(TAG, "getRealm , key constructor : " + key);
//                PreferUtils.setKeyEncryption(context, key);
//            } else {
//                key = PreferUtils.getKeyEncryption(context);
//                LogUtils.d(TAG, "getRealm , key : " + key);
//            }
//        }
//
//        if (realmConfiguration == null)
//            realmConfiguration = new RealmConfiguration.Builder()
//                    .name(Constants.DB_NAME)
//                    .deleteRealmIfMigrationNeeded()
//                    .encryptionKey(Base64.decode(key, Base64.DEFAULT))
//                    .build();
//
//        if(context instanceof Activity){
//            ((Activity) context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    LogUtils.d(TAG,"main thread");
//                        realm = Realm.getInstance(realmConfiguration);
//                }
//            });
//        }else {
//            LogUtils.d(TAG,"!!!!!!!!! main thread");
//        }
//
//        return realm;
//    }
//
//    public void beginTransaction() {
//        realm.beginTransaction();
//    }
//
//    public void commitTransaction() {
//        realm.commitTransaction();
//    }
//
//    public void close() {
//        realm.close();
//    }


}