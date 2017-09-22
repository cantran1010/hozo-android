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
    public static final int HTTP_CODE_BLOCK_USER = 423;
    public static final int MAX_IMAGE_ATTACH = 6;
    public static final String INTENT_EXTRA_ALBUM = "album_name";
    public static final int REQUEST_CODE_PICK_IMAGE = 123;
    public static final int RESPONSE_CODE_PICK_IMAGE = 321;
    public static final String INTENT_EXTRA_IMAGES = "extra_image";
    public static final int REQUEST_CODE_CAMERA = 567;
    public static final int REQUEST_CODE_ADDRESS = 2345;
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
    public static final int POST_A_TASK_REQUEST_CODE = 2468;
    public static final int POST_A_TASK_RESPONSE_CODE = 8642;
    public static final String COUNT_IMAGE_ATTACH_EXTRA = "extra_count_image";

    public static final String DB_NAME = "hozo";
    public static final String KEY_ENCRYPTION_DEFAULT = "abcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefgh";

    public static final String USER_ID = "id";
    public static final String USER_FULL_NAME = "full_name";
    public static final String USER_MOBILE = "phone";
    public static final String USER_OTP = "otp_code";

    public static final int PERMISSION_REQUEST_CODE = 987;
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
    public static final int RESULT_CODE_TASK_TYPE = 2020;
    public static final int REQUEST_CODE_COST = 2001;
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

    public static final String REVIEW_TYPE_POSTER = "poster";

    //update user parameter
    public static final String PARAMETER_FULL_NAME = "full_name";
    public static final String PARAMETER_ADDRESS = "address";
    public static final String PARAMETER_DATE_OF_BIRTH = "date_of_birth";
    public static final String PARAMETER_AVATA_ID = "avatar_id";
    public static final String PARAMETER_FACEBOOK_ID = "facebook_id";
    public static final String PARAMETER_EMAIL = "email";
    // Addvance setting
    public static final int REQUEST_CODE_TASK_TYPE = 456;

    public static final String PARAMETER_ACCEPTED_OFFER_USER_ID = "user_id";

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
    public static final String TASK_TYPE_POSTER_DRAFT = "draft";
    public static final String TASK_TYPE_BLOCK = "block";

    // parameter update device token
    public static final String UPDATE_TOKEN_DEVICE_TOKEN = "device_token";
    public static final String UPDATE_TOKEN_DEVICE_TYPE = "device_type";
    public static final String UPDATE_TOKEN_DEVICE_TYPE_ANDROID = "android";

    // push type notification
    public static final String PUSH_TYPE_REVIEW_RECEIVED = "review_received";
    public static final String PUSH_TYPE_COMMENT_RECEIVED = "comment_received";
    public static final String PUSH_TYPE_COMMENT_REPLIED = "comment_replied";
    public static final String PUSH_TYPE_BIDDER_CANCELED = "bidder_canceled";
    public static final String PUSH_TYPE_BID_RECEIVED = "bid_received";
    public static final String PUSH_TYPE_BID_ACCEPTED = "bid_accepted";
    public static final String PUSH_TYPE_BID_CANCEL = "poster_cancel_bid";
    public static final String PUSH_TYPE_TASK_REMINDER = "task_reminder";
    public static final String PUSH_TYPE_NEW_TASK_ALERT = "new_task_alert";
    public static final String PUSH_TYPE_POSTER_CANCELED = "poster_canceled";
    public static final String PUSH_TYPE_TASK_COMPLETE = "task_completed";
    public static final String PUSH_TYPE_TASK_OVERDUE = "task_overdue";
    public static final String PUSH_TYPE_BID_MISSED = "bid_missed";
    public static final String PUSH_TYPE_TASK_REOPEN = "task_reopen";
    public static final String PUSH_TYPE_ADMIN_PUSH = "admin_push";
    public static final String PUSH_TYPE_BLOCK_USER = "block_user";
    public static final String PUSH_TYPE_ACTIVE_USER = "active_user";
    public static final String PUSH_TYPE_BLOCK_TASK = "block_task";
    public static final String PUSH_TYPE_ACTIVE_TASK = "active_task";
    public static final String PUSH_TYPE_BLOCK_COMMENT = "block_comment";
    public static final String PUSH_TYPE_ACTIVE_COMMENT = "active_comment";
    public static final String PUSH_TYPE_ADMIN_NEW_TASK_ALERT = "admin_new_task_alert";
    public static final String PUSH_TYPE_CHAT = "message_received";

    public static final String ROLE_EXTRA = "role_extra";
    public static final String REFRESH_EXTRA = "refresh_extra";


    public static final int RESULT_RADIUS = 1015;
    public static final String EXTRA_RADIUS = "radius";
    public static final int REQUEST_CODE_RADIUS = 1017;
    public static final String REQUEST_EXTRAS_RADIUS = "extra_radius";
    public static final int REQUEST_CODE_SETTING = 1018;
    public static final int RESULT_CODE_SETTING = 1019;
    public static final String ASSIGNER_RATE_EXTRA = "assign_rate_extra";
    public static final String CATEGORY_ACTIVE = "active";
    public static final int REQUEST_CODE_RATE = 345;
    public static final int RESPONSE_CODE_RATE = 543;
    public static final long MAX_FILE_SIZE = 5; //MB
    public static final String COMMENT_VISIBILITY = "comment_visibility";
    public static final String EXTRA_SEND_COMMENT = "send_comment";
    public static final int RESULT_CODE_SEND_COMMENT = 1021;
    public static final int REQUEST_CODE_SEND_COMMENT = 1022;
    public static final String BLOCK_EXTRA = "block_extra";
    public static final String BROAD_CAST_BLOCK_USER = "vn.tonish.hozo.block_user";
    public static final String BROAD_CAST_MY = "MyBroadcast";
    public static final String LOGOUT_EXTRA = "logout_extra";
    public static final String BROAD_CAST_PUSH_COUNT = "push_count";
    public static final String PARAMETER_DESCRIPTION = "description";
    public static final String MYTASK_FILTER_EXTRA = "mytask_filter_extra";
    public static final int RESULT_CODE_TASK_DELETE = 642;
    public static final String ASSIGNER_CANCEL_BID_EXTRA = "cancel_bid_extra";
    public static final int REQUEST_CODE_VERIFY = 789;
    public static final int RESULT_CODE_VERIFY = 987;
    public static final String COUNT_NEW_TASK_EXTRA = "count_new_task_extra";
    public static final String EXTRA_ADDRESS = "address_extra";
    public static final String LAT_EXTRA = "lat_extra";
    public static final int RESULT_CODE_ADDRESS = 5432;
    public static final String CONTENT_INFO_EXTRA = "content_extra";
    public static final String TASK_DETAIL_EXTRA = "task_detail_extra";
    public static final String AVATAR_EXTRA = "avatar_extra";
    public static final String NAME_EXTRA = "name_extra";
    public static final String TASK_RESPONSE_EXTRA = "task_response";
    public static final int REQUEST_CODE_SEND_BINDDER = 1115;
    public static final String BIDDER_TYPE_EXTRA = "bidder_type";
    public static final String EXTRA_BIDDER_TASKRESPONSE = "bidder_taskresponse";
    public static final int RESULT_CODE_BIDDER = 1116;
    public static final String ASSIGNER_TYPE_EXTRA = "assigner_type";
    public static final int REQUEST_CODE_SEND_ASSIGNER = 1117;
    public static final String EXTRA_ASSIGNER_TASKRESPONSE = "assigner_taskresponse";
    public static final int RESULT_CODE_ASSIGNER = 1118;
    public static final String POST_TASK_DUPLICATE = "duplicate_task";
    public static final int COMMENT_REQUEST_CODE = 258;
    public static final int COMMENT_RESPONSE_CODE = 852;
    public static final String COMMENT_INPUT_EXTRA = "comment_input_extra";
    public static final String CREATE_TASK_STATUS_PUBLISH = "publish";
    public static final String CREATE_TASK_STATUS_DRAFT = "draft";
    public static final String DUPLICATE_COMMENT = "duplicate_comment";
    public static final String PUSH_COUNT_EXTRA = "push_count";
    public static final String COUNT_COMMENT = "count_comment";
    public static final String COUNT_BIDDER = "count_bidder";
    public static final String BIDDER_EXTRA = "bidder_extra";
    public static final String VN1 = ", Việt Nam";
    public static final String VN2 = ", Vietnam";
    public static final String COUNT_NEW_MSG_EXTRA = "count_new_msg_extra";
    public static String BID_ERROR_SAME_TIME = "no_permission_offer";
    public static String BID_ERROR_INVALID_DATA = "invalid_data";
    public static String BID_LIMIT_OFFER = "limit_offer";
    public static String TASK_DETAIL_INPUT_REQUIRE = "input_required";
    public static String TASK_DETAIL_NO_EXIT = "no_exist";
    public static final String TASK_DETAIL_BLOCK = "block";
    public static String LON_EXTRA = "lon_extra";

    public static final String OFFER_ACTIVE = "OFFER_ACTIVE";
    public static final String OFFER_PENDING = "OFFER_PENDING";
    public static final String OFFER_CALL = "OFFER_CALL";
    public static final String OFFER_GONE = "OFFER_GONE";
    public static final String OFFER_RATTING = "OFFER_RATTING";

    public static final String TASK_EDIT_EXTRA = "task_extra";

    public static final String TASK_COPY = "task_copy";
    public static final String TASK_EDIT = "task_edit";
    public static final String TASK_DRAFT = "task_draft";
    public static final String BROAD_CAST_COUNT_NEW_MSG = "broad_cast_new_msg";
}