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

public class NotificationRealmProxy extends vn.tonish.hozo.model.Notification
    implements RealmObjectProxy, NotificationRealmProxyInterface {

    static final class NotificationColumnInfo extends ColumnInfo
        implements Cloneable {

        public long statusIndex;
        public long idIndex;
        public long titleIndex;
        public long contentIndex;
        public long created_dateIndex;

        NotificationColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(5);
            this.statusIndex = getValidColumnIndex(path, table, "Notification", "status");
            indicesMap.put("status", this.statusIndex);
            this.idIndex = getValidColumnIndex(path, table, "Notification", "id");
            indicesMap.put("id", this.idIndex);
            this.titleIndex = getValidColumnIndex(path, table, "Notification", "title");
            indicesMap.put("title", this.titleIndex);
            this.contentIndex = getValidColumnIndex(path, table, "Notification", "content");
            indicesMap.put("content", this.contentIndex);
            this.created_dateIndex = getValidColumnIndex(path, table, "Notification", "created_date");
            indicesMap.put("created_date", this.created_dateIndex);

            setIndicesMap(indicesMap);
        }

        @Override
        public final void copyColumnInfoFrom(ColumnInfo other) {
            final NotificationColumnInfo otherInfo = (NotificationColumnInfo) other;
            this.statusIndex = otherInfo.statusIndex;
            this.idIndex = otherInfo.idIndex;
            this.titleIndex = otherInfo.titleIndex;
            this.contentIndex = otherInfo.contentIndex;
            this.created_dateIndex = otherInfo.created_dateIndex;

            setIndicesMap(otherInfo.getIndicesMap());
        }

        @Override
        public final NotificationColumnInfo clone() {
            return (NotificationColumnInfo) super.clone();
        }

    }
    private NotificationColumnInfo columnInfo;
    private ProxyState<vn.tonish.hozo.model.Notification> proxyState;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("status");
        fieldNames.add("id");
        fieldNames.add("title");
        fieldNames.add("content");
        fieldNames.add("created_date");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    NotificationRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (NotificationColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<vn.tonish.hozo.model.Notification>(this);
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
    public int realmGet$id() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.idIndex);
    }

    @Override
    public void realmSet$id(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.idIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.idIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$title() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.titleIndex);
    }

    @Override
    public void realmSet$title(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.titleIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.titleIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.titleIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.titleIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$content() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.contentIndex);
    }

    @Override
    public void realmSet$content(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.contentIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.contentIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.contentIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.contentIndex, value);
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
        if (!realmSchema.contains("Notification")) {
            RealmObjectSchema realmObjectSchema = realmSchema.create("Notification");
            realmObjectSchema.add("status", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
            realmObjectSchema.add("id", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
            realmObjectSchema.add("title", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("content", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("created_date", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            return realmObjectSchema;
        }
        return realmSchema.get("Notification");
    }

    public static NotificationColumnInfo validateTable(SharedRealm sharedRealm, boolean allowExtraColumns) {
        if (!sharedRealm.hasTable("class_Notification")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "The 'Notification' class is missing from the schema for this Realm.");
        }
        Table table = sharedRealm.getTable("class_Notification");
        final long columnCount = table.getColumnCount();
        if (columnCount != 5) {
            if (columnCount < 5) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is less than expected - expected 5 but was " + columnCount);
            }
            if (allowExtraColumns) {
                RealmLog.debug("Field count is more than expected - expected 5 but was %1$d", columnCount);
            } else {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is more than expected - expected 5 but was " + columnCount);
            }
        }
        Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
        for (long i = 0; i < columnCount; i++) {
            columnTypes.put(table.getColumnName(i), table.getColumnType(i));
        }

        final NotificationColumnInfo columnInfo = new NotificationColumnInfo(sharedRealm.getPath(), table);

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
        if (columnTypes.get("id") != RealmFieldType.INTEGER) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'int' for field 'id' in existing Realm file.");
        }
        if (table.isColumnNullable(columnInfo.idIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'id' does support null values in the existing Realm file. Use corresponding boxed type for field 'id' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("title")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'title' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("title") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'title' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.titleIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'title' is required. Either set @Required to field 'title' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("content")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'content' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("content") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'content' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.contentIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'content' is required. Either set @Required to field 'content' or migrate using RealmObjectSchema.setNullable().");
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
        return "class_Notification";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static vn.tonish.hozo.model.Notification createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        vn.tonish.hozo.model.Notification obj = realm.createObjectInternal(vn.tonish.hozo.model.Notification.class, true, excludeFields);
        if (json.has("status")) {
            if (json.isNull("status")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'status' to null.");
            } else {
                ((NotificationRealmProxyInterface) obj).realmSet$status((int) json.getInt("status"));
            }
        }
        if (json.has("id")) {
            if (json.isNull("id")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'id' to null.");
            } else {
                ((NotificationRealmProxyInterface) obj).realmSet$id((int) json.getInt("id"));
            }
        }
        if (json.has("title")) {
            if (json.isNull("title")) {
                ((NotificationRealmProxyInterface) obj).realmSet$title(null);
            } else {
                ((NotificationRealmProxyInterface) obj).realmSet$title((String) json.getString("title"));
            }
        }
        if (json.has("content")) {
            if (json.isNull("content")) {
                ((NotificationRealmProxyInterface) obj).realmSet$content(null);
            } else {
                ((NotificationRealmProxyInterface) obj).realmSet$content((String) json.getString("content"));
            }
        }
        if (json.has("created_date")) {
            if (json.isNull("created_date")) {
                ((NotificationRealmProxyInterface) obj).realmSet$created_date(null);
            } else {
                ((NotificationRealmProxyInterface) obj).realmSet$created_date((String) json.getString("created_date"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static vn.tonish.hozo.model.Notification createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        vn.tonish.hozo.model.Notification obj = new vn.tonish.hozo.model.Notification();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("status")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'status' to null.");
                } else {
                    ((NotificationRealmProxyInterface) obj).realmSet$status((int) reader.nextInt());
                }
            } else if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'id' to null.");
                } else {
                    ((NotificationRealmProxyInterface) obj).realmSet$id((int) reader.nextInt());
                }
            } else if (name.equals("title")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((NotificationRealmProxyInterface) obj).realmSet$title(null);
                } else {
                    ((NotificationRealmProxyInterface) obj).realmSet$title((String) reader.nextString());
                }
            } else if (name.equals("content")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((NotificationRealmProxyInterface) obj).realmSet$content(null);
                } else {
                    ((NotificationRealmProxyInterface) obj).realmSet$content((String) reader.nextString());
                }
            } else if (name.equals("created_date")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((NotificationRealmProxyInterface) obj).realmSet$created_date(null);
                } else {
                    ((NotificationRealmProxyInterface) obj).realmSet$created_date((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        obj = realm.copyToRealm(obj);
        return obj;
    }

    public static vn.tonish.hozo.model.Notification copyOrUpdate(Realm realm, vn.tonish.hozo.model.Notification object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (vn.tonish.hozo.model.Notification) cachedRealmObject;
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static vn.tonish.hozo.model.Notification copy(Realm realm, vn.tonish.hozo.model.Notification newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (vn.tonish.hozo.model.Notification) cachedRealmObject;
        } else {
            // rejecting default values to avoid creating unexpected objects from RealmModel/RealmList fields.
            vn.tonish.hozo.model.Notification realmObject = realm.createObjectInternal(vn.tonish.hozo.model.Notification.class, false, Collections.<String>emptyList());
            cache.put(newObject, (RealmObjectProxy) realmObject);
            ((NotificationRealmProxyInterface) realmObject).realmSet$status(((NotificationRealmProxyInterface) newObject).realmGet$status());
            ((NotificationRealmProxyInterface) realmObject).realmSet$id(((NotificationRealmProxyInterface) newObject).realmGet$id());
            ((NotificationRealmProxyInterface) realmObject).realmSet$title(((NotificationRealmProxyInterface) newObject).realmGet$title());
            ((NotificationRealmProxyInterface) realmObject).realmSet$content(((NotificationRealmProxyInterface) newObject).realmGet$content());
            ((NotificationRealmProxyInterface) realmObject).realmSet$created_date(((NotificationRealmProxyInterface) newObject).realmGet$created_date());
            return realmObject;
        }
    }

    public static long insert(Realm realm, vn.tonish.hozo.model.Notification object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(vn.tonish.hozo.model.Notification.class);
        long tableNativePtr = table.getNativeTablePointer();
        NotificationColumnInfo columnInfo = (NotificationColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.Notification.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        Table.nativeSetLong(tableNativePtr, columnInfo.statusIndex, rowIndex, ((NotificationRealmProxyInterface)object).realmGet$status(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.idIndex, rowIndex, ((NotificationRealmProxyInterface)object).realmGet$id(), false);
        String realmGet$title = ((NotificationRealmProxyInterface)object).realmGet$title();
        if (realmGet$title != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.titleIndex, rowIndex, realmGet$title, false);
        }
        String realmGet$content = ((NotificationRealmProxyInterface)object).realmGet$content();
        if (realmGet$content != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.contentIndex, rowIndex, realmGet$content, false);
        }
        String realmGet$created_date = ((NotificationRealmProxyInterface)object).realmGet$created_date();
        if (realmGet$created_date != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.created_dateIndex, rowIndex, realmGet$created_date, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(vn.tonish.hozo.model.Notification.class);
        long tableNativePtr = table.getNativeTablePointer();
        NotificationColumnInfo columnInfo = (NotificationColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.Notification.class);
        vn.tonish.hozo.model.Notification object = null;
        while (objects.hasNext()) {
            object = (vn.tonish.hozo.model.Notification) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                Table.nativeSetLong(tableNativePtr, columnInfo.statusIndex, rowIndex, ((NotificationRealmProxyInterface)object).realmGet$status(), false);
                Table.nativeSetLong(tableNativePtr, columnInfo.idIndex, rowIndex, ((NotificationRealmProxyInterface)object).realmGet$id(), false);
                String realmGet$title = ((NotificationRealmProxyInterface)object).realmGet$title();
                if (realmGet$title != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.titleIndex, rowIndex, realmGet$title, false);
                }
                String realmGet$content = ((NotificationRealmProxyInterface)object).realmGet$content();
                if (realmGet$content != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.contentIndex, rowIndex, realmGet$content, false);
                }
                String realmGet$created_date = ((NotificationRealmProxyInterface)object).realmGet$created_date();
                if (realmGet$created_date != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.created_dateIndex, rowIndex, realmGet$created_date, false);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, vn.tonish.hozo.model.Notification object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(vn.tonish.hozo.model.Notification.class);
        long tableNativePtr = table.getNativeTablePointer();
        NotificationColumnInfo columnInfo = (NotificationColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.Notification.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        Table.nativeSetLong(tableNativePtr, columnInfo.statusIndex, rowIndex, ((NotificationRealmProxyInterface)object).realmGet$status(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.idIndex, rowIndex, ((NotificationRealmProxyInterface)object).realmGet$id(), false);
        String realmGet$title = ((NotificationRealmProxyInterface)object).realmGet$title();
        if (realmGet$title != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.titleIndex, rowIndex, realmGet$title, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.titleIndex, rowIndex, false);
        }
        String realmGet$content = ((NotificationRealmProxyInterface)object).realmGet$content();
        if (realmGet$content != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.contentIndex, rowIndex, realmGet$content, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.contentIndex, rowIndex, false);
        }
        String realmGet$created_date = ((NotificationRealmProxyInterface)object).realmGet$created_date();
        if (realmGet$created_date != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.created_dateIndex, rowIndex, realmGet$created_date, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.created_dateIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(vn.tonish.hozo.model.Notification.class);
        long tableNativePtr = table.getNativeTablePointer();
        NotificationColumnInfo columnInfo = (NotificationColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.Notification.class);
        vn.tonish.hozo.model.Notification object = null;
        while (objects.hasNext()) {
            object = (vn.tonish.hozo.model.Notification) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                Table.nativeSetLong(tableNativePtr, columnInfo.statusIndex, rowIndex, ((NotificationRealmProxyInterface)object).realmGet$status(), false);
                Table.nativeSetLong(tableNativePtr, columnInfo.idIndex, rowIndex, ((NotificationRealmProxyInterface)object).realmGet$id(), false);
                String realmGet$title = ((NotificationRealmProxyInterface)object).realmGet$title();
                if (realmGet$title != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.titleIndex, rowIndex, realmGet$title, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.titleIndex, rowIndex, false);
                }
                String realmGet$content = ((NotificationRealmProxyInterface)object).realmGet$content();
                if (realmGet$content != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.contentIndex, rowIndex, realmGet$content, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.contentIndex, rowIndex, false);
                }
                String realmGet$created_date = ((NotificationRealmProxyInterface)object).realmGet$created_date();
                if (realmGet$created_date != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.created_dateIndex, rowIndex, realmGet$created_date, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.created_dateIndex, rowIndex, false);
                }
            }
        }
    }

    public static vn.tonish.hozo.model.Notification createDetachedCopy(vn.tonish.hozo.model.Notification realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        vn.tonish.hozo.model.Notification unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (vn.tonish.hozo.model.Notification)cachedObject.object;
            } else {
                unmanagedObject = (vn.tonish.hozo.model.Notification)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new vn.tonish.hozo.model.Notification();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        }
        ((NotificationRealmProxyInterface) unmanagedObject).realmSet$status(((NotificationRealmProxyInterface) realmObject).realmGet$status());
        ((NotificationRealmProxyInterface) unmanagedObject).realmSet$id(((NotificationRealmProxyInterface) realmObject).realmGet$id());
        ((NotificationRealmProxyInterface) unmanagedObject).realmSet$title(((NotificationRealmProxyInterface) realmObject).realmGet$title());
        ((NotificationRealmProxyInterface) unmanagedObject).realmSet$content(((NotificationRealmProxyInterface) realmObject).realmGet$content());
        ((NotificationRealmProxyInterface) unmanagedObject).realmSet$created_date(((NotificationRealmProxyInterface) realmObject).realmGet$created_date());
        return unmanagedObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Notification = [");
        stringBuilder.append("{status:");
        stringBuilder.append(realmGet$status());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{title:");
        stringBuilder.append(realmGet$title() != null ? realmGet$title() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{content:");
        stringBuilder.append(realmGet$content() != null ? realmGet$content() : "null");
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
        NotificationRealmProxy aNotification = (NotificationRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aNotification.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aNotification.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aNotification.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
