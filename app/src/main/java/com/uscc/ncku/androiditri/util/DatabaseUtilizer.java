package com.uscc.ncku.androiditri.util;

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
    public static final String MODE_SPLASH_BLUR = "splash_blur_vertical";
    public static final String MODE_DID_READ = "did_read";

    // zone table
    public static final String ZONE_TABLE = "zone";
    public static final String ZONE_ID = "zone_id";
    public static final String FIELD_ID = "field_id";

    // path table
    public static final String PATH_TABLE = "path"; // path
    public static final String CHOOSE_PATH_ID = "choose_path_id"; // int
    public static final String PATH_ORDER = "path_order"; // int
    public static final String PATH_SVG_ID = "svg_id"; // int
    public static final String START = "start"; // start
    public static final String PATH_SN = "Sn"; // Sn
    public static final String END = "end";
    public static final String PATH_EN = "En"; // En

    // beacon table
    public static final String BEACON_TABLE = "beacon";
    public static final String BEACON_ID = "beacon_id";
    public static final String BEACON_POWER = "power";
    public static final String BEACON_STATUS = "status";
    public static final String BEACON_ZONE = "zone";
    public static final String BEACON_FIELD_NAME = "field_name";
    public static final String BEACON_FIELD_ID = "field_id";

    // company
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
    public static final String MAP_SVG = "map_svg";

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
    public static final String PAD_ID = "pad_id";
    public static final String BORROWER = "borrower";
    public static final String BORROWER_TEL = "borrower_tel";
    public static final String LEASE_DATE = "lease_date";
    public static final String RETURN_DATE = "return_date";

    // survey table
    public static final String SURVEY_TABLE = "survey";
    public static final String SURVEY_ID = "survey_id";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String EDUCATION = "education";
    public static final String CAREER = "career";
    public static final String EXPERIENCE = "experience";
    public static final String SALARY = "salary";
    public static final String LOCATION = "location";
    public static final String HOUSE_TYPE = "house_type";
    public static final String FAMILY_TYPE = "family_type";
    public static final String FAMILY_MEMBER = "family_member";
    public static final String KNOW_WAY = "know_way";

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
    public static final String COMPETENCE = "competence";
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

    // device
    public static final String DB_CREATE_TABLE_DEVICE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.DEVICE_TABLE + " ("
            + DatabaseUtilizer.DEVICE_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.NAME + " TEXT NOT NULL, "
            + DatabaseUtilizer.NAME_EN + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.INTRODUCTION + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.GUIDE_VOICE + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.DEVICE_PHOTO + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.DEVICE_PHOTO_VER + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.DEVICE_HINT + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.DEVICE_MODE_ID + " INT NOT NULL, "
            + DatabaseUtilizer.DEVICE_COMPANY_ID + " INT DEFAULT NULL, "
            + DatabaseUtilizer.READ_COUNT + " INT DEFAULT 0"
            + ")";
    // project
    public static final String DB_CREATE_TABLE_PROJECT = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.PROJECT_TABLE + " ("
            + DatabaseUtilizer.PROJECT_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.VERSION + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.NAME + " TEXT NOT NULL, "
            + DatabaseUtilizer.INTRODUCTION + " TEXT, "
            + DatabaseUtilizer.ACTIVE + " INT NOT NULL DEFAULT 1"
            + ")";
    // beacon
    public static final String DB_CREATE_TABLE_BEACON = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.BEACON_TABLE + " ("
            + DatabaseUtilizer.BEACON_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.MAC_ADDR + " TEXT NOT NULL, "
            + DatabaseUtilizer.NAME + " TEXT NOT NULL, "
            + DatabaseUtilizer.BEACON_POWER + " INT NOT NULL, "
            + DatabaseUtilizer.BEACON_STATUS + " INT NOT NULL, "
            + DatabaseUtilizer.BEACON_ZONE + " INT NOT NULL, "
            + DatabaseUtilizer.X + " INT NOT NULL, "
            + DatabaseUtilizer.Y + " INT NOT NULL, "
            + DatabaseUtilizer.BEACON_FIELD_ID + " INT, "
            + DatabaseUtilizer.BEACON_FIELD_NAME + " TEXT"
            + ")";
    // company
    public static final String DB_CREATE_TABLE_COMPANY = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.COMPANY_TABLE + " ("
            + DatabaseUtilizer.COMPANY_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.NAME + " TEXT NOT NULL, "
            + DatabaseUtilizer.COMPANY_TEL + " TEXT NOT NULL, "
            + DatabaseUtilizer.COMPANY_FAX + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.COMPANY_ADDR + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.COMPANY_WEB + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.QRCODE+ " TEXT DEFAULT NULL"
            + ")";
    // field map
    public static final String DB_CREATE_TABLE_FIELD_MAP = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.FIELD_MAP_TABLE + " ("
            + DatabaseUtilizer.FIELD_MAP_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.NAME + " TEXT NOT NULL, "
            + DatabaseUtilizer.NAME_EN + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.PROJECT_ID + " INT NOT NULL, "
            + DatabaseUtilizer.INTRODUCTION + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.GUIDE_VOICE + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.DEVICE_PHOTO + " TEXT NOT NULL, "
            + DatabaseUtilizer.DEVICE_PHOTO_VER + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.MAP_SVG + " TEXT NOT NULL"
            + ")";
    // hipster content
    public static final String DB_CREATE_TABLE_HIPSTER_CONTENT = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.HIPSTER_CONTENT_TABLE + " ("
            + DatabaseUtilizer.HIPSTER_CONTENT_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.CONTENT + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.PICTURE + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.COMBINE_PICTURE + " TEXT NOT NULL, "
            + DatabaseUtilizer.HIPSTER_TEMPLATE_ID + " INT DEFAULT NULL, "
            + DatabaseUtilizer.HIPSTER_TEXT_ID + " INT DEFAULT NULL, "
            + DatabaseUtilizer.ZONE_ID + " INT DEFAULT NULL"
            + ")";
    // hipster template
    public static final String DB_CREATE_TABLE_HIPSTER_TEMPLATE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.HIPSTER_TEMPLATE_TABLE + " ("
            + DatabaseUtilizer.HIPSTER_TEMPLATE_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.NAME + " TEXT NOT NULL, "
            + DatabaseUtilizer.TEMPLATE + " TEXT NOT NULL"
            + ")";
    // hipster text
    public static final String DB_CREATE_TABLE_HIPSTER_TEXT = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.HIPSTER_TEXT_TABLE + " ("
            + DatabaseUtilizer.HIPSTER_TEXT_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.CONTENT + " TEXT NOT NULL"
            + ")";
    // lease
    public static final String DB_CREATE_TABLE_LEASE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.LEASE_TABLE + " ("
            + DatabaseUtilizer.LEASE_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.PAD_ID + " TEXT NOT NULL, "
            + DatabaseUtilizer.BORROWER + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.BORROWER_TEL + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.LEASE_DATE + " datetime DEFAULT NULL, "
            + DatabaseUtilizer.RETURN_DATE + " datetime DEFAULT NULL"
            + ")";
    // mode
    public static final String DB_CREATE_TABLE_MODE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.MODE_TABLE + " ("
            + DatabaseUtilizer.MODE_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.MODE_NAME + " TEXT NOT NULL, "
            + DatabaseUtilizer.NAME_EN + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.INTRODUCTION + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.GUIDE_VOICE + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.VIDEO + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.MODE_SPLASH_BG + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.MODE_SPLASH_FG + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.MODE_SPLASH_BLUR + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.LIKE_COUNT + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.READ_COUNT + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.TIME_TOTAL + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.ZONE_ID + " INT NOT NULL, "
            + DatabaseUtilizer.MODE_DID_READ + " INT DEFAULT 0"
            + ")";
    // path --> path_id is the only primary key, different from database
    public static final String DB_CREATE_TABLE_PATH = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.PATH_TABLE + " ("
            + DatabaseUtilizer.CHOOSE_PATH_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.PATH_ORDER + " INT, "
            + DatabaseUtilizer.PATH_SVG_ID + " INT, "
            + DatabaseUtilizer.START + " INT, "
            + DatabaseUtilizer.PATH_SN + " TEXT, "
            + DatabaseUtilizer.END + " INT, "
            + DatabaseUtilizer.PATH_EN + " TEXT"
            + ")";
    // survey
    public static final String DB_CREATE_TABLE_SURVEY = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.SURVEY_TABLE + " ("
            + DatabaseUtilizer.SURVEY_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.NAME + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.EMAIL + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.GENDER + " INT NOT NULL, "
            + DatabaseUtilizer.AGE + " INT NOT NULL, "
            + DatabaseUtilizer.EDUCATION + " INT NOT NULL, "
            + DatabaseUtilizer.CAREER + " INT NOT NULL, "
            + DatabaseUtilizer.EXPERIENCE + " INT NOT NULL, "
            + DatabaseUtilizer.SALARY + " INT NOT NULL, "
            + DatabaseUtilizer.LOCATION + " INT NOT NULL, "
            + DatabaseUtilizer.HOUSE_TYPE + " INT NOT NULL, "
            + DatabaseUtilizer.FAMILY_TYPE + " INT NOT NULL, "
            + DatabaseUtilizer.FAMILY_MEMBER + " INT NOT NULL, "
            + DatabaseUtilizer.KNOW_WAY + " INT NOT NULL"
            + ")";
    // survey result
    public static final String DB_CREATE_TABLE_SURVEY_RESULT = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.SURVEY_RESULT_TABLE + " ("
            + DatabaseUtilizer.SURVEY_RESULT_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.SURVEY_QUESTION + " INT NOT NULL, "
            + DatabaseUtilizer.SURVEY_ANSWER + " INT NOT NULL, "
            + DatabaseUtilizer.TOTAL + " INT NOT NULL"
            + ")";
    // users
    public static final String DB_CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.USERS_TABLE + " ("
            + DatabaseUtilizer.USER_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.EMAIL + " TEXT NOT NULL, "
            + DatabaseUtilizer.PASSWORD + " TEXT NOT NULL, "
            + DatabaseUtilizer.COMPETENCE + " INT NOT NULL, "
            + DatabaseUtilizer.LAST_LOGIN + " datetime DEFAULT NULL"
            + ")";
    // vip device
    public static final String DB_CREATE_TABLE_VIP_DEVICE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.VIP_DEVICE_TABLE + " ("
            + DatabaseUtilizer.VIP_DEVICE_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.MAC_ADDR + " TEXT NOT NULL, "
            + DatabaseUtilizer.NAME + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.POWER + " INT NOT NULL"
            + ")";
    // vip pi
    public static final String DB_CREATE_TABLE_VIP_PI = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.VIP_PI_TABLE + " ("
            + DatabaseUtilizer.VIP_PI_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.IPADDRESS + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.INTRODUCTION + " TEXT, "
            + DatabaseUtilizer.VIP_VISIBLE + " INT NOT NULL DEFAULT 1"
            + ")";
    // vip voice
    public static final String DB_CREATE_TABLE_VIP_VOICE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.VIP_VOICE_TABLE + " ("
            + DatabaseUtilizer.VOICE_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.VOICE + " TEXT NOT NULL, "
            + DatabaseUtilizer.VIP_DEVICE + " INT DEFAULT NULL"
            + ")";
    // zone
    public static final String DB_CREATE_TABLE_ZONE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.ZONE_TABLE + " ("
            + DatabaseUtilizer.ZONE_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.NAME + " TEXT NOT NULL, "
            + DatabaseUtilizer.NAME_EN + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.INTRODUCTION + " TEXT, "
            + DatabaseUtilizer.GUIDE_VOICE + " TEXT, "
            + DatabaseUtilizer.DEVICE_HINT + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.DEVICE_PHOTO + " TEXT NOT NULL, "
            + DatabaseUtilizer.DEVICE_PHOTO_VER + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.FIELD_ID + " INT NOT NULL"
            + ")";

}
