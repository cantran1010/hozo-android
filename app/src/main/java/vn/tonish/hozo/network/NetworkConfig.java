package vn.tonish.hozo.network;
/**
 * Created by LongBui.
 */
public class NetworkConfig {

    public static final int NETWORK_TIME_OUT = 10000;

    //==================== DOMAIN =====================
    public static final String DOMAIN = "http://104.198.92.15:8080/v1/";

    //==================== API=========================
    public static final String API_LOGIN = DOMAIN + "auth/login";
    public static final String API_OTP = DOMAIN + "auth/otp_code";
    public static final String API_NAME = DOMAIN + "user/verify_name";
    public static final String API_REFRESH_TOKEN = DOMAIN + "auth/refresh_token";
    public static final String API_LOGOUT = DOMAIN + "user/logout";


}
