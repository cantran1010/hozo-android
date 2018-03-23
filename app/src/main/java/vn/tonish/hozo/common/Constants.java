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
    public static final String EXTRA_MY_TASK = "extra_status_mytask";
    public static final int REQUEST_CODE_FILTER_MY_TASK = 474;
    public static final int FILTER_MY_TASK_RESPONSE_CODE = 476;
    public static final int REQUEST_CODE_CAMERA_AVATA = 987;
    public static final int REQUEST_CODE_PICK_IMAGE_AVATA = 654;
    public static final int REQUEST_CODE_SKILL = 741;
    public static final int RESULT_CODE_TAG = 136;
    public static final String REQUEST_CODE_EXTRA = "request_code_extra";
    public static final int REQUEST_CODE_LANGUAGE = 456;
    public static final int PERMISSION_REQUEST_CODE_BACKGROUND = 238;
    public static final int REQUEST_CODE_CAMERA_BACKGROUND = 369;
    public static final int REQUEST_CODE_PICK_IMAGE_BACKGROUND = 147;
    public static final String ASSIGNER_CONTACT_EXTRA = "assigner_contact_extra";
    public static final String BROAD_CAST_SMOOTH_TOP_SYS_TEM = "broad_cast_smooth_top_notification_system";
    public static final String BROAD_CAST_SMOOTH_TOP_NEW_TASK = "broad_cast_smooth_top_notification_new_task";
    public static final String BROAD_CAST_SMOOTH_TOP_CHAT = "broad_cast_smooth_top_notification_chat";
    public static final String BROAD_CAST_SMOOTH_TOP_NOTIFICATION = "broad_cast_smooth_top_notification";
    public static final String REFERRER_PHONE_YOURSELF = "referrer_phone_yourself";
    public static final String INVALID_REFERRER_PHONE = "invalid_referrer_phone";
    public static final String NO_EXIST = "no_exist";
    public static final String SYSTEM_ERROR = "system_error";
    public static final String TASK_RESPONSE_RATING = "task_respone_rating";
    public static final String ORDER_BY_DISTANCE = "distance";
    public static final String ORDER_BY_INTTEREST = "interest";
    public static final String ORDER_ASC = "asc";
    public static final String ORDER_DESC = "desc";
    public static final String POSTER_ID_EXTRA = "poster_extra";
    public static final int PROMOTION_REQUEST_CODE = 456;
    public static final int PROMOTION_RESULT_CODE = 654;
    public static final String PUSH_TYPE_ASSIGNER_CANCELED = "assigner_canceled";
    public static final String EVENT_NOTIFICATION_EXTRA = "notification_extra";
    public static final int MAX_LENGTH_TITLE = 50;
    public static final int MAX_LENGTH_DES = 500;
    public static final int MIN_LENGTH_TITLE = 10;
    public static final int MIN_LENGTH_DES = 25;
    public static final int RESULT_CODE_TASK_CANCEL = 77;
    public static final int BID_REQUEST_CODE = 741;
    public static final int BID_RESPONSE_CODE = 147;
    public static final String OFFER_TASK_ID = "task_id";
    public static final String BIDDER_ID = "bidder_id";
    public static final String PARAMETER_OFFER_PRICE = "price";
    public static final String PARAMETER_OFFER_SMS = "message";
    public static final String LOCATION_TASK = "location_task";
    public static final String ASSSIGNER_COUNT = "assigner_count";
    public static final String WORKER_COUNT = "assigner_amount";
    public static final int BANK_REQUEST_CODE = 987;
    public static final String BANK_EXTRA = "bank_extra";
    public static final int BANK_RESULT_CODE = 789;
    public static final int EDIT_BANK_REQUEST_CODE = 159;
    public static final String BANK_EDIT_EXTRA = "bank_edit_extra";
    public static final int EDIT_BANK_RESULT_CODE = 258;
    public static final String WALLET_TYPE_EXTRA = "wallet_extra";
    public static final String WALLET_COUNT_HISTORY_EXTRA = "wallet_count_extra";
    public static final String ASSIGNER_POSITION = "assigner_id";
    public static final String BROAD_CAST_CHAT_COUNT = "broad_cast_chat_count";
    public static final String CHECK_GROUP = "check_group";
    public static final String ASSIGNER_PRIVATE_ID = "assigner_id";
    public static final String BALANCE_CASH_EXTRA = "balance_cash_extra";
    public static final String ASSIGNER_EXTRA = "assigner_extra";
    public static final String COUNT_PRIVATE_CHAT_EXTRA = "push_count_chat";
    public static final String PAYMENT_EXTRA = "payment_extra";
    public static final String PARAMETER_REFERRER = "referrer_id";
    public static final String NO_USER = "no_user";
    public static final int REQUEST_CODE_GOOGLE_PLACE = 831;
    public static final String NOT_ENOUGH_BALANCE = "not_enough_balance";
    public static final String NOT_ENOUGH_PREPAID = "not_enough_prepaid";
    public static final String TASK_REFUND_PREPAID = "task_refund_prepaid";
    public static final String BID_PAYMENT_RECEIVED = "bid_payment_received";
    public static final String BID_COMMISSION_RECEIVED = "bid_commission_received";
    public static final String PARAMETER_ADD_PREPAY = "add_prepay";
    public static final String PREPAY_TYPE_EXTRA = "prepay_type_extra";
    public static final String INVALID_STATUS = "invalid_status";
    public static final String DISCOUNT_TYPE_EXTRA = "discount_extra";
    public static final String PREPAY_TYPE_DEDUCTION = "deduction_extra";
    public static final String WORK_DEDUCTED = "work_deducted";
    public static final String POST_TASK_NOT_ALLOWED = "post_task_not_allowed";
    public static int MAX_IMAGE_ATTACH = 6;
    public static final String INTENT_EXTRA_ALBUM = "album_name";
    public static final int REQUEST_CODE_PICK_IMAGE = 357;
    public static final int RESPONSE_CODE_PICK_IMAGE = 753;
    public static final String INTENT_EXTRA_IMAGES = "extra_image";
    public static final int REQUEST_CODE_CAMERA = 567;
    public static final int REQUEST_CODE_ADDRESS = 2345;
    public static final String EXTRA_ONLY_IMAGE = "extra_only_image";
    public static final String EXTRA_IS_CROP_PROFILE = "extra_is_crop_profile";
    public static final String EXTRA_IMAGE_PATH = "extra_image_path";
    public static final int REQUEST_CODE_CROP_IMAGE = 951;
    public static final int RESPONSE_CODE_CROP_IMAGE = 159;

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

    public static final int PERMISSION_REQUEST_CODE = 987;
    public static final int PERMISSION_REQUEST_CODE_AVATA = 852;
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
    public static final String ROLE_FIND_TASK = "find_task";
    public static final int RESULT_CODE_COST = 1999;
    public static final int RESULT_CODE_TASK_TYPE = 2020;
    public static final int REQUEST_CODE_COST = 2001;
    public static final int RESULT_CODE_TASK_EDIT = 234;
    public static final int REQUEST_CODE_TASK_EDIT = 432;
    public static final String NOTIFICATION_EXTRA = "notification_extra";
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 876;
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
    public static final String GENDER_ANY = "any";

    //task type
    public static final String TASK_TYPE_BIDDER_PENDING = "pending";
    public static final String TASK_TYPE_BIDDER_ACCEPTED = "accepted";
    public static final String TASK_TYPE_BIDDER_MISSED = "missed";
    public static final String TASK_TYPE_BIDDER_CANCELED = "canceled";
    public static final String TASK_TYPE_BIDDER_COMPLETED = "completed";
    public static final String TASK_TYPE_BIDDER_NOT_APPROVED = "not_approved";

    public static final String TASK_TYPE_POSTER_OPEN = "open";
    public static final String TASK_TYPE_POSTER_ASSIGNED = "assigned";
    public static final String TASK_TYPE_POSTER_COMPLETED = "completed";
    public static final String TASK_TYPE_POSTER_OVERDUE = "overdue";
    public static final String TASK_TYPE_POSTER_AWAIT_APPROVAL = "await_approval";
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
    public static final String PUSH_TYPE_ENOUGH_BIDDER = "enough_bidder";
    public static final String PUSH_TYPE_REVIEW_POSTER = "review_received_ra";
    public static final String PUSH_TYPE_REVIEW_WORKER_DONE = "review_received_ca";
    public static final String PUSH_TYPE_REVIEW_WORKER_NA = "review_received_na";
    public static final String PUSH_TYPE_REVIEW_WORKER = "review_received";
    public static final String PUSH_TYPE_AWAIT_APPROVAL = "task_await_approval";
    public static final String PUSH_TYPE_MONEY_RECEIVED = "money_received";
    public static final String PUSH_TYPE_BID_REFUNDED = "bid_refunded";
    public static final String PUSH_TYPE_PRIVATE_CHAT = "prv_message_received";
    public static final String PUSH_TYPE_WAGE_RECEIVED = "wage_received";
    public static final String PUSH_TYPE_POSTING_BONUS_RECEIVED = "posting_bonus_received";
    public static final String PUSH_TYPE_REF_BONUS_RECEIVED = "ref_bonus_received";
    public static final String PUSH_TYPE_TASK_REAPPLIED = "task_reapplied";

    public static final String ROLE_EXTRA = "role_extra";

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
    public static final String BROAD_CAST_PUSH_CHAT_COUNT = "push_count";
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
    public static final String COUNT_NEW_CHAT_EXTRA = "count_new_msg_extra";
    public static final int REQUEST_CODE_CHAT = 246;
    public static final int RESULT_CODE_CHAT = 147;
    public static final String TAB_EXTRA = "tab_extra";
    public static final String BID_ERROR_SAME_TIME = "no_permission_offer";
    public static final String BID_ERROR_INVALID_DATA = "invalid_data";
    public static final String BID_LIMIT_OFFER = "limit_offer";
    public static final String BID_NOT_ENOUGH_BALANCE = "not_enough_balance";
    public static final String TASK_DETAIL_INPUT_REQUIRE = "input_required";
    public static final String TASK_DETAIL_NO_EXIT = "no_exist";
    public static final String TASK_DETAIL_BLOCK = "block";
    public static final String LON_EXTRA = "lon_extra";

    public static final String OFFER_ACTIVE = "OFFER_ACTIVE";
    public static final String OFFER_PENDING = "OFFER_PENDING";
    public static final String OFFER_CALL = "OFFER_CALL";
    public static final String OFFER_GONE = "OFFER_GONE";
    public static final String OFFER_RATTING = "OFFER_RATTING";

    public static final String TASK_EDIT_EXTRA = "task_extra";

    public static final String TASK_COPY = "task_copy";
    public static final String TASK_EDIT = "task_edit";
    public static final String TASK_DRAFT = "task_draft";
    public static final String ACCOUNT_CODE = "token";
    public static final int ACCOUNT_KIT_REQUEST_CODE = 1101;

    public static final String STATUS_SETTING_OPEN = "open";
    public static final String STATUS_SETTING_ASSIGED = "assigned";

    public static final String PROMOTION_ERROR_EMPTY = "empty_parameters";
    public static final String PROMOTION_ERROR_NO = "no_promotion";
    public static final String PROMOTION_ERROR_NOT_STARTED = "promotion_not_started";
    public static final String PROMOTION_ERROR_EXPRIED = "promotion_expired";
    public static final String PROMOTION_ERROR_LIMITED = "promotion_limited";
    public static final String PROMOTION_ERROR_USED = "promotion_used";


    public static final String INVALID_CATEGORY = "invalid_category";

    public static final String INVALID_TITLE = "invalid_title";
    public static final String INVALID_DESCRIPTION = "invalid_description";
    public static final String INVALID_TIME = "invalid_time";
    public static final String INVALID_ADRESS = "invalid_address";
    public static final String INVALID_UPDATE_TASK_FAILED = "update_task_failed";
    public static final String NO_PERMISSION = "no_permission";
    public static final String EMPTY_PARAMETERS = "empty_parameters";
    public static final String INVALID_DATA = "invalid_data";
    public static final String NO_REFERRER = "no_referrer";

    public static final String NOTIFICATION_NEW_TASK = "new_task";
    public static final String NOTIFICATION_SYS = "system";

    public static final String ORDER_BY_START_TIME = "start_time";
}