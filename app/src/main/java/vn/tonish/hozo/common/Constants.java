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
    public static final int MAX_IMAGE_ATTACH = 6;
    public static final String INTENT_EXTRA_ALBUM = "album_name";
    public static final int REQUEST_CODE_PICK_IMAGE = 123;
    public static final int RESPONSE_CODE_PICK_IMAGE = 321;
    public static final String INTENT_EXTRA_IMAGES = "extra_image";
    public static final int REQUEST_CODE_CAMERA = 567;
    public static final int REQUEST_CODE_ADDRESS = 2345;
    public static final String EXTRA_ADDRESS = "extra_address";
    public static final String EXTRA_ONLY_IMAGE = "extra_only_image";
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
    public static final int SPLASH_TIME = 1000;
    public static final String DB_NAME = "hozo";
    public static final String KEY_ENCRYPTION_DEFAULT = "abcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefgh";

    public static final String USER_ID = "id";
    public static final String USER_FULL_NAME = "full_name";
    public static final String USER_MOBILE = "phone";
    public static final String USER_OTP = "otp_code";


    // Json Constant here / :)

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
    public static final String COMMENT_STATUS_EXTRA = "task_status_extra";
    public static final String USER_ID_EXTRA = "user_id_extra";
    public static final String COMMENT_ID_EXTRA = "comment_id_extra";
    public static final String URL_EXTRA = "url_extra";
    public static final String TITLE_INFO_EXTRA = "title_info_extra";
    public static final String LIST_TASK_EXTRA = "list_task_extra";
    public static final String IS_MY_USER = "is_my_user";
    public static final String PARAMETER_GENDER = "gender";
    public static final int DURATION = 200;
    public static final String ROLE_FIND_TASK = "find_task";
    public static final int RESULT_CODE_COST = 1999;
    public static final int RESULT_CODE_TASK_TYPE =2020 ;
    public static final int REQUEST_CODE_COST = 2001;
//    public static final String TASK_TYPE = "task_type";
    public static final int RESULT_CODE_TASK_EDIT = 234;
    public static final int REQUEST_CODE_TASK_EDIT = 432;
    public static final String NOTIFICATION_EXTRA = "notification_extra";
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 876;

    public static final String BROAD_CAST_SMOOTH_TOP_INBOX = "broad_cast_smooth_top_inbox";
    public static final String BROAD_CAST_SMOOTH_TOP_SEARCH = "broad_cast_smooth_top_search";
    public static final String BROAD_CAST_SMOOTH_TOP_MY_TASK = "broad_cast_smooth_top_my_task";
    public static final String BROAD_CAST_SMOOTH_TOP_MY_TASK_WORKER = "broad_cast_smooth_top_mytask_worker";
    public static final String BROAD_CAST_SMOOTH_TOP_MY_TASK_POSTER = "broad_cast_smooth_top_mytask_poster";
    public static final String BROAD_CAST_REFRESH_CATEGORY = "broad_cast_refresh_category";

    public static final String USER_POSTER_REVIEWS = "poster_reviews";
    public static final String USER_TASKER_REVIEWS = "tasker_reviews";


    public static final String REVIEW_TYPE_POSTER = "poster";

    //update user parameter
    public static final String PARAMETER_FULL_NAME = "full_name";
    public static final String PARAMETER_ADDRESS = "address";
    public static final String PARAMETER_DATE_OF_BIRTH = "date_of_birth";
    public static final String PARAMETER_AVATA_ID = "avatar_id";
    // Addvance setting
    public static final int REQUEST_CODE_TASK_TYPE = 456;

    public static final String PARAMETER_ACCEPTED_OFFER_USER_ID = "user_id";

    //    //task status
//    public static final String OFFER_STATUS_PENDING = "pending";
//    public static final String OFFER_STATUS_ACCEPTED = "accepted";
//    public static final String OFFER_STATUS_MISSED = "missed";
//    public static final String OFFER_STATUS_CANCELED = "canceled";

//    //offer status
//    public static final String TASK_STATUS_OPEN = "open";
//    public static final String TASK_STATUS_ASSIGNED = "assigned";
//    public static final String TASK_STATUS_COMPLETED = "completed";

    // my task role
    public static final String ROLE_TASKER = "tasker";
    public static final String ROLE_POSTER = "poster";

    // post task
    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";

    //task type
    public static final String TASK_TYPE_BIDDER_PENDING = "pending";
    public static final String TASK_TYPE_BIDDER_ACCEPTED = "accepted";
    public static final String TASK_TYPE_BIDDER_MISSED = "missed";
    public static final String TASK_TYPE_BIDDER_CANCELED = "canceled";

    public static final String TASK_TYPE_POSTER_OPEN = "open";
    public static final String TASK_TYPE_POSTER_ASSIGNED = "assigned";
    public static final String TASK_TYPE_POSTER_COMPLETED = "completed";
    public static final String TASK_TYPE_POSTER_OVERDUE = "overdue";
    public static final String TASK_TYPE_POSTER_CANCELED = "canceled";


    // parameter update device token
    public static final String UPDATE_TOKEN_DEVICE_TOKEN = "device_token";
    public static final String UPDATE_TOKEN_DEVICE_TYPE = "device_type";
    public static final String UPDATE_TOKEN_DEVICE_TYPE_ANDROID = "android";

    public static final String PUSH_TITLE_EXTRA = "push_info_title";
    public static final String PUSH_MESSAGE_EXTRA = "push_info_messageß";

    // push type notification
    public static final String PUSH_TYPE_REVIEW_RECEIVED = "review_received";
    public static final String PUSH_TYPE_COMMENT_RECEIVED = "comment_received";
    public static final String PUSH_TYPE_BIDDER_CANCELED = "bidder_canceled";
    public static final String PUSH_TYPE_BID_RECEIVED = "bid_received";
    public static final String PUSH_TYPE_BID_ACCEPTED = "bid_accepted";
    public static final String PUSH_TYPE_TASK_REMINDER = "task_reminder";
    public static final String PUSH_TYPE_NEW_TASK_ALERT = "new_task_alert";
    public static final String PUSH_TYPE_POSTER_CANCELED = "poster_canceled";
    public static final String PUSH_TYPE_ADMIN_PUSH = "admin_push";
    public static final String PUSH_TYPE_TASK_COMPLETE = "task_completed";
    public static final String PUSH_TYPE_TASK_OVERDUE = "task_overdue";

    public static final String ROLE_EXTRA = "role_extra";
    public static final String REFRESH_EXTRA = "refresh_extra";


    public static final int RESULT_RADIUS = 1015;
    public static final String EXTRA_RADIUS ="radius" ;
    public static final int REQUEST_CODE_RADIUS = 1017;
    public static final String REQUEST_EXTRAS_RADIUS ="extra_radius" ;
}