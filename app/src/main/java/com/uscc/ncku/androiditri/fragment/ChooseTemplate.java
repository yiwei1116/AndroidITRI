package com.uscc.ncku.androiditri.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;
import com.uscc.ncku.androiditri.util.TourViewPager;

import org.w3c.dom.Text;

import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link ChooseTemplate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseTemplate extends Fragment {
    Context mContext;
    LayoutInflater mLayoutInflater;
    ViewPager viewPager;
    ChooseTemp adapter;
    TemplateContext TC;
    private int viewPageIndex;

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

        MainActivity.transparateToolbar();
        MainActivity.hideMainBtn();
        MainActivity.setToolbarTitle(R.string.choose_template);

        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        Button btnNextStep = (Button)view.findViewById(R.id.btn_next_step);
        btnNextStep.setBackgroundResource(R.drawable.camera_btn_select);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TC = new TemplateContext();
                Bundle bundle = new Bundle();
                bundle.putString("Template", String.valueOf(viewPageIndex));
                TC.setArguments(bundle);
                replaceFragment(TC);

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
                Log.e("position",String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter = new ChooseTemp(getActivity());
        viewPager.setAdapter(adapter);
        return view   ;
    }

    private void replaceFragment (Fragment fragment) {
        String fragmentTag = fragment.getClass().getSimpleName();
        LinkedList<Fragment> fragmentBackStack = MainActivity.getFragmentBackStack();

        // find fragment in back stack
        int i = 0;
        while (i < fragmentBackStack.size()) {
            Fragment f = fragmentBackStack.get(i);
            if (f.getClass().getSimpleName().equals(fragmentTag)) {
                fragmentBackStack.remove(i);
                break;
            }
            i++;
        }

        // add current fragment to back stack
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);
        fragmentBackStack.addFirst(currentFragment);

        // replace fragment with input fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.flayout_fragment_continer, fragment, fragmentTag);
        ft.commit();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.showDefaultToolbar();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
