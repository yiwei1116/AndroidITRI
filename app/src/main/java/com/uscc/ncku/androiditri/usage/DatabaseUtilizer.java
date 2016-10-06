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

}
