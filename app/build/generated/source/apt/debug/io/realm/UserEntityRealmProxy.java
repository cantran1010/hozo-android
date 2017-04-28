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

public class UserEntityRealmProxy extends vn.tonish.hozo.database.entity.UserEntity
    implements RealmObjectProxy, UserEntityRealmProxyInterface {

    static final class UserEntityColumnInfo extends ColumnInfo
        implements Cloneable {

        public long idIndex;
        public long tokenIndex;
        public long refreshTokenIndex;
        public long tokenExpIndex;
        public long phoneNumberIndex;
        public long emailIndex;
        public long fullNameIndex;
        public long passwordIndex;
        public long tokenDeviceIndex;
        public long profileImageIndex;
        public long birthdayIndex;
        public long genderIndex;
        public long jobIndex;
        public long descriptionIndex;
        public long loginAtIndex;

        UserEntityColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(15);
            this.idIndex = getValidColumnIndex(path, table, "UserEntity", "id");
            indicesMap.put("id", this.idIndex);
            this.tokenIndex = getValidColumnIndex(path, table, "UserEntity", "token");
            indicesMap.put("token", this.tokenIndex);
            this.refreshTokenIndex = getValidColumnIndex(path, table, "UserEntity", "refreshToken");
            indicesMap.put("refreshToken", this.refreshTokenIndex);
            this.tokenExpIndex = getValidColumnIndex(path, table, "UserEntity", "tokenExp");
            indicesMap.put("tokenExp", this.tokenExpIndex);
            this.phoneNumberIndex = getValidColumnIndex(path, table, "UserEntity", "phoneNumber");
            indicesMap.put("phoneNumber", this.phoneNumberIndex);
            this.emailIndex = getValidColumnIndex(path, table, "UserEntity", "email");
            indicesMap.put("email", this.emailIndex);
            this.fullNameIndex = getValidColumnIndex(path, table, "UserEntity", "fullName");
            indicesMap.put("fullName", this.fullNameIndex);
            this.passwordIndex = getValidColumnIndex(path, table, "UserEntity", "password");
            indicesMap.put("password", this.passwordIndex);
            this.tokenDeviceIndex = getValidColumnIndex(path, table, "UserEntity", "tokenDevice");
            indicesMap.put("tokenDevice", this.tokenDeviceIndex);
            this.profileImageIndex = getValidColumnIndex(path, table, "UserEntity", "profileImage");
            indicesMap.put("profileImage", this.profileImageIndex);
            this.birthdayIndex = getValidColumnIndex(path, table, "UserEntity", "birthday");
            indicesMap.put("birthday", this.birthdayIndex);
            this.genderIndex = getValidColumnIndex(path, table, "UserEntity", "gender");
            indicesMap.put("gender", this.genderIndex);
            this.jobIndex = getValidColumnIndex(path, table, "UserEntity", "job");
            indicesMap.put("job", this.jobIndex);
            this.descriptionIndex = getValidColumnIndex(path, table, "UserEntity", "description");
            indicesMap.put("description", this.descriptionIndex);
            this.loginAtIndex = getValidColumnIndex(path, table, "UserEntity", "loginAt");
            indicesMap.put("loginAt", this.loginAtIndex);

            setIndicesMap(indicesMap);
        }

        @Override
        public final void copyColumnInfoFrom(ColumnInfo other) {
            final UserEntityColumnInfo otherInfo = (UserEntityColumnInfo) other;
            this.idIndex = otherInfo.idIndex;
            this.tokenIndex = otherInfo.tokenIndex;
            this.refreshTokenIndex = otherInfo.refreshTokenIndex;
            this.tokenExpIndex = otherInfo.tokenExpIndex;
            this.phoneNumberIndex = otherInfo.phoneNumberIndex;
            this.emailIndex = otherInfo.emailIndex;
            this.fullNameIndex = otherInfo.fullNameIndex;
            this.passwordIndex = otherInfo.passwordIndex;
            this.tokenDeviceIndex = otherInfo.tokenDeviceIndex;
            this.profileImageIndex = otherInfo.profileImageIndex;
            this.birthdayIndex = otherInfo.birthdayIndex;
            this.genderIndex = otherInfo.genderIndex;
            this.jobIndex = otherInfo.jobIndex;
            this.descriptionIndex = otherInfo.descriptionIndex;
            this.loginAtIndex = otherInfo.loginAtIndex;

            setIndicesMap(otherInfo.getIndicesMap());
        }

        @Override
        public final UserEntityColumnInfo clone() {
            return (UserEntityColumnInfo) super.clone();
        }

    }
    private UserEntityColumnInfo columnInfo;
    private ProxyState<vn.tonish.hozo.database.entity.UserEntity> proxyState;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("token");
        fieldNames.add("refreshToken");
        fieldNames.add("tokenExp");
        fieldNames.add("phoneNumber");
        fieldNames.add("email");
        fieldNames.add("fullName");
        fieldNames.add("password");
        fieldNames.add("tokenDevice");
        fieldNames.add("profileImage");
        fieldNames.add("birthday");
        fieldNames.add("gender");
        fieldNames.add("job");
        fieldNames.add("description");
        fieldNames.add("loginAt");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    UserEntityRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (UserEntityColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<vn.tonish.hozo.database.entity.UserEntity>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$id() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.idIndex);
    }

    @Override
    public void realmSet$id(int value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'id' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$token() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.tokenIndex);
    }

    @Override
    public void realmSet$token(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.tokenIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.tokenIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.tokenIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.tokenIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$refreshToken() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.refreshTokenIndex);
    }

    @Override
    public void realmSet$refreshToken(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.refreshTokenIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.refreshTokenIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.refreshTokenIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.refreshTokenIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$tokenExp() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.tokenExpIndex);
    }

    @Override
    public void realmSet$tokenExp(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.tokenExpIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.tokenExpIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.tokenExpIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.tokenExpIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$phoneNumber() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.phoneNumberIndex);
    }

    @Override
    public void realmSet$phoneNumber(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.phoneNumberIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.phoneNumberIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.phoneNumberIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.phoneNumberIndex, value);
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
    public String realmGet$fullName() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fullNameIndex);
    }

    @Override
    public void realmSet$fullName(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fullNameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.fullNameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fullNameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fullNameIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$password() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.passwordIndex);
    }

    @Override
    public void realmSet$password(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.passwordIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.passwordIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.passwordIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.passwordIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$tokenDevice() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.tokenDeviceIndex);
    }

    @Override
    public void realmSet$tokenDevice(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.tokenDeviceIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.tokenDeviceIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.tokenDeviceIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.tokenDeviceIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$profileImage() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.profileImageIndex);
    }

    @Override
    public void realmSet$profileImage(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.profileImageIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.profileImageIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.profileImageIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.profileImageIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$birthday() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.birthdayIndex);
    }

    @Override
    public void realmSet$birthday(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.birthdayIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.birthdayIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.birthdayIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.birthdayIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$gender() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.genderIndex);
    }

    @Override
    public void realmSet$gender(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.genderIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.genderIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$job() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.jobIndex);
    }

    @Override
    public void realmSet$job(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.jobIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.jobIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.jobIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.jobIndex, value);
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
    public String realmGet$loginAt() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.loginAtIndex);
    }

    @Override
    public void realmSet$loginAt(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.loginAtIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.loginAtIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.loginAtIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.loginAtIndex, value);
    }

    public static RealmObjectSchema createRealmObjectSchema(RealmSchema realmSchema) {
        if (!realmSchema.contains("UserEntity")) {
            RealmObjectSchema realmObjectSchema = realmSchema.create("UserEntity");
            realmObjectSchema.add("id", RealmFieldType.INTEGER, Property.PRIMARY_KEY, Property.INDEXED, Property.REQUIRED);
            realmObjectSchema.add("token", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("refreshToken", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("tokenExp", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("phoneNumber", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("email", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("fullName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("password", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("tokenDevice", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("profileImage", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("birthday", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("gender", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
            realmObjectSchema.add("job", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("description", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("loginAt", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            return realmObjectSchema;
        }
        return realmSchema.get("UserEntity");
    }

    public static UserEntityColumnInfo validateTable(SharedRealm sharedRealm, boolean allowExtraColumns) {
        if (!sharedRealm.hasTable("class_UserEntity")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "The 'UserEntity' class is missing from the schema for this Realm.");
        }
        Table table = sharedRealm.getTable("class_UserEntity");
        final long columnCount = table.getColumnCount();
        if (columnCount != 15) {
            if (columnCount < 15) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is less than expected - expected 15 but was " + columnCount);
            }
            if (allowExtraColumns) {
                RealmLog.debug("Field count is more than expected - expected 15 but was %1$d", columnCount);
            } else {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is more than expected - expected 15 but was " + columnCount);
            }
        }
        Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
        for (long i = 0; i < columnCount; i++) {
            columnTypes.put(table.getColumnName(i), table.getColumnType(i));
        }

        final UserEntityColumnInfo columnInfo = new UserEntityColumnInfo(sharedRealm.getPath(), table);

        if (!table.hasPrimaryKey()) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Primary key not defined for field 'id' in existing Realm file. @PrimaryKey was added.");
        } else {
            if (table.getPrimaryKey() != columnInfo.idIndex) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Primary Key annotation definition was changed, from field " + table.getColumnName(table.getPrimaryKey()) + " to field id");
            }
        }

        if (!columnTypes.containsKey("id")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'id' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("id") != RealmFieldType.INTEGER) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'int' for field 'id' in existing Realm file.");
        }
        if (table.isColumnNullable(columnInfo.idIndex) && table.findFirstNull(columnInfo.idIndex) != Table.NO_MATCH) {
            throw new IllegalStateException("Cannot migrate an object with null value in field 'id'. Either maintain the same type for primary key field 'id', or remove the object with null value before migration.");
        }
        if (!table.hasSearchIndex(table.getColumnIndex("id"))) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Index not defined for field 'id' in existing Realm file. Either set @Index or migrate using io.realm.internal.Table.removeSearchIndex().");
        }
        if (!columnTypes.containsKey("token")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'token' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("token") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'token' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.tokenIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'token' is required. Either set @Required to field 'token' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("refreshToken")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'refreshToken' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("refreshToken") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'refreshToken' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.refreshTokenIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'refreshToken' is required. Either set @Required to field 'refreshToken' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("tokenExp")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'tokenExp' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("tokenExp") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'tokenExp' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.tokenExpIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'tokenExp' is required. Either set @Required to field 'tokenExp' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("phoneNumber")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'phoneNumber' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("phoneNumber") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'phoneNumber' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.phoneNumberIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'phoneNumber' is required. Either set @Required to field 'phoneNumber' or migrate using RealmObjectSchema.setNullable().");
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
        if (!columnTypes.containsKey("fullName")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'fullName' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("fullName") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'fullName' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.fullNameIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'fullName' is required. Either set @Required to field 'fullName' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("password")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'password' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("password") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'password' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.passwordIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'password' is required. Either set @Required to field 'password' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("tokenDevice")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'tokenDevice' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("tokenDevice") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'tokenDevice' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.tokenDeviceIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'tokenDevice' is required. Either set @Required to field 'tokenDevice' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("profileImage")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'profileImage' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("profileImage") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'profileImage' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.profileImageIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'profileImage' is required. Either set @Required to field 'profileImage' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("birthday")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'birthday' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("birthday") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'birthday' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.birthdayIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'birthday' is required. Either set @Required to field 'birthday' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("gender")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'gender' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("gender") != RealmFieldType.INTEGER) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'int' for field 'gender' in existing Realm file.");
        }
        if (table.isColumnNullable(columnInfo.genderIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'gender' does support null values in the existing Realm file. Use corresponding boxed type for field 'gender' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("job")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'job' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("job") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'job' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.jobIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'job' is required. Either set @Required to field 'job' or migrate using RealmObjectSchema.setNullable().");
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
        if (!columnTypes.containsKey("loginAt")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'loginAt' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("loginAt") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'loginAt' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.loginAtIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'loginAt' is required. Either set @Required to field 'loginAt' or migrate using RealmObjectSchema.setNullable().");
        }

        return columnInfo;
    }

    public static String getTableName() {
        return "class_UserEntity";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static vn.tonish.hozo.database.entity.UserEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        vn.tonish.hozo.database.entity.UserEntity obj = null;
        if (update) {
            Table table = realm.getTable(vn.tonish.hozo.database.entity.UserEntity.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = Table.NO_MATCH;
            if (!json.isNull("id")) {
                rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("id"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.schema.getColumnInfo(vn.tonish.hozo.database.entity.UserEntity.class), false, Collections.<String> emptyList());
                    obj = new io.realm.UserEntityRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (io.realm.UserEntityRealmProxy) realm.createObjectInternal(vn.tonish.hozo.database.entity.UserEntity.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.UserEntityRealmProxy) realm.createObjectInternal(vn.tonish.hozo.database.entity.UserEntity.class, json.getInt("id"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'id'.");
            }
        }
        if (json.has("token")) {
            if (json.isNull("token")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$token(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$token((String) json.getString("token"));
            }
        }
        if (json.has("refreshToken")) {
            if (json.isNull("refreshToken")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$refreshToken(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$refreshToken((String) json.getString("refreshToken"));
            }
        }
        if (json.has("tokenExp")) {
            if (json.isNull("tokenExp")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$tokenExp(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$tokenExp((String) json.getString("tokenExp"));
            }
        }
        if (json.has("phoneNumber")) {
            if (json.isNull("phoneNumber")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$phoneNumber(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$phoneNumber((String) json.getString("phoneNumber"));
            }
        }
        if (json.has("email")) {
            if (json.isNull("email")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$email(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$email((String) json.getString("email"));
            }
        }
        if (json.has("fullName")) {
            if (json.isNull("fullName")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$fullName(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$fullName((String) json.getString("fullName"));
            }
        }
        if (json.has("password")) {
            if (json.isNull("password")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$password(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$password((String) json.getString("password"));
            }
        }
        if (json.has("tokenDevice")) {
            if (json.isNull("tokenDevice")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$tokenDevice(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$tokenDevice((String) json.getString("tokenDevice"));
            }
        }
        if (json.has("profileImage")) {
            if (json.isNull("profileImage")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$profileImage(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$profileImage((String) json.getString("profileImage"));
            }
        }
        if (json.has("birthday")) {
            if (json.isNull("birthday")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$birthday(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$birthday((String) json.getString("birthday"));
            }
        }
        if (json.has("gender")) {
            if (json.isNull("gender")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'gender' to null.");
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$gender((int) json.getInt("gender"));
            }
        }
        if (json.has("job")) {
            if (json.isNull("job")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$job(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$job((String) json.getString("job"));
            }
        }
        if (json.has("description")) {
            if (json.isNull("description")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$description(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$description((String) json.getString("description"));
            }
        }
        if (json.has("loginAt")) {
            if (json.isNull("loginAt")) {
                ((UserEntityRealmProxyInterface) obj).realmSet$loginAt(null);
            } else {
                ((UserEntityRealmProxyInterface) obj).realmSet$loginAt((String) json.getString("loginAt"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static vn.tonish.hozo.database.entity.UserEntity createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        vn.tonish.hozo.database.entity.UserEntity obj = new vn.tonish.hozo.database.entity.UserEntity();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'id' to null.");
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$id((int) reader.nextInt());
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("token")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$token(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$token((String) reader.nextString());
                }
            } else if (name.equals("refreshToken")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$refreshToken(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$refreshToken((String) reader.nextString());
                }
            } else if (name.equals("tokenExp")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$tokenExp(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$tokenExp((String) reader.nextString());
                }
            } else if (name.equals("phoneNumber")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$phoneNumber(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$phoneNumber((String) reader.nextString());
                }
            } else if (name.equals("email")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$email(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$email((String) reader.nextString());
                }
            } else if (name.equals("fullName")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$fullName(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$fullName((String) reader.nextString());
                }
            } else if (name.equals("password")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$password(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$password((String) reader.nextString());
                }
            } else if (name.equals("tokenDevice")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$tokenDevice(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$tokenDevice((String) reader.nextString());
                }
            } else if (name.equals("profileImage")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$profileImage(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$profileImage((String) reader.nextString());
                }
            } else if (name.equals("birthday")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$birthday(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$birthday((String) reader.nextString());
                }
            } else if (name.equals("gender")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'gender' to null.");
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$gender((int) reader.nextInt());
                }
            } else if (name.equals("job")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$job(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$job((String) reader.nextString());
                }
            } else if (name.equals("description")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$description(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$description((String) reader.nextString());
                }
            } else if (name.equals("loginAt")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((UserEntityRealmProxyInterface) obj).realmSet$loginAt(null);
                } else {
                    ((UserEntityRealmProxyInterface) obj).realmSet$loginAt((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'id'.");
        }
        obj = realm.copyToRealm(obj);
        return obj;
    }

    public static vn.tonish.hozo.database.entity.UserEntity copyOrUpdate(Realm realm, vn.tonish.hozo.database.entity.UserEntity object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (vn.tonish.hozo.database.entity.UserEntity) cachedRealmObject;
        } else {
            vn.tonish.hozo.database.entity.UserEntity realmObject = null;
            boolean canUpdate = update;
            if (canUpdate) {
                Table table = realm.getTable(vn.tonish.hozo.database.entity.UserEntity.class);
                long pkColumnIndex = table.getPrimaryKey();
                long rowIndex = table.findFirstLong(pkColumnIndex, ((UserEntityRealmProxyInterface) object).realmGet$id());
                if (rowIndex != Table.NO_MATCH) {
                    try {
                        objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.schema.getColumnInfo(vn.tonish.hozo.database.entity.UserEntity.class), false, Collections.<String> emptyList());
                        realmObject = new io.realm.UserEntityRealmProxy();
                        cache.put(object, (RealmObjectProxy) realmObject);
                    } finally {
                        objectContext.clear();
                    }
                } else {
                    canUpdate = false;
                }
            }

            if (canUpdate) {
                return update(realm, realmObject, object, cache);
            } else {
                return copy(realm, object, update, cache);
            }
        }
    }

    public static vn.tonish.hozo.database.entity.UserEntity copy(Realm realm, vn.tonish.hozo.database.entity.UserEntity newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (vn.tonish.hozo.database.entity.UserEntity) cachedRealmObject;
        } else {
            // rejecting default values to avoid creating unexpected objects from RealmModel/RealmList fields.
            vn.tonish.hozo.database.entity.UserEntity realmObject = realm.createObjectInternal(vn.tonish.hozo.database.entity.UserEntity.class, ((UserEntityRealmProxyInterface) newObject).realmGet$id(), false, Collections.<String>emptyList());
            cache.put(newObject, (RealmObjectProxy) realmObject);
            ((UserEntityRealmProxyInterface) realmObject).realmSet$token(((UserEntityRealmProxyInterface) newObject).realmGet$token());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$refreshToken(((UserEntityRealmProxyInterface) newObject).realmGet$refreshToken());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$tokenExp(((UserEntityRealmProxyInterface) newObject).realmGet$tokenExp());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$phoneNumber(((UserEntityRealmProxyInterface) newObject).realmGet$phoneNumber());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$email(((UserEntityRealmProxyInterface) newObject).realmGet$email());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$fullName(((UserEntityRealmProxyInterface) newObject).realmGet$fullName());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$password(((UserEntityRealmProxyInterface) newObject).realmGet$password());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$tokenDevice(((UserEntityRealmProxyInterface) newObject).realmGet$tokenDevice());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$profileImage(((UserEntityRealmProxyInterface) newObject).realmGet$profileImage());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$birthday(((UserEntityRealmProxyInterface) newObject).realmGet$birthday());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$gender(((UserEntityRealmProxyInterface) newObject).realmGet$gender());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$job(((UserEntityRealmProxyInterface) newObject).realmGet$job());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$description(((UserEntityRealmProxyInterface) newObject).realmGet$description());
            ((UserEntityRealmProxyInterface) realmObject).realmSet$loginAt(((UserEntityRealmProxyInterface) newObject).realmGet$loginAt());
            return realmObject;
        }
    }

    public static long insert(Realm realm, vn.tonish.hozo.database.entity.UserEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(vn.tonish.hozo.database.entity.UserEntity.class);
        long tableNativePtr = table.getNativeTablePointer();
        UserEntityColumnInfo columnInfo = (UserEntityColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.database.entity.UserEntity.class);
        long pkColumnIndex = table.getPrimaryKey();
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((UserEntityRealmProxyInterface) object).realmGet$id();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((UserEntityRealmProxyInterface) object).realmGet$id());
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = table.addEmptyRowWithPrimaryKey(((UserEntityRealmProxyInterface) object).realmGet$id(), false);
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$token = ((UserEntityRealmProxyInterface)object).realmGet$token();
        if (realmGet$token != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.tokenIndex, rowIndex, realmGet$token, false);
        }
        String realmGet$refreshToken = ((UserEntityRealmProxyInterface)object).realmGet$refreshToken();
        if (realmGet$refreshToken != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.refreshTokenIndex, rowIndex, realmGet$refreshToken, false);
        }
        String realmGet$tokenExp = ((UserEntityRealmProxyInterface)object).realmGet$tokenExp();
        if (realmGet$tokenExp != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.tokenExpIndex, rowIndex, realmGet$tokenExp, false);
        }
        String realmGet$phoneNumber = ((UserEntityRealmProxyInterface)object).realmGet$phoneNumber();
        if (realmGet$phoneNumber != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.phoneNumberIndex, rowIndex, realmGet$phoneNumber, false);
        }
        String realmGet$email = ((UserEntityRealmProxyInterface)object).realmGet$email();
        if (realmGet$email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
        }
        String realmGet$fullName = ((UserEntityRealmProxyInterface)object).realmGet$fullName();
        if (realmGet$fullName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
        }
        String realmGet$password = ((UserEntityRealmProxyInterface)object).realmGet$password();
        if (realmGet$password != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.passwordIndex, rowIndex, realmGet$password, false);
        }
        String realmGet$tokenDevice = ((UserEntityRealmProxyInterface)object).realmGet$tokenDevice();
        if (realmGet$tokenDevice != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.tokenDeviceIndex, rowIndex, realmGet$tokenDevice, false);
        }
        String realmGet$profileImage = ((UserEntityRealmProxyInterface)object).realmGet$profileImage();
        if (realmGet$profileImage != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profileImageIndex, rowIndex, realmGet$profileImage, false);
        }
        String realmGet$birthday = ((UserEntityRealmProxyInterface)object).realmGet$birthday();
        if (realmGet$birthday != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.birthdayIndex, rowIndex, realmGet$birthday, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.genderIndex, rowIndex, ((UserEntityRealmProxyInterface)object).realmGet$gender(), false);
        String realmGet$job = ((UserEntityRealmProxyInterface)object).realmGet$job();
        if (realmGet$job != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.jobIndex, rowIndex, realmGet$job, false);
        }
        String realmGet$description = ((UserEntityRealmProxyInterface)object).realmGet$description();
        if (realmGet$description != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description, false);
        }
        String realmGet$loginAt = ((UserEntityRealmProxyInterface)object).realmGet$loginAt();
        if (realmGet$loginAt != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.loginAtIndex, rowIndex, realmGet$loginAt, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(vn.tonish.hozo.database.entity.UserEntity.class);
        long tableNativePtr = table.getNativeTablePointer();
        UserEntityColumnInfo columnInfo = (UserEntityColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.database.entity.UserEntity.class);
        long pkColumnIndex = table.getPrimaryKey();
        vn.tonish.hozo.database.entity.UserEntity object = null;
        while (objects.hasNext()) {
            object = (vn.tonish.hozo.database.entity.UserEntity) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.NO_MATCH;
                Object primaryKeyValue = ((UserEntityRealmProxyInterface) object).realmGet$id();
                if (primaryKeyValue != null) {
                    rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((UserEntityRealmProxyInterface) object).realmGet$id());
                }
                if (rowIndex == Table.NO_MATCH) {
                    rowIndex = table.addEmptyRowWithPrimaryKey(((UserEntityRealmProxyInterface) object).realmGet$id(), false);
                } else {
                    Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
                }
                cache.put(object, rowIndex);
                String realmGet$token = ((UserEntityRealmProxyInterface)object).realmGet$token();
                if (realmGet$token != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.tokenIndex, rowIndex, realmGet$token, false);
                }
                String realmGet$refreshToken = ((UserEntityRealmProxyInterface)object).realmGet$refreshToken();
                if (realmGet$refreshToken != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.refreshTokenIndex, rowIndex, realmGet$refreshToken, false);
                }
                String realmGet$tokenExp = ((UserEntityRealmProxyInterface)object).realmGet$tokenExp();
                if (realmGet$tokenExp != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.tokenExpIndex, rowIndex, realmGet$tokenExp, false);
                }
                String realmGet$phoneNumber = ((UserEntityRealmProxyInterface)object).realmGet$phoneNumber();
                if (realmGet$phoneNumber != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.phoneNumberIndex, rowIndex, realmGet$phoneNumber, false);
                }
                String realmGet$email = ((UserEntityRealmProxyInterface)object).realmGet$email();
                if (realmGet$email != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
                }
                String realmGet$fullName = ((UserEntityRealmProxyInterface)object).realmGet$fullName();
                if (realmGet$fullName != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
                }
                String realmGet$password = ((UserEntityRealmProxyInterface)object).realmGet$password();
                if (realmGet$password != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.passwordIndex, rowIndex, realmGet$password, false);
                }
                String realmGet$tokenDevice = ((UserEntityRealmProxyInterface)object).realmGet$tokenDevice();
                if (realmGet$tokenDevice != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.tokenDeviceIndex, rowIndex, realmGet$tokenDevice, false);
                }
                String realmGet$profileImage = ((UserEntityRealmProxyInterface)object).realmGet$profileImage();
                if (realmGet$profileImage != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.profileImageIndex, rowIndex, realmGet$profileImage, false);
                }
                String realmGet$birthday = ((UserEntityRealmProxyInterface)object).realmGet$birthday();
                if (realmGet$birthday != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.birthdayIndex, rowIndex, realmGet$birthday, false);
                }
                Table.nativeSetLong(tableNativePtr, columnInfo.genderIndex, rowIndex, ((UserEntityRealmProxyInterface)object).realmGet$gender(), false);
                String realmGet$job = ((UserEntityRealmProxyInterface)object).realmGet$job();
                if (realmGet$job != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.jobIndex, rowIndex, realmGet$job, false);
                }
                String realmGet$description = ((UserEntityRealmProxyInterface)object).realmGet$description();
                if (realmGet$description != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description, false);
                }
                String realmGet$loginAt = ((UserEntityRealmProxyInterface)object).realmGet$loginAt();
                if (realmGet$loginAt != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.loginAtIndex, rowIndex, realmGet$loginAt, false);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, vn.tonish.hozo.database.entity.UserEntity object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(vn.tonish.hozo.database.entity.UserEntity.class);
        long tableNativePtr = table.getNativeTablePointer();
        UserEntityColumnInfo columnInfo = (UserEntityColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.database.entity.UserEntity.class);
        long pkColumnIndex = table.getPrimaryKey();
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((UserEntityRealmProxyInterface) object).realmGet$id();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((UserEntityRealmProxyInterface) object).realmGet$id());
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = table.addEmptyRowWithPrimaryKey(((UserEntityRealmProxyInterface) object).realmGet$id(), false);
        }
        cache.put(object, rowIndex);
        String realmGet$token = ((UserEntityRealmProxyInterface)object).realmGet$token();
        if (realmGet$token != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.tokenIndex, rowIndex, realmGet$token, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.tokenIndex, rowIndex, false);
        }
        String realmGet$refreshToken = ((UserEntityRealmProxyInterface)object).realmGet$refreshToken();
        if (realmGet$refreshToken != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.refreshTokenIndex, rowIndex, realmGet$refreshToken, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.refreshTokenIndex, rowIndex, false);
        }
        String realmGet$tokenExp = ((UserEntityRealmProxyInterface)object).realmGet$tokenExp();
        if (realmGet$tokenExp != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.tokenExpIndex, rowIndex, realmGet$tokenExp, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.tokenExpIndex, rowIndex, false);
        }
        String realmGet$phoneNumber = ((UserEntityRealmProxyInterface)object).realmGet$phoneNumber();
        if (realmGet$phoneNumber != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.phoneNumberIndex, rowIndex, realmGet$phoneNumber, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.phoneNumberIndex, rowIndex, false);
        }
        String realmGet$email = ((UserEntityRealmProxyInterface)object).realmGet$email();
        if (realmGet$email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.emailIndex, rowIndex, false);
        }
        String realmGet$fullName = ((UserEntityRealmProxyInterface)object).realmGet$fullName();
        if (realmGet$fullName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fullNameIndex, rowIndex, false);
        }
        String realmGet$password = ((UserEntityRealmProxyInterface)object).realmGet$password();
        if (realmGet$password != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.passwordIndex, rowIndex, realmGet$password, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.passwordIndex, rowIndex, false);
        }
        String realmGet$tokenDevice = ((UserEntityRealmProxyInterface)object).realmGet$tokenDevice();
        if (realmGet$tokenDevice != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.tokenDeviceIndex, rowIndex, realmGet$tokenDevice, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.tokenDeviceIndex, rowIndex, false);
        }
        String realmGet$profileImage = ((UserEntityRealmProxyInterface)object).realmGet$profileImage();
        if (realmGet$profileImage != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profileImageIndex, rowIndex, realmGet$profileImage, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.profileImageIndex, rowIndex, false);
        }
        String realmGet$birthday = ((UserEntityRealmProxyInterface)object).realmGet$birthday();
        if (realmGet$birthday != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.birthdayIndex, rowIndex, realmGet$birthday, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.birthdayIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.genderIndex, rowIndex, ((UserEntityRealmProxyInterface)object).realmGet$gender(), false);
        String realmGet$job = ((UserEntityRealmProxyInterface)object).realmGet$job();
        if (realmGet$job != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.jobIndex, rowIndex, realmGet$job, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.jobIndex, rowIndex, false);
        }
        String realmGet$description = ((UserEntityRealmProxyInterface)object).realmGet$description();
        if (realmGet$description != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.descriptionIndex, rowIndex, false);
        }
        String realmGet$loginAt = ((UserEntityRealmProxyInterface)object).realmGet$loginAt();
        if (realmGet$loginAt != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.loginAtIndex, rowIndex, realmGet$loginAt, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.loginAtIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(vn.tonish.hozo.database.entity.UserEntity.class);
        long tableNativePtr = table.getNativeTablePointer();
        UserEntityColumnInfo columnInfo = (UserEntityColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.database.entity.UserEntity.class);
        long pkColumnIndex = table.getPrimaryKey();
        vn.tonish.hozo.database.entity.UserEntity object = null;
        while (objects.hasNext()) {
            object = (vn.tonish.hozo.database.entity.UserEntity) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.NO_MATCH;
                Object primaryKeyValue = ((UserEntityRealmProxyInterface) object).realmGet$id();
                if (primaryKeyValue != null) {
                    rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((UserEntityRealmProxyInterface) object).realmGet$id());
                }
                if (rowIndex == Table.NO_MATCH) {
                    rowIndex = table.addEmptyRowWithPrimaryKey(((UserEntityRealmProxyInterface) object).realmGet$id(), false);
                }
                cache.put(object, rowIndex);
                String realmGet$token = ((UserEntityRealmProxyInterface)object).realmGet$token();
                if (realmGet$token != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.tokenIndex, rowIndex, realmGet$token, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.tokenIndex, rowIndex, false);
                }
                String realmGet$refreshToken = ((UserEntityRealmProxyInterface)object).realmGet$refreshToken();
                if (realmGet$refreshToken != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.refreshTokenIndex, rowIndex, realmGet$refreshToken, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.refreshTokenIndex, rowIndex, false);
                }
                String realmGet$tokenExp = ((UserEntityRealmProxyInterface)object).realmGet$tokenExp();
                if (realmGet$tokenExp != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.tokenExpIndex, rowIndex, realmGet$tokenExp, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.tokenExpIndex, rowIndex, false);
                }
                String realmGet$phoneNumber = ((UserEntityRealmProxyInterface)object).realmGet$phoneNumber();
                if (realmGet$phoneNumber != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.phoneNumberIndex, rowIndex, realmGet$phoneNumber, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.phoneNumberIndex, rowIndex, false);
                }
                String realmGet$email = ((UserEntityRealmProxyInterface)object).realmGet$email();
                if (realmGet$email != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.emailIndex, rowIndex, false);
                }
                String realmGet$fullName = ((UserEntityRealmProxyInterface)object).realmGet$fullName();
                if (realmGet$fullName != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.fullNameIndex, rowIndex, false);
                }
                String realmGet$password = ((UserEntityRealmProxyInterface)object).realmGet$password();
                if (realmGet$password != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.passwordIndex, rowIndex, realmGet$password, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.passwordIndex, rowIndex, false);
                }
                String realmGet$tokenDevice = ((UserEntityRealmProxyInterface)object).realmGet$tokenDevice();
                if (realmGet$tokenDevice != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.tokenDeviceIndex, rowIndex, realmGet$tokenDevice, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.tokenDeviceIndex, rowIndex, false);
                }
                String realmGet$profileImage = ((UserEntityRealmProxyInterface)object).realmGet$profileImage();
                if (realmGet$profileImage != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.profileImageIndex, rowIndex, realmGet$profileImage, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.profileImageIndex, rowIndex, false);
                }
                String realmGet$birthday = ((UserEntityRealmProxyInterface)object).realmGet$birthday();
                if (realmGet$birthday != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.birthdayIndex, rowIndex, realmGet$birthday, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.birthdayIndex, rowIndex, false);
                }
                Table.nativeSetLong(tableNativePtr, columnInfo.genderIndex, rowIndex, ((UserEntityRealmProxyInterface)object).realmGet$gender(), false);
                String realmGet$job = ((UserEntityRealmProxyInterface)object).realmGet$job();
                if (realmGet$job != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.jobIndex, rowIndex, realmGet$job, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.jobIndex, rowIndex, false);
                }
                String realmGet$description = ((UserEntityRealmProxyInterface)object).realmGet$description();
                if (realmGet$description != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.descriptionIndex, rowIndex, false);
                }
                String realmGet$loginAt = ((UserEntityRealmProxyInterface)object).realmGet$loginAt();
                if (realmGet$loginAt != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.loginAtIndex, rowIndex, realmGet$loginAt, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.loginAtIndex, rowIndex, false);
                }
            }
        }
    }

    public static vn.tonish.hozo.database.entity.UserEntity createDetachedCopy(vn.tonish.hozo.database.entity.UserEntity realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        vn.tonish.hozo.database.entity.UserEntity unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (vn.tonish.hozo.database.entity.UserEntity)cachedObject.object;
            } else {
                unmanagedObject = (vn.tonish.hozo.database.entity.UserEntity)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new vn.tonish.hozo.database.entity.UserEntity();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        }
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$id(((UserEntityRealmProxyInterface) realmObject).realmGet$id());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$token(((UserEntityRealmProxyInterface) realmObject).realmGet$token());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$refreshToken(((UserEntityRealmProxyInterface) realmObject).realmGet$refreshToken());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$tokenExp(((UserEntityRealmProxyInterface) realmObject).realmGet$tokenExp());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$phoneNumber(((UserEntityRealmProxyInterface) realmObject).realmGet$phoneNumber());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$email(((UserEntityRealmProxyInterface) realmObject).realmGet$email());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$fullName(((UserEntityRealmProxyInterface) realmObject).realmGet$fullName());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$password(((UserEntityRealmProxyInterface) realmObject).realmGet$password());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$tokenDevice(((UserEntityRealmProxyInterface) realmObject).realmGet$tokenDevice());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$profileImage(((UserEntityRealmProxyInterface) realmObject).realmGet$profileImage());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$birthday(((UserEntityRealmProxyInterface) realmObject).realmGet$birthday());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$gender(((UserEntityRealmProxyInterface) realmObject).realmGet$gender());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$job(((UserEntityRealmProxyInterface) realmObject).realmGet$job());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$description(((UserEntityRealmProxyInterface) realmObject).realmGet$description());
        ((UserEntityRealmProxyInterface) unmanagedObject).realmSet$loginAt(((UserEntityRealmProxyInterface) realmObject).realmGet$loginAt());
        return unmanagedObject;
    }

    static vn.tonish.hozo.database.entity.UserEntity update(Realm realm, vn.tonish.hozo.database.entity.UserEntity realmObject, vn.tonish.hozo.database.entity.UserEntity newObject, Map<RealmModel, RealmObjectProxy> cache) {
        ((UserEntityRealmProxyInterface) realmObject).realmSet$token(((UserEntityRealmProxyInterface) newObject).realmGet$token());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$refreshToken(((UserEntityRealmProxyInterface) newObject).realmGet$refreshToken());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$tokenExp(((UserEntityRealmProxyInterface) newObject).realmGet$tokenExp());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$phoneNumber(((UserEntityRealmProxyInterface) newObject).realmGet$phoneNumber());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$email(((UserEntityRealmProxyInterface) newObject).realmGet$email());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$fullName(((UserEntityRealmProxyInterface) newObject).realmGet$fullName());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$password(((UserEntityRealmProxyInterface) newObject).realmGet$password());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$tokenDevice(((UserEntityRealmProxyInterface) newObject).realmGet$tokenDevice());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$profileImage(((UserEntityRealmProxyInterface) newObject).realmGet$profileImage());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$birthday(((UserEntityRealmProxyInterface) newObject).realmGet$birthday());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$gender(((UserEntityRealmProxyInterface) newObject).realmGet$gender());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$job(((UserEntityRealmProxyInterface) newObject).realmGet$job());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$description(((UserEntityRealmProxyInterface) newObject).realmGet$description());
        ((UserEntityRealmProxyInterface) realmObject).realmSet$loginAt(((UserEntityRealmProxyInterface) newObject).realmGet$loginAt());
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("UserEntity = [");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{token:");
        stringBuilder.append(realmGet$token() != null ? realmGet$token() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{refreshToken:");
        stringBuilder.append(realmGet$refreshToken() != null ? realmGet$refreshToken() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{tokenExp:");
        stringBuilder.append(realmGet$tokenExp() != null ? realmGet$tokenExp() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{phoneNumber:");
        stringBuilder.append(realmGet$phoneNumber() != null ? realmGet$phoneNumber() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{email:");
        stringBuilder.append(realmGet$email() != null ? realmGet$email() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fullName:");
        stringBuilder.append(realmGet$fullName() != null ? realmGet$fullName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{password:");
        stringBuilder.append(realmGet$password() != null ? realmGet$password() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{tokenDevice:");
        stringBuilder.append(realmGet$tokenDevice() != null ? realmGet$tokenDevice() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{profileImage:");
        stringBuilder.append(realmGet$profileImage() != null ? realmGet$profileImage() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{birthday:");
        stringBuilder.append(realmGet$birthday() != null ? realmGet$birthday() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{gender:");
        stringBuilder.append(realmGet$gender());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{job:");
        stringBuilder.append(realmGet$job() != null ? realmGet$job() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{description:");
        stringBuilder.append(realmGet$description() != null ? realmGet$description() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{loginAt:");
        stringBuilder.append(realmGet$loginAt() != null ? realmGet$loginAt() : "null");
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
        UserEntityRealmProxy aUserEntity = (UserEntityRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aUserEntity.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aUserEntity.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aUserEntity.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
