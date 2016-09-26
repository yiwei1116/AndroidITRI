package com.uscc.ncku.androiditri.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.app.Fragment;

import com.uscc.ncku.androiditri.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextFragment extends Fragment {
    private TextView demo;
    private SeekBar seekBar;


    public TextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TextFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TextFragment newInstance() {
        TextFragment fragment = new TextFragment();
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
        View root = inflater.inflate(R.layout.fragment_text, container, false);
        demo = (TextView)root.findViewById(R.id.demo_text_id);
        seekBar = (SeekBar)root.findViewById(R.id.textBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int trackProgress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b == true) {
                    int level = i/5;
                    switch (level) {
                        case 0:
                            demo.setTextSize(15);
                            break;
                        case 1:
                            demo.setTextSize(16);
                            break;
                        case 2:
                            demo.setTextSize(17);
                            break;
                        case 3:
                            demo.setTextSize(18);
                            break;
                        case 4:
                            demo.setTextSize(19);
                            break;
                        case 5:
                            demo.setTextSize(20);
                            break;
                        case 6:
                            demo.setTextSize(21);
                            break;
                        case 7:
                            demo.setTextSize(22);
                            break;
                        case 8:
                            demo.setTextSize(23);
                            break;
                        case 9:
                            demo.setTextSize(24);
                            break;
                        case 10:
                            demo.setTextSize(25);
                            break;
                        case 11:
                            demo.setTextSize(26);
                            break;
                        case 12:
                            demo.setTextSize(27);
                            break;
                        case 13:
                            demo.setTextSize(28);
                            break;
                        case 14:
                            demo.setTextSize(29);
                            break;
                        case 15:
                            demo.setTextSize(30);
                            break;
                        case 16:
                            demo.setTextSize(31);
                            break;
                        case 17:
                            demo.setTextSize(32);
                            break;
                        case 18:
                            demo.setTextSize(33);
                            break;
                        case 19:
                            demo.setTextSize(34);
                            break;
                        case 20:
                            demo.setTextSize(35);
                            break;
                        default:
                            demo.setTextSize(15);
                            break;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("start track", String.valueOf(trackProgress));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("start track", String.valueOf(seekBar.getProgress()));
            }
        });

        return root;
    }

}
