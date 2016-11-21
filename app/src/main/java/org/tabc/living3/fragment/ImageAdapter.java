package org.tabc.living3.fragment;

/**
 * Created by yiwei on 2016/9/28.
 */

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import org.tabc.living3.R;

import java.io.File;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

     String uri;
     Context context;
     List coll;
     CustomPhoto customPhoto;
    public ImageAdapter(Context context, List coll) {

        super();
        this.context = context;
        this.coll = coll;

    }
    class ImageAdapterHolder{


        ImageView imageView;
        ViewGroup layout;



    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageAdapterHolder Holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            Holder = new ImageAdapterHolder();
            convertView = inflater.inflate(R.layout.item_photo, parent, false);
            Holder.layout = (ViewGroup) convertView.findViewById(R.id.rl_item_photo);
            Holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            Holder.layout.setTag(Holder);

        }

        else
        {
            Holder = (ImageAdapterHolder)convertView.getTag();
        }


        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dd = dm.density;
        float px = 25 * dd;
        float screenWidth = dm.widthPixels;
        int newWidth = (int) (screenWidth - px) / 4; // 一行顯示四個縮圖

        Holder.layout.setLayoutParams(new GridView.LayoutParams(newWidth, newWidth));
        Holder.imageView.setId(position);

        uri = customPhoto.imagePaths.get(position);

        Picasso.with(context)
                .load(new File(uri))
                .fit().centerCrop()
                .into(Holder.imageView);


        return convertView;
    }


    @Override
    public int getCount() {
        return coll.size();
    }

    @Override
    public Object getItem(int arg0) {
           return coll.get(arg0);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

}