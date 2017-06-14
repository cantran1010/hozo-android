package vn.tonish.hozo.database.manager;

import android.content.Context;
import android.util.Base64;

import java.util.Random;

import io.realm.DynamicRealm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.PreferUtils;

/**
 * Created by LongBui on 4/12/17.
 */
public class RealmDbHelper {

    private static RealmConfiguration realmConfiguration;
    private static String key = null;

    public static RealmConfiguration getRealmConfig(Context context) {
        if (key == null) {
            if (PreferUtils.getKeyEncryption(context).equals(Constants.KEY_ENCRYPTION_DEFAULT)) {
                byte[] b = new byte[64];
                new Random().nextBytes(b);
                key = Base64.encodeToString(b, Base64.DEFAULT);
                PreferUtils.setKeyEncryption(context, key);
            } else {
                key = PreferUtils.getKeyEncryption(context);
            }
        }

        if (realmConfiguration == null)
            realmConfiguration = new RealmConfiguration.Builder()
                    .name(Constants.DB_NAME)
                    .schemaVersion(1)
                    .migration(migration)
//                    .deleteRealmIfMigrationNeeded()
                    .encryptionKey(Base64.decode(key, Base64.DEFAULT))
                    .build();

        return realmConfiguration;
    }

    public static RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            // DynamicRealm exposes an editable schema
//            RealmSchema schema = realm.getSchema();

            // Migrate to version 1: Add a new class.
            // Example:
            // public Person extends RealmObject {
            //     private String name;
            //     private int age;
            //     // getters and setters left out for brevity
            // }
//            if (oldVersion == 0) {
//                schema.create("Person")
//                        .addField("name", String.class)
//                        .addField("age", int.class);
//                oldVersion++;
//            }

            // Migrate to version 2: Add a primary key + object references
            // Example:
            // public Person extends RealmObject {
            //     private String name;
            //     @PrimaryKey
            //     private int age;
            //     private Dog favoriteDog;
            //     private RealmList<Dog> dogs;
            //     // getters and setters left out for brevity
            // }
//            if (oldVersion == 1) {
//                schema.get("CategoryEntity")
//                        .addField("newColumn", String.class);
////                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
////                        .addRealmObjectField("favoriteDog", schema.get("Dog"))
////                        .addRealmListField("dogs", schema.get("Dog"));
//                oldVersion++;
//            }
        }
    };

}