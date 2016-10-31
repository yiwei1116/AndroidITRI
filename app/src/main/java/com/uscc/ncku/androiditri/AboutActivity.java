package com.uscc.ncku.androiditri;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    public static AboutActivity instance = null;

    private boolean isEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        instance = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.nothing);

        Bundle bundle = this.getIntent().getExtras();
        isEnglish = bundle.getBoolean(MainActivity.GET_TOUR_INDEX);
        originPage();

    }

    private void originPage() {
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0,0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView titleTxt = (TextView) findViewById(R.id.txt_toolbar_about);
        titleTxt.setText(R.string.origin_exhibition);

        TextView contentTxt = (TextView) findViewById(R.id.txt_content_about);
        String content = getString(R.string.origin_content);
        contentTxt.setText(content);

        Button nextBtn = (Button) findViewById(R.id.btn_next_about);
        nextBtn.setText(R.string.next_page);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guidePage();
            }
        });
    }

    private void guidePage() {
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0,0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundResource(R.drawable.header_blank);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originPage();
            }
        });

        TextView titleTxt = (TextView) findViewById(R.id.txt_toolbar_about);
        titleTxt.setText(R.string.guide_introduction);

        TextView contentTxt = (TextView) findViewById(R.id.txt_content_about);
        String content = getString(R.string.tour_content);
        contentTxt.setText(content);

        Button nextBtn = (Button) findViewById(R.id.btn_next_about);
        nextBtn.setText(R.string.next_page);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rulePage();
            }
        });
    }

    private void rulePage() {
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0,0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundResource(R.drawable.header_blank);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guidePage();
            }
        });

        TextView titleTxt = (TextView) findViewById(R.id.txt_toolbar_about);
        titleTxt.setText(R.string.rules_automated_guide);

        TextView contentTxt = (TextView) findViewById(R.id.txt_content_about);
        String content = getString(R.string.rule_content);
        contentTxt.setText(Html.fromHtml(content));

        Button nextBtn = (Button) findViewById(R.id.btn_next_about);
        nextBtn.setText(R.string.ok);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, TourSelectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(MainActivity.GET_IS_ENGLISH, isEnglish);
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
