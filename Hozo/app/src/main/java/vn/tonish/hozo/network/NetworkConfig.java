package vn.tonish.hozo.network;

public class NetworkConfig {

    public static int NETWORK_TIME_OUT = 10000;
    public static String NETWORK_API_KEY = "54697c49-9cac-11e6-9095-001279d65bdf";

    //==================== DOMAIN =====================
    public static String DOMAIN = "http://api.veteranscout.co.kr/";

    //==================== API=========================
    public static final String API_LOGIN = DOMAIN + "corp_user/api/login";
    public static final String API_LOGIN_TOKEN = DOMAIN + "corp_user/api/login_mobile_regid";
    public static final String API_CHANGE_PASSWORD = DOMAIN + "corp_user/api/update_pwd";
    public static final String API_UPDATE_TT = DOMAIN + "corp_user/api/update_mobile";
    public static final String API_CORP_INFO = DOMAIN + "corp_user/api/corp_info";

}
