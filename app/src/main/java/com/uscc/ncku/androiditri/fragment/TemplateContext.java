package com.uscc.ncku.androiditri.fragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uscc.ncku.androiditri.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TemplateContext#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateContext extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button writeContext,buildContext;
    private EditText editText;

    public TemplateContext() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TemplateContext.
     */
    // TODO: Rename and change types and number of parameters
    public static TemplateContext newInstance(String param1, String param2) {
        TemplateContext fragment = new TemplateContext();
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
        View view = inflater.inflate(R.layout.fragment_template_context, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar_context);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        writeContext = (Button)view.findViewById(R.id.writeContext) ;
        buildContext = (Button)view.findViewById(R.id.buildContext) ;
        writeContext.setBackgroundResource(R.drawable.context_btn_write);
        buildContext.setBackgroundResource(R.drawable.context_btn_write_1);
        editText = (EditText)view.findViewById(R.id.edit);
        writeContext.setOnClickListener(this);
        buildContext.setOnClickListener(this);
        return view;
    }
    public void writeText(){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setHint(null);
            }
        });



    }
    public void buildText(){




    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.writeContext:
                writeText();

                break;
            case R.id.buildContext:
                buildText();

                break;
        }
    }
}
