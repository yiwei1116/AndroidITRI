package com.uscc.ncku.androiditri.fragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.uscc.ncku.androiditri.CommunicationWithServer;
import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;
import com.uscc.ncku.androiditri.util.SQLiteDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TemplateContext#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateContext extends Fragment {
    private ToggleButton writeContext,buildContext;
    private EditText editText;
    private RadioGroup radiogroup1;
    private FrameLayout write,build;
    private String templateIndex;
    private String textBulid ;
    MergeTemplatePic MTP;
    private String StringContext;
    private  Bundle bundle1;
    private  Spinner spinner;
    private RadioButton radioSexButton;
    private int selectedId;
    public String db_name = "android_itri_1.db";
    public String table_name_zone = "zone";
    private SQLiteDbManager dbManager;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private CommunicationWithServer communicationWithServer = new CommunicationWithServer();
    public TemplateContext() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TemplateContext.
     */
    public static TemplateContext newInstance() {
        TemplateContext fragment = new TemplateContext();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new SQLiteDbManager(getActivity(), db_name);
        db = dbManager.getReadableDatabase();
        cursor = db.query(table_name_zone,null,null,null,null,null,null);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_template_context, container, false);
        spinner = (Spinner) view.findViewById(R.id.spinner_item);

        spinnerArea();

        MainActivity.setToolbarTitle(R.string.text_master);

        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        writeContext = (ToggleButton)view.findViewById(R.id.writeContext) ;
        buildContext = (ToggleButton)view.findViewById(R.id.buildContext) ;
       /* writeContext = (Button)view.findViewById(R.id.writeContext) ;
        buildContext = (Button)view.findViewById(R.id.buildContext) ;
        writeContext.setBackgroundResource(R.drawable.context_btn_write);
        buildContext.setBackgroundResource(R.drawable.context_btn_write_1);*/
        writeContext.setBackgroundResource(R.drawable.btn_left_active);
        buildContext.setBackgroundResource(R.drawable.btn_right_normal);
        editText = (EditText)view.findViewById(R.id.edit);
    /*        writeContext.setOnClickListener(this);
            buildContext.setOnClickListener(this);*/
        radiogroup1 = (RadioGroup)view.findViewById(R.id.rGroup);
        writeContext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (writeContext.isChecked()) {
                    buildContext.setChecked(false);
                    editText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText.setHint(null);
                        }
                    });
                    write.setVisibility(View.VISIBLE);
                    build.setVisibility(View.GONE);
                    writeContext.setBackgroundResource(R.drawable.btn_left_active);
                    buildContext.setBackgroundResource(R.drawable.btn_right_normal);
                    textBulid = null;
                    Log.e("textBulid", String.valueOf(isChecked));


                }
            }
        });
        buildContext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buildContext.isChecked()) {
                    writeContext.setChecked(false);
                    write.setVisibility(View.GONE);
                    build.setVisibility(View.VISIBLE);
                    writeContext.setBackgroundResource(R.drawable.btn_left_normal);
                    buildContext.setBackgroundResource(R.drawable.btn_right_active);
                    StringContext = null;
                    Log.e("buildtext", String.valueOf(isChecked));


                }
            }
        });

        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    RadioButton btn = (RadioButton) rg.getChildAt(i);
                    if (btn.getId() == checkedId) {
                        textBulid = (String) btn.getText();
                        Log.e("String", textBulid);
                        return;
                    }
                }
            }
        });
        write = (FrameLayout)view.findViewById(R.id.write);
        build = (FrameLayout)view.findViewById(R.id.build);
        Bundle bundle = getArguments();
        if (bundle != null) {
            templateIndex = (String)getArguments().get("Template");

            Log.e("templateContext",templateIndex);

        }

        //writeText();

        Button btnNextStep = (Button)view.findViewById(R.id.btn_next_step);
        btnNextStep.setBackgroundResource(R.drawable.camera_btn_select);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MTP = new MergeTemplatePic();
                bundle1 = new Bundle();
                StringContext = editText.getText().toString().trim();

                Log.e("StringContext", StringContext);
                bundle1.putString("TemplateNum", templateIndex);
                bundle1.putString("WriteContext", StringContext);
                bundle1.putString("BuildContext", textBulid);
                MTP.setArguments(bundle1);
                replaceFragment(MTP);
            }
        });
        return view;
    }
 /*   public void writeText() {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setHint(null);
            }
        });
        write.setVisibility(View.VISIBLE);
        build.setVisibility(View.GONE);
        writeContext.setBackgroundResource(R.drawable.btn_left_active);
        buildContext.setBackgroundResource(R.drawable.btn_right_normal);


    }
    public void buildText(){
        write.setVisibility(View.GONE);
        build.setVisibility(View.VISIBLE);
        writeContext.setBackgroundResource(R.drawable.btn_left_normal);
        buildContext.setBackgroundResource(R.drawable.btn_right_active);

    }*/
    public void spinnerArea(){


        arrayList.add("請選擇");
        getZone();
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayList);
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {

                /*String city = "The Area is " + parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), city, Toast.LENGTH_LONG).show();*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

    }

    private void replaceFragment (Fragment fragment) {
        String fragmentTag = fragment.getClass().getSimpleName();
        LinkedList<Fragment> fragmentBackStack = MainActivity.getFragmentBackStack();

        // find fragment in back stack
        int i = 0;
        while (i < fragmentBackStack.size()) {
            Fragment f = fragmentBackStack.get(i);
            if (f.getClass().getSimpleName().equals(fragmentTag)) {
                fragmentBackStack.remove(i);
                break;
            }
            i++;
        }

        // add current fragment to back stack
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);
        fragmentBackStack.addFirst(currentFragment);

        // replace fragment with input fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.flayout_fragment_continer, fragment, fragmentTag);
        ft.commit();
    }
        public void getZone(){
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

                arrayList.add(cursor.getString(cursor.getColumnIndex("name_en")));




            }

            cursor.close();

        }

 /*   @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.writeContext:
                writeText();

                break;
            case R.id.buildContext:
                buildText();

                break;
        }
    }*/
}
