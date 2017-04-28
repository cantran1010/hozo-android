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

public class NotificationMessageRealmProxy extends vn.tonish.hozo.model.NotificationMessage
    implements RealmObjectProxy, NotificationMessageRealmProxyInterface {

    static final class NotificationMessageColumnInfo extends ColumnInfo
        implements Cloneable {

        public long statusIndex;
        public long idIndex;
        public long typeIndex;
        public long avatarIndex;
        public long nameIndex;
        public long created_dateIndex;

        NotificationMessageColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(6);
            this.statusIndex = getValidColumnIndex(path, table, "NotificationMessage", "status");
            indicesMap.put("status", this.statusIndex);
            this.idIndex = getValidColumnIndex(path, table, "NotificationMessage", "id");
            indicesMap.put("id", this.idIndex);
            this.typeIndex = getValidColumnIndex(path, table, "NotificationMessage", "type");
            indicesMap.put("type", this.typeIndex);
            this.avatarIndex = getValidColumnIndex(path, table, "NotificationMessage", "avatar");
            indicesMap.put("avatar", this.avatarIndex);
            this.nameIndex = getValidColumnIndex(path, table, "NotificationMessage", "name");
            indicesMap.put("name", this.nameIndex);
            this.created_dateIndex = getValidColumnIndex(path, table, "NotificationMessage", "created_date");
            indicesMap.put("created_date", this.created_dateIndex);

            setIndicesMap(indicesMap);
        }

        @Override
        public final void copyColumnInfoFrom(ColumnInfo other) {
            final NotificationMessageColumnInfo otherInfo = (NotificationMessageColumnInfo) other;
            this.statusIndex = otherInfo.statusIndex;
            this.idIndex = otherInfo.idIndex;
            this.typeIndex = otherInfo.typeIndex;
            this.avatarIndex = otherInfo.avatarIndex;
            this.nameIndex = otherInfo.nameIndex;
            this.created_dateIndex = otherInfo.created_dateIndex;

            setIndicesMap(otherInfo.getIndicesMap());
        }

        @Override
        public final NotificationMessageColumnInfo clone() {
            return (NotificationMessageColumnInfo) super.clone();
        }

    }
    private NotificationMessageColumnInfo columnInfo;
    private ProxyState<vn.tonish.hozo.model.NotificationMessage> proxyState;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("status");
        fieldNames.add("id");
        fieldNames.add("type");
        fieldNames.add("avatar");
        fieldNames.add("name");
        fieldNames.add("created_date");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    NotificationMessageRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (NotificationMessageColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<vn.tonish.hozo.model.NotificationMessage>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$status() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.statusIndex);
    }

    @Override
    public void realmSet$status(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.statusIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.statusIndex, value);
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
    public String realmGet$type() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.typeIndex);
    }

    @Override
    public void realmSet$type(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.typeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.typeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.typeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.typeIndex, value);
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
    public String realmGet$name() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.nameIndex);
    }

    @Override
    public void realmSet$name(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.nameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.nameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.nameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.nameIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$created_date() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.created_dateIndex);
    }

    @Override
    public void realmSet$created_date(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.created_dateIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.created_dateIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.created_dateIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.created_dateIndex, value);
    }

    public static RealmObjectSchema createRealmObjectSchema(RealmSchema realmSchema) {
        if (!realmSchema.contains("NotificationMessage")) {
            RealmObjectSchema realmObjectSchema = realmSchema.create("NotificationMessage");
            realmObjectSchema.add("status", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
            realmObjectSchema.add("id", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("type", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("avatar", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("name", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("created_date", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            return realmObjectSchema;
        }
        return realmSchema.get("NotificationMessage");
    }

    public static NotificationMessageColumnInfo validateTable(SharedRealm sharedRealm, boolean allowExtraColumns) {
        if (!sharedRealm.hasTable("class_NotificationMessage")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "The 'NotificationMessage' class is missing from the schema for this Realm.");
        }
        Table table = sharedRealm.getTable("class_NotificationMessage");
        final long columnCount = table.getColumnCount();
        if (columnCount != 6) {
            if (columnCount < 6) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is less than expected - expected 6 but was " + columnCount);
            }
            if (allowExtraColumns) {
                RealmLog.debug("Field count is more than expected - expected 6 but was %1$d", columnCount);
            } else {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is more than expected - expected 6 but was " + columnCount);
            }
        }
        Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
        for (long i = 0; i < columnCount; i++) {
            columnTypes.put(table.getColumnName(i), table.getColumnType(i));
        }

        final NotificationMessageColumnInfo columnInfo = new NotificationMessageColumnInfo(sharedRealm.getPath(), table);

        if (table.hasPrimaryKey()) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Primary Key defined for field " + table.getColumnName(table.getPrimaryKey()) + " was removed.");
        }

        if (!columnTypes.containsKey("status")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'status' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("status") != RealmFieldType.INTEGER) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'int' for field 'status' in existing Realm file.");
        }
        if (table.isColumnNullable(columnInfo.statusIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'status' does support null values in the existing Realm file. Use corresponding boxed type for field 'status' or migrate using RealmObjectSchema.setNullable().");
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
        if (!columnTypes.containsKey("type")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'type' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("type") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'type' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.typeIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'type' is required. Either set @Required to field 'type' or migrate using RealmObjectSchema.setNullable().");
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
        if (!columnTypes.containsKey("name")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'name' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("name") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'name' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.nameIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'name' is required. Either set @Required to field 'name' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("created_date")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'created_date' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("created_date") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'created_date' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.created_dateIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'created_date' is required. Either set @Required to field 'created_date' or migrate using RealmObjectSchema.setNullable().");
        }

        return columnInfo;
    }

    public static String getTableName() {
        return "class_NotificationMessage";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static vn.tonish.hozo.model.NotificationMessage createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        vn.tonish.hozo.model.NotificationMessage obj = realm.createObjectInternal(vn.tonish.hozo.model.NotificationMessage.class, true, excludeFields);
        if (json.has("status")) {
            if (json.isNull("status")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'status' to null.");
            } else {
                ((NotificationMessageRealmProxyInterface) obj).realmSet$status((int) json.getInt("status"));
            }
        }
        if (json.has("id")) {
            if (json.isNull("id")) {
                ((NotificationMessageRealmProxyInterface) obj).realmSet$id(null);
            } else {
                ((NotificationMessageRealmProxyInterface) obj).realmSet$id((String) json.getString("id"));
            }
        }
        if (json.has("type")) {
            if (json.isNull("type")) {
                ((NotificationMessageRealmProxyInterface) obj).realmSet$type(null);
            } else {
                ((NotificationMessageRealmProxyInterface) obj).realmSet$type((String) json.getString("type"));
            }
        }
        if (json.has("avatar")) {
            if (json.isNull("avatar")) {
                ((NotificationMessageRealmProxyInterface) obj).realmSet$avatar(null);
            } else {
                ((NotificationMessageRealmProxyInterface) obj).realmSet$avatar((String) json.getString("avatar"));
            }
        }
        if (json.has("name")) {
            if (json.isNull("name")) {
                ((NotificationMessageRealmProxyInterface) obj).realmSet$name(null);
            } else {
                ((NotificationMessageRealmProxyInterface) obj).realmSet$name((String) json.getString("name"));
            }
        }
        if (json.has("created_date")) {
            if (json.isNull("created_date")) {
                ((NotificationMessageRealmProxyInterface) obj).realmSet$created_date(null);
            } else {
                ((NotificationMessageRealmProxyInterface) obj).realmSet$created_date((String) json.getString("created_date"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static vn.tonish.hozo.model.NotificationMessage createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        vn.tonish.hozo.model.NotificationMessage obj = new vn.tonish.hozo.model.NotificationMessage();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("status")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'status' to null.");
                } else {
                    ((NotificationMessageRealmProxyInterface) obj).realmSet$status((int) reader.nextInt());
                }
            } else if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((NotificationMessageRealmProxyInterface) obj).realmSet$id(null);
                } else {
                    ((NotificationMessageRealmProxyInterface) obj).realmSet$id((String) reader.nextString());
                }
            } else if (name.equals("type")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((NotificationMessageRealmProxyInterface) obj).realmSet$type(null);
                } else {
                    ((NotificationMessageRealmProxyInterface) obj).realmSet$type((String) reader.nextString());
                }
            } else if (name.equals("avatar")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((NotificationMessageRealmProxyInterface) obj).realmSet$avatar(null);
                } else {
                    ((NotificationMessageRealmProxyInterface) obj).realmSet$avatar((String) reader.nextString());
                }
            } else if (name.equals("name")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((NotificationMessageRealmProxyInterface) obj).realmSet$name(null);
                } else {
                    ((NotificationMessageRealmProxyInterface) obj).realmSet$name((String) reader.nextString());
                }
            } else if (name.equals("created_date")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((NotificationMessageRealmProxyInterface) obj).realmSet$created_date(null);
                } else {
                    ((NotificationMessageRealmProxyInterface) obj).realmSet$created_date((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        obj = realm.copyToRealm(obj);
        return obj;
    }

    public static vn.tonish.hozo.model.NotificationMessage copyOrUpdate(Realm realm, vn.tonish.hozo.model.NotificationMessage object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (vn.tonish.hozo.model.NotificationMessage) cachedRealmObject;
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static vn.tonish.hozo.model.NotificationMessage copy(Realm realm, vn.tonish.hozo.model.NotificationMessage newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (vn.tonish.hozo.model.NotificationMessage) cachedRealmObject;
        } else {
            // rejecting default values to avoid creating unexpected objects from RealmModel/RealmList fields.
            vn.tonish.hozo.model.NotificationMessage realmObject = realm.createObjectInternal(vn.tonish.hozo.model.NotificationMessage.class, false, Collections.<String>emptyList());
            cache.put(newObject, (RealmObjectProxy) realmObject);
            ((NotificationMessageRealmProxyInterface) realmObject).realmSet$status(((NotificationMessageRealmProxyInterface) newObject).realmGet$status());
            ((NotificationMessageRealmProxyInterface) realmObject).realmSet$id(((NotificationMessageRealmProxyInterface) newObject).realmGet$id());
            ((NotificationMessageRealmProxyInterface) realmObject).realmSet$type(((NotificationMessageRealmProxyInterface) newObject).realmGet$type());
            ((NotificationMessageRealmProxyInterface) realmObject).realmSet$avatar(((NotificationMessageRealmProxyInterface) newObject).realmGet$avatar());
            ((NotificationMessageRealmProxyInterface) realmObject).realmSet$name(((NotificationMessageRealmProxyInterface) newObject).realmGet$name());
            ((NotificationMessageRealmProxyInterface) realmObject).realmSet$created_date(((NotificationMessageRealmProxyInterface) newObject).realmGet$created_date());
            return realmObject;
        }
    }

    public static long insert(Realm realm, vn.tonish.hozo.model.NotificationMessage object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(vn.tonish.hozo.model.NotificationMessage.class);
        long tableNativePtr = table.getNativeTablePointer();
        NotificationMessageColumnInfo columnInfo = (NotificationMessageColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.NotificationMessage.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        Table.nativeSetLong(tableNativePtr, columnInfo.statusIndex, rowIndex, ((NotificationMessageRealmProxyInterface)object).realmGet$status(), false);
        String realmGet$id = ((NotificationMessageRealmProxyInterface)object).realmGet$id();
        if (realmGet$id != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
        }
        String realmGet$type = ((NotificationMessageRealmProxyInterface)object).realmGet$type();
        if (realmGet$type != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
        }
        String realmGet$avatar = ((NotificationMessageRealmProxyInterface)object).realmGet$avatar();
        if (realmGet$avatar != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.avatarIndex, rowIndex, realmGet$avatar, false);
        }
        String realmGet$name = ((NotificationMessageRealmProxyInterface)object).realmGet$name();
        if (realmGet$name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
        }
        String realmGet$created_date = ((NotificationMessageRealmProxyInterface)object).realmGet$created_date();
        if (realmGet$created_date != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.created_dateIndex, rowIndex, realmGet$created_date, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(vn.tonish.hozo.model.NotificationMessage.class);
        long tableNativePtr = table.getNativeTablePointer();
        NotificationMessageColumnInfo columnInfo = (NotificationMessageColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.NotificationMessage.class);
        vn.tonish.hozo.model.NotificationMessage object = null;
        while (objects.hasNext()) {
            object = (vn.tonish.hozo.model.NotificationMessage) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                Table.nativeSetLong(tableNativePtr, columnInfo.statusIndex, rowIndex, ((NotificationMessageRealmProxyInterface)object).realmGet$status(), false);
                String realmGet$id = ((NotificationMessageRealmProxyInterface)object).realmGet$id();
                if (realmGet$id != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
                }
                String realmGet$type = ((NotificationMessageRealmProxyInterface)object).realmGet$type();
                if (realmGet$type != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
                }
                String realmGet$avatar = ((NotificationMessageRealmProxyInterface)object).realmGet$avatar();
                if (realmGet$avatar != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.avatarIndex, rowIndex, realmGet$avatar, false);
                }
                String realmGet$name = ((NotificationMessageRealmProxyInterface)object).realmGet$name();
                if (realmGet$name != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
                }
                String realmGet$created_date = ((NotificationMessageRealmProxyInterface)object).realmGet$created_date();
                if (realmGet$created_date != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.created_dateIndex, rowIndex, realmGet$created_date, false);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, vn.tonish.hozo.model.NotificationMessage object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(vn.tonish.hozo.model.NotificationMessage.class);
        long tableNativePtr = table.getNativeTablePointer();
        NotificationMessageColumnInfo columnInfo = (NotificationMessageColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.NotificationMessage.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        Table.nativeSetLong(tableNativePtr, columnInfo.statusIndex, rowIndex, ((NotificationMessageRealmProxyInterface)object).realmGet$status(), false);
        String realmGet$id = ((NotificationMessageRealmProxyInterface)object).realmGet$id();
        if (realmGet$id != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.idIndex, rowIndex, false);
        }
        String realmGet$type = ((NotificationMessageRealmProxyInterface)object).realmGet$type();
        if (realmGet$type != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.typeIndex, rowIndex, false);
        }
        String realmGet$avatar = ((NotificationMessageRealmProxyInterface)object).realmGet$avatar();
        if (realmGet$avatar != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.avatarIndex, rowIndex, realmGet$avatar, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.avatarIndex, rowIndex, false);
        }
        String realmGet$name = ((NotificationMessageRealmProxyInterface)object).realmGet$name();
        if (realmGet$name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.nameIndex, rowIndex, false);
        }
        String realmGet$created_date = ((NotificationMessageRealmProxyInterface)object).realmGet$created_date();
        if (realmGet$created_date != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.created_dateIndex, rowIndex, realmGet$created_date, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.created_dateIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(vn.tonish.hozo.model.NotificationMessage.class);
        long tableNativePtr = table.getNativeTablePointer();
        NotificationMessageColumnInfo columnInfo = (NotificationMessageColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.NotificationMessage.class);
        vn.tonish.hozo.model.NotificationMessage object = null;
        while (objects.hasNext()) {
            object = (vn.tonish.hozo.model.NotificationMessage) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                Table.nativeSetLong(tableNativePtr, columnInfo.statusIndex, rowIndex, ((NotificationMessageRealmProxyInterface)object).realmGet$status(), false);
                String realmGet$id = ((NotificationMessageRealmProxyInterface)object).realmGet$id();
                if (realmGet$id != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.idIndex, rowIndex, false);
                }
                String realmGet$type = ((NotificationMessageRealmProxyInterface)object).realmGet$type();
                if (realmGet$type != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.typeIndex, rowIndex, false);
                }
                String realmGet$avatar = ((NotificationMessageRealmProxyInterface)object).realmGet$avatar();
                if (realmGet$avatar != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.avatarIndex, rowIndex, realmGet$avatar, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.avatarIndex, rowIndex, false);
                }
                String realmGet$name = ((NotificationMessageRealmProxyInterface)object).realmGet$name();
                if (realmGet$name != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.nameIndex, rowIndex, false);
                }
                String realmGet$created_date = ((NotificationMessageRealmProxyInterface)object).realmGet$created_date();
                if (realmGet$created_date != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.created_dateIndex, rowIndex, realmGet$created_date, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.created_dateIndex, rowIndex, false);
                }
            }
        }
    }

    public static vn.tonish.hozo.model.NotificationMessage createDetachedCopy(vn.tonish.hozo.model.NotificationMessage realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        vn.tonish.hozo.model.NotificationMessage unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (vn.tonish.hozo.model.NotificationMessage)cachedObject.object;
            } else {
                unmanagedObject = (vn.tonish.hozo.model.NotificationMessage)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new vn.tonish.hozo.model.NotificationMessage();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        }
        ((NotificationMessageRealmProxyInterface) unmanagedObject).realmSet$status(((NotificationMessageRealmProxyInterface) realmObject).realmGet$status());
        ((NotificationMessageRealmProxyInterface) unmanagedObject).realmSet$id(((NotificationMessageRealmProxyInterface) realmObject).realmGet$id());
        ((NotificationMessageRealmProxyInterface) unmanagedObject).realmSet$type(((NotificationMessageRealmProxyInterface) realmObject).realmGet$type());
        ((NotificationMessageRealmProxyInterface) unmanagedObject).realmSet$avatar(((NotificationMessageRealmProxyInterface) realmObject).realmGet$avatar());
        ((NotificationMessageRealmProxyInterface) unmanagedObject).realmSet$name(((NotificationMessageRealmProxyInterface) realmObject).realmGet$name());
        ((NotificationMessageRealmProxyInterface) unmanagedObject).realmSet$created_date(((NotificationMessageRealmProxyInterface) realmObject).realmGet$created_date());
        return unmanagedObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("NotificationMessage = [");
        stringBuilder.append("{status:");
        stringBuilder.append(realmGet$status());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id() != null ? realmGet$id() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{type:");
        stringBuilder.append(realmGet$type() != null ? realmGet$type() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{avatar:");
        stringBuilder.append(realmGet$avatar() != null ? realmGet$avatar() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{name:");
        stringBuilder.append(realmGet$name() != null ? realmGet$name() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{created_date:");
        stringBuilder.append(realmGet$created_date() != null ? realmGet$created_date() : "null");
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
        NotificationMessageRealmProxy aNotificationMessage = (NotificationMessageRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aNotificationMessage.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aNotificationMessage.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aNotificationMessage.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
