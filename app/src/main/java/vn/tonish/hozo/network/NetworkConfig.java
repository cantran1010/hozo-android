package vn.tonish.hozo.network;

public class NetworkConfig {

    public static int NETWORK_TIME_OUT = 10000;

    //==================== DOMAIN =====================
    public static String DOMAIN = "http://104.198.92.15:8080/v1/";

    //==================== API=========================
    public static final String API_LOGIN = DOMAIN + " /auth/login";
    public static final String API_OTP = DOMAIN + "auth/otp_code";
    public static final String API_NAME = DOMAIN + "/user/verify_name";


}
