package com.uscc.ncku.androiditri;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    public final static String GET_TOUR_INDEX = "GET_TOUR_INDEX";
    public final static int[] TOUR_IMG = {
            R.drawable.tour_designer,
            R.drawable.tour_robot,
            R.drawable.tour_housekeeper
    };

    private int tourSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = this.getIntent().getExtras();
        tourSelect = bundle.getInt(GET_TOUR_INDEX);
    }

}
