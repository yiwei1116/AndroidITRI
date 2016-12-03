package org.tabc.living3.fragment;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
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

import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


import org.tabc.living3.MainActivity;
import org.tabc.living3.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomPhoto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomPhoto extends Fragment  {

    private GridView gridView;
    private ImageView imageView;
    public List<String> thumbs;  //存放縮圖的id
    public static List<String> imagePaths ;  //存放圖片的路徑
    private ImageAdapter imageAdapter;  //用來顯示縮圖
    private Toolbar toolbar;
    public String photoUri,flagSelect;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CustomPhoto.
     */
    public static CustomPhoto newInstance(String param1, String param2) {
        CustomPhoto fragment = new CustomPhoto();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_custom_photo, container, false);
        setHasOptionsMenu(true);
        gridView = (GridView) view.findViewById(R.id.gridView1);


        ((MainActivity) getActivity()).setToolbarTitle(R.string.choose_photo);
        Bundle bundle1 = getArguments();
        if(bundle1 != null) {
            flagSelect  = (String) getArguments().get("flagSelect");

        }
        toolbar = ((MainActivity) getActivity()).getToolbar();

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
                switch (item.getItemId()) {
                    case R.id.menu_nextstep:
                        if(photoUri==null){
                            Toast.makeText(getActivity(),R.string.please_select_the_picture, Toast.LENGTH_LONG).show();
                        }
                        else{
                        nextStep();
                        }
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
        ((MainActivity) getActivity()).hideMainBtn();

        ContentResolver cr = getActivity().getContentResolver();
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };

        //查詢SD卡的圖片
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);

        thumbs = new ArrayList<String>();
        imagePaths = new ArrayList<String>();

        for (int i = (cursor.getCount()-1) ; i >= 0 ; i--) {

            cursor.moveToPosition(i);
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Images.Media._ID));// ID
            thumbs.add(id + "");

            String filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//抓路徑

            imagePaths.add(filepath);

        }

        cursor.close();

        imageAdapter = new ImageAdapter(getActivity(), thumbs);
        gridView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {

                photoUri = imagePaths.get(position);
            }
        });
    }

    public void nextStep(){
        ChooseTemplate chooseTemplate = new ChooseTemplate();
        Bundle bundle = new Bundle();
        bundle.putString("flagSelect", String.valueOf(flagSelect));
        bundle.putString("photoUri", photoUri);
        chooseTemplate.setArguments(bundle);
        ((MainActivity) getActivity()).replaceFragment(chooseTemplate);
        Log.e("flagSelect", flagSelect);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).showDefaultToolbar();
        ((MainActivity) getActivity()).setToolbarTitle(R.string.nothing);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}


