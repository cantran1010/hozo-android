package vn.tonish.hozo.database.manager;

import android.content.Context;
import android.util.Base64;

import java.util.Random;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;

import static com.facebook.login.widget.ProfilePictureView.TAG;

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
                    .schemaVersion(4)
                    .migration(migration)
//                    .deleteRealmIfMigrationNeeded()
                    .encryptionKey(Base64.decode(key, Base64.DEFAULT))
                    .build();

        return realmConfiguration;
    }

    public static RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            LogUtils.d(TAG, "RealmMigration , oldVersion : " + oldVersion + " , newVersion : " + newVersion);
            // DynamicRealm exposes an editable schema
            RealmSchema schema = realm.getSchema();

            if (oldVersion == 1) {
                schema.get("UserEntity")
                        .addField("taskerDoneCount", int.class)
                        .addField("posterDoneCount", int.class)
                        .addField("taskerDoneRate", float.class)
                        .addField("posterDoneRate", float.class)
                        .addField("emailActive", boolean.class);

                schema.get("PosterEntity")
                        .addField("email", String.class)
                        .addField("emailActive", boolean.class)
                        .addField("facebookId", String.class);

                schema.get("BidderEntity")
                        .addField("email", String.class)
                        .addField("emailActive", boolean.class)
                        .addField("facebookId", String.class);

                schema.get("AssignerEntity")
                        .addField("email", String.class)
                        .addField("emailActive", boolean.class)
                        .addField("facebookId", String.class);

                schema.get("SettingEntiny")
                        .addField("city", String.class);

                oldVersion++;
            }

            if (oldVersion == 2) {
                schema.create("RealmInt")
                        .addField("val", int.class);
                schema.create("RealmDouble")
                        .addField("val", double.class);
                schema.create("RealmString")
                        .addField("val", String.class);
                schema.create("SettingAdvanceEntity")
                        .addField("userId", int.class, FieldAttribute.PRIMARY_KEY)
                        .addField("notification", boolean.class)
                        .addField("status", String.class)
                        .addRealmListField("categories", schema.get("RealmInt"))
                        .addRealmListField("days", schema.get("RealmInt"))
                        .addField("distance", int.class)
                        .addRealmListField("latlon", schema.get("RealmDouble"))
                        .addField("minWorkerRate", int.class)
                        .addField("maxWorkerRate", int.class)
                        .addField("address", String.class)
                        .addRealmListField("keywords", schema.get("RealmString"));

                schema.get("UserEntity")
                        .addField("latitude", double.class)
                        .addField("longitude", double.class);

                schema.create("PostTaskEntity")
                        .addField("id", int.class, FieldAttribute.PRIMARY_KEY)
                        .addField("lat", double.class)
                        .addField("lon", double.class)
                        .addField("address", String.class);
                oldVersion++;

            }

            if (oldVersion == 3) {

                schema.get("UserEntity")
                        .addRealmListField("skills", schema.get("RealmString"))
                        .addRealmListField("languages", schema.get("RealmString"))
                        .addRealmListField("images", schema.get("ImageProfileResponse"))
                        .addField("experiences", String.class)
                        .addField("privacyGender", boolean.class)
                        .addField("privacyBirth", boolean.class)
                        .addField("activitiesCount", int.class)
                        .addField("followersCount", int.class)
                        .addField("role", String.class);

                schema.create("TagResponse")
                        .addField("id", int.class)
                        .addField("value", String.class);

                oldVersion++;

            }

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