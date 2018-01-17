package vn.tonish.hozo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import vn.tonish.hozo.common.Constants;

/**
 * Created by LongBui.
 */
public class PreferUtils {

    private static final String PREFER_NAME = "hozo";

    private static final String PREFER_USER_ID = "user_id";
    private static final String PREFER_HOME_LIST = "home_list";
    private static final String PREFER_SESSION = "session";
    private static final String PREFER_STORE_DB = "store_db";
    private static final String PREFER_PUSH_CHAT = "push_chat";
    private static final String PREFER_IS_LOGIN = "is_login";
    private static final String PREFER_IS_AUTO_LOGIN = "auto_login";
    private static final String PREFER_NEW_PUSH_COUNT = "new_push_count";
    private static final String PREFER_NEW_PUSH_CHAT_COUNT = "new_push_chat_count";
    private static final String PREFER_PUSH_NEW_TASK_COUNT = "new_task_push_count";

    private static final String PREFER_PUSH_ACCEPT = "push_accept";
    private static final String PREFER_PUSH_DENY = "push_deny";
    private static final String PREFER_PUSH_SHOW = "push_show_notification_chat";
    private static final String KEY_ENCRYPTION = "key";
    private static final String LAST_TIME_COUNT_TASK = "last_time";

    public SharedPreferences preferences;

    // just example method
    public static void setMessage() {

    }

    public static boolean isPushShow(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_PUSH_SHOW, true);
    }

    public static void setPushShow(Context context, boolean isPush) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_PUSH_SHOW, isPush);
        editor.apply();
    }

    public static boolean isPushPrivateShow(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_PUSH_SHOW, true);
    }

    public static void setPushPrivateShow(Context context, boolean isPush) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_PUSH_SHOW, isPush);
        editor.apply();
    }
    public static boolean isPushDeny(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_PUSH_DENY, false);
    }

    public static void setPushDeny(Context context, boolean isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_PUSH_DENY, isLogin);
        editor.apply();
    }

    public static boolean isPushAccept(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_PUSH_ACCEPT, false);
    }

    public static void setPushAccept(Context context, boolean isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_PUSH_ACCEPT, isLogin);
        editor.apply();
    }

    public static boolean isAutoLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_IS_AUTO_LOGIN, false);
    }

    public static void setIsAutoLogin(Context context, boolean isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_IS_AUTO_LOGIN, isLogin);
        editor.apply();
    }

    public static boolean isLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_IS_LOGIN, true);
    }

    public static void setIsLogin(Context context, boolean isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_IS_LOGIN, isLogin);
        editor.apply();
    }

    public static void setHomeList(Context context, boolean isList) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_HOME_LIST, isList);
        editor.apply();
    }

    public static boolean isHomeList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_HOME_LIST, true);
    }

    public static boolean isStoreDb(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_STORE_DB, true);
    }

    public static void setStoreDb(Context context, boolean isStore) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_STORE_DB, isStore);
        editor.apply();
    }

    public static boolean isPushChat(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_PUSH_CHAT, true);
    }

    public static void setPushChat(Context context, boolean isPush) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_PUSH_CHAT, isPush);
        editor.apply();
    }

    public static void setUserId(Context context, int userId) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(PREFER_USER_ID, userId);
        editor.apply();
    }

    public static int getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(PREFER_USER_ID, 0);
    }

    public static void setSession(Context context, String session) {
        SharedPreferences prefs = context.getSharedPreferences(
                PREFER_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(PREFER_SESSION, session);
        editor.apply();
    }

    public static String getSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PREFER_SESSION, "");
    }

    public static void setKeyEncryption(Context context, String key) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_ENCRYPTION, key);
        editor.apply();
    }

    public static String getKeyEncryption(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_ENCRYPTION, Constants.KEY_ENCRYPTION_DEFAULT);
    }

    public static void setPushNewTaskCount(Context context, int count) {

        if (context == null) return;
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(PREFER_PUSH_NEW_TASK_COUNT, count);
        editor.apply();
    }

    public static int getPushNewTaskCount(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(PREFER_PUSH_NEW_TASK_COUNT, 0);
    }

    public static void setNewPushCount(Context context, int count) {
        if (context == null) return;
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(PREFER_NEW_PUSH_COUNT, count);
        editor.apply();
    }

    public static int getNewPushCount(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(PREFER_NEW_PUSH_COUNT, 0);
    }

    public static void setNewPushChatCount(Context context, int count) {
        if (context == null) return;
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(PREFER_NEW_PUSH_CHAT_COUNT, count);
        editor.apply();
    }

    public static int getNewPushChatCount(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(PREFER_NEW_PUSH_CHAT_COUNT, 0);
    }

    public static void setLastTimeCountTask(Context context, String lastTime) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(LAST_TIME_COUNT_TASK, lastTime);
        editor.apply();
    }

    public static String getLastTimeCountTask(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getString(LAST_TIME_COUNT_TASK, "");
    }

}