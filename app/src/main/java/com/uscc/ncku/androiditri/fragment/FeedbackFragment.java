package com.uscc.ncku.androiditri.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;

/**
 * use this to call feedback popup dialog
 *
 * FeedbackFragment feedback = new FeedbackFragment();
 * feedback.feedbackAlertDialog(getActivity(), feedback);
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment {

    private View view;

    public FeedbackFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FeedbackFragment.
     */
    public static FeedbackFragment newInstance() {
        FeedbackFragment fragment = new FeedbackFragment();
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
        MainActivity.hideMainBtn();
        MainActivity.showFeedbackToolbar();
        Toolbar toolbar = MainActivity.getToolbar();

        toolbar.setNavigationIcon(R.drawable.grey_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        if (toolbar.getMenu() != null) {
            toolbar.getMenu().clear();
        }
        toolbar.inflateMenu(R.menu.main_close);

        view = inflater.inflate(R.layout.fragment_feedback, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.showMainBtn();
        MainActivity.showDefaultToolbar();
        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.getMenu().clear();
        toolbar.setNavigationIcon(R.drawable.btn_back);
    }

    public void feedbackAlertDialog(final Activity activity, final FeedbackFragment f) {
        final Dialog dialog = new Dialog(activity, R.style.selectorDialogTitle);
        dialog.setContentView(R.layout.alertdialog_feedback);
        dialog.setTitle(R.string.feedback_title);

        Button btnYes = (Button) dialog.findViewById(R.id.btn_confirm_alertdialog_feedback);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_skip_alertdialog_feedback);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = activity.getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.flayout_fragment_continer, f).addToBackStack(null);
                transaction.commit();

                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
