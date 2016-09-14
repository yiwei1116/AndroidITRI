package com.uscc.ncku.androiditri;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SurveyActivity extends AppCompatActivity {

    private int tourIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView toolBarTxt = (TextView) findViewById(R.id.txt_toolbar_survey);
        toolBarTxt.setText(R.string.survey_title);


        Bundle bundle = this.getIntent().getExtras();
        tourIndex = bundle.getInt(MainActivity.GET_TOUR_INDEX);

        final Dialog dialog = new Dialog(SurveyActivity.this, R.style.selectorDialog);
        dialog.setContentView(R.layout.alertdialog_survey);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.2f;
        dialog.getWindow().setAttributes(lp);

        Button confirmBtn = (Button) dialog.findViewById(R.id.btn_confirm_alertdialog_survey);
        Button skipBtn = (Button) dialog.findViewById(R.id.btn_skip_alertdialog_survey);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageGender();
                dialog.dismiss();
            }
        });
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SurveyActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.GET_TOUR_INDEX, tourIndex);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        dialog.show();

    }

    private void pageGender() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page1);
        layout.setVisibility(View.VISIBLE);

        TextView page = (TextView) findViewById(R.id.survey_page_txt);
        page.setText(R.string.one_twelfth);

        TextView pageTitle = (TextView) findViewById(R.id.survey_page_title);
        pageTitle.setText(R.string.survey01_gender);

        Button btnFemale = (Button) findViewById(R.id.survey_page1_female);
        Button btnMale = (Button) findViewById(R.id.survey_page1_male);
        btnFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageAge();
            }
        });
        btnMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageAge();
            }
        });
    }

    private void pageAge() {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page2);
        layout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageGender();
            }
        });

        TextView page = (TextView) findViewById(R.id.survey_page_txt);
        page.setText(R.string.two_twelfth);

        TextView pageTitle = (TextView) findViewById(R.id.survey_page_title);
        pageTitle.setText(R.string.survey02_age);

        Button btn0 = (Button) findViewById(R.id.survey_page2_below_20);
        Button btn1 = (Button) findViewById(R.id.survey_page2_20_25);
        Button btn2 = (Button) findViewById(R.id.survey_page2_26_30);
        Button btn3 = (Button) findViewById(R.id.survey_page2_31_40);
        Button btn4 = (Button) findViewById(R.id.survey_page2_41_50);
        Button btn5 = (Button) findViewById(R.id.survey_page2_51_60);
        Button btn6 = (Button) findViewById(R.id.survey_page2_61_65);
        Button btn7 = (Button) findViewById(R.id.survey_page2_over_66);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageEducation();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageEducation();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageEducation();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageEducation();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageEducation();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageEducation();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageEducation();
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageEducation();
            }
        });
    }

    private void pageEducation() {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page3);
        layout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageAge();
            }
        });

        TextView page = (TextView) findViewById(R.id.survey_page_txt);
        page.setText(R.string.three_twelfth);

        TextView pageTitle = (TextView) findViewById(R.id.survey_page_title);
        pageTitle.setText(R.string.survey03_education);

        Button btn0 = (Button) findViewById(R.id.survey_page3_high_school);
        Button btn1 = (Button) findViewById(R.id.survey_page3_college);
        Button btn2 = (Button) findViewById(R.id.survey_page3_university);
        Button btn3 = (Button) findViewById(R.id.survey_page3_master);
    }



}
