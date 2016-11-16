package com.uscc.ncku.androiditri.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;
import com.uscc.ncku.androiditri.util.HelperFunctions;
import com.uscc.ncku.androiditri.util.SQLiteDbManager;
import com.uscc.ncku.androiditri.util.TourViewPager;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link ChooseTemplate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseTemplate extends Fragment {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ViewPager viewPager;
    private ChooseTemp adapter;
    private TemplateContext TC;
    private int viewPageIndex;
    private Bundle bundle1;
    private String photoUri,picPath,flagSelect;
    private SQLiteDbManager sqliteDbManager;
    private List<String> imageList = new ArrayList<>();
    private ArrayList<Bitmap> bitmapArray;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChooseTemplate.
     */
    public static ChooseTemplate newInstance() {
        ChooseTemplate fragment = new ChooseTemplate();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_choose_template, container, false);

        ((MainActivity) getActivity()).transparateToolbar();
        ((MainActivity) getActivity()).hideMainBtn();
        ((MainActivity) getActivity()).setToolbarTitle(R.string.choose_template);
        sqliteDbManager = new SQLiteDbManager(getActivity(), SQLiteDbManager.DATABASE_NAME);
        imageList = sqliteDbManager.getHipsterTemplateDownloadFiles();

        double x =0.5;
        double y = 0.5;
        float xx = (float) x;
        float yy = (float) y;
         bitmapArray = new ArrayList<Bitmap>();

        for(int i=0;i<imageList.size();i++){
            try {
                Matrix matrix = new Matrix();

                matrix.postScale(xx, yy);
                Bitmap resizedBitmap = Bitmap.createBitmap(HelperFunctions.getBitmapFromFile(getActivity(),imageList.get(i)), 0, 0, 1080,1920, matrix, true);
                bitmapArray.add(resizedBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.e("imageList",String.valueOf(imageList.size()));

        }
        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        bundle1 = getArguments();
        if (bundle1 != null) {
            photoUri = (String)getArguments().get("photoUri");
            picPath = (String)getArguments().get("picPath");
            flagSelect = (String)getArguments().get("flagSelect");


        }
        Button btnNextStep = (Button)view.findViewById(R.id.btn_next_step);
        btnNextStep.setBackgroundResource(R.drawable.camera_btn_select);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TC = new TemplateContext();
                Bundle bundle = new Bundle();
                bundle.putString("Template", String.valueOf(viewPageIndex));
                if (flagSelect.equals("true")) {


                    bundle.putString("picPath", picPath);
                } else {

                    bundle.putString("picPath", photoUri);

                }

                TC.setArguments(bundle);
                ((MainActivity) getActivity()).replaceFragment(TC);

            }
        });

        viewPager = (ViewPager)view.findViewById(R.id.template_choose);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPageIndex = position;
                Log.e("position", String.valueOf(position));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        adapter = new ChooseTemp(getActivity());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.viewpager_dis));
        return view   ;
    }


    class ChooseTemp extends PagerAdapter {



        private HelperFunctions helperFunctions = new HelperFunctions();
        private LayoutInflater mLayoutInflater;

        public ChooseTemp(Context context){
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.item_template, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.templateview);


            imageView.setImageBitmap(bitmapArray.get(position));
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        @Override
        public float getPageWidth(int position) {
            return 0.8f;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).showDefaultToolbar();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

}