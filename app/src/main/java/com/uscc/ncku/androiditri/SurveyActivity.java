package com.uscc.ncku.androiditri;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class SurveyActivity extends AppCompatActivity {

    private String tourSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_survey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView toolBarTxt = (TextView) findViewById(R.id.txt_toolbar_survey);
        toolBarTxt.setText(R.string.survey_title);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tourSelect = extras.getString("EXTRA_SESSION_ID");
        }

        Dialog dialog = new Dialog(SurveyActivity.this, R.style.selectorDialog);
        dialog.setContentView(R.layout.alertdialog_survey);

        WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
        lp.dimAmount=0.2f;
        dialog.getWindow().setAttributes(lp);

        Button confirmBtn = (Button) dialog.findViewById(R.id.btn_confirm_alertdialog_survey);
        Button skipBtn = (Button) dialog.findViewById(R.id.btn_skip_alertdialog_survey);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SurveyActivity.this, MainActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", tourSelect);
                startActivity(intent);
            }
        });

        dialog.show();

    }

}
