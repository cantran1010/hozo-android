package vn.tonish.hozo.common;

public class Constants {

    //http code
    public static final int HTTP_CODE_OK = 200;
    public static final int HTTP_CODE_CREATED = 201;
    public static final int HTTP_CODE_NO_CONTENT = 204;
    public static final int HTTP_CODE_BAD_REQUEST = 400;
    public static final int HTTP_CODE_UNAUTHORIZED = 401;
    public static final int HTTP_CODE_FORBIDEN = 403;
    public static final int HTTP_CODE_NOT_FOUND = 404;
    public static final int HTTP_CODE_UNPROCESSABLE_ENTITY = 422;
    public static final int HTTP_CODE_INTERNAL_SERVER_ERROR = 500;

    public static final String BADGE = "badge____";
    public static final int MAX_IMAGE_ATTACH = 6;

    public static final String INTENT_EXTRA_ALBUM = "album_name";
    public static final int REQUEST_CODE_PICK_IMAGE = 123;
    public static final int RESPONSE_CODE_PICK_IMAGE = 321;
    public static final String INTENT_EXTRA_IMAGES = "extra_image";
    public static final int REQUEST_CODE_CAMERA = 567;
    public static final int REQUEST_CODE_ADDRESS = 2345;
    public static final int RESPONSE_CODE_ADDRESS = 1234;
    public static final String EXTRA_ADDRESS = "extra_address";
    public static final String EXTRA_ONLY_IMAGE = "extra_only_image";
    public static final String ERROR_AUTHENTICATION = "java.io.IOException: No authentication challenges found";
    public static final String EXTRA_IS_CROP_PROFILE = "extra_is_crop_profile";
    public static final String EXTRA_IMAGE_PATH = "extra_image_path";
    public static final int REQUEST_CODE_CROP_IMAGE = 123;
    public static final int RESPONSE_CODE_CROP_IMAGE = 321;

    public static final String EXTRA_CATEGORY = "extra_category";
    public static final String EXTRA_TASK = "extra_work";
    public static final int RESULT_CODE_UPDATE_PROFILE = 876;
    public static final int REQUEST_CODE_UPDATE_PROFILE = 678;
    public static final float DEFAULT_MAP_ZOOM_LEVEL = 16f;
    public static final int POST_A_TASK_REQUEST_CODE = 246;
    public static final int POST_A_TASK_RESPONSE_CODE = 642;
    public static final String COUNT_IMAGE_ATTACH_EXTRA = "extra_count_image";

    public static final boolean DEBUG = true;
    public static final int SPLASH_TIME = 1500;
    public static final String DB_NAME = "hozo";
    public static final String KEY_ENCRYPTION_DEFAULT = "abcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefgh";

    public static final String OTP_VIEW = "otp_view";
    public static final String NAME_VIEW = "name_view";

    public static final String REGISTER = "register";
    public static final String TOCKEN = "token";
    public static final String ACCESS_TOCKEN = "access_token";
    public static final String REFRESH_TOCKEN = "refresh_token";
    public static final String EXP_TOCKEN = "token_exp";
    public static final String USER_ID = "id";
    public static final String USER_FULL_NAME = "full_name";
    public static final String USER_MOBILE = "phone";
    public static final String USER_OTP = "otp_code";
    public static final String USER_LOGIN_AT = "login_at";
    public static final String USER_POSTER_RATING = "poster_rating";
    public static final String USER_TASKER_RATING = "tasker_rating";


    // request http code here
    public static final int REQUEST_SUCCESSFUL = 0;

    // Json Constant here / :)

    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String DATA = "data";
    public static final String USER = "user";

    public static final int PERMISSION_REQUEST_CODE = 987;
    public static final int REQUEST_CODE_ADD_VERIFY = 1019;
    public static final String TRANSITION_EXTRA = "transition_extra";
    public static final String IMAGE_ATTACHS_EXTRA = "image_attachs_extra";
    public static final String IMAGE_POSITITON_EXTRA = "position_extra";
    public static final String COMMENT_EXTRA = "comment_extra";
    public static final String EXTRA_CATEGORY_ID = "Category_id";
    public static final String EXTRA_MIN_PRICE = "min_price";
    public static final String EXTRA_MAX_PRICE = "max_price";
    public static final String TASK_ID_EXTRA = "task_id_extra";
    public static final String USER_ID_EXTRA = "user_id_extra";
    public static final String COMMENT_ID_EXTRA = "comment_id_extra";
    public static final String TASK_STATUS_EXTRA = "task_status_extra";
    public static final String URL_EXTRA = "url_extra";
    public static final String TITLE_INFO_EXTRA = "title_info_extra";
    public static final String LIST_TASK_EXTRA = "list_task_extra";
    public static final String IS_MY_USER = "is_my_user";
    public static final String PARAMETER_GENDER = "gender";
    public static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 876;

    public static final String USER_POSTER_REVIEWS = "poster_reviews";
    public static final String USER_TASKER_REVIEWS = "tasker_reviews";


    public static final String REVIEW_TYPE_TASKER = "tasker";
    public static final String REVIEW_TYPE_POSTER = "poster";

    //update user parameter
    public static final String PARAMETER_FULL_NAME = "full_name";
    public static final String PARAMETER_EMAIL = "email";
    public static final String PARAMETER_ADDRESS = "address";
    public static final String PARAMETER_DATE_OF_BIRTH = "date_of_birth";
    public static final String PARAMETER_AVATA_ID = "avatar_id";
    // Addvance setting
    public static final int REQUEST_CODE_TASK_TYPE = 456;
    public static final int REQUEST_CODE_SETTING_PRICE = 406;


    // accept offer
    public static final String PARAMETER_ACCEPT_OFFER = "status";
    public static final String PARAMETER_ACCEPTED_OFFER = "accepted";
    public static final String PARAMETER_ACCEPTED_OFFER_USER_ID = "user_id";

    //update task
    public static final String PARAM_UPDATE_TASK = "status";
    public static final String PARAM_UPDATE_TASK_CANCEL = "canceled";

    //task status
    public static final String TASK_STATUS_OPEN = "open";
    public static final String TASK_STATUS_ASSIGNED = "assigned";
    public static final String TASK_STATUS_COMPLETED = "completed";

    // my task role
    public static final String ROLE_WORKER = "worker";
    public static final String ROLE_POSTER = "poster";

    // post task
    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";

}