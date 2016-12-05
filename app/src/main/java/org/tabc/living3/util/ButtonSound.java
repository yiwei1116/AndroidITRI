package org.tabc.living3.util;

import android.content.Context;
import android.media.MediaPlayer;

import org.tabc.living3.R;

/**
 * Created by Lin on 2016/12/1.
 */
public class ButtonSound {
    private static MediaPlayer buttonSound;

    public static void play(Context c) {
        if (buttonSound == null) {
            buttonSound = MediaPlayer.create(c, R.raw.multimedia_button_click_015);
        } else if (buttonSound.isPlaying()) {
            buttonSound.stop();
            buttonSound.release();
            buttonSound = MediaPlayer.create(c, R.raw.multimedia_button_click_015);
        }

        buttonSound.start();
    }
}
