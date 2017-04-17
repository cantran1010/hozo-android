package vn.tonish.hozo.database.manager;

import android.content.Context;
import android.util.Base64;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;

public class RealmDbHelper {

    private static final String TAG = RealmDbHelper.class.getName();
    private static RealmDbHelper instance;
    private Realm realm;
    private RealmConfiguration realmConfiguration;

    public static RealmDbHelper getInstance() {

        if (instance == null) {
            instance = new RealmDbHelper();
        }

        return instance;
    }

    private static String key = null;

    public Realm getRealm(Context context) {

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

        if (realm == null)
            realm = Realm.getInstance(realmConfiguration);

        return realm;
    }

    public void beginTransaction() {
        realm.beginTransaction();
    }

    public void commitTransaction() {
        realm.commitTransaction();
    }

    public void close() {
        realm.close();
    }


}
