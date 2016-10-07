package com.uscc.ncku.androiditri.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link MergeTemplatePic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MergeTemplatePic extends Fragment {
    private String templateIndex;
    private String WriteContext,BuildContext;
    private ImageView mergeImage;
    private TextView textView;

    private static final int[] Template_Image = {
            R.drawable.template_1,
            R.drawable.template_2,
    };
    public MergeTemplatePic() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MergeTemplatePic.
     */
    public static MergeTemplatePic newInstance() {
        MergeTemplatePic fragment = new MergeTemplatePic();
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
        MainActivity.transparateToolbar();
        MainActivity.setToolbarTitle(R.string.nothing);

        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        /**
         * 下面OOM Failed to allocate a 8294412 byte allocation with 7047616 free bytes and 6MB until OOM
         */
        View view =  inflater.inflate(R.layout.fragment_merge_template_pic, container, false);
        //FrameLayout frameLayout = (FrameLayout)view.findViewById(R.id.mergeFramelayout);
        textView = (TextView)view.findViewById(R.id.context);
        mergeImage = (ImageView)view.findViewById(R.id.mergeImage);
        Bundle bundle1 = getArguments();
       if(bundle1 != null) {
           templateIndex = (String) getArguments().get("TemplateNum");
           mergeImage.setImageResource(Template_Image[Integer.valueOf(templateIndex).intValue()]);
           WriteContext = (String) getArguments().get("WriteContext");
           BuildContext = (String) getArguments().get("BuildContext");

           if (WriteContext != null) {
               textView.setText(WriteContext);
           }
           Log.e("textview1",textView.getText().toString());
           if (BuildContext != null) {
               textView.setText(BuildContext);
           }
           Log.e("textview2",textView.getText().toString());
       }
        textView.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.showDefaultToolbar();
    }
}
