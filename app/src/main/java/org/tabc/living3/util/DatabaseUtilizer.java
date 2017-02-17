package org.tabc.living3.util;

/**
 * Created by Oslo on 9/27/16.
 */
public class DatabaseUtilizer {
    // base IP address
//    public static final String IP = "140.116.82.48";
    public static final String IP = "60.251.33.54:98";

    public static final String serverURL = "http://" + IP + "/interface/jsondecode.php";
    public static final String downloadURL  = "http://" + IP + "/interface/getfile.php";
    public static final String hipsterContentURL = "http://" + IP + "/interface/hipster.php"; // hipster content
    public static final String surveyOneURL = "http://" + IP + "/interface/survey.php"; // first survey
    public static final String feedbackURL = "http://" + IP + "/interface/survey2.php";
    public static final String deviceaddURL = "http://" + IP + "/interface/deviceadd.php"; // counter
    public static final String modeaddURL = "http://" + IP + "/interface/modeadd.php"; // counter
    public static final String zoneaddURL = "http://" + IP + "/interface/zoneadd.php"; // counter
    public static final String filePathURLPrefix = "http://" + IP + "/web/";
    // 2/13/2017
    public static final String counttypeURL = "http://" + IP + "/interface/count_type.php";

    /*
        declare useful strings for downloading & uploading data
     */

    // general usage
    public static final String NAME = "name";
    public static final String NAME_EN = "name_en";
    public static final String INTRODUCTION = "introduction";
    public static final String INTRODUCTION_EN = "introduction_en";
    public static final String GUIDE_VOICE = "guide_voice";
    // guide_voice_size
    public static final String TEMPLATE_SIZE = "template_size";
    public static final String GUIDE_VOICE_EN = "guide_voice_en";
    public static final String VIDEO = "video";
    public static final String PHOTO_SIZE = "photo_size";
    public static final String PHOTO_VERTICAL_SIZE = "photo_vertical_size";
    public static final String READ_COUNT = "read_count";
    public static final String LIKE_COUNT = "like_count";
    public static final String TIME_TOTAL = "time_total";
    public static final String CREATE_DATE = "create_date";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String EMAIL = "email";

    // project table
    public static final String PROJECT_ID = "project_id";

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
    public static final String SPLASH_BG_SIZE = "splash_bg_vertical_size";
    public static final String MODE_SPLASH_FG = "splash_fg_vertical";
    public static final String SPLASH_FG_SIZE = "splash_fg_vertical_size";
    public static final String MODE_SPLASH_BLUR = "splash_blur_vertical";
    public static final String SPLASH_BLUR_SIZE = "splash_blur_vertical_size";
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
    public static final String MAP_SVG_EN = "map_svg_en";
    public static final String MAP_BG = "map_bg";
    public static final String MAP_SVG_SIZE = "map_svg_size";
    public static final String MAP_SVG_EN_SIZE = "map_svg_en_size";
    public static final String MAP_BG_SIZE = "map_bg_size";

    // hipster template table
    public static final String HIPSTER_TEMPLATE_TABLE = "hipster_template";
    public static final String HIPSTER_TEMPLATE_ID = "hipster_template_id";
    public static final String TEMPLATE = "template";

    // hipster text table
    public static final String HIPSTER_TEXT_TABLE = "hipster_text";
    public static final String HIPSTER_TEXT_ID = "hipster_text_id";
    public static final String CONTENT = "content";
    public static final String CONTENT_EN = "content_en";
    public static final String MAC_ADDR = "mac_addr";

    // TODO: not sure last update time & isDelete is required or not
    public static final String LASTUPDATE_DATE = "lastupdate_date";

    // device
    public static final String DB_CREATE_TABLE_DEVICE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.DEVICE_TABLE + " ("
            + DatabaseUtilizer.DEVICE_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.NAME + " TEXT NOT NULL, "
            + DatabaseUtilizer.NAME_EN + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.INTRODUCTION + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.INTRODUCTION_EN + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.GUIDE_VOICE + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.GUIDE_VOICE_EN + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.DEVICE_PHOTO + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.PHOTO_SIZE + " INT DEFAULT 0, "
            + DatabaseUtilizer.DEVICE_PHOTO_VER + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.PHOTO_VERTICAL_SIZE + " INT DEFAULT 0, "
            + DatabaseUtilizer.DEVICE_HINT + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.DEVICE_MODE_ID + " INT NOT NULL, "
            + DatabaseUtilizer.DEVICE_COMPANY_ID + " INT DEFAULT NULL, "
            + DatabaseUtilizer.READ_COUNT + " INT DEFAULT 0, "
            + DatabaseUtilizer.LIKE_COUNT + " INT DEFAULT 0"
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
            + DatabaseUtilizer.NAME_EN + " TEXT DEFAULT NULL, "
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
            + DatabaseUtilizer.DEVICE_PHOTO + " TEXT NOT NULL, "
            + DatabaseUtilizer.PHOTO_SIZE + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.DEVICE_PHOTO_VER + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.PHOTO_VERTICAL_SIZE + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.MAP_SVG + " TEXT NOT NULL, "
            + DatabaseUtilizer.MAP_SVG_SIZE + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.MAP_SVG_EN + " TEXT NOT NULL, "
            + DatabaseUtilizer.MAP_SVG_EN_SIZE + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.MAP_BG + " TEXT NOT NULL, "
            + DatabaseUtilizer.MAP_BG_SIZE + " INT NOT NULL DEFAULT 0"
            + ")";
    // hipster template
    public static final String DB_CREATE_TABLE_HIPSTER_TEMPLATE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.HIPSTER_TEMPLATE_TABLE + " ("
            + DatabaseUtilizer.HIPSTER_TEMPLATE_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.NAME + " TEXT NOT NULL, "
            + DatabaseUtilizer.TEMPLATE + " TEXT NOT NULL, "
            + DatabaseUtilizer.TEMPLATE_SIZE + " INT NOT NULL DEFAULT 0"
            + ")";
    // hipster text
    public static final String DB_CREATE_TABLE_HIPSTER_TEXT = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.HIPSTER_TEXT_TABLE + " ("
            + DatabaseUtilizer.HIPSTER_TEXT_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.CONTENT + " TEXT NOT NULL, "
            + DatabaseUtilizer.CONTENT_EN + " TEXT NOT NULL"
            + ")";
    // mode
    public static final String DB_CREATE_TABLE_MODE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.MODE_TABLE + " ("
            + DatabaseUtilizer.MODE_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.MODE_NAME + " TEXT NOT NULL, "
            + DatabaseUtilizer.NAME_EN + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.INTRODUCTION + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.INTRODUCTION_EN + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.GUIDE_VOICE + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.GUIDE_VOICE_EN + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.VIDEO + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.MODE_SPLASH_BG + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.SPLASH_BG_SIZE + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.MODE_SPLASH_FG + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.SPLASH_FG_SIZE + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.MODE_SPLASH_BLUR + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.SPLASH_BLUR_SIZE + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.LIKE_COUNT + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.READ_COUNT + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.TIME_TOTAL + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.ZONE_ID + " INT NOT NULL, "
            + DatabaseUtilizer.MODE_DID_READ + " INT DEFAULT 0"
            + ")";
    // path --> path_id is the only primary key, different from database
    public static final String DB_CREATE_TABLE_PATH = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.PATH_TABLE + " ("
            + DatabaseUtilizer.CHOOSE_PATH_ID + " INT NOT NULL, "
            + DatabaseUtilizer.PATH_ORDER + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.PATH_SVG_ID + " TEXT, "
            + DatabaseUtilizer.START + " INT, "
            + DatabaseUtilizer.PATH_SN + " TEXT, "
            + DatabaseUtilizer.END + " INT, "
            + DatabaseUtilizer.PATH_EN + " TEXT"
            + ")";
    // zone
    public static final String DB_CREATE_TABLE_ZONE = "CREATE TABLE IF NOT EXISTS " + DatabaseUtilizer.ZONE_TABLE + " ("
            + DatabaseUtilizer.ZONE_ID + " INT NOT NULL UNIQUE, "
            + DatabaseUtilizer.NAME + " TEXT NOT NULL, "
            + DatabaseUtilizer.NAME_EN + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.INTRODUCTION + " TEXT, "
            + DatabaseUtilizer.INTRODUCTION_EN + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.GUIDE_VOICE + " TEXT, "
            + DatabaseUtilizer.GUIDE_VOICE_EN + " TEXT, "
            + DatabaseUtilizer.DEVICE_HINT + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.DEVICE_PHOTO + " TEXT NOT NULL, "
            + DatabaseUtilizer.PHOTO_SIZE + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.DEVICE_PHOTO_VER + " TEXT DEFAULT NULL, "
            + DatabaseUtilizer.PHOTO_VERTICAL_SIZE + " INT NOT NULL DEFAULT 0, "
            + DatabaseUtilizer.FIELD_ID + " INT NOT NULL, "
            + DatabaseUtilizer.LIKE_COUNT + " INT DEFAULT 0"
            + ")";

    public String getIP(){
        return IP;
    }

}
