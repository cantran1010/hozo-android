package io.realm;


import android.util.JsonReader;
import io.realm.RealmObjectSchema;
import io.realm.internal.ColumnInfo;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Row;
import io.realm.internal.SharedRealm;
import io.realm.internal.Table;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

@io.realm.annotations.RealmModule
class DefaultRealmModuleMediator extends RealmProxyMediator {

    private static final Set<Class<? extends RealmModel>> MODEL_CLASSES;
    static {
        Set<Class<? extends RealmModel>> modelClasses = new HashSet<Class<? extends RealmModel>>();
        modelClasses.add(vn.tonish.hozo.model.Notification.class);
        modelClasses.add(vn.tonish.hozo.model.Comment.class);
        modelClasses.add(vn.tonish.hozo.model.NotificationMessage.class);
        modelClasses.add(vn.tonish.hozo.database.entity.UserEntity.class);
        modelClasses.add(vn.tonish.hozo.model.User.class);
        MODEL_CLASSES = Collections.unmodifiableSet(modelClasses);
    }

    @Override
    public RealmObjectSchema createRealmObjectSchema(Class<? extends RealmModel> clazz, RealmSchema realmSchema) {
        checkClass(clazz);

        if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
            return io.realm.NotificationRealmProxy.createRealmObjectSchema(realmSchema);
        }
        if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
            return io.realm.CommentRealmProxy.createRealmObjectSchema(realmSchema);
        }
        if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
            return io.realm.NotificationMessageRealmProxy.createRealmObjectSchema(realmSchema);
        }
        if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
            return io.realm.UserEntityRealmProxy.createRealmObjectSchema(realmSchema);
        }
        if (clazz.equals(vn.tonish.hozo.model.User.class)) {
            return io.realm.UserRealmProxy.createRealmObjectSchema(realmSchema);
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public ColumnInfo validateTable(Class<? extends RealmModel> clazz, SharedRealm sharedRealm, boolean allowExtraColumns) {
        checkClass(clazz);

        if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
            return io.realm.NotificationRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        }
        if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
            return io.realm.CommentRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        }
        if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
            return io.realm.NotificationMessageRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        }
        if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
            return io.realm.UserEntityRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        }
        if (clazz.equals(vn.tonish.hozo.model.User.class)) {
            return io.realm.UserRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public List<String> getFieldNames(Class<? extends RealmModel> clazz) {
        checkClass(clazz);

        if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
            return io.realm.NotificationRealmProxy.getFieldNames();
        }
        if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
            return io.realm.CommentRealmProxy.getFieldNames();
        }
        if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
            return io.realm.NotificationMessageRealmProxy.getFieldNames();
        }
        if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
            return io.realm.UserEntityRealmProxy.getFieldNames();
        }
        if (clazz.equals(vn.tonish.hozo.model.User.class)) {
            return io.realm.UserRealmProxy.getFieldNames();
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public String getTableName(Class<? extends RealmModel> clazz) {
        checkClass(clazz);

        if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
            return io.realm.NotificationRealmProxy.getTableName();
        }
        if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
            return io.realm.CommentRealmProxy.getTableName();
        }
        if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
            return io.realm.NotificationMessageRealmProxy.getTableName();
        }
        if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
            return io.realm.UserEntityRealmProxy.getTableName();
        }
        if (clazz.equals(vn.tonish.hozo.model.User.class)) {
            return io.realm.UserRealmProxy.getTableName();
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E newInstance(Class<E> clazz, Object baseRealm, Row row, ColumnInfo columnInfo, boolean acceptDefaultValue, List<String> excludeFields) {
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        try {
            objectContext.set((BaseRealm) baseRealm, row, columnInfo, acceptDefaultValue, excludeFields);
            checkClass(clazz);

            if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
                return clazz.cast(new io.realm.NotificationRealmProxy());
            }
            if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
                return clazz.cast(new io.realm.CommentRealmProxy());
            }
            if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
                return clazz.cast(new io.realm.NotificationMessageRealmProxy());
            }
            if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
                return clazz.cast(new io.realm.UserEntityRealmProxy());
            }
            if (clazz.equals(vn.tonish.hozo.model.User.class)) {
                return clazz.cast(new io.realm.UserRealmProxy());
            }
            throw getMissingProxyClassException(clazz);
        } finally {
            objectContext.clear();
        }
    }

    @Override
    public Set<Class<? extends RealmModel>> getModelClasses() {
        return MODEL_CLASSES;
    }

    @Override
    public <E extends RealmModel> E copyOrUpdate(Realm realm, E obj, boolean update, Map<RealmModel, RealmObjectProxy> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
            return clazz.cast(io.realm.NotificationRealmProxy.copyOrUpdate(realm, (vn.tonish.hozo.model.Notification) obj, update, cache));
        }
        if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
            return clazz.cast(io.realm.CommentRealmProxy.copyOrUpdate(realm, (vn.tonish.hozo.model.Comment) obj, update, cache));
        }
        if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
            return clazz.cast(io.realm.NotificationMessageRealmProxy.copyOrUpdate(realm, (vn.tonish.hozo.model.NotificationMessage) obj, update, cache));
        }
        if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
            return clazz.cast(io.realm.UserEntityRealmProxy.copyOrUpdate(realm, (vn.tonish.hozo.database.entity.UserEntity) obj, update, cache));
        }
        if (clazz.equals(vn.tonish.hozo.model.User.class)) {
            return clazz.cast(io.realm.UserRealmProxy.copyOrUpdate(realm, (vn.tonish.hozo.model.User) obj, update, cache));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public void insert(Realm realm, RealmModel object, Map<RealmModel, Long> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

        if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
            io.realm.NotificationRealmProxy.insert(realm, (vn.tonish.hozo.model.Notification) object, cache);
        } else if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
            io.realm.CommentRealmProxy.insert(realm, (vn.tonish.hozo.model.Comment) object, cache);
        } else if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
            io.realm.NotificationMessageRealmProxy.insert(realm, (vn.tonish.hozo.model.NotificationMessage) object, cache);
        } else if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
            io.realm.UserEntityRealmProxy.insert(realm, (vn.tonish.hozo.database.entity.UserEntity) object, cache);
        } else if (clazz.equals(vn.tonish.hozo.model.User.class)) {
            io.realm.UserRealmProxy.insert(realm, (vn.tonish.hozo.model.User) object, cache);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insert(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator<? extends RealmModel> iterator = objects.iterator();
        RealmModel object = null;
        Map<RealmModel, Long> cache = new HashMap<RealmModel, Long>(objects.size());
        if (iterator.hasNext()) {
            //  access the first element to figure out the clazz for the routing below
            object = iterator.next();
            // This cast is correct because obj is either
            // generated by RealmProxy or the original type extending directly from RealmObject
            @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

            if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
                io.realm.NotificationRealmProxy.insert(realm, (vn.tonish.hozo.model.Notification) object, cache);
            } else if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
                io.realm.CommentRealmProxy.insert(realm, (vn.tonish.hozo.model.Comment) object, cache);
            } else if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
                io.realm.NotificationMessageRealmProxy.insert(realm, (vn.tonish.hozo.model.NotificationMessage) object, cache);
            } else if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
                io.realm.UserEntityRealmProxy.insert(realm, (vn.tonish.hozo.database.entity.UserEntity) object, cache);
            } else if (clazz.equals(vn.tonish.hozo.model.User.class)) {
                io.realm.UserRealmProxy.insert(realm, (vn.tonish.hozo.model.User) object, cache);
            } else {
                throw getMissingProxyClassException(clazz);
            }
            if (iterator.hasNext()) {
                if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
                    io.realm.NotificationRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
                    io.realm.CommentRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
                    io.realm.NotificationMessageRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
                    io.realm.UserEntityRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(vn.tonish.hozo.model.User.class)) {
                    io.realm.UserRealmProxy.insert(realm, iterator, cache);
                } else {
                    throw getMissingProxyClassException(clazz);
                }
            }
        }
    }

    @Override
    public void insertOrUpdate(Realm realm, RealmModel obj, Map<RealmModel, Long> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
            io.realm.NotificationRealmProxy.insertOrUpdate(realm, (vn.tonish.hozo.model.Notification) obj, cache);
        } else if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
            io.realm.CommentRealmProxy.insertOrUpdate(realm, (vn.tonish.hozo.model.Comment) obj, cache);
        } else if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
            io.realm.NotificationMessageRealmProxy.insertOrUpdate(realm, (vn.tonish.hozo.model.NotificationMessage) obj, cache);
        } else if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
            io.realm.UserEntityRealmProxy.insertOrUpdate(realm, (vn.tonish.hozo.database.entity.UserEntity) obj, cache);
        } else if (clazz.equals(vn.tonish.hozo.model.User.class)) {
            io.realm.UserRealmProxy.insertOrUpdate(realm, (vn.tonish.hozo.model.User) obj, cache);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insertOrUpdate(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator<? extends RealmModel> iterator = objects.iterator();
        RealmModel object = null;
        Map<RealmModel, Long> cache = new HashMap<RealmModel, Long>(objects.size());
        if (iterator.hasNext()) {
            //  access the first element to figure out the clazz for the routing below
            object = iterator.next();
            // This cast is correct because obj is either
            // generated by RealmProxy or the original type extending directly from RealmObject
            @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

            if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
                io.realm.NotificationRealmProxy.insertOrUpdate(realm, (vn.tonish.hozo.model.Notification) object, cache);
            } else if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
                io.realm.CommentRealmProxy.insertOrUpdate(realm, (vn.tonish.hozo.model.Comment) object, cache);
            } else if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
                io.realm.NotificationMessageRealmProxy.insertOrUpdate(realm, (vn.tonish.hozo.model.NotificationMessage) object, cache);
            } else if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
                io.realm.UserEntityRealmProxy.insertOrUpdate(realm, (vn.tonish.hozo.database.entity.UserEntity) object, cache);
            } else if (clazz.equals(vn.tonish.hozo.model.User.class)) {
                io.realm.UserRealmProxy.insertOrUpdate(realm, (vn.tonish.hozo.model.User) object, cache);
            } else {
                throw getMissingProxyClassException(clazz);
            }
            if (iterator.hasNext()) {
                if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
                    io.realm.NotificationRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
                    io.realm.CommentRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
                    io.realm.NotificationMessageRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
                    io.realm.UserEntityRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(vn.tonish.hozo.model.User.class)) {
                    io.realm.UserRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else {
                    throw getMissingProxyClassException(clazz);
                }
            }
        }
    }

    @Override
    public <E extends RealmModel> E createOrUpdateUsingJsonObject(Class<E> clazz, Realm realm, JSONObject json, boolean update)
        throws JSONException {
        checkClass(clazz);

        if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
            return clazz.cast(io.realm.NotificationRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
            return clazz.cast(io.realm.CommentRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
            return clazz.cast(io.realm.NotificationMessageRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
            return clazz.cast(io.realm.UserEntityRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(vn.tonish.hozo.model.User.class)) {
            return clazz.cast(io.realm.UserRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E createUsingJsonStream(Class<E> clazz, Realm realm, JsonReader reader)
        throws IOException {
        checkClass(clazz);

        if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
            return clazz.cast(io.realm.NotificationRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
            return clazz.cast(io.realm.CommentRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
            return clazz.cast(io.realm.NotificationMessageRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
            return clazz.cast(io.realm.UserEntityRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(vn.tonish.hozo.model.User.class)) {
            return clazz.cast(io.realm.UserRealmProxy.createUsingJsonStream(realm, reader));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E createDetachedCopy(E realmObject, int maxDepth, Map<RealmModel, RealmObjectProxy.CacheData<RealmModel>> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) realmObject.getClass().getSuperclass();

        if (clazz.equals(vn.tonish.hozo.model.Notification.class)) {
            return clazz.cast(io.realm.NotificationRealmProxy.createDetachedCopy((vn.tonish.hozo.model.Notification) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(vn.tonish.hozo.model.Comment.class)) {
            return clazz.cast(io.realm.CommentRealmProxy.createDetachedCopy((vn.tonish.hozo.model.Comment) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(vn.tonish.hozo.model.NotificationMessage.class)) {
            return clazz.cast(io.realm.NotificationMessageRealmProxy.createDetachedCopy((vn.tonish.hozo.model.NotificationMessage) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(vn.tonish.hozo.database.entity.UserEntity.class)) {
            return clazz.cast(io.realm.UserEntityRealmProxy.createDetachedCopy((vn.tonish.hozo.database.entity.UserEntity) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(vn.tonish.hozo.model.User.class)) {
            return clazz.cast(io.realm.UserRealmProxy.createDetachedCopy((vn.tonish.hozo.model.User) realmObject, 0, maxDepth, cache));
        }
        throw getMissingProxyClassException(clazz);
    }

}
