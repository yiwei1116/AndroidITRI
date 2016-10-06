package com.uscc.ncku.androiditri.usage;

/**
 * Created by Oslo on 9/27/16.
 */
public class DatabaseUtilizer {

    /*
        declare useful strings for downloading & uploading data
     */
    public static String serverURL = "http://140.116.82.48/phpmyadmin/index.php";
    public static String queryPhp = "http://140.116.82.48/interface/deviceadd.php";

    public static String dbProject = "project";
    public static String dbFieldMap = "field_map";
    public static String dbZone = "zone";
    public static String dbPath = "path";
    public static String dbBeacon = "beacon";
    public static String dbMode = "mode";
    public static String dbDevice = "device";
    public static String dbCompany = "company";
    public static String dbHipsterContent = "hipster_content";
    public static String dbHipsterText = "hipster_text";
    public static String dbHipsterTemplate = "hipster_template";
    public static String dbVipDevice = "vip_device";
    public static String dbVipVoice = "vip_voice";
    public static String dbLease = "lease";
    public static String dbUsers = "users";
    public static String dbServerResult = "survey_result";
    public static String dbVipPi = "vip_pi";
    public static String dbSurvey = "survey";

    //public static int

    // general usage
    public static final String NAME = "name";
    public static final String NAME_EN = "name_en";
    public static final String INTRODUCTION = "introduction";
    public static final String GUIDE_VOICE = "guide_voice";
    public static final String VIDEO = "video";
    public static final String READ_COUNT = "read_count";
    public static final String LIKE_COUNT = "like_count";
    public static final String TIME_TOTAL = "time_total";
    public static final String CREATE_DATE = "create_date";
    public static final String LASTUPDATE_DATE = "lastupdate_date";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String EMAIL = "email";

    // project table
    public static final String PROJECT_TABLE = "project";
    public static final String PROJECT_ID = "project_id";
    public static final String VERSION = "version";
    public static final String ACTIVE = "active";

    // device table
    public static final String DEVICE_TABLE = "device";
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_PHOTO = "photo";
    public static final String DEVICE_PHOTO_VER = "photo_vertical";
    public static final String DEVICE_HINT = "hint";
    public static final String DEVICE_MODE_ID = "mode_id";
    public static final String DEVICE_COMPANY_ID = "company_id";

    // mode table
    public static final String MODE_TABLE = "mode";
    public static final String MODE_ID = "mode_id";
    public static final String MODE_NAME = "name";
    public static final String MODE_SPLASH_BG = "splash_bg_vertical";
    public static final String MODE_SPLASH_FG = "splash_fg_vertical";
    public static final String MODE_SPLASH_BLUR = "splash_spur_vertical";

    // zone table
    public static final String ZONE_TABLE = "zone";
    public static final String ZONE_ID = "zone_id";

    // path table
    public static final String PATH_TABLE = "path";
    public static final String PATH_ID = "path_id";
    public static final String PATH_ORDER = "order";

    // beacon table
    public static final String BEACON_TABLE = "beacon";
    public static final String BEACON_ID = "beacon_id";
    public static final String BEACON_POWER = "power";
    public static final String BEACON_STATUS = "status";
    public static final String BEACON_ZONE = "zone";

    // compnay
    public static final String COMPANY_TABLE = "company";
    public static final String COMPANY_ID = "company_id";
    public static final String COMPANY_TEL = "tel";
    public static final String COMPANY_FAX = "fax";
    public static final String COMPANY_ADDR = "addr";
    public static final String COMPANY_WEB = "web";
    public static final String QRCODE = "qrcode";

    // field map table
    public static final String FIELD_MAP_TABLE = "field_map";
    public static final String FIELD_MAP_ID = "field_map_id";

    // hipster content table
    public static final String HIPSTER_CONTENT_TABLE = "hipster_content";
    public static final String HIPSTER_CONTENT_ID = "hipster_content_id";
    public static final String PICTURE = "picture";
    public static final String COMBINE_PICTURE = "combine_picture";

    // hipster template table
    public static final String HIPSTER_TEMPLATE_TABLE = "hipster_template";
    public static final String HIPSTER_TEMPLATE_ID = "hipster_template_id";
    public static final String TEMPLATE = "template";

    // hipster text table
    public static final String HIPSTER_TEXT_TABLE = "hipster_text";
    public static final String HIPSTER_TEXT_ID = "hipster_text_id";
    public static final String CONTENT = "content";

    // lease table
    public static final String LEASE_TABLE = "lease";
    public static final String LEASE_ID = "id";
    public static final String BORROWER = "borrower";
    public static final String BORROWER_TEL = "borrower_tel";
    public static final String LEASE_DATE = "lease_date";
    public static final String RETURN_DATE = "return_date";

    // survey table
    public static final String SURBEY_TABLE = "survey_table";
    public static final String SURVEY_ID = "survey_id";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String EDUCATION = "education";
    public static final String CAREER = "career";
    public static final String LOCATION = "location";
    public static final String HOUSE_TYPE = "house_type";
    public static final String FAMILY_TYPE = "family_type";

    // survey result
    public static final String SURVEY_RESULT_TABLE = "survey_result";
    public static final String SURVEY_RESULT_ID = "id";
    public static final String SURVEY_QUESTION = "question";
    public static final String SURVEY_ANSWER = "answer";
    // maybe don't need total
    public static final String TOTAL = "total";

    // users table
    public static final String USERS_TABLE = "users";
    public static final String USER_ID = "user_id";
    public static final String PASSWORD = "password";
    public static final String LAST_LOGIN = "last_login";

    // vip device table
    public static final String VIP_DEVICE_TABLE = "vip_device";
    public static final String VIP_DEVICE_ID = "vip_device_id";
    public static final String MAC_ADDR = "mac_addr";
    public static final String POWER = "power";

    // vip pi
    public static final String VIP_PI_TABLE = "vip_pi";
    public static final String VIP_PI_ID = "vip_pi_id";
    public static final String IPADDRESS = "IPaddress";
    public static final String VIP_VISIBLE = "visible";

    // vip voice
    public static final String VIP_VOICE_TABLE = "vip_voice";
    public static final String VOICE_ID = "voice_id";
    public static final String VOICE = "voice";
    public static final String VIP_DEVICE = "vip_device";


}
