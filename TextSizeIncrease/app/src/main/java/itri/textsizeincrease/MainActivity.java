package itri.textsizeincrease;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends Activity {

    private ImageButton textButton;
    private TextView textView;
    private SeekBar seekBar;
    private Button button;
    private Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // layout
        setupLayout();
        textView.setMovementMethod(new ScrollingMovementMethod());
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do nothing
                textButton.setBackgroundResource(R.drawable.sound_active);
                seekBar.setEnabled(true);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textButton.setEnabled(true);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                textButton.setBackgroundResource(R.drawable.sound_disabled);
                seekBar.setEnabled(false);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int trackProgress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b == true) {
//                    Log.d("previous: ", String.valueOf(seekBar.getProgress()));
//                    int degree = 0;
//                    if (trackProgress < i) {
//                        // increase
//                        degree = getDegree(trackProgress, i);
//                        Log.d("degree:", String.valueOf(degree));
//                        float currentSize = textView.getTextSize();
//                        // update size
//                        currentSize = currentSize + (0.0000005f * degree);
//                        textView.setTextSize(currentSize);
//                    } else if (i < trackProgress) {
//                        degree = getDegree(i, trackProgress);
//                        Log.d("degree:", String.valueOf(degree));
//                        float current = textView.getTextSize();
//                        // get the amount to be decreased
//                        current = current - (0.0000005f * degree);
//                        textView.setTextSize(current);
//                    }
//                    // update trackProgress to current one
//                    trackProgress = i;
//                    Log.d("progress: ", String.valueOf(i));
                    int level = i/5;
                    switch (level) {
                        case 0:
                            textView.setTextSize(15);
                            break;
                        case 1:
                            textView.setTextSize(16);
                            break;
                        case 2:
                            textView.setTextSize(17);
                            break;
                        case 3:
                            textView.setTextSize(18);
                            break;
                        case 4:
                            textView.setTextSize(19);
                            break;
                        case 5:
                            textView.setTextSize(20);
                            break;
                        case 6:
                            textView.setTextSize(21);
                            break;
                        case 7:
                            textView.setTextSize(22);
                            break;
                        case 8:
                            textView.setTextSize(23);
                            break;
                        case 9:
                            textView.setTextSize(24);
                            break;
                        case 10:
                            textView.setTextSize(25);
                            break;
                        case 11:
                            textView.setTextSize(26);
                            break;
                        case 12:
                            textView.setTextSize(27);
                            break;
                        case 13:
                            textView.setTextSize(28);
                            break;
                        case 14:
                            textView.setTextSize(29);
                            break;
                        case 15:
                            textView.setTextSize(30);
                            break;
                        case 16:
                            textView.setTextSize(31);
                            break;
                        case 17:
                            textView.setTextSize(32);
                            break;
                        case 18:
                            textView.setTextSize(33);
                            break;
                        case 19:
                            textView.setTextSize(34);
                            break;
                        case 20:
                            textView.setTextSize(35);
                            break;
                        default:
                            textView.setTextSize(15);
                            break;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //trackProgress = seekBar.getProgress();
                Log.d("start track", String.valueOf(trackProgress));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("end track", String.valueOf(seekBar.getProgress()));
            }
        });
    }

    private void setupLayout() {
        textButton = (ImageButton)findViewById(R.id.imageButton);
        textButton.setBackgroundResource(R.drawable.thumb);
        textButton.setEnabled(false);
        textView = (TextView)findViewById(R.id.randomText);
        textView.setTextSize(15);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        button = (Button)findViewById(R.id.button);
        stop = (Button)findViewById(R.id.button2);
    }

    private int getDegree(int previous, int after) {
        int count = previous - after;
        count = count / 10;
        switch (count) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            default:
                break;
        }
        return 0;
    }

}
