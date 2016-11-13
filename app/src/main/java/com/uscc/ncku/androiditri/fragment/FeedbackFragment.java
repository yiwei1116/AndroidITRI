package com.uscc.ncku.androiditri.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.uscc.ncku.androiditri.CommunicationWithServer;
import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;
import com.uscc.ncku.androiditri.util.SQLiteDbManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * use this to call feedback popup dialog
 *
 * FeedbackFragment feedback = new FeedbackFragment(currentZone);
 * feedback.feedbackAlertDialog(getActivity(), feedback);
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment {
    private static final String ZONE_ID = "ZONE_ID";

    private View view;
    private ScrollView scrollView;

    private ArrayList<String> spinnerList;

    private boolean isEnglish;

    private SQLiteDbManager dbManager;

    private int zoneId;
    private int field;
    private CommunicationWithServer comm;
    private int attitude = 0;
    private int functionality = 0;
    private int visual = 0;
    private int operability = 0;
    private int user_friendly = 0;
    private int price = 0;
    private int maintenance = 0;
    private int safety = 0;
    private int energy = 0;
    private int first_choise = 0, second_choise = 0, third_choise = 0, fourth_choise = 0, fifth_choise = 0;
    private int first_consider = 0, second_consider = 0, third_consider = 0, fourth_consider = 0, fifth_consider = 0;
    private int subscription1 = 0;
    private int subscription2 = 0;
    private int subscription3 = 0;
    private int install1 = 0, install2 = 0, install3 = 0, install4 = 0, install5 = 0;
    private int impression1 = 0, impression2 = 0, impression3 = 0, impression4 = 0, impression5 = 0;
    private int buy = 0;
    private int reasonable_price = 0;

    public FeedbackFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FeedbackFragment.
     */
    public static FeedbackFragment newInstance(int zoneId) {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        args.putInt(ZONE_ID, zoneId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            zoneId = getArguments().getInt(ZONE_ID);
            // TODO: zone id = 2
            if (zoneId == 0)
                zoneId = 2;
        }
        dbManager = new SQLiteDbManager(getActivity(), SQLiteDbManager.DATABASE_NAME);
        spinnerList = new ArrayList<String>();

        try {
            JSONObject zone = dbManager.queryZone(zoneId);

            field = zone.getInt("field_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).hideMainBtn();
        ((MainActivity) getActivity()).showFeedbackToolbar();
        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        setHasOptionsMenu(true);

        toolbar.setNavigationIcon(R.drawable.grey_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        if (toolbar.getMenu() != null) {
            toolbar.getMenu().clear();
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getActivity().onBackPressed();
                return true;
            }
        });

        view = inflater.inflate(R.layout.fragment_feedback, container, false);

        scrollView = (ScrollView) view.findViewById(R.id.scrollview_feedback);

        isEnglish = ((MainActivity) getActivity()).isEnglish();
        comm = ((MainActivity) getActivity()).getCommunicationWithServer();

        addSpinnerList();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_close, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        q1();
    }

    @Override
    public void onPause() {
        super.onPause();
        // hide all view in feedback while destroy view
        RadioGroup rGroup = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_1);
        rGroup.setVisibility(View.GONE);

        rGroup = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_8);
        rGroup.setVisibility(View.GONE);

        RelativeLayout rLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_2);
        rLayout.setVisibility(View.GONE);

        rLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_3);
        rLayout.setVisibility(View.GONE);

        rLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_4);
        rLayout.setVisibility(View.GONE);

        rLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_5);
        rLayout.setVisibility(View.GONE);

        rLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_6);
        rLayout.setVisibility(View.GONE);

        rLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_7);
        rLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).showMainBtn();
        ((MainActivity) getActivity()).showDefaultToolbar();
        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.getMenu().clear();
        toolbar.setNavigationIcon(R.drawable.btn_back);
    }

    private void addSpinnerList() {
        spinnerList.add(getResources().getString(R.string.spinner_please_select));

        try {
            JSONArray deviceNames = queryDeviceNameByField(field);

            for (int i = 0; i < deviceNames.length(); i++) {
                JSONObject deviceName = deviceNames.getJSONObject(i);

                int devicd_id = deviceName.getInt("device_id");
                String name = deviceName.getString("name");
                name = String.valueOf(devicd_id) + ". " + name;
                if(isEnglish) {
                    String name_en = deviceName.getString("name_en");
                    name_en = String.valueOf(devicd_id) + ". " + name_en;
                    if (name_en == null)
                        spinnerList.add(name);
                    else
                        spinnerList.add(name_en);
                } else {
                    spinnerList.add(name);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void q1() {
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_1);
        radioGroup.setVisibility(View.VISIBLE);
        getView().post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        });

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_1);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_1);

        if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_1_very_support) {
            attitude = 1;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_1_support) {
            attitude = 2;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_1_soso) {
            attitude = 3;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_1_unsupport) {
            attitude = 4;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_1_very_unsupport) {
            attitude = 5;
        }

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q2();
                radioGroup.setVisibility(View.GONE);
            }
        });

    }

    private void q2() {
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_2);
        relativeLayout.setVisibility(View.VISIBLE);
        getView().post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        });

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q1();
                relativeLayout.setVisibility(View.GONE);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_2);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_2);

        RadioGroup rGFunction = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_function);
        if (rGFunction.getCheckedRadioButtonId() == R.id.rbtn_feedback_function_very_important) {
            functionality = 1;
        } else if (rGFunction.getCheckedRadioButtonId() == R.id.rbtn_feedback_function_important) {
            functionality = 2;
        } else if (rGFunction.getCheckedRadioButtonId() == R.id.rbtn_feedback_function_soso) {
            functionality = 3;
        } else if (rGFunction.getCheckedRadioButtonId() == R.id.rbtn_feedback_function_unimportant) {
            functionality = 4;
        } else if (rGFunction.getCheckedRadioButtonId() == R.id.rbtn_feedback_function_very_unimportant) {
            functionality = 5;
        }

        RadioGroup rGBeauty = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_beauty);
        if (rGBeauty.getCheckedRadioButtonId() == R.id.rbtn_feedback_beauty_very_important) {
            visual = 1;
        } else if (rGBeauty.getCheckedRadioButtonId() == R.id.rbtn_feedback_beauty_important) {
            visual = 2;
        } else if (rGBeauty.getCheckedRadioButtonId() == R.id.rbtn_feedback_beauty_soso) {
            visual = 3;
        } else if (rGBeauty.getCheckedRadioButtonId() == R.id.rbtn_feedback_beauty_unimportant) {
            visual = 4;
        } else if (rGBeauty.getCheckedRadioButtonId() == R.id.rbtn_feedback_beauty_very_unimportant) {
            visual = 5;
        }

        RadioGroup rGOperation = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_operability);
        if (rGOperation.getCheckedRadioButtonId() == R.id.rbtn_feedback_operability_very_important) {
            operability = 1;
        } else if (rGOperation.getCheckedRadioButtonId() == R.id.rbtn_feedback_operability_important) {
            operability = 2;
        } else if (rGOperation.getCheckedRadioButtonId() == R.id.rbtn_feedback_operability_soso) {
            operability = 3;
        } else if (rGOperation.getCheckedRadioButtonId() == R.id.rbtn_feedback_operability_unimportant) {
            operability = 4;
        } else if (rGOperation.getCheckedRadioButtonId() == R.id.rbtn_feedback_operability_very_unimportant) {
            operability = 5;
        }

        RadioGroup rGHumility = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_humility);
        if (rGHumility.getCheckedRadioButtonId() == R.id.rbtn_feedback_humility_very_important) {
            user_friendly = 1;
        } else if (rGHumility.getCheckedRadioButtonId() == R.id.rbtn_feedback_humility_important) {
            user_friendly = 2;
        } else if (rGHumility.getCheckedRadioButtonId() == R.id.rbtn_feedback_humility_soso) {
            user_friendly = 3;
        } else if (rGHumility.getCheckedRadioButtonId() == R.id.rbtn_feedback_humility_unimportant) {
            user_friendly = 4;
        } else if (rGHumility.getCheckedRadioButtonId() == R.id.rbtn_feedback_humility_very_unimportant) {
            user_friendly = 5;
        }

        RadioGroup rGPrice = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_price);
        if (rGPrice.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_very_important) {
            price = 1;
        } else if (rGPrice.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_important) {
            price = 2;
        } else if (rGPrice.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_soso) {
            price = 3;
        } else if (rGPrice.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_unimportant) {
            price = 4;
        } else if (rGPrice.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_very_unimportant) {
            price = 5;
        }

        RadioGroup rGMaintenance = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_maintenance);
        if (rGMaintenance.getCheckedRadioButtonId() == R.id.rbtn_feedback_maintenance_very_important) {
            maintenance = 1;
        } else if (rGMaintenance.getCheckedRadioButtonId() == R.id.rbtn_feedback_maintenance_important) {
            maintenance = 2;
        } else if (rGMaintenance.getCheckedRadioButtonId() == R.id.rbtn_feedback_maintenance_soso) {
            maintenance = 3;
        } else if (rGMaintenance.getCheckedRadioButtonId() == R.id.rbtn_feedback_maintenance_unimportant) {
            maintenance = 4;
        } else if (rGMaintenance.getCheckedRadioButtonId() == R.id.rbtn_feedback_maintenance_very_unimportant) {
            maintenance = 5;
        }

        RadioGroup rGSafe = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_safetly);
        if (rGSafe.getCheckedRadioButtonId() == R.id.rbtn_feedback_safetly_very_important) {
            safety = 1;
        } else if (rGSafe.getCheckedRadioButtonId() == R.id.rbtn_feedback_safetly_important) {
            safety = 2;
        } else if (rGSafe.getCheckedRadioButtonId() == R.id.rbtn_feedback_safetly_soso) {
            safety = 3;
        } else if (rGSafe.getCheckedRadioButtonId() == R.id.rbtn_feedback_safetly_unimportant) {
            safety = 4;
        } else if (rGSafe.getCheckedRadioButtonId() == R.id.rbtn_feedback_safetly_very_unimportant) {
            safety = 5;
        }

        RadioGroup rGEnergy = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_energy);
        if (rGEnergy.getCheckedRadioButtonId() == R.id.rbtn_feedback_energy_very_important) {
            energy = 1;
        } else if (rGEnergy.getCheckedRadioButtonId() == R.id.rbtn_feedback_energy_important) {
            energy = 2;
        } else if (rGEnergy.getCheckedRadioButtonId() == R.id.rbtn_feedback_energy_soso) {
            energy = 3;
        } else if (rGEnergy.getCheckedRadioButtonId() == R.id.rbtn_feedback_energy_unimportant) {
            energy = 4;
        } else if (rGEnergy.getCheckedRadioButtonId() == R.id.rbtn_feedback_energy_very_unimportant) {
            energy = 5;
        }

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q3();
                relativeLayout.setVisibility(View.GONE);
            }
        });

    }

    private void q3() {
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_3);
        relativeLayout.setVisibility(View.VISIBLE);
        getView().post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        });

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q2();
                relativeLayout.setVisibility(View.GONE);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_3);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_3);

        Spinner spinner_1 = (Spinner) view.findViewById(R.id.feedback_spinner_first_order);
        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerList);
        spinner_1.setAdapter(lunchList);
        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                first_choise = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CheckBox function_1 = (CheckBox) view.findViewById(R.id.rbtn_first_order_reason_function);
        CheckBox beauty_1 = (CheckBox) view.findViewById(R.id.rbtn_first_order_reason_beauty);
        CheckBox operation_1 = (CheckBox) view.findViewById(R.id.rbtn_first_order_reason_operation);
        CheckBox humility_1 = (CheckBox) view.findViewById(R.id.rbtn_first_order_reason_humility);
        CheckBox maintainence_1 = (CheckBox) view.findViewById(R.id.rbtn_first_order_reason_maintainence);
        if (function_1.isChecked()) {

        }
        if (beauty_1.isChecked()) {

        }
        if (operation_1.isChecked()) {

        }
        if (humility_1.isChecked()) {

        }
        if (maintainence_1.isChecked()) {

        }



        Spinner spinner_2 = (Spinner) view.findViewById(R.id.feedback_spinner_second_order);
        spinner_2.setAdapter(lunchList);
        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                second_choise = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CheckBox function_2 = (CheckBox) view.findViewById(R.id.rbtn_second_order_reason_function);
        CheckBox beauty_2 = (CheckBox) view.findViewById(R.id.rbtn_second_order_reason_beauty);
        CheckBox operation_2 = (CheckBox) view.findViewById(R.id.rbtn_second_order_reason_operation);
        CheckBox humility_2 = (CheckBox) view.findViewById(R.id.rbtn_second_order_reason_humility);
        CheckBox maintainence_2 = (CheckBox) view.findViewById(R.id.rbtn_second_order_reason_maintainence);
        if (function_2.isChecked()) {

        }
        if (beauty_2.isChecked()) {

        }
        if (operation_2.isChecked()) {

        }
        if (humility_2.isChecked()) {

        }
        if (maintainence_2.isChecked()) {

        }



        Spinner spinner_3 = (Spinner) view.findViewById(R.id.feedback_spinner_third_order);
        spinner_3.setAdapter(lunchList);
        spinner_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                third_choise = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CheckBox function_3 = (CheckBox) view.findViewById(R.id.rbtn_third_order_reason_function);
        CheckBox beauty_3 = (CheckBox) view.findViewById(R.id.rbtn_third_order_reason_beauty);
        CheckBox operation_3 = (CheckBox) view.findViewById(R.id.rbtn_third_order_reason_operation);
        CheckBox humility_3 = (CheckBox) view.findViewById(R.id.rbtn_third_order_reason_humility);
        CheckBox maintainence_3 = (CheckBox) view.findViewById(R.id.rbtn_third_order_reason_maintainence);
        if (function_3.isChecked()) {

        }
        if (beauty_3.isChecked()) {

        }
        if (operation_3.isChecked()) {

        }
        if (humility_3.isChecked()) {

        }
        if (maintainence_3.isChecked()) {

        }



        Spinner spinner_4 = (Spinner) view.findViewById(R.id.feedback_spinner_fourth_order);
        spinner_4.setAdapter(lunchList);
        spinner_4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                fourth_choise = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CheckBox function_4 = (CheckBox) view.findViewById(R.id.rbtn_fourth_order_reason_function);
        CheckBox beauty_4 = (CheckBox) view.findViewById(R.id.rbtn_fourth_order_reason_beauty);
        CheckBox operation_4 = (CheckBox) view.findViewById(R.id.rbtn_fourth_order_reason_operation);
        CheckBox humility_4 = (CheckBox) view.findViewById(R.id.rbtn_fourth_order_reason_humility);
        CheckBox maintainence_4 = (CheckBox) view.findViewById(R.id.rbtn_fourth_order_reason_maintainence);
        if (function_4.isChecked()) {

        }
        if (beauty_4.isChecked()) {

        }
        if (operation_4.isChecked()) {

        }
        if (humility_4.isChecked()) {

        }
        if (maintainence_4.isChecked()) {

        }



        Spinner spinner_5 = (Spinner) view.findViewById(R.id.feedback_spinner_fifth_order);
        spinner_5.setAdapter(lunchList);
        spinner_5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                fifth_choise = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CheckBox function_5 = (CheckBox) view.findViewById(R.id.rbtn_fifth_order_reason_function);
        CheckBox beauty_5 = (CheckBox) view.findViewById(R.id.rbtn_fifth_order_reason_beauty);
        CheckBox operation_5 = (CheckBox) view.findViewById(R.id.rbtn_fifth_order_reason_operation);
        CheckBox humility_5 = (CheckBox) view.findViewById(R.id.rbtn_fifth_order_reason_humility);
        CheckBox maintainence_5 = (CheckBox) view.findViewById(R.id.rbtn_fifth_order_reason_maintainence);
        if (function_5.isChecked()) {

        }
        if (beauty_5.isChecked()) {

        }
        if (operation_5.isChecked()) {

        }
        if (humility_5.isChecked()) {

        }
        if (maintainence_5.isChecked()) {

        }

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q4();
                relativeLayout.setVisibility(View.GONE);
            }
        });

    }

    private void q4() {
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_4);
        relativeLayout.setVisibility(View.VISIBLE);
        getView().post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        });

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q3();
                relativeLayout.setVisibility(View.GONE);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_4);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_4);

        RadioGroup rGroup = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_4);
        if (rGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_4_no) {

        } else if (rGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_4_yes) {

        }

        Spinner spinner1 = (Spinner) view.findViewById(R.id.feedback_spinner_equip_1);
        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerList);
        spinner1.setAdapter(lunchList);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                subscription1 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner2 = (Spinner) view.findViewById(R.id.feedback_spinner_equip_2);
        spinner2.setAdapter(lunchList);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                subscription2 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner3 = (Spinner) view.findViewById(R.id.feedback_spinner_equip_3);
        spinner3.setAdapter(lunchList);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                subscription3 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q5();
                relativeLayout.setVisibility(View.GONE);
            }
        });

    }

    private void q5() {
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_5);
        relativeLayout.setVisibility(View.VISIBLE);
        getView().post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        });

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q4();
                relativeLayout.setVisibility(View.GONE);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_5);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_5);

        Spinner spinner1 = (Spinner) view.findViewById(R.id.feedback_spinner_free_1);
        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerList);
        spinner1.setAdapter(lunchList);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                install1 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner2 = (Spinner) view.findViewById(R.id.feedback_spinner_free_2);
        spinner2.setAdapter(lunchList);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                install2 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner3 = (Spinner) view.findViewById(R.id.feedback_spinner_free_3);
        spinner3.setAdapter(lunchList);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                install3 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner4 = (Spinner) view.findViewById(R.id.feedback_spinner_free_4);
        spinner4.setAdapter(lunchList);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                install4 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner5 = (Spinner) view.findViewById(R.id.feedback_spinner_free_5);
        spinner5.setAdapter(lunchList);
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                install5 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q6();
                relativeLayout.setVisibility(View.GONE);
            }
        });

    }

    private void q6() {
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_6);
        relativeLayout.setVisibility(View.VISIBLE);
        getView().post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        });

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q5();
                relativeLayout.setVisibility(View.GONE);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_6);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_6);

        Spinner spinner1 = (Spinner) view.findViewById(R.id.feedback_spinner_impressed_1);
        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerList);
        spinner1.setAdapter(lunchList);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                impression1 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner2 = (Spinner) view.findViewById(R.id.feedback_spinner_impressed_2);
        spinner2.setAdapter(lunchList);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                impression2 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner3 = (Spinner) view.findViewById(R.id.feedback_spinner_impressed_3);
        spinner3.setAdapter(lunchList);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                impression3 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner4 = (Spinner) view.findViewById(R.id.feedback_spinner_impressed_4);
        spinner4.setAdapter(lunchList);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                impression4 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner5 = (Spinner) view.findViewById(R.id.feedback_spinner_impressed_5);
        spinner5.setAdapter(lunchList);
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String device = spinnerList.get(position);
                impression5 = getDeviceIdFromSpinnerListName(device);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q7();
                relativeLayout.setVisibility(View.GONE);
            }
        });

    }

    private void q7() {
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_7);
        relativeLayout.setVisibility(View.VISIBLE);
        getView().post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        });

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q6();
                relativeLayout.setVisibility(View.GONE);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_7);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_7);

        RadioGroup rGroup = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_7);
        if (rGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_7_no) {
            buy = 1;
        } else if (rGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_7_yes) {
            buy = 2;
        }

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q8();
                relativeLayout.setVisibility(View.GONE);
            }
        });

    }

    private void q8() {
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_8);
        radioGroup.setVisibility(View.VISIBLE);
        getView().post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        });

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q7();
                radioGroup.setVisibility(View.GONE);
            }
        });

        if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_10k) {
            reasonable_price = 1;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_50k) {
            reasonable_price = 2;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_100k) {
            reasonable_price = 3;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_200k) {
            reasonable_price = 4;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_over_200k) {
            reasonable_price = 5;
        }

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_8);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_8);

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update feedback to server
//                try {
//                    comm.uploadSecondSurveyData(attitude,
//                            functionality, visual, operability, user_friendly, price, maintenance, safety, energy,
//                            first_choise, second_choise, third_choise, fourth_choise, fifth_choise,
//                            first_consider, second_consider, third_consider, fourth_consider, fifth_consider,
//                            subscription1, subscription2, subscription3,
//                            install1, install2, install3, install4, install5,
//                            impression1, impression2, impression3, impression4, impression5,
//                            buy,
//                            reasonable_price);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                getActivity().onBackPressed();
            }
        });

    }

    public JSONArray queryDeviceNameByField(int filed) throws JSONException {
        SQLiteDatabase db = dbManager.getReadableDatabase();

        // get zone id
        Cursor cursor = db.rawQuery("select zone_id from zone where field_id=" + filed, null);
        cursor.moveToFirst();
        ArrayList<Integer> zoneIds_inField = new ArrayList<Integer>();
        while (cursor.isAfterLast() == false) {
            zoneIds_inField.add(cursor.getInt(cursor.getColumnIndex("zone_id")));
            cursor.moveToNext();
        }
        cursor.close();

        // get mode id
        ArrayList<Integer> modeIds_inField = new ArrayList<Integer>();
        for (int i = 0; i < zoneIds_inField.size(); i++) {
            JSONArray modes = dbManager.queryModeDataWithZoneId(zoneIds_inField.get(i));
            for (int j = 0; j < modes.length(); j++) {
                JSONObject mode = modes.getJSONObject(j);
                modeIds_inField.add(mode.getInt("mode_id"));
            }
        }

        // get device id
        JSONArray filePaths = new JSONArray();
        for (int i = 0; i < modeIds_inField.size(); i++) {
            JSONArray devices = dbManager.queryDeviceFilesWithModeId(modeIds_inField.get(i));
            for (int j = 0; j < devices.length(); j++) {
                JSONObject device = devices.getJSONObject(j);

                JSONObject file = new JSONObject();
                int devicd_id = device.getInt("device_id");
                String name = device.getString("name");
                String name_en = device.getString("name_en");
                file.put("device_id", devicd_id);
                file.put("name", name);
                file.put("name_en", name_en);
                filePaths.put(file);
            }
        }

        return filePaths;
    }

    private int getDeviceIdFromSpinnerListName(String name) {
        // the device name is [device id]. [device name]
        // split device with dot than device id will at first place
        String deviceIdString = name.split("\\.")[0];

        // if device id is not a number return 0;
        int deviceId;
        try {
            deviceId = Integer.parseInt(deviceIdString);
        } catch (NumberFormatException nfe) {
            deviceId = 0;
        }

        return deviceId;
    }

    public void feedbackAlertDialog(final Activity activity, final FeedbackFragment f) {
        final Dialog dialog = new Dialog(activity, R.style.selectorDialogTitle);
        dialog.setContentView(R.layout.alertdialog_feedback);
        dialog.setTitle(R.string.feedback_title);

        Button btnYes = (Button) dialog.findViewById(R.id.btn_confirm_alertdialog_feedback);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_skip_alertdialog_feedback);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = activity.getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.flayout_fragment_continer, f);
                transaction.commit();

                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
