package com.uscc.ncku.androiditri;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SurveyActivity extends AppCompatActivity {
    public static SurveyActivity instance = null;

    private int tourIndex;
    private boolean menuHide = true;

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
        instance = this;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // show survey popup dialog
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

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuHide) {
            return false;
        } else {
            getMenuInflater().inflate(R.menu.survey_menu, menu);
            return true;
        }
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
        menuHide = true;

        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page3);
        layout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageOccupation();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageOccupation();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageOccupation();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageOccupation();
            }
        });
    }

    private void pageOccupation() {
        menuHide = false;

        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page4);
        layout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageEducation();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
                return true;
            }
        });

        TextView page = (TextView) findViewById(R.id.survey_page_txt);
        page.setText(R.string.four_twelfth);

        TextView pageTitle = (TextView) findViewById(R.id.survey_page_title);
        pageTitle.setText(R.string.survey04_occupation);

        Button btn0 = (Button) findViewById(R.id.survey_page4_student);
        Button btn1 = (Button) findViewById(R.id.survey_page4_government);
        Button btn2 = (Button) findViewById(R.id.survey_page4_education);
        Button btn3 = (Button) findViewById(R.id.survey_page4_building);
        Button btn4 = (Button) findViewById(R.id.survey_page4_electronic);
        Button btn5 = (Button) findViewById(R.id.survey_page4_service);
        Button btn6 = (Button) findViewById(R.id.survey_page4_real_estate);
        Button btn7 = (Button) findViewById(R.id.survey_page4_communication);
        Button btn8 = (Button) findViewById(R.id.survey_page4_finance_insurance);
        Button btn9 = (Button) findViewById(R.id.survey_page4_manufacture);
        Button btn10 = (Button) findViewById(R.id.survey_page4_houskeep);
        Button btn11 = (Button) findViewById(R.id.survey_page4_other);

        if (getResources().getString(R.string.survey04_occupation).equals("Occupation")) {
            btn8.setTextSize(12);
            btn7.setTextSize(12);
        }

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
    }

    private void pageExperiment() {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page5);
        layout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageOccupation();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                layout.setVisibility(View.INVISIBLE);
                pageIncome();
                return true;
            }
        });

        TextView page = (TextView) findViewById(R.id.survey_page_txt);
        page.setText(R.string.five_twelfth);

        TextView pageTitle = (TextView) findViewById(R.id.survey_page_title);
        pageTitle.setText(R.string.survey05_experience);

        Button btn0 = (Button) findViewById(R.id.survey_page5_none);
        Button btn1 = (Button) findViewById(R.id.survey_page5_1y);
        Button btn2 = (Button) findViewById(R.id.survey_page5_3y);
        Button btn3 = (Button) findViewById(R.id.survey_page5_5y);
        Button btn4 = (Button) findViewById(R.id.survey_page5_10y);
        Button btn5 = (Button) findViewById(R.id.survey_page5_over_10y);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageIncome();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageIncome();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageIncome();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageIncome();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageIncome();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageIncome();
            }
        });
    }

    private void pageIncome() {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page6);
        layout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageExperiment();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                layout.setVisibility(View.INVISIBLE);
                pageResidence();
                return true;
            }
        });

        TextView page = (TextView) findViewById(R.id.survey_page_txt);
        page.setText(R.string.six_twelfth);

        TextView pageTitle = (TextView) findViewById(R.id.survey_page_title);
        pageTitle.setText(R.string.survey06_income);

        Button btn0 = (Button) findViewById(R.id.survey_page6_5k_10k);
        Button btn1 = (Button) findViewById(R.id.survey_page6_11k_20k);
        Button btn2 = (Button) findViewById(R.id.survey_page6_21k_30k);
        Button btn3 = (Button) findViewById(R.id.survey_page6_31k_40k);
        Button btn4 = (Button) findViewById(R.id.survey_page6_41k_50k);
        Button btn5 = (Button) findViewById(R.id.survey_page6_51k_60k);
        Button btn6 = (Button) findViewById(R.id.survey_page6_over_60k);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageResidence();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageResidence();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageResidence();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageResidence();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageResidence();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageResidence();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageResidence();
            }
        });
    }

    private void pageResidence() {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page7);
        layout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageIncome();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                layout.setVisibility(View.INVISIBLE);
                pageHouse();
                return true;
            }
        });

        TextView page = (TextView) findViewById(R.id.survey_page_txt);
        page.setText(R.string.seven_twelfth);

        TextView pageTitle = (TextView) findViewById(R.id.survey_page_title);
        pageTitle.setText(R.string.survey07_residence);

        Button btn0 = (Button) findViewById(R.id.survey_page7_north);
        Button btn1 = (Button) findViewById(R.id.survey_page7_middle);
        Button btn2 = (Button) findViewById(R.id.survey_page7_south);
        Button btn3 = (Button) findViewById(R.id.survey_page7_east);
        Button btn4 = (Button) findViewById(R.id.survey_page7_outside);
        Button btn5 = (Button) findViewById(R.id.survey_page7_other);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageHouse();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageHouse();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageHouse();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageHouse();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageHouse();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageHouse();
            }
        });
    }

    private void pageHouse() {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page8);
        layout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageResidence();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyOrganization();
                return true;
            }
        });

        TextView page = (TextView) findViewById(R.id.survey_page_txt);
        page.setText(R.string.eight_twelfth);

        TextView pageTitle = (TextView) findViewById(R.id.survey_page_title);
        pageTitle.setText(R.string.survey08_house);

        Button btn0 = (Button) findViewById(R.id.survey_page8_apartment);
        Button btn1 = (Button) findViewById(R.id.survey_page8_revenue);
        Button btn2 = (Button) findViewById(R.id.survey_page8_mixed);
        Button btn3 = (Button) findViewById(R.id.survey_page8_townhouse);
        Button btn4 = (Button) findViewById(R.id.survey_page8_other);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyOrganization();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyOrganization();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyOrganization();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyOrganization();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyOrganization();
            }
        });
    }

    private void pageFamilyOrganization() {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page9);
        layout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageHouse();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyMember();
                return true;
            }
        });

        TextView page = (TextView) findViewById(R.id.survey_page_txt);
        page.setText(R.string.night_twelfth);

        TextView pageTitle = (TextView) findViewById(R.id.survey_page_title);
        pageTitle.setText(R.string.survey09_family_organization);

        Button btn0 = (Button) findViewById(R.id.survey_page9_core);
        Button btn1 = (Button) findViewById(R.id.survey_page9_single);
        Button btn2 = (Button) findViewById(R.id.survey_page9_twice);
        Button btn3 = (Button) findViewById(R.id.survey_page9_single_parent);
        Button btn4 = (Button) findViewById(R.id.survey_page9_three);
        Button btn5 = (Button) findViewById(R.id.survey_page9_other);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyMember();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyMember();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyMember();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyMember();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyMember();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyMember();
            }
        });
    }

    private void pageFamilyMember() {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page10);
        layout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyOrganization();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                layout.setVisibility(View.INVISIBLE);
                pageNotice();
                return true;
            }
        });

        TextView page = (TextView) findViewById(R.id.survey_page_txt);
        page.setText(R.string.ten_twelfth);

        TextView pageTitle = (TextView) findViewById(R.id.survey_page_title);
        pageTitle.setText(R.string.survey10_family);

        Button btn0 = (Button) findViewById(R.id.btn_survey_page10_confirm);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageNotice();
            }
        });
    }

    private void pageNotice() {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page11);
        layout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageFamilyMember();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                layout.setVisibility(View.INVISIBLE);
                pagePersonal();
                return true;
            }
        });

        TextView page = (TextView) findViewById(R.id.survey_page_txt);
        page.setText(R.string.eleven_twelfth);

        TextView pageTitle = (TextView) findViewById(R.id.survey_page_title);
        pageTitle.setText(R.string.survey11_notice);

        Button btn0 = (Button) findViewById(R.id.btn_survey_page11_confirm);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.edtxt_survey_page11);
                String notice = editText.getText().toString();

                layout.setVisibility(View.INVISIBLE);
                pagePersonal();
            }
        });
    }

    private void pagePersonal() {
        menuHide = true;

        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.survey_page12);
        layout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                pageNotice();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(SurveyActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.GET_TOUR_INDEX, tourIndex);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }
        });

        TextView page = (TextView) findViewById(R.id.survey_page_txt);
        page.setText(R.string.twelve_twelfth);

        TextView pageTitle = (TextView) findViewById(R.id.survey_page_title);
        pageTitle.setText(R.string.survey12_personal);

        Button btn0 = (Button) findViewById(R.id.btn_survey_page12_confirm);
        Button btn1 = (Button) findViewById(R.id.btn_survey_page12_skip);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEdtxt = (EditText) findViewById(R.id.edtxt_survey_page12_name);
                EditText mailEdtxt = (EditText) findViewById(R.id.edtxt_survey_page12_mail) ;
                String name = nameEdtxt.getText().toString();
                String mail = mailEdtxt.getText().toString();

                Intent intent = new Intent(SurveyActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.GET_TOUR_INDEX, tourIndex);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SurveyActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.GET_TOUR_INDEX, tourIndex);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        instance = null;
    }

}
