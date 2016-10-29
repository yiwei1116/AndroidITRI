package com.uscc.ncku.androiditri.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Oslo on 10/3/16.
 */
public class ITRIObject {
    // fragments:
    public static String areaFragment = "AreaFragment";
    public static String choosePhoto = "ChoosePhoto";
    public static String chooseTemplate = "ChooseTemplate";
    public static String confirmPic = "ConfirmPic";
    public static String customCameras = "CustomCameras";
    public static String diaryFragment = "DiaryFragment";
    public static String equipmentTagFragment = "EquipmentTagFragment";
    public static String feedbackFragment = "FeedbackFragment";
    public static String mapFragment = "MapFragment";
    public static String mergeTemplatePic = "MergeTemplatePic";
    public static String modeHighlightFragment = "ModeHighlightFragment";
    public static String modeSelectFragment = "ModeSelectFragment";
    public static String templateContext = "TemplateContext";
    public static String textFragment = "TextFragment";

    // usage
    public static String databaseUtilizer = "DatabaseUtilizer";

    // util
    public static String downloadProject = "DownloadProject";
    public static String mainButton = "MainButton";
    public static String tourViewPager = "TourViewPager";

    // activity
    public static String aboutActivity = "AboutActivity";
    public static String counterActvity = "CounterActivity";
    public static String feedbackIntentService = "FeedbackIntentService";
    public static String homeActivity = "HomeActivity";
    public static String loadingActivity = "LoadingActivity";
    public static String mainActivity = "MainActivity";
    public static String myFeedbackService = "MyFeedbackService";
    public static String surveyActivity = "SurveyActivity";
    public static String tourSelectActivity = "TourSelectActivity";

    // "GLOBAL" JSON Object
    public static JSONObject counterJSON;

    public ITRIObject() {
        counterJSON = new JSONObject();
    }

    public JSONObject makeJSONObjectWithIdAndJSONArray(int id, JSONArray array) throws JSONException {
        JSONObject newObj = new JSONObject();
        newObj.put(String.valueOf(id), array);
        return newObj;
    }

    /////////// ****** method to get which fragment is it
//    Fragment currentFragment = getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);
//    EquipmentTabFragment ef = null;
//    if (currentFragment instanceof EquipmentTabFragment) {
//        ef = (EquipmentTabFragment) currentFragment;
//    }




}
