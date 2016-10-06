package com.uscc.ncku.androiditri.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

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
    private static final String[] SPINNER_LIEST = {"PPAP", "PPAP", "PAPP", "PPAP", "PPAP"};

    private View view;
    private ScrollView scrollView;

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
        setHasOptionsMenu(true);

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
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getActivity().onBackPressed();
                return true;
            }
        });

        view = inflater.inflate(R.layout.fragment_feedback, container, false);

        scrollView = (ScrollView) view.findViewById(R.id.scrollview_feedback);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_close, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        q1();
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

    private void q1() {
        scrollView.smoothScrollTo(0, 0);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_1);
        radioGroup.setVisibility(View.VISIBLE);

        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_1);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_1);

        if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_1_very_support) {

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_1_support) {

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_1_soso) {

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_1_unsupport) {

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_1_very_unsupport) {

        }

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q2();
                radioGroup.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void q2() {
        scrollView.smoothScrollTo(0, 0);
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_2);
        relativeLayout.setVisibility(View.VISIBLE);

        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q1();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_2);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_2);

        RadioGroup rGFunction = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_function);
        if (rGFunction.getCheckedRadioButtonId() == R.id.rbtn_feedback_function_very_support) {

        } else if (rGFunction.getCheckedRadioButtonId() == R.id.rbtn_feedback_function_support) {

        } else if (rGFunction.getCheckedRadioButtonId() == R.id.rbtn_feedback_function_soso) {

        } else if (rGFunction.getCheckedRadioButtonId() == R.id.rbtn_feedback_function_unsupport) {

        } else if (rGFunction.getCheckedRadioButtonId() == R.id.rbtn_feedback_function_very_unsupport) {

        }

        RadioGroup rGBeauty = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_beauty);
        if (rGBeauty.getCheckedRadioButtonId() == R.id.rbtn_feedback_beauty_very_support) {

        } else if (rGBeauty.getCheckedRadioButtonId() == R.id.rbtn_feedback_beauty_support) {

        } else if (rGBeauty.getCheckedRadioButtonId() == R.id.rbtn_feedback_beauty_soso) {

        } else if (rGBeauty.getCheckedRadioButtonId() == R.id.rbtn_feedback_beauty_unsupport) {

        } else if (rGBeauty.getCheckedRadioButtonId() == R.id.rbtn_feedback_beauty_very_unsupport) {

        }

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q3();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void q3() {
        scrollView.smoothScrollTo(0, 0);
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_3);
        relativeLayout.setVisibility(View.VISIBLE);

        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q2();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_3);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_3);

        Spinner spinner = (Spinner) view.findViewById(R.id.feedback_spinner_first_order);
        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, SPINNER_LIEST);
        spinner.setAdapter(lunchList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CheckBox function = (CheckBox) view.findViewById(R.id.rbtn_first_order_reason_function);
        CheckBox beauty = (CheckBox) view.findViewById(R.id.rbtn_first_order_reason_beauty);
        CheckBox operation = (CheckBox) view.findViewById(R.id.rbtn_first_order_reason_operation);
        CheckBox humility = (CheckBox) view.findViewById(R.id.rbtn_first_order_reason_humility);
        CheckBox maintainence = (CheckBox) view.findViewById(R.id.rbtn_first_order_reason_maintainence);
        if (function.isChecked()) {

        }
        if (beauty.isChecked()) {

        }
        if (operation.isChecked()) {

        }
        if (humility.isChecked()) {

        }
        if (maintainence.isChecked()) {

        }

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q4();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void q4() {
        scrollView.smoothScrollTo(0, 0);
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_4);
        relativeLayout.setVisibility(View.VISIBLE);

        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q3();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_4);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_4);

        RadioGroup rGroup = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_4);
        if (rGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_4_no) {

        } else if (rGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_4_yes) {

        }

        Spinner spinner1 = (Spinner) view.findViewById(R.id.feedback_spinner_equip_1);
        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, SPINNER_LIEST);
        spinner1.setAdapter(lunchList);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner2 = (Spinner) view.findViewById(R.id.feedback_spinner_equip_2);
//        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, SPINNER_LIEST);
        spinner2.setAdapter(lunchList);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner3 = (Spinner) view.findViewById(R.id.feedback_spinner_equip_3);
//        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, SPINNER_LIEST);
        spinner3.setAdapter(lunchList);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q5();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void q5() {
        scrollView.smoothScrollTo(0, 0);
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_5);
        relativeLayout.setVisibility(View.VISIBLE);

        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q4();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_5);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_5);

        Spinner spinner1 = (Spinner) view.findViewById(R.id.feedback_spinner_free_1);
        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, SPINNER_LIEST);
        spinner1.setAdapter(lunchList);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner2 = (Spinner) view.findViewById(R.id.feedback_spinner_free_2);
//        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, SPINNER_LIEST);
        spinner2.setAdapter(lunchList);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner3 = (Spinner) view.findViewById(R.id.feedback_spinner_free_3);
//        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, SPINNER_LIEST);
        spinner3.setAdapter(lunchList);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner4 = (Spinner) view.findViewById(R.id.feedback_spinner_free_4);
//        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, SPINNER_LIEST);
        spinner4.setAdapter(lunchList);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner5 = (Spinner) view.findViewById(R.id.feedback_spinner_free_5);
//        ArrayAdapter<String> lunchList = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, SPINNER_LIEST);
        spinner5.setAdapter(lunchList);
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q6();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void q6() {
        scrollView.smoothScrollTo(0, 0);
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_6);
        relativeLayout.setVisibility(View.VISIBLE);

        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q5();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_6);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_6);

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q7();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void q7() {
        scrollView.smoothScrollTo(0, 0);
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.flayout_feedback_7);
        relativeLayout.setVisibility(View.VISIBLE);

        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q6();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_7);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_7);

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q8();
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void q8() {
        scrollView.smoothScrollTo(0, 0);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup_feedback_8);
        radioGroup.setVisibility(View.VISIBLE);

        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q7();
                radioGroup.setVisibility(View.INVISIBLE);
            }
        });

        if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_10k) {

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_50k) {

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_100k) {

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_200k) {

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtn_feedback_price_over_200k) {

        }

        TextView title = (TextView) view.findViewById(R.id.txt_feedback_title);
        title.setText(R.string.feedback_title_8);

        TextView question = (TextView) view.findViewById(R.id.txt_feedback_question);
        question.setText(R.string.feedback_question_8);

        Button next = (Button) view.findViewById(R.id.btn_feedback_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FeedbackUpload().execute();
            }
        });

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

    class FeedbackUpload extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getActivity().onBackPressed();
        }
    }
}
