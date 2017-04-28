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

public class CommentRealmProxy extends vn.tonish.hozo.model.Comment
    implements RealmObjectProxy, CommentRealmProxyInterface {

    static final class CommentColumnInfo extends ColumnInfo
        implements Cloneable {

        public long idIndex;
        public long authorIdIndex;
        public long fullNameIndex;
        public long avatarIndex;
        public long bodyIndex;
        public long createdAtIndex;
        public long taskIdIndex;

        CommentColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(7);
            this.idIndex = getValidColumnIndex(path, table, "Comment", "id");
            indicesMap.put("id", this.idIndex);
            this.authorIdIndex = getValidColumnIndex(path, table, "Comment", "authorId");
            indicesMap.put("authorId", this.authorIdIndex);
            this.fullNameIndex = getValidColumnIndex(path, table, "Comment", "fullName");
            indicesMap.put("fullName", this.fullNameIndex);
            this.avatarIndex = getValidColumnIndex(path, table, "Comment", "avatar");
            indicesMap.put("avatar", this.avatarIndex);
            this.bodyIndex = getValidColumnIndex(path, table, "Comment", "body");
            indicesMap.put("body", this.bodyIndex);
            this.createdAtIndex = getValidColumnIndex(path, table, "Comment", "createdAt");
            indicesMap.put("createdAt", this.createdAtIndex);
            this.taskIdIndex = getValidColumnIndex(path, table, "Comment", "taskId");
            indicesMap.put("taskId", this.taskIdIndex);

            setIndicesMap(indicesMap);
        }

        @Override
        public final void copyColumnInfoFrom(ColumnInfo other) {
            final CommentColumnInfo otherInfo = (CommentColumnInfo) other;
            this.idIndex = otherInfo.idIndex;
            this.authorIdIndex = otherInfo.authorIdIndex;
            this.fullNameIndex = otherInfo.fullNameIndex;
            this.avatarIndex = otherInfo.avatarIndex;
            this.bodyIndex = otherInfo.bodyIndex;
            this.createdAtIndex = otherInfo.createdAtIndex;
            this.taskIdIndex = otherInfo.taskIdIndex;

            setIndicesMap(otherInfo.getIndicesMap());
        }

        @Override
        public final CommentColumnInfo clone() {
            return (CommentColumnInfo) super.clone();
        }

    }
    private CommentColumnInfo columnInfo;
    private ProxyState<vn.tonish.hozo.model.Comment> proxyState;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("authorId");
        fieldNames.add("fullName");
        fieldNames.add("avatar");
        fieldNames.add("body");
        fieldNames.add("createdAt");
        fieldNames.add("taskId");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    CommentRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (CommentColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<vn.tonish.hozo.model.Comment>(this);
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
    public String realmGet$authorId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.authorIdIndex);
    }

    @Override
    public void realmSet$authorId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.authorIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.authorIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.authorIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.authorIdIndex, value);
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
    public String realmGet$body() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.bodyIndex);
    }

    @Override
    public void realmSet$body(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.bodyIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.bodyIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.bodyIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.bodyIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$createdAt() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.createdAtIndex);
    }

    @Override
    public void realmSet$createdAt(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.createdAtIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.createdAtIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.createdAtIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.createdAtIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$taskId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.taskIdIndex);
    }

    @Override
    public void realmSet$taskId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.taskIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.taskIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.taskIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.taskIdIndex, value);
    }

    public static RealmObjectSchema createRealmObjectSchema(RealmSchema realmSchema) {
        if (!realmSchema.contains("Comment")) {
            RealmObjectSchema realmObjectSchema = realmSchema.create("Comment");
            realmObjectSchema.add("id", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("authorId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("fullName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("avatar", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("body", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("createdAt", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            realmObjectSchema.add("taskId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
            return realmObjectSchema;
        }
        return realmSchema.get("Comment");
    }

    public static CommentColumnInfo validateTable(SharedRealm sharedRealm, boolean allowExtraColumns) {
        if (!sharedRealm.hasTable("class_Comment")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "The 'Comment' class is missing from the schema for this Realm.");
        }
        Table table = sharedRealm.getTable("class_Comment");
        final long columnCount = table.getColumnCount();
        if (columnCount != 7) {
            if (columnCount < 7) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is less than expected - expected 7 but was " + columnCount);
            }
            if (allowExtraColumns) {
                RealmLog.debug("Field count is more than expected - expected 7 but was %1$d", columnCount);
            } else {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is more than expected - expected 7 but was " + columnCount);
            }
        }
        Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
        for (long i = 0; i < columnCount; i++) {
            columnTypes.put(table.getColumnName(i), table.getColumnType(i));
        }

        final CommentColumnInfo columnInfo = new CommentColumnInfo(sharedRealm.getPath(), table);

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
        if (!columnTypes.containsKey("authorId")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'authorId' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("authorId") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'authorId' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.authorIdIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'authorId' is required. Either set @Required to field 'authorId' or migrate using RealmObjectSchema.setNullable().");
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
        if (!columnTypes.containsKey("avatar")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'avatar' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("avatar") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'avatar' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.avatarIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'avatar' is required. Either set @Required to field 'avatar' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("body")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'body' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("body") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'body' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.bodyIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'body' is required. Either set @Required to field 'body' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("createdAt")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'createdAt' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("createdAt") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'createdAt' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.createdAtIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'createdAt' is required. Either set @Required to field 'createdAt' or migrate using RealmObjectSchema.setNullable().");
        }
        if (!columnTypes.containsKey("taskId")) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'taskId' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
        }
        if (columnTypes.get("taskId") != RealmFieldType.STRING) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'taskId' in existing Realm file.");
        }
        if (!table.isColumnNullable(columnInfo.taskIdIndex)) {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'taskId' is required. Either set @Required to field 'taskId' or migrate using RealmObjectSchema.setNullable().");
        }

        return columnInfo;
    }

    public static String getTableName() {
        return "class_Comment";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static vn.tonish.hozo.model.Comment createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        vn.tonish.hozo.model.Comment obj = realm.createObjectInternal(vn.tonish.hozo.model.Comment.class, true, excludeFields);
        if (json.has("id")) {
            if (json.isNull("id")) {
                ((CommentRealmProxyInterface) obj).realmSet$id(null);
            } else {
                ((CommentRealmProxyInterface) obj).realmSet$id((String) json.getString("id"));
            }
        }
        if (json.has("authorId")) {
            if (json.isNull("authorId")) {
                ((CommentRealmProxyInterface) obj).realmSet$authorId(null);
            } else {
                ((CommentRealmProxyInterface) obj).realmSet$authorId((String) json.getString("authorId"));
            }
        }
        if (json.has("fullName")) {
            if (json.isNull("fullName")) {
                ((CommentRealmProxyInterface) obj).realmSet$fullName(null);
            } else {
                ((CommentRealmProxyInterface) obj).realmSet$fullName((String) json.getString("fullName"));
            }
        }
        if (json.has("avatar")) {
            if (json.isNull("avatar")) {
                ((CommentRealmProxyInterface) obj).realmSet$avatar(null);
            } else {
                ((CommentRealmProxyInterface) obj).realmSet$avatar((String) json.getString("avatar"));
            }
        }
        if (json.has("body")) {
            if (json.isNull("body")) {
                ((CommentRealmProxyInterface) obj).realmSet$body(null);
            } else {
                ((CommentRealmProxyInterface) obj).realmSet$body((String) json.getString("body"));
            }
        }
        if (json.has("createdAt")) {
            if (json.isNull("createdAt")) {
                ((CommentRealmProxyInterface) obj).realmSet$createdAt(null);
            } else {
                ((CommentRealmProxyInterface) obj).realmSet$createdAt((String) json.getString("createdAt"));
            }
        }
        if (json.has("taskId")) {
            if (json.isNull("taskId")) {
                ((CommentRealmProxyInterface) obj).realmSet$taskId(null);
            } else {
                ((CommentRealmProxyInterface) obj).realmSet$taskId((String) json.getString("taskId"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static vn.tonish.hozo.model.Comment createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        vn.tonish.hozo.model.Comment obj = new vn.tonish.hozo.model.Comment();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((CommentRealmProxyInterface) obj).realmSet$id(null);
                } else {
                    ((CommentRealmProxyInterface) obj).realmSet$id((String) reader.nextString());
                }
            } else if (name.equals("authorId")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((CommentRealmProxyInterface) obj).realmSet$authorId(null);
                } else {
                    ((CommentRealmProxyInterface) obj).realmSet$authorId((String) reader.nextString());
                }
            } else if (name.equals("fullName")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((CommentRealmProxyInterface) obj).realmSet$fullName(null);
                } else {
                    ((CommentRealmProxyInterface) obj).realmSet$fullName((String) reader.nextString());
                }
            } else if (name.equals("avatar")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((CommentRealmProxyInterface) obj).realmSet$avatar(null);
                } else {
                    ((CommentRealmProxyInterface) obj).realmSet$avatar((String) reader.nextString());
                }
            } else if (name.equals("body")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((CommentRealmProxyInterface) obj).realmSet$body(null);
                } else {
                    ((CommentRealmProxyInterface) obj).realmSet$body((String) reader.nextString());
                }
            } else if (name.equals("createdAt")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((CommentRealmProxyInterface) obj).realmSet$createdAt(null);
                } else {
                    ((CommentRealmProxyInterface) obj).realmSet$createdAt((String) reader.nextString());
                }
            } else if (name.equals("taskId")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((CommentRealmProxyInterface) obj).realmSet$taskId(null);
                } else {
                    ((CommentRealmProxyInterface) obj).realmSet$taskId((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        obj = realm.copyToRealm(obj);
        return obj;
    }

    public static vn.tonish.hozo.model.Comment copyOrUpdate(Realm realm, vn.tonish.hozo.model.Comment object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (vn.tonish.hozo.model.Comment) cachedRealmObject;
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static vn.tonish.hozo.model.Comment copy(Realm realm, vn.tonish.hozo.model.Comment newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (vn.tonish.hozo.model.Comment) cachedRealmObject;
        } else {
            // rejecting default values to avoid creating unexpected objects from RealmModel/RealmList fields.
            vn.tonish.hozo.model.Comment realmObject = realm.createObjectInternal(vn.tonish.hozo.model.Comment.class, false, Collections.<String>emptyList());
            cache.put(newObject, (RealmObjectProxy) realmObject);
            ((CommentRealmProxyInterface) realmObject).realmSet$id(((CommentRealmProxyInterface) newObject).realmGet$id());
            ((CommentRealmProxyInterface) realmObject).realmSet$authorId(((CommentRealmProxyInterface) newObject).realmGet$authorId());
            ((CommentRealmProxyInterface) realmObject).realmSet$fullName(((CommentRealmProxyInterface) newObject).realmGet$fullName());
            ((CommentRealmProxyInterface) realmObject).realmSet$avatar(((CommentRealmProxyInterface) newObject).realmGet$avatar());
            ((CommentRealmProxyInterface) realmObject).realmSet$body(((CommentRealmProxyInterface) newObject).realmGet$body());
            ((CommentRealmProxyInterface) realmObject).realmSet$createdAt(((CommentRealmProxyInterface) newObject).realmGet$createdAt());
            ((CommentRealmProxyInterface) realmObject).realmSet$taskId(((CommentRealmProxyInterface) newObject).realmGet$taskId());
            return realmObject;
        }
    }

    public static long insert(Realm realm, vn.tonish.hozo.model.Comment object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(vn.tonish.hozo.model.Comment.class);
        long tableNativePtr = table.getNativeTablePointer();
        CommentColumnInfo columnInfo = (CommentColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.Comment.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        String realmGet$id = ((CommentRealmProxyInterface)object).realmGet$id();
        if (realmGet$id != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
        }
        String realmGet$authorId = ((CommentRealmProxyInterface)object).realmGet$authorId();
        if (realmGet$authorId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.authorIdIndex, rowIndex, realmGet$authorId, false);
        }
        String realmGet$fullName = ((CommentRealmProxyInterface)object).realmGet$fullName();
        if (realmGet$fullName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
        }
        String realmGet$avatar = ((CommentRealmProxyInterface)object).realmGet$avatar();
        if (realmGet$avatar != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.avatarIndex, rowIndex, realmGet$avatar, false);
        }
        String realmGet$body = ((CommentRealmProxyInterface)object).realmGet$body();
        if (realmGet$body != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.bodyIndex, rowIndex, realmGet$body, false);
        }
        String realmGet$createdAt = ((CommentRealmProxyInterface)object).realmGet$createdAt();
        if (realmGet$createdAt != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.createdAtIndex, rowIndex, realmGet$createdAt, false);
        }
        String realmGet$taskId = ((CommentRealmProxyInterface)object).realmGet$taskId();
        if (realmGet$taskId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.taskIdIndex, rowIndex, realmGet$taskId, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(vn.tonish.hozo.model.Comment.class);
        long tableNativePtr = table.getNativeTablePointer();
        CommentColumnInfo columnInfo = (CommentColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.Comment.class);
        vn.tonish.hozo.model.Comment object = null;
        while (objects.hasNext()) {
            object = (vn.tonish.hozo.model.Comment) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                String realmGet$id = ((CommentRealmProxyInterface)object).realmGet$id();
                if (realmGet$id != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
                }
                String realmGet$authorId = ((CommentRealmProxyInterface)object).realmGet$authorId();
                if (realmGet$authorId != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.authorIdIndex, rowIndex, realmGet$authorId, false);
                }
                String realmGet$fullName = ((CommentRealmProxyInterface)object).realmGet$fullName();
                if (realmGet$fullName != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
                }
                String realmGet$avatar = ((CommentRealmProxyInterface)object).realmGet$avatar();
                if (realmGet$avatar != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.avatarIndex, rowIndex, realmGet$avatar, false);
                }
                String realmGet$body = ((CommentRealmProxyInterface)object).realmGet$body();
                if (realmGet$body != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.bodyIndex, rowIndex, realmGet$body, false);
                }
                String realmGet$createdAt = ((CommentRealmProxyInterface)object).realmGet$createdAt();
                if (realmGet$createdAt != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.createdAtIndex, rowIndex, realmGet$createdAt, false);
                }
                String realmGet$taskId = ((CommentRealmProxyInterface)object).realmGet$taskId();
                if (realmGet$taskId != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.taskIdIndex, rowIndex, realmGet$taskId, false);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, vn.tonish.hozo.model.Comment object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(vn.tonish.hozo.model.Comment.class);
        long tableNativePtr = table.getNativeTablePointer();
        CommentColumnInfo columnInfo = (CommentColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.Comment.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        String realmGet$id = ((CommentRealmProxyInterface)object).realmGet$id();
        if (realmGet$id != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.idIndex, rowIndex, false);
        }
        String realmGet$authorId = ((CommentRealmProxyInterface)object).realmGet$authorId();
        if (realmGet$authorId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.authorIdIndex, rowIndex, realmGet$authorId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.authorIdIndex, rowIndex, false);
        }
        String realmGet$fullName = ((CommentRealmProxyInterface)object).realmGet$fullName();
        if (realmGet$fullName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fullNameIndex, rowIndex, false);
        }
        String realmGet$avatar = ((CommentRealmProxyInterface)object).realmGet$avatar();
        if (realmGet$avatar != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.avatarIndex, rowIndex, realmGet$avatar, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.avatarIndex, rowIndex, false);
        }
        String realmGet$body = ((CommentRealmProxyInterface)object).realmGet$body();
        if (realmGet$body != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.bodyIndex, rowIndex, realmGet$body, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.bodyIndex, rowIndex, false);
        }
        String realmGet$createdAt = ((CommentRealmProxyInterface)object).realmGet$createdAt();
        if (realmGet$createdAt != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.createdAtIndex, rowIndex, realmGet$createdAt, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.createdAtIndex, rowIndex, false);
        }
        String realmGet$taskId = ((CommentRealmProxyInterface)object).realmGet$taskId();
        if (realmGet$taskId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.taskIdIndex, rowIndex, realmGet$taskId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.taskIdIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(vn.tonish.hozo.model.Comment.class);
        long tableNativePtr = table.getNativeTablePointer();
        CommentColumnInfo columnInfo = (CommentColumnInfo) realm.schema.getColumnInfo(vn.tonish.hozo.model.Comment.class);
        vn.tonish.hozo.model.Comment object = null;
        while (objects.hasNext()) {
            object = (vn.tonish.hozo.model.Comment) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                String realmGet$id = ((CommentRealmProxyInterface)object).realmGet$id();
                if (realmGet$id != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.idIndex, rowIndex, false);
                }
                String realmGet$authorId = ((CommentRealmProxyInterface)object).realmGet$authorId();
                if (realmGet$authorId != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.authorIdIndex, rowIndex, realmGet$authorId, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.authorIdIndex, rowIndex, false);
                }
                String realmGet$fullName = ((CommentRealmProxyInterface)object).realmGet$fullName();
                if (realmGet$fullName != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.fullNameIndex, rowIndex, false);
                }
                String realmGet$avatar = ((CommentRealmProxyInterface)object).realmGet$avatar();
                if (realmGet$avatar != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.avatarIndex, rowIndex, realmGet$avatar, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.avatarIndex, rowIndex, false);
                }
                String realmGet$body = ((CommentRealmProxyInterface)object).realmGet$body();
                if (realmGet$body != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.bodyIndex, rowIndex, realmGet$body, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.bodyIndex, rowIndex, false);
                }
                String realmGet$createdAt = ((CommentRealmProxyInterface)object).realmGet$createdAt();
                if (realmGet$createdAt != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.createdAtIndex, rowIndex, realmGet$createdAt, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.createdAtIndex, rowIndex, false);
                }
                String realmGet$taskId = ((CommentRealmProxyInterface)object).realmGet$taskId();
                if (realmGet$taskId != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.taskIdIndex, rowIndex, realmGet$taskId, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.taskIdIndex, rowIndex, false);
                }
            }
        }
    }

    public static vn.tonish.hozo.model.Comment createDetachedCopy(vn.tonish.hozo.model.Comment realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        vn.tonish.hozo.model.Comment unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (vn.tonish.hozo.model.Comment)cachedObject.object;
            } else {
                unmanagedObject = (vn.tonish.hozo.model.Comment)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new vn.tonish.hozo.model.Comment();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        }
        ((CommentRealmProxyInterface) unmanagedObject).realmSet$id(((CommentRealmProxyInterface) realmObject).realmGet$id());
        ((CommentRealmProxyInterface) unmanagedObject).realmSet$authorId(((CommentRealmProxyInterface) realmObject).realmGet$authorId());
        ((CommentRealmProxyInterface) unmanagedObject).realmSet$fullName(((CommentRealmProxyInterface) realmObject).realmGet$fullName());
        ((CommentRealmProxyInterface) unmanagedObject).realmSet$avatar(((CommentRealmProxyInterface) realmObject).realmGet$avatar());
        ((CommentRealmProxyInterface) unmanagedObject).realmSet$body(((CommentRealmProxyInterface) realmObject).realmGet$body());
        ((CommentRealmProxyInterface) unmanagedObject).realmSet$createdAt(((CommentRealmProxyInterface) realmObject).realmGet$createdAt());
        ((CommentRealmProxyInterface) unmanagedObject).realmSet$taskId(((CommentRealmProxyInterface) realmObject).realmGet$taskId());
        return unmanagedObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Comment = [");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id() != null ? realmGet$id() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{authorId:");
        stringBuilder.append(realmGet$authorId() != null ? realmGet$authorId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fullName:");
        stringBuilder.append(realmGet$fullName() != null ? realmGet$fullName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{avatar:");
        stringBuilder.append(realmGet$avatar() != null ? realmGet$avatar() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{body:");
        stringBuilder.append(realmGet$body() != null ? realmGet$body() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{createdAt:");
        stringBuilder.append(realmGet$createdAt() != null ? realmGet$createdAt() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{taskId:");
        stringBuilder.append(realmGet$taskId() != null ? realmGet$taskId() : "null");
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
        CommentRealmProxy aComment = (CommentRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aComment.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aComment.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aComment.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
