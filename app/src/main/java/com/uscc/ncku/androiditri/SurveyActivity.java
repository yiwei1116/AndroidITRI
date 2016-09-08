package com.uscc.ncku.androiditri;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
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
            //The key argument here must match that used in the other activity
        }

        LayoutInflater inflater = this.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflater.inflate(R.layout.alertdialog_survey, null));
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

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
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                finish();
            }
        });
        dialog.show();


    }

}
