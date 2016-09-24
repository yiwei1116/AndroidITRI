package com.uscc.ncku.androiditri.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uscc.ncku.androiditri.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseTemplate.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseTemplate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseTemplate extends Fragment {
    Context mContext;
    LayoutInflater mLayoutInflater;
    private static final int[] Template_Image = {
            R.drawable.card_1,
            R.drawable.card_2,
    };
    private OnFragmentInteractionListener mListener;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_choose_template, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar_camera);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolBarTxt = (TextView)view.findViewById(R.id.txt_toolbar_tour_select);
        toolBarTxt.setText(R.string.choose_template);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ViewPager viewPager;
        ChooseTemp adapter;
        viewPager = (ViewPager)view.findViewById(R.id.template_choose);
        adapter = new ChooseTemp(getActivity());
        viewPager.setAdapter(adapter);

        return view   ;
    }


    class ChooseTemp extends PagerAdapter {
        private  final int[] Template_Image = {
                R.drawable.card_1,
                R.drawable.card_2,
        };
        private LayoutInflater mLayoutInflater;
        public ChooseTemp(Context context){
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return Template_Image.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.item_template, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.templateview);
            imageView.setImageResource(Template_Image[position]);
            container.addView(itemView);
           /* Button btnNextStep = (Button)itemView.findViewById(R.id.btn_next_step);
            btnNextStep.setBackgroundResource(R.drawable.selector_btn_nextpage);
            btnNextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }

        @Override
        public float getPageWidth(int position) {
            return 0.8f;
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
