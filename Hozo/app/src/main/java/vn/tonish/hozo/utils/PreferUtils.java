package vn.tonish.hozo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import vn.tonish.hozo.common.Constants;

/**
 *
 */
public class PreferUtils {

    private static final String PREFER_NAME = "veteran";

    private static final String PREFER_USER_ID = "user_id";
    private static final String PREFER_HOME_LIST = "home_list";
    private static final String PREFER_SESSION = "session";
    private static final String PREFER_STORE_DB = "store_db";
    private static final String PREFER_PUSH_CHAT = "push_chat";
    private static final String PREFER_IS_LOGIN = "is_login";
    private static final String PREFER_IS_AUTO_LOGIN = "auto_login";

    private static final String PREFER_PUSH_ACCEPT = "push_accept";
    private static final String PREFER_PUSH_DENY = "push_deny";
    private static final String PREFER_PUSH_SHOW = "push_show";
    private static final String PREFER_PUSH_NOSHOW = "push_noshow";
    private static final String PREFER_PUSH_MESSAGE = "push_message";
    private static final String KEY_ENCRYPTION = "kenc";

    public static boolean isPushMessage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_PUSH_MESSAGE, false);
    }

    public static void setPushMessage(Context context, boolean isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_PUSH_MESSAGE, isLogin);
        editor.commit();
    }

    public static boolean isPushNoShow(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_PUSH_NOSHOW, false);
    }

    public static void setPushNoShow(Context context, boolean isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_PUSH_NOSHOW, isLogin);
        editor.commit();
    }

    public static boolean isPushShow(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_PUSH_SHOW, false);
    }

    public static void setPushShow(Context context, boolean isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_PUSH_SHOW, isLogin);
        editor.commit();
    }

    public static boolean isPushDeny(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_PUSH_DENY, false);
    }

    public static void setPushDeny(Context context, boolean isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_PUSH_DENY, isLogin);
        editor.commit();
    }

    public static boolean isPushAccept(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_PUSH_ACCEPT, false);
    }

    public static void setPushAccept(Context context, boolean isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_PUSH_ACCEPT, isLogin);
        editor.commit();
    }

    public static boolean isAutoLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_IS_AUTO_LOGIN, false);
    }

    public static void setIsAutoLogin(Context context, boolean isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_IS_AUTO_LOGIN, isLogin);
        editor.commit();
    }

    public static boolean isLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_IS_LOGIN, true);
    }

    public static void setIsLogin(Context context, boolean isLogin) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_IS_LOGIN, isLogin);
        editor.commit();
    }

    public static void setHomeList(Context context, boolean isList) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_HOME_LIST, isList);
        editor.commit();
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
        editor.commit();
    }

    public static boolean isPushChat(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREFER_PUSH_CHAT, true);
    }

    public static void setPushChat(Context context, boolean isPush) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFER_PUSH_CHAT, isPush);
        editor.commit();
    }

    public static void setUserId(Context context, int userId) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(PREFER_USER_ID, userId);
        editor.commit();
    }

    public static int getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(PREFER_USER_ID, 0);
    }

    public static final void setSession(Context context, String session) {
        SharedPreferences prefs = context.getSharedPreferences(
                PREFER_NAME, Context.MODE_MULTI_PROCESS);
        Editor editor = prefs.edit();
        editor.putString(PREFER_SESSION, session);
        editor.commit();
    }

    public static String getSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PREFER_SESSION, "");
    }

    public static void setKeyEncryption(Context context, String key) {
        Editor editor = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_ENCRYPTION, key);
        editor.commit();
    }

    public static String getKeyEncryption(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_ENCRYPTION, Constants.KEY_ENCRYPTION_DEFAULT);
    }

}