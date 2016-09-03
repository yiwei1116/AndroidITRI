package com.uscc.ncku.androiditri;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.nothing);

        originPage();

    }

    private void originPage() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView toolBarTxt = (TextView) findViewById(R.id.txt_toolbar_about);
        toolBarTxt.setText(R.string.about_origin);

        TextView contentTxt = (TextView) findViewById(R.id.txt_content_about);
        contentTxt.setText(R.string.origin_content);

        Button nextBtn = (Button) findViewById(R.id.btn_next_about);
        nextBtn.setBackgroundResource(R.drawable.selecter_btn_next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourPage();
            }
        });
    }

    private void tourPage() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originPage();
            }
        });

        TextView toolBarTxt = (TextView) findViewById(R.id.txt_toolbar_about);
        toolBarTxt.setText(R.string.about_tour);

        TextView contentTxt = (TextView) findViewById(R.id.txt_content_about);
        contentTxt.setText(R.string.tour_content);

        Button nextBtn = (Button) findViewById(R.id.btn_next_about);
        nextBtn.setBackgroundResource(R.drawable.selecter_btn_next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rulePage();
            }
        });
    }

    private void rulePage() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourPage();
            }
        });

        TextView toolBarTxt = (TextView) findViewById(R.id.txt_toolbar_about);
        toolBarTxt.setText(R.string.about_rule);

        TextView contentTxt = (TextView) findViewById(R.id.txt_content_about);
        String content = getString(R.string.rule_content);
        contentTxt.setText(Html.fromHtml(content));

        Button nextBtn = (Button) findViewById(R.id.btn_next_about);
        nextBtn.setBackgroundResource(R.drawable.selecter_btn_ok);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

}
