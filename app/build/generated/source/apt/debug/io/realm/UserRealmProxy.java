package io.realm;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.LinkView;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.SharedRealm;
import io.realm.internal.Table;
import io.realm.internal.android.JsonUtils;
import io.realm.log.RealmLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserRealmProxy extends vn.tonish.hozo.model.User
    implements RealmObjectProxy, UserRealmProxyInterface {

    static final class UserColumnInfo extends ColumnInfo
        implements Cloneable {

        public long idIndex;
        public long mobileIndex;
        public long token_keyIndex;
        public long full_nameIndex;
        public long created_atIndex;
        public long avatarIndex;
        public long addressIndex;
        public long facebook_idIndex;
        public long date_of_birthIndex;
        public long genderIndex;
        public long verifyIndex;
        public long average_ratingIndex;
        public long average_starsIndex;
        public long descriptionIndex;
        public long educationIndex;
        public long emailIndex;
        public long languagesIndex;
        public long rankingIndex;
        public long rating_percentageIndex;
        public long review_countsIndex;
        public long updated_atIndex;
        public long reviewIndex;
        public long priceBitIndex;

        UserColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(23);
            this.idIndex = getValidColumnIndex(path, table, "User", "id");
            indicesMap.put("id", this.idIndex);
            this.mobileIndex = getValidColumnIndex(path, table, "User", "mobile");
            indicesMap.put("mobile", this.mobileIndex);
            this.token_keyIndex = getValidColumnIndex(path, table, "User", "token_key");
            indicesMap.put("token_key", this.token_keyIndex);
            this.full_nameIndex = getValidColumnIndex(path, table, "User", "full_name");
            indicesMap.put("full_name", this.full_nameIndex);
            this.created_atIndex = getValidColumnIndex(path, table, "User", "created_at");
            indicesMap.put("created_at", this.created_atIndex);
            this.avatarIndex = getValidColumnIndex(path, table, "User", "avatar");
            indicesMap.put("avatar", this.avatarIndex);
            this.addressIndex = getValidColumnIndex(path, table, "User", "address");
            indicesMap.put("address", this.addressIndex);
            this.facebook_idIndex = getValidColumnIndex(path, table, "User", "facebook_id");
            indicesMap.put("facebook_id", this.facebook_idIndex);
            this.date_of_birthIndex = getValidColumnIndex(path, table, "User", "date_of_birth");
            indicesMap.put("date_of_birth", this.date_of_birthIndex);
            this.genderIndex = getValidColumnIndex(path, table, "User", "gender");
            indicesMap.put("gender", this.genderIndex);
            this.verifyIndex = getValidColumnIndex(path, table, "User", "verify");
            indicesMap.put("verify", this.verifyIndex);
            this.average_ratingIndex = getValidColumnIndex(path, table, "User", "average_rating");
            indicesMap.put("average_rating", this.average_ratingIndex);
            this.average_starsIndex = getValidColumnIndex(path, table, "User", "average_stars");
            indicesMap.put("average_stars", this.average_starsIndex);
            this.descriptionIndex = getValidColumnIndex(path, table, "User", "description");
            indicesMap.put("description", this.descriptionIndex);
            this.educationIndex = getValidColumnIndex(path, table, "User", "education");
            indicesMap.put("education", this.educationIndex);
            this.emailIndex = getValidColumnIndex(path, table, "User", "email");
            indicesMap.put("email", this.emailIndex);
            this.languagesIndex = getValidColumnIndex(path, table, "User", "languages");
            indicesMap.put("languages", this.languagesIndex);
            this.rankingIndex = getValidColumnIndex(path, table, "User", "ranking");
            indicesMap.put("ranking", this.rankingIndex);
            this.rating_percentageIndex = getValidColumnIndex(path, table, "User", "rating_percentage");
            indicesMap.put("rating_percentage", this.rating_percentageIndex);
            this.review_countsIndex = getValidColumnIndex(path, table, "User", "review_counts");
            indicesMap.put("review_counts", this.review_countsIndex);
            this.updated_atIndex = getValidColumnIndex(path, table, "User", "updated_at");
            indicesMap.put("updated_at", this.updated_atIndex);
            this.reviewIndex = getValidColumnIndex(path, table, "User", "review");
            indicesMap.put("review", this.reviewIndex);
            this.priceBitIndex = getValidColumnIndex(path, table, "User", "priceBit");
            indicesMap.put("priceBit", this.priceBitIndex);

            setIndicesMap(indicesMap);
        }

        @Override
        public final void copyColumnInfoFrom(ColumnInfo other) {
            final UserColumnInfo otherInfo = (UserColumnInfo) other;
            this.idIndex = otherInfo.idIndex;
            this.mobileIndex = otherInfo.mobileIndex;
            this.token_keyIndex = otherInfo.token_keyIndex;
            this.full_nameIndex = otherInfo.full_nameIndex;
            this.created_atIndex = otherInfo.created_atIndex;
            this.avatarIndex = otherInfo.avatarIndex;
            this.addressIndex = otherInfo.addressIndex;
            this.facebook_idIndex = otherInfo.facebook_idIndex;
            this.date_of_birthIndex = otherInfo.date_of_birthIndex;
            this.genderIndex = otherInfo.genderIndex;
            this.verifyIndex = otherInfo.verifyIndex;
            this.average_ratingIndex = otherInfo.average_ratingIndex;
            this.average_starsIndex = otherInfo.average_starsIndex;
            this.descriptionIndex = otherInfo.descriptionIndex;
            this.educationIndex = otherInfo.educationIndex;
            this.emailIndex = otherInfo.emailIndex;
            this.languagesIndex = otherInfo.languagesIndex;
            this.rankingIndex = otherInfo.rankingIndex;
            this.rating_percentageIndex = otherInfo.rating_percentageIndex;
            this.review_countsIndex = otherInfo.review_countsIndex;
            this.updated_atIndex = otherInfo.updated_atIndex;
            this.reviewIndex = otherInfo.reviewIndex;
            this.priceBitIndex = otherInfo.priceBitIndex;

            setIndicesMap(otherInfo.getIndicesMap());
        }

        @Override
        public final UserColumnInfo clone() {
            return (UserColumnInfo) super.clone();
        }

    }
    private UserColumnInfo columnInfo;
    private ProxyState<vn.tonish.hozo.model.User> proxyState;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("mobile");
        fieldNames.add("token_key");
        fieldNames.add("full_name");
        fieldNames.add("created_at");
        fieldNames.add("avatar");
        fieldNames.add("address");
        fieldNames.add("facebook_id");
        fieldNames.add("date_of_birth");
        fieldNames.add("gender");
        fieldNames.add("verify");
        fieldNames.add("average_rating");
        fieldNames.add("average_stars");
        fieldNames.add("description");
        fieldNames.add("education");
        fieldNames.add("email");
        fieldNames.add("languages");
        fieldNames.add("ranking");
        fieldNames.add("rating_percentage");
        fieldNames.add("review_counts");
        fieldNames.add("updated_at");
        fieldNames.add("review");
        fieldNames.add("priceBit");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    UserRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (UserColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<vn.tonish.hozo.model.User>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$id() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.idIndex);
    }

    @Override
    public void realmSet$id(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.idIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.idIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.idIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.idIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$mobile() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.mobileIndex);
    }

    @Override
    public void realmSet$mobile(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.mobileIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.mobileIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.mobileIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.mobileIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$token_key() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.token_keyIndex);
    }

    @Override
    public void realmSet$token_key(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.token_keyIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.token_keyIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.token_keyIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.token_keyIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$full_name() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.full_nameIndex);
    }

    @Override
    public void realmSet$full_name(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.full_nameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.full_nameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.full_nameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.full_nameIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$created_at() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.created_atIndex);
    }

    @Override
    public void realmSet$created_at(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.created_atIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.created_atIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.created_atIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.created_atIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$avatar() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.avatarIndex);
    }

    @Override
    public void realmSet$avatar(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.avatarIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.avatarIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.avatarIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.avatarIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$address() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.addressIndex);
    }

    @Override
    public void realmSet$address(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.addressIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.addressIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.addressIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.addressIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$facebook_id() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.facebook_idIndex);
    }

    @Override
    public void realmSet$facebook_id(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.facebook_idIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.facebook_idIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.facebook_idIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.facebook_idIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$date_of_birth() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.date_of_birthIndex);
    }

    @Override
    public void realmSet$date_of_birth(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.date_of_birthIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.date_of_birthIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.date_of_birthIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.date_of_birthIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$gender() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.genderIndex);
    }

    @Override
    public void realmSet$gender(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.genderIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.genderIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.genderIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.genderIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$verify() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.verifyIndex);
    }

    @Override
    public void realmSet$verify(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.verifyIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.verifyIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.verifyIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.verifyIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$average_rating() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.average_ratingIndex);
    }

    @Override
    public void realmSet$average_rating(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.average_ratingIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.average_ratingIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.average_ratingIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.average_ratingIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$average_stars() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.average_starsIndex);
    }

    @Override
    public void realmSet$average_stars(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.average_starsIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.average_starsIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.average_starsIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.average_starsIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$description() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.descriptionIndex);
    }

    @Override
    public void realmSet$description(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.descriptionIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.descriptionIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.descriptionIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.descriptionIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$education() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.educationIndex);
    }

    @Override
    public void realmSet$education(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.educationIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.educationIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.educationIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.educationIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$email() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.emailIndex);
    }

    @Override
    public void realmSet$email(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.emailIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.emailIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.emailIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.emailIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$languages() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.languagesIndex);
    }

    @Override
    public void realmSet$languages(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.languagesIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.languagesIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.languagesIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.languagesIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$ranking() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.rankingIndex);
    }

    @Override
    public void realmSet$ranking(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.rankingIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.rankingIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.rankingIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.rankingIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$rating_percentage() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.rating_percentageIndex);
    }

    @Override
    public void realmSet$rating_percentage(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.rating_percentageIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.rating_percentageIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.rating_percentageIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.rating_percentageIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$review_counts() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.review_countsIndex);
    }

    @Override
    public void realmSet$review_counts(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.review_countsIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.review_countsIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.review_countsIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.review_countsIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$updated_at() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.updated_atIndex);
    }

    @Override
    public void realmSet$updated_at(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.updated_atIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.updated_atIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.updated_atIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.updated_atIndex, value);
    }

    @Override
    public vn.tonish.hozo.model.Comment realmGet$review() {
        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNullLink(columnInfo.reviewIndex)) {
            return null;
        }
        return proxyState.getRealm$realm().get(vn.tonish.hozo.model.Comment.class, proxyState.getRow$realm().getLink(columnInfo.reviewIndex), false, Collections.<String>emptyList());
    }

    @Override
    public void realmSet$review(vn.tonish.hozo.model.Comment value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("review")) {
                return;
            }
            if (value != null && !RealmObject.isManaged(value)) {
                value = ((Realm) proxyState.getRealm$realm()).copyToRealm(value);
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                // Table#nullifyLink() does not support default value. Just using Row.
                row.nullifyLink(columnInfo.reviewIndex);
                return;
            }
            if (!RealmObject.isValid(value)) {
                throw new IllegalArgumentException("'value' is not a valid managed object.");
            }
            if (((RealmObjectProxy) value).realmGet$proxyState().getRealm$realm() != proxyState.getRealm$realm()) {
                throw new IllegalArgumentException("'value' belongs to a different Realm.");
            }
            row.getTable().setLink(columnInfo.reviewIndex, row.getIndex(), ((RealmObjectProxy) value).realmGet$proxyState().getRow$realm().getIndex(), true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().nullifyLink(columnInfo.reviewIndex);
            return;
        }
        if (!(RealmObject.isManaged(value) && RealmObject.isValid(value))) {
            throw new IllegalArgumentException("'value' is not a valid managed object.");
        }
        if (((RealmObjectProxy)value).realmGet$proxyState().getRealm$realm() != proxyState.getRealm$realm()) {
            throw new IllegalArgumentException("'value' belongs to a different Realm.");
        }
        proxyState.getRow$realm().setLink(columnInfo.reviewIndex, ((RealmObjectProxy)value).realmGet$proxyState().getRow$realm().getIndex());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$priceBit() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.priceBitIndex);
    }

    @Override
    public void realmSet$priceBit(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.priceBitIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.priceBitIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.priceBitIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.priceBitIndex, value);
    }

    public static RealmObjectSchema createRealmObjectSchema(RealmSchema realmSchema) {
        if (!realmSchema.contains("User")) {
            RealmObjectSchema realmObjectSchema = realmSchema.create("User");
            realmObjectSchema.add("id", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("mobile", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("token_key", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("full_name", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("created_at", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("avatar", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("address", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("facebook_id", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("date_of_birth", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("gender", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("verify", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("average_rating", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("average_stars", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("description", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("education", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("email", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("languages", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("ranking", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("rating_percentage", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("review_counts", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("updated_at", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            if (!realmSchema.contains("Comment")) {
                CommentRealmProxy.createRealmObjectSchema(realmSchema);
            }
            realmObjectSchema.add("review", RealmFieldType.OBJECT, realmSchema.get("Comment"));
            realmObjectSchema.add("priceBit", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            return realmObjectSchema;
        }
        return realmSchema.get("User");
    }

    public static UserColumnInfo validateTable(SharedRealm sharedRealm, boolean allowExtraColumns) {
        if (!sharedRealm.hasTable("class_User")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "The 'User' class is missing from the schema for this Realm.");
        }
        Table table = sharedRealm.getTable("class_User");
        final long columnCount = table.getColumnCount();
        if (columnCount != 23) {
            if (columnCount < 23) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is less than expected - expected 23 but was " + columnCount);
            }
            if (allowExtraColumns) {
                RealmLog.debug("Field count is more than expected - expected 23 but was %1$d", columnCount);
            } else {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is more than expected - expected 23 but was " + columnCount);
            }
        }
        Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
        for (long i = 0; i < columnCount; i++) {
            columnTypes.put(table.getColumnName(i), table.getColumnType(i));
        }

        final UserColumnInfo columnInfo = new UserColumnInfo(sharedRealm.getPath(), table);

        if (table.hasPrimaryKey()) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Primary Key defined for field " + table.getColumnName(table.getPrimaryKey()) + " was removed.");
        }

        if (!columnTypes.containsKey("id")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'id' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("id") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'id' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.idIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'id' is required. Either set @Required to field 'id' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("mobile")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'mobile' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("mobile") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'mobile' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.mobileIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'mobile' is required. Either set @Required to field 'mobile' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("token_key")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'token_key' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("token_key") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'token_key' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.token_keyIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'token_key' is required. Either set @Required to field 'token_key' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("full_name")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'full_name' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("full_name") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'full_name' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.full_nameIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'full_name' is required. Either set @Required to field 'full_name' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("created_at")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'created_at' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("created_at") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'created_at' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.created_atIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'created_at' is required. Either set @Required to field 'created_at' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("avatar")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'avatar' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("avatar") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'avatar' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.avatarIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'avatar' is required. Either set @Required to field 'avatar' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("address")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'address' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("address") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'address' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.addressIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'address' is required. Either set @Required to field 'address' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("facebook_id")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'facebook_id' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("facebook_id") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'facebook_id' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.facebook_idIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'facebook_id' is required. Either set @Required to field 'facebook_id' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("date_of_birth")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'date_of_birth' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("date_of_birth") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'date_of_birth' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.date_of_birthIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'date_of_birth' is required. Either set @Required to field 'date_of_birth' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("gender")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'gender' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("gender") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'gender' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.genderIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'gender' is required. Either set @Required to field 'gender' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("verify")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'verify' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("verify") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'verify' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.verifyIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'verify' is required. Either set @Required to field 'verify' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("average_rating")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'average_rating' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("average_rating") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'average_rating' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.average_ratingIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'average_rating' is required. Either set @Required to field 'average_rating' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("average_stars")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'average_stars' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("average_stars") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'average_stars' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.average_starsIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'average_stars' is required. Either set @Required to field 'average_stars' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("description")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'description' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("description") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'description' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.descriptionIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'description' is required. Either set @Required to field 'description' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("education")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'education' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("education") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'education' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.educationIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'education' is required. Either set @Required to field 'education' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("email")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'email' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("email") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'email' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.emailIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'email' is required. Either set @Required to field 'email' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("languages")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'languages' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("languages") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'languages' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.languagesIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'languages' is required. Either set @Required to field 'languages' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("ranking")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'ranking' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("ranking") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'ranking' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.rankingIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'ranking' is required. Either set @Required to field 'ranking' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("rating_percentage")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'rating_percentage' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("rating_percentage") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'rating_percentage' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.rating_percentageIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'rating_percentage' is required. Either set @Required to field 'rating_percentage' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("review_counts")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'review_counts' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("review_counts") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'review_counts' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.review_countsIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'review_counts' is required. Either set @Required to field 'review_counts' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("updated_at")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'updated_at' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("updated_at") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'updated_at' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.updated_atIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'updated_at' is required. Either set @Required to field 'updated_at' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("review")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'review' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("review") != RealmFieldType.OBJECT) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'Comment' for field 'review'");
        }
        if (!sharedRealm.hasTable("class_Comment")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing class 'class_Comment' for field 'review'");
        }
        Table table_21 = sharedRealm.getTable("class_Comment");
        if (!table.getLinkTarget(columnInfo.reviewIndex).hasSameSchema(table_21)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid RealmObject for field 'review': '" + table.getLinkTarget(columnInfo.reviewIndex).getName() + "' expected - was '" + table_21.getName() + "'");
        }
        if (!columnTypes.containsKey("priceBit")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'priceBit' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("priceBit") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'priceBit' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.priceBitIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'priceBit' is required. Either set @Required to field 'priceBit' or migrate using RealmObjectSchema.setNullable().");
        }

        return columnInfo;
    }

    public static String getTableName() {
        return "class_User";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static vn.tonish.hozo.model.User createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = new ArrayList<String>(1);
        if (json.has("review")) {
            excludeFields.add("review");
        }
        vn.tonish.hozo.model.User obj = realm.createObjectInternal(vn.tonish.hozo.model.User.class, true, excludeFields);
        if (json.has("id")) {
            if (json.isNull("id")) {
                ((UserRealmProxyInterface) obj).realmSet$id(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$id((String) json.getString("id"));
            }
        }
        if (json.has("mobile")) {
            if (json.isNull("mobile")) {
                ((UserRealmProxyInterface) obj).realmSet$mobile(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$mobile((String) json.getString("mobile"));
            }
        }
        if (json.has("token_key")) {
            if (json.isNull("token_key")) {
                ((UserRealmProxyInterface) obj).realmSet$token_key(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$token_key((String) json.getString("token_key"));
            }
        }
        if (json.has("full_name")) {
            if (json.isNull("full_name")) {
                ((UserRealmProxyInterface) obj).realmSet$full_name(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$full_name((String) json.getString("full_name"));
            }
        }
        if (json.has("created_at")) {
            if (json.isNull("created_at")) {
                ((UserRealmProxyInterface) obj).realmSet$created_at(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$created_at((String) json.getString("created_at"));
            }
        }
        if (json.has("avatar")) {
            if (json.isNull("avatar")) {
                ((UserRealmProxyInterface) obj).realmSet$avatar(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$avatar((String) json.getString("avatar"));
            }
        }
        if (json.has("address")) {
            if (json.isNull("address")) {
                ((UserRealmProxyInterface) obj).realmSet$address(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$address((String) json.getString("address"));
            }
        }
        if (json.has("facebook_id")) {
            if (json.isNull("facebook_id")) {
                ((UserRealmProxyInterface) obj).realmSet$facebook_id(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$facebook_id((String) json.getString("facebook_id"));
            }
        }
        if (json.has("date_of_birth")) {
            if (json.isNull("date_of_birth")) {
                ((UserRealmProxyInterface) obj).realmSet$date_of_birth(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$date_of_birth((String) json.getString("date_of_birth"));
            }
        }
        if (json.has("gender")) {
            if (json.isNull("gender")) {
                ((UserRealmProxyInterface) obj).realmSet$gender(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$gender((String) json.getString("gender"));
            }
        }
        if (json.has("verify")) {
            if (json.isNull("verify")) {
                ((UserRealmProxyInterface) obj).realmSet$verify(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$verify((String) json.getString("verify"));
            }
        }
        if (json.has("average_rating")) {
            if (json.isNull("average_rating")) {
                ((UserRealmProxyInterface) obj).realmSet$average_rating(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$average_rating((String) json.getString("average_rating"));
            }
        }
        if (json.has("average_stars")) {
            if (json.isNull("average_stars")) {
                ((UserRealmProxyInterface) obj).realmSet$average_stars(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$average_stars((String) json.getString("average_stars"));
            }
        }
        if (json.has("description")) {
            if (json.isNull("description")) {
                ((UserRealmProxyInterface) obj).realmSet$description(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$description((String) json.getString("description"));
            }
        }
        if (json.has("education")) {
            if (json.isNull("education")) {
                ((UserRealmProxyInterface) obj).realmSet$education(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$education((String) json.getString("education"));
            }
        }
        if (json.has("email")) {
            if (json.isNull("email")) {
                ((UserRealmProxyInterface) obj).realmSet$email(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$email((String) json.getString("email"));
            }
        }
        if (json.has("languages")) {
            if (json.isNull("languages")) {
                ((UserRealmProxyInterface) obj).realmSet$languages(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$languages((String) json.getString("languages"));
            }
        }
        if (json.has("ranking")) {
            if (json.isNull("ranking")) {
                ((UserRealmProxyInterface) obj).realmSet$ranking(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$ranking((String) json.getString("ranking"));
            }
        }
        if (json.has("rating_percentage")) {
            if (json.isNull("rating_percentage")) {
                ((UserRealmProxyInterface) obj).realmSet$rating_percentage(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$rating_percentage((String) json.getString("rating_percentage"));
            }
        }
        if (json.has("review_counts")) {
            if (json.isNull("review_counts")) {
                ((UserRealmProxyInterface) obj).realmSet$review_counts(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$review_counts((String) json.getString("review_counts"));
            }
        }
        if (json.has("updated_at")) {
            if (json.isNull("updated_at")) {
                ((UserRealmProxyInterface) obj).realmSet$updated_at(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$updated_at((String) json.getString("updated_at"));
            }
        }
        if (json.has("review")) {
            if (json.isNull("review")) {
                ((UserRealmProxyInterface) obj).realmSet$review(null);
            } else {
                vn.tonish.hozo.model.Comment reviewObj = CommentRealmProxy.createOrUpdateUsingJsonObject(realm, json.getJSONObject("review"), update);
                ((UserRealmProxyInterface) obj).realmSet$review(reviewObj);
            }
        }
        if (json.has("priceBit")) {
            if (json.isNull("priceBit")) {
                ((UserRealmProxyInterface) obj).realmSet$priceBit(null);
            } else {
                ((UserRealmProxyInterface) obj).realmSet$priceBit((String) json.getString("priceBit"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static vn.tonish.hozo.model.User createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        vn.tonish.hozo.model.User obj = new vn.tonish.hozo.model.User();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$id(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$id((String) reader.nextString());
                }
            } else if (name.equals("mobile")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$mobile(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$mobile((String) reader.nextString());
                }
            } else if (name.equals("token_key")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$token_key(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$token_key((String) reader.nextString());
                }
            } else if (name.equals("full_name")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$full_name(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$full_name((String) reader.nextString());
                }
            } else if (name.equals("created_at")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$created_at(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$created_at((String) reader.nextString());
                }
            } else if (name.equals("avatar")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$avatar(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$avatar((String) reader.nextString());
                }
            } else if (name.equals("address")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$address(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$address((String) reader.nextString());
                }
            } else if (name.equals("facebook_id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$facebook_id(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$facebook_id((String) reader.nextString());
                }
            } else if (name.equals("date_of_birth")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$date_of_birth(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$date_of_birth((String) reader.nextString());
                }
            } else if (name.equals("gender")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$gender(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$gender((String) reader.nextString());
                }
            } else if (name.equals("verify")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$verify(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$verify((String) reader.nextString());
                }
            } else if (name.equals("average_rating")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$average_rating(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$average_rating((String) reader.nextString());
                }
            } else if (name.equals("average_stars")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$average_stars(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$average_stars((String) reader.nextString());
                }
            } else if (name.equals("description")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$description(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$description((String) reader.nextString());
                }
            } else if (name.equals("education")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$education(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$education((String) reader.nextString());
                }
            } else if (name.equals("email")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$email(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$email((String) reader.nextString());
                }
            } else if (name.equals("languages")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$languages(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$languages((String) reader.nextString());
                }
            } else if (name.equals("ranking")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$ranking(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$ranking((String) reader.nextString());
                }
            } else if (name.equals("rating_percentage")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$rating_percentage(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$rating_percentage((String) reader.nextString());
                }
            } else if (name.equals("review_counts")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$review_counts(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$review_counts((String) reader.nextString());
                }
            } else if (name.equals("updated_at")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$updated_at(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$updated_at((String) reader.nextString());
                }
            } else if (name.equals("review")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$review(null);
                } else {
                    vn.tonish.hozo.model.Comment reviewObj = CommentRealmProxy.createUsingJsonStream(realm, reader);
                    ((UserRealmProxyInterface) obj).realmSet$review(reviewObj);
                }
            } else if (name.equals("priceBit")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserRealmProxyInterface) obj).realmSet$priceBit(null);
                } else {
                    ((UserRealmProxyInterface) obj).realmSet$priceBit((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        obj = realm.copyToRealm(obj);
        return obj;
    }

    public static vn.tonish.hozo.model.User copyOrUpdate(Realm realm, vn.tonish.hozo.model.User object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (vn.tonish.hozo.model.User) cachedRealmObject;
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static vn.tonish.hozo.model.User copy(Realm realm, vn.tonish.hozo.model.User newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (vn.tonish.hozo.model.User) cachedRealmObject;
        } else {
            // rejecting default values to avoid creating unexpected objects from RealmModel/RealmList fields.
            vn.tonish.hozo.model.User realmObject = realm.createObjectInternal(vn.tonish.hozo.model.User.class, false, Collections.<String>emptyList());
            cache.put(newObject, (RealmObjectProxy) realmObject);
            ((UserRealmProxyInterface) realmObject).realmSet$id(((UserRealmProxyInterface) newObject).realmGet$id());
            ((UserRealmProxyInterface) realmObject).realmSet$mobile(((UserRealmProxyInterface) newObject).realmGet$mobile());
            ((UserRealmProxyInterface) realmObject).realmSet$token_key(((UserRealmProxyInterface) newObject).realmGet$token_key());
            ((UserRealmProxyInterface) realmObject).realmSet$full_name(((UserRealmProxyInterface) newObject).realmGet$full_name());
            ((UserRealmProxyInterface) realmObject).realmSet$created_at(((UserRealmProxyInterface) newObject).realmGet$created_at());
            ((UserRealmProxyInterface) realmObject).realmSet$avatar(((UserRealmProxyInterface) newObject).realmGet$avatar());
            ((UserRealmProxyInterface) realmObject).realmSet$address(((UserRealmProxyInterface) newObject).realmGet$address());
            ((UserRealmProxyInterface) realmObject).realmSet$facebook_id(((UserRealmProxyInterface) newObject).realmGet$facebook_id());
            ((UserRealmProxyInterface) realmObject).realmSet$date_of_birth(((UserRealmProxyInterface) newObject).realmGet$date_of_birth());
            ((UserRealmProxyInterface) realmObject).realmSet$gender(((UserRealmProxyInterface) newObject).realmGet$gender());
            ((UserRealmProxyInterface) realmObject).realmSet$verify(((UserRealmProxyInterface) newObject).realmGet$verify());
            ((UserRealmProxyInterface) realmObject).realmSet$average_rating(((UserRealmProxyInterface) newObject).realmGet$average_rating());
            ((UserRealmProxyInterface) realmObject).realmSet$average_stars(((UserRealmProxyInterface) newObject).realmGet$average_stars());
            ((UserRealmProxyInterface) realmObject).realmSet$description(((UserRealmProxyInterface) newObject).realmGet$description());
            ((UserRealmProxyInterface) realmObject).realmSet$education(((UserRealmProxyInterface) newObject).realmGet$education());
            ((UserRealmProxyInterface) realmObject).realmSet$email(((UserRealmProxyInterface) newObject).realmGet$email());
            ((UserRealmProxyInterface) realmObject).realmSet$languages(((UserRealmProxyInterface) newObject).realmGet$languages());
            ((UserRealmProxyInterface) realmObject).realmSet$ranking(((UserRealmProxyInterface) newObject).realmGet$ranking());
            ((UserRealmProxyInterface) realmObject).realmSet$rating_percentage(((UserRealmProxyInterface) newObject).realmGet$rating_percentage());
            ((UserRealmProxyInterface) realmObject).realmSet$review_counts(((UserRealmProxyInterface) newObject).realmGet$review_counts());
            ((UserRealmProxyInterface) realmObject).realmSet$updated_at(((UserRealmProxyInterface) newObject).realmGet$updated_at());

            vn.tonish.hozo.model.Comment reviewObj = ((UserRealmProxyInterface) newObject).realmGet$review();
            if (reviewObj != null) {
                vn.tonish.hozo.model.Comment cachereview = (vn.tonish.hozo.model.Comment) cache.get(reviewObj);
                if (cachereview != null) {
                    ((UserRealmProxyInterface) realmObject).realmSet$review(cachereview);
                } else {
                    ((UserRealmProxyInterface) realmObject).realmSet$review(CommentRealmProxy.copyOrUpdate(realm, reviewObj, update, cache));
                }
            } else {
                ((UserRealmProxyInterface) realmObject).realmSet$review(null);
            }
            ((UserRealmProxyInterface) realmObject).realmSet$priceBit(((UserRealmProxyInterface) newObject).realmGet$priceBit());
            return realmObject;
        }
    }

    public static long insert(Realm realm, vn.tonish.hozo.model.User object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(vn.tonish.hozo.model.User.class);
        long tableNativePtr = table.getNativeTablePointer();
        UserColumnInfo columnInfo = (UserColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.User.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        String realmGet$id = ((UserRealmProxyInterface)object).realmGet$id();
        if (realmGet$id != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
        }
        String realmGet$mobile = ((UserRealmProxyInterface)object).realmGet$mobile();
        if (realmGet$mobile != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.mobileIndex, rowIndex, realmGet$mobile, false);
        }
        String realmGet$token_key = ((UserRealmProxyInterface)object).realmGet$token_key();
        if (realmGet$token_key != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.token_keyIndex, rowIndex, realmGet$token_key, false);
        }
        String realmGet$full_name = ((UserRealmProxyInterface)object).realmGet$full_name();
        if (realmGet$full_name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.full_nameIndex, rowIndex, realmGet$full_name, false);
        }
        String realmGet$created_at = ((UserRealmProxyInterface)object).realmGet$created_at();
        if (realmGet$created_at != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.created_atIndex, rowIndex, realmGet$created_at, false);
        }
        String realmGet$avatar = ((UserRealmProxyInterface)object).realmGet$avatar();
        if (realmGet$avatar != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.avatarIndex, rowIndex, realmGet$avatar, false);
        }
        String realmGet$address = ((UserRealmProxyInterface)object).realmGet$address();
        if (realmGet$address != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.addressIndex, rowIndex, realmGet$address, false);
        }
        String realmGet$facebook_id = ((UserRealmProxyInterface)object).realmGet$facebook_id();
        if (realmGet$facebook_id != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.facebook_idIndex, rowIndex, realmGet$facebook_id, false);
        }
        String realmGet$date_of_birth = ((UserRealmProxyInterface)object).realmGet$date_of_birth();
        if (realmGet$date_of_birth != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.date_of_birthIndex, rowIndex, realmGet$date_of_birth, false);
        }
        String realmGet$gender = ((UserRealmProxyInterface)object).realmGet$gender();
        if (realmGet$gender != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.genderIndex, rowIndex, realmGet$gender, false);
        }
        String realmGet$verify = ((UserRealmProxyInterface)object).realmGet$verify();
        if (realmGet$verify != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.verifyIndex, rowIndex, realmGet$verify, false);
        }
        String realmGet$average_rating = ((UserRealmProxyInterface)object).realmGet$average_rating();
        if (realmGet$average_rating != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.average_ratingIndex, rowIndex, realmGet$average_rating, false);
        }
        String realmGet$average_stars = ((UserRealmProxyInterface)object).realmGet$average_stars();
        if (realmGet$average_stars != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.average_starsIndex, rowIndex, realmGet$average_stars, false);
        }
        String realmGet$description = ((UserRealmProxyInterface)object).realmGet$description();
        if (realmGet$description != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description, false);
        }
        String realmGet$education = ((UserRealmProxyInterface)object).realmGet$education();
        if (realmGet$education != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.educationIndex, rowIndex, realmGet$education, false);
        }
        String realmGet$email = ((UserRealmProxyInterface)object).realmGet$email();
        if (realmGet$email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
        }
        String realmGet$languages = ((UserRealmProxyInterface)object).realmGet$languages();
        if (realmGet$languages != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.languagesIndex, rowIndex, realmGet$languages, false);
        }
        String realmGet$ranking = ((UserRealmProxyInterface)object).realmGet$ranking();
        if (realmGet$ranking != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.rankingIndex, rowIndex, realmGet$ranking, false);
        }
        String realmGet$rating_percentage = ((UserRealmProxyInterface)object).realmGet$rating_percentage();
        if (realmGet$rating_percentage != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.rating_percentageIndex, rowIndex, realmGet$rating_percentage, false);
        }
        String realmGet$review_counts = ((UserRealmProxyInterface)object).realmGet$review_counts();
        if (realmGet$review_counts != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.review_countsIndex, rowIndex, realmGet$review_counts, false);
        }
        String realmGet$updated_at = ((UserRealmProxyInterface)object).realmGet$updated_at();
        if (realmGet$updated_at != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.updated_atIndex, rowIndex, realmGet$updated_at, false);
        }

        vn.tonish.hozo.model.Comment reviewObj = ((UserRealmProxyInterface) object).realmGet$review();
        if (reviewObj != null) {
            Long cachereview = cache.get(reviewObj);
            if (cachereview == null) {
                cachereview = CommentRealmProxy.insert(realm, reviewObj, cache);
            }
            Table.nativeSetLink(tableNativePtr, columnInfo.reviewIndex, rowIndex, cachereview, false);
        }
        String realmGet$priceBit = ((UserRealmProxyInterface)object).realmGet$priceBit();
        if (realmGet$priceBit != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.priceBitIndex, rowIndex, realmGet$priceBit, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(vn.tonish.hozo.model.User.class);
        long tableNativePtr = table.getNativeTablePointer();
        UserColumnInfo columnInfo = (UserColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.User.class);
        vn.tonish.hozo.model.User object = null;
        while (objects.hasNext()) {
            object = (vn.tonish.hozo.model.User) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                String realmGet$id = ((UserRealmProxyInterface)object).realmGet$id();
                if (realmGet$id != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
                }
                String realmGet$mobile = ((UserRealmProxyInterface)object).realmGet$mobile();
                if (realmGet$mobile != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.mobileIndex, rowIndex, realmGet$mobile, false);
                }
                String realmGet$token_key = ((UserRealmProxyInterface)object).realmGet$token_key();
                if (realmGet$token_key != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.token_keyIndex, rowIndex, realmGet$token_key, false);
                }
                String realmGet$full_name = ((UserRealmProxyInterface)object).realmGet$full_name();
                if (realmGet$full_name != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.full_nameIndex, rowIndex, realmGet$full_name, false);
                }
                String realmGet$created_at = ((UserRealmProxyInterface)object).realmGet$created_at();
                if (realmGet$created_at != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.created_atIndex, rowIndex, realmGet$created_at, false);
                }
                String realmGet$avatar = ((UserRealmProxyInterface)object).realmGet$avatar();
                if (realmGet$avatar != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.avatarIndex, rowIndex, realmGet$avatar, false);
                }
                String realmGet$address = ((UserRealmProxyInterface)object).realmGet$address();
                if (realmGet$address != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.addressIndex, rowIndex, realmGet$address, false);
                }
                String realmGet$facebook_id = ((UserRealmProxyInterface)object).realmGet$facebook_id();
                if (realmGet$facebook_id != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.facebook_idIndex, rowIndex, realmGet$facebook_id, false);
                }
                String realmGet$date_of_birth = ((UserRealmProxyInterface)object).realmGet$date_of_birth();
                if (realmGet$date_of_birth != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.date_of_birthIndex, rowIndex, realmGet$date_of_birth, false);
                }
                String realmGet$gender = ((UserRealmProxyInterface)object).realmGet$gender();
                if (realmGet$gender != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.genderIndex, rowIndex, realmGet$gender, false);
                }
                String realmGet$verify = ((UserRealmProxyInterface)object).realmGet$verify();
                if (realmGet$verify != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.verifyIndex, rowIndex, realmGet$verify, false);
                }
                String realmGet$average_rating = ((UserRealmProxyInterface)object).realmGet$average_rating();
                if (realmGet$average_rating != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.average_ratingIndex, rowIndex, realmGet$average_rating, false);
                }
                String realmGet$average_stars = ((UserRealmProxyInterface)object).realmGet$average_stars();
                if (realmGet$average_stars != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.average_starsIndex, rowIndex, realmGet$average_stars, false);
                }
                String realmGet$description = ((UserRealmProxyInterface)object).realmGet$description();
                if (realmGet$description != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description, false);
                }
                String realmGet$education = ((UserRealmProxyInterface)object).realmGet$education();
                if (realmGet$education != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.educationIndex, rowIndex, realmGet$education, false);
                }
                String realmGet$email = ((UserRealmProxyInterface)object).realmGet$email();
                if (realmGet$email != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
                }
                String realmGet$languages = ((UserRealmProxyInterface)object).realmGet$languages();
                if (realmGet$languages != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.languagesIndex, rowIndex, realmGet$languages, false);
                }
                String realmGet$ranking = ((UserRealmProxyInterface)object).realmGet$ranking();
                if (realmGet$ranking != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.rankingIndex, rowIndex, realmGet$ranking, false);
                }
                String realmGet$rating_percentage = ((UserRealmProxyInterface)object).realmGet$rating_percentage();
                if (realmGet$rating_percentage != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.rating_percentageIndex, rowIndex, realmGet$rating_percentage, false);
                }
                String realmGet$review_counts = ((UserRealmProxyInterface)object).realmGet$review_counts();
                if (realmGet$review_counts != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.review_countsIndex, rowIndex, realmGet$review_counts, false);
                }
                String realmGet$updated_at = ((UserRealmProxyInterface)object).realmGet$updated_at();
                if (realmGet$updated_at != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.updated_atIndex, rowIndex, realmGet$updated_at, false);
                }

                vn.tonish.hozo.model.Comment reviewObj = ((UserRealmProxyInterface) object).realmGet$review();
                if (reviewObj != null) {
                    Long cachereview = cache.get(reviewObj);
                    if (cachereview == null) {
                        cachereview = CommentRealmProxy.insert(realm, reviewObj, cache);
                    }
                    table.setLink(columnInfo.reviewIndex, rowIndex, cachereview, false);
                }
                String realmGet$priceBit = ((UserRealmProxyInterface)object).realmGet$priceBit();
                if (realmGet$priceBit != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.priceBitIndex, rowIndex, realmGet$priceBit, false);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, vn.tonish.hozo.model.User object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(vn.tonish.hozo.model.User.class);
        long tableNativePtr = table.getNativeTablePointer();
        UserColumnInfo columnInfo = (UserColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.User.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        String realmGet$id = ((UserRealmProxyInterface)object).realmGet$id();
        if (realmGet$id != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.idIndex, rowIndex, false);
        }
        String realmGet$mobile = ((UserRealmProxyInterface)object).realmGet$mobile();
        if (realmGet$mobile != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.mobileIndex, rowIndex, realmGet$mobile, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.mobileIndex, rowIndex, false);
        }
        String realmGet$token_key = ((UserRealmProxyInterface)object).realmGet$token_key();
        if (realmGet$token_key != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.token_keyIndex, rowIndex, realmGet$token_key, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.token_keyIndex, rowIndex, false);
        }
        String realmGet$full_name = ((UserRealmProxyInterface)object).realmGet$full_name();
        if (realmGet$full_name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.full_nameIndex, rowIndex, realmGet$full_name, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.full_nameIndex, rowIndex, false);
        }
        String realmGet$created_at = ((UserRealmProxyInterface)object).realmGet$created_at();
        if (realmGet$created_at != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.created_atIndex, rowIndex, realmGet$created_at, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.created_atIndex, rowIndex, false);
        }
        String realmGet$avatar = ((UserRealmProxyInterface)object).realmGet$avatar();
        if (realmGet$avatar != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.avatarIndex, rowIndex, realmGet$avatar, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.avatarIndex, rowIndex, false);
        }
        String realmGet$address = ((UserRealmProxyInterface)object).realmGet$address();
        if (realmGet$address != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.addressIndex, rowIndex, realmGet$address, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.addressIndex, rowIndex, false);
        }
        String realmGet$facebook_id = ((UserRealmProxyInterface)object).realmGet$facebook_id();
        if (realmGet$facebook_id != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.facebook_idIndex, rowIndex, realmGet$facebook_id, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.facebook_idIndex, rowIndex, false);
        }
        String realmGet$date_of_birth = ((UserRealmProxyInterface)object).realmGet$date_of_birth();
        if (realmGet$date_of_birth != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.date_of_birthIndex, rowIndex, realmGet$date_of_birth, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.date_of_birthIndex, rowIndex, false);
        }
        String realmGet$gender = ((UserRealmProxyInterface)object).realmGet$gender();
        if (realmGet$gender != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.genderIndex, rowIndex, realmGet$gender, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.genderIndex, rowIndex, false);
        }
        String realmGet$verify = ((UserRealmProxyInterface)object).realmGet$verify();
        if (realmGet$verify != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.verifyIndex, rowIndex, realmGet$verify, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.verifyIndex, rowIndex, false);
        }
        String realmGet$average_rating = ((UserRealmProxyInterface)object).realmGet$average_rating();
        if (realmGet$average_rating != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.average_ratingIndex, rowIndex, realmGet$average_rating, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.average_ratingIndex, rowIndex, false);
        }
        String realmGet$average_stars = ((UserRealmProxyInterface)object).realmGet$average_stars();
        if (realmGet$average_stars != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.average_starsIndex, rowIndex, realmGet$average_stars, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.average_starsIndex, rowIndex, false);
        }
        String realmGet$description = ((UserRealmProxyInterface)object).realmGet$description();
        if (realmGet$description != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.descriptionIndex, rowIndex, false);
        }
        String realmGet$education = ((UserRealmProxyInterface)object).realmGet$education();
        if (realmGet$education != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.educationIndex, rowIndex, realmGet$education, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.educationIndex, rowIndex, false);
        }
        String realmGet$email = ((UserRealmProxyInterface)object).realmGet$email();
        if (realmGet$email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.emailIndex, rowIndex, false);
        }
        String realmGet$languages = ((UserRealmProxyInterface)object).realmGet$languages();
        if (realmGet$languages != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.languagesIndex, rowIndex, realmGet$languages, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.languagesIndex, rowIndex, false);
        }
        String realmGet$ranking = ((UserRealmProxyInterface)object).realmGet$ranking();
        if (realmGet$ranking != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.rankingIndex, rowIndex, realmGet$ranking, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.rankingIndex, rowIndex, false);
        }
        String realmGet$rating_percentage = ((UserRealmProxyInterface)object).realmGet$rating_percentage();
        if (realmGet$rating_percentage != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.rating_percentageIndex, rowIndex, realmGet$rating_percentage, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.rating_percentageIndex, rowIndex, false);
        }
        String realmGet$review_counts = ((UserRealmProxyInterface)object).realmGet$review_counts();
        if (realmGet$review_counts != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.review_countsIndex, rowIndex, realmGet$review_counts, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.review_countsIndex, rowIndex, false);
        }
        String realmGet$updated_at = ((UserRealmProxyInterface)object).realmGet$updated_at();
        if (realmGet$updated_at != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.updated_atIndex, rowIndex, realmGet$updated_at, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.updated_atIndex, rowIndex, false);
        }

        vn.tonish.hozo.model.Comment reviewObj = ((UserRealmProxyInterface) object).realmGet$review();
        if (reviewObj != null) {
            Long cachereview = cache.get(reviewObj);
            if (cachereview == null) {
                cachereview = CommentRealmProxy.insertOrUpdate(realm, reviewObj, cache);
            }
            Table.nativeSetLink(tableNativePtr, columnInfo.reviewIndex, rowIndex, cachereview, false);
        } else {
            Table.nativeNullifyLink(tableNativePtr, columnInfo.reviewIndex, rowIndex);
        }
        String realmGet$priceBit = ((UserRealmProxyInterface)object).realmGet$priceBit();
        if (realmGet$priceBit != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.priceBitIndex, rowIndex, realmGet$priceBit, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.priceBitIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(vn.tonish.hozo.model.User.class);
        long tableNativePtr = table.getNativeTablePointer();
        UserColumnInfo columnInfo = (UserColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.User.class);
        vn.tonish.hozo.model.User object = null;
        while (objects.hasNext()) {
            object = (vn.tonish.hozo.model.User) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                String realmGet$id = ((UserRealmProxyInterface)object).realmGet$id();
                if (realmGet$id != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.idIndex, rowIndex, false);
                }
                String realmGet$mobile = ((UserRealmProxyInterface)object).realmGet$mobile();
                if (realmGet$mobile != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.mobileIndex, rowIndex, realmGet$mobile, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.mobileIndex, rowIndex, false);
                }
                String realmGet$token_key = ((UserRealmProxyInterface)object).realmGet$token_key();
                if (realmGet$token_key != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.token_keyIndex, rowIndex, realmGet$token_key, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.token_keyIndex, rowIndex, false);
                }
                String realmGet$full_name = ((UserRealmProxyInterface)object).realmGet$full_name();
                if (realmGet$full_name != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.full_nameIndex, rowIndex, realmGet$full_name, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.full_nameIndex, rowIndex, false);
                }
                String realmGet$created_at = ((UserRealmProxyInterface)object).realmGet$created_at();
                if (realmGet$created_at != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.created_atIndex, rowIndex, realmGet$created_at, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.created_atIndex, rowIndex, false);
                }
                String realmGet$avatar = ((UserRealmProxyInterface)object).realmGet$avatar();
                if (realmGet$avatar != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.avatarIndex, rowIndex, realmGet$avatar, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.avatarIndex, rowIndex, false);
                }
                String realmGet$address = ((UserRealmProxyInterface)object).realmGet$address();
                if (realmGet$address != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.addressIndex, rowIndex, realmGet$address, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.addressIndex, rowIndex, false);
                }
                String realmGet$facebook_id = ((UserRealmProxyInterface)object).realmGet$facebook_id();
                if (realmGet$facebook_id != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.facebook_idIndex, rowIndex, realmGet$facebook_id, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.facebook_idIndex, rowIndex, false);
                }
                String realmGet$date_of_birth = ((UserRealmProxyInterface)object).realmGet$date_of_birth();
                if (realmGet$date_of_birth != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.date_of_birthIndex, rowIndex, realmGet$date_of_birth, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.date_of_birthIndex, rowIndex, false);
                }
                String realmGet$gender = ((UserRealmProxyInterface)object).realmGet$gender();
                if (realmGet$gender != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.genderIndex, rowIndex, realmGet$gender, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.genderIndex, rowIndex, false);
                }
                String realmGet$verify = ((UserRealmProxyInterface)object).realmGet$verify();
                if (realmGet$verify != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.verifyIndex, rowIndex, realmGet$verify, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.verifyIndex, rowIndex, false);
                }
                String realmGet$average_rating = ((UserRealmProxyInterface)object).realmGet$average_rating();
                if (realmGet$average_rating != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.average_ratingIndex, rowIndex, realmGet$average_rating, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.average_ratingIndex, rowIndex, false);
                }
                String realmGet$average_stars = ((UserRealmProxyInterface)object).realmGet$average_stars();
                if (realmGet$average_stars != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.average_starsIndex, rowIndex, realmGet$average_stars, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.average_starsIndex, rowIndex, false);
                }
                String realmGet$description = ((UserRealmProxyInterface)object).realmGet$description();
                if (realmGet$description != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.descriptionIndex, rowIndex, false);
                }
                String realmGet$education = ((UserRealmProxyInterface)object).realmGet$education();
                if (realmGet$education != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.educationIndex, rowIndex, realmGet$education, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.educationIndex, rowIndex, false);
                }
                String realmGet$email = ((UserRealmProxyInterface)object).realmGet$email();
                if (realmGet$email != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.emailIndex, rowIndex, false);
                }
                String realmGet$languages = ((UserRealmProxyInterface)object).realmGet$languages();
                if (realmGet$languages != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.languagesIndex, rowIndex, realmGet$languages, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.languagesIndex, rowIndex, false);
                }
                String realmGet$ranking = ((UserRealmProxyInterface)object).realmGet$ranking();
                if (realmGet$ranking != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.rankingIndex, rowIndex, realmGet$ranking, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.rankingIndex, rowIndex, false);
                }
                String realmGet$rating_percentage = ((UserRealmProxyInterface)object).realmGet$rating_percentage();
                if (realmGet$rating_percentage != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.rating_percentageIndex, rowIndex, realmGet$rating_percentage, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.rating_percentageIndex, rowIndex, false);
                }
                String realmGet$review_counts = ((UserRealmProxyInterface)object).realmGet$review_counts();
                if (realmGet$review_counts != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.review_countsIndex, rowIndex, realmGet$review_counts, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.review_countsIndex, rowIndex, false);
                }
                String realmGet$updated_at = ((UserRealmProxyInterface)object).realmGet$updated_at();
                if (realmGet$updated_at != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.updated_atIndex, rowIndex, realmGet$updated_at, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.updated_atIndex, rowIndex, false);
                }

                vn.tonish.hozo.model.Comment reviewObj = ((UserRealmProxyInterface) object).realmGet$review();
                if (reviewObj != null) {
                    Long cachereview = cache.get(reviewObj);
                    if (cachereview == null) {
                        cachereview = CommentRealmProxy.insertOrUpdate(realm, reviewObj, cache);
                    }
                    Table.nativeSetLink(tableNativePtr, columnInfo.reviewIndex, rowIndex, cachereview, false);
                } else {
                    Table.nativeNullifyLink(tableNativePtr, columnInfo.reviewIndex, rowIndex);
                }
                String realmGet$priceBit = ((UserRealmProxyInterface)object).realmGet$priceBit();
                if (realmGet$priceBit != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.priceBitIndex, rowIndex, realmGet$priceBit, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.priceBitIndex, rowIndex, false);
                }
            }
        }
    }

    public static vn.tonish.hozo.model.User createDetachedCopy(vn.tonish.hozo.model.User realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        vn.tonish.hozo.model.User unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (vn.tonish.hozo.model.User)cachedObject.object;
            } else {
                unmanagedObject = (vn.tonish.hozo.model.User)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new vn.tonish.hozo.model.User();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        }
        ((UserRealmProxyInterface) unmanagedObject).realmSet$id(((UserRealmProxyInterface) realmObject).realmGet$id());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$mobile(((UserRealmProxyInterface) realmObject).realmGet$mobile());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$token_key(((UserRealmProxyInterface) realmObject).realmGet$token_key());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$full_name(((UserRealmProxyInterface) realmObject).realmGet$full_name());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$created_at(((UserRealmProxyInterface) realmObject).realmGet$created_at());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$avatar(((UserRealmProxyInterface) realmObject).realmGet$avatar());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$address(((UserRealmProxyInterface) realmObject).realmGet$address());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$facebook_id(((UserRealmProxyInterface) realmObject).realmGet$facebook_id());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$date_of_birth(((UserRealmProxyInterface) realmObject).realmGet$date_of_birth());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$gender(((UserRealmProxyInterface) realmObject).realmGet$gender());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$verify(((UserRealmProxyInterface) realmObject).realmGet$verify());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$average_rating(((UserRealmProxyInterface) realmObject).realmGet$average_rating());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$average_stars(((UserRealmProxyInterface) realmObject).realmGet$average_stars());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$description(((UserRealmProxyInterface) realmObject).realmGet$description());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$education(((UserRealmProxyInterface) realmObject).realmGet$education());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$email(((UserRealmProxyInterface) realmObject).realmGet$email());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$languages(((UserRealmProxyInterface) realmObject).realmGet$languages());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$ranking(((UserRealmProxyInterface) realmObject).realmGet$ranking());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$rating_percentage(((UserRealmProxyInterface) realmObject).realmGet$rating_percentage());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$review_counts(((UserRealmProxyInterface) realmObject).realmGet$review_counts());
        ((UserRealmProxyInterface) unmanagedObject).realmSet$updated_at(((UserRealmProxyInterface) realmObject).realmGet$updated_at());

        // Deep copy of review
        ((UserRealmProxyInterface) unmanagedObject).realmSet$review(CommentRealmProxy.createDetachedCopy(((UserRealmProxyInterface) realmObject).realmGet$review(), currentDepth + 1, maxDepth, cache));
        ((UserRealmProxyInterface) unmanagedObject).realmSet$priceBit(((UserRealmProxyInterface) realmObject).realmGet$priceBit());
        return unmanagedObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("User = [");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id() != null ? realmGet$id() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{mobile:");
        stringBuilder.append(realmGet$mobile() != null ? realmGet$mobile() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{token_key:");
        stringBuilder.append(realmGet$token_key() != null ? realmGet$token_key() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{full_name:");
        stringBuilder.append(realmGet$full_name() != null ? realmGet$full_name() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{created_at:");
        stringBuilder.append(realmGet$created_at() != null ? realmGet$created_at() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{avatar:");
        stringBuilder.append(realmGet$avatar() != null ? realmGet$avatar() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{address:");
        stringBuilder.append(realmGet$address() != null ? realmGet$address() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{facebook_id:");
        stringBuilder.append(realmGet$facebook_id() != null ? realmGet$facebook_id() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{date_of_birth:");
        stringBuilder.append(realmGet$date_of_birth() != null ? realmGet$date_of_birth() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{gender:");
        stringBuilder.append(realmGet$gender() != null ? realmGet$gender() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{verify:");
        stringBuilder.append(realmGet$verify() != null ? realmGet$verify() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{average_rating:");
        stringBuilder.append(realmGet$average_rating() != null ? realmGet$average_rating() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{average_stars:");
        stringBuilder.append(realmGet$average_stars() != null ? realmGet$average_stars() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{description:");
        stringBuilder.append(realmGet$description() != null ? realmGet$description() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{education:");
        stringBuilder.append(realmGet$education() != null ? realmGet$education() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{email:");
        stringBuilder.append(realmGet$email() != null ? realmGet$email() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{languages:");
        stringBuilder.append(realmGet$languages() != null ? realmGet$languages() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{ranking:");
        stringBuilder.append(realmGet$ranking() != null ? realmGet$ranking() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{rating_percentage:");
        stringBuilder.append(realmGet$rating_percentage() != null ? realmGet$rating_percentage() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{review_counts:");
        stringBuilder.append(realmGet$review_counts() != null ? realmGet$review_counts() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{updated_at:");
        stringBuilder.append(realmGet$updated_at() != null ? realmGet$updated_at() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{review:");
        stringBuilder.append(realmGet$review() != null ? "Comment" : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{priceBit:");
        stringBuilder.append(realmGet$priceBit() != null ? realmGet$priceBit() : "null");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState<?> realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long rowIndex = proxyState.getRow$realm().getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRealmProxy aUser = (UserRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aUser.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aUser.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aUser.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
