package com.uscc.ncku.androiditri.fragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;


import android.content.ContentResolver;
import android.database.Cursor;

import android.provider.MediaStore;

import android.widget.GridView;
import android.widget.ImageView;


import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomPhoto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomPhoto extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GridView gridView;
    private ImageView imageView;
    public List<String> thumbs;  //存放縮圖的id
    public static List<String> imagePaths ;  //存放圖片的路徑
    private ImageAdapter imageAdapter;  //用來顯示縮圖
    private Toolbar toolbar;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomPhoto.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomPhoto newInstance(String param1, String param2) {
        CustomPhoto fragment = new CustomPhoto();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_custom_photo, container, false);
        setHasOptionsMenu(true);
        gridView = (GridView) view.findViewById(R.id.gridView1);


        MainActivity.setToolbarTitle(R.string.choose_photo);

        toolbar = MainActivity.getToolbar();

        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_nextstep:
                        nextStep();
                        break;
                }
                return false;
            }
        });
      CustomPhoto();
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.choosephoto_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public void CustomPhoto() {
        MainActivity.hideMainBtn();

        ContentResolver cr = getActivity().getContentResolver();
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };

        //查詢SD卡的圖片
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);

        thumbs = new ArrayList<String>();
        imagePaths = new ArrayList<String>();

        for (int i = cursor.getCount()-1 ; i >= 0 ; i--) {

            cursor.moveToPosition(i);
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Images.Media._ID));// ID
            thumbs.add(id + "");

            String filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//抓路徑

            imagePaths.add(filepath);
            Log.e("filepath", filepath);
        }

        cursor.close();

        imageAdapter = new ImageAdapter(getActivity(), thumbs);
        gridView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
    }
    public void nextStep(){
        FragmentManager fm = getFragmentManager();
        ChooseTemplate CT = new ChooseTemplate();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.flayout_fragment_continer, CT );
        transaction.addToBackStack(null);
        transaction.commit();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.showDefaultToolbar();
        MainActivity.setToolbarTitle(R.string.nothing);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}


