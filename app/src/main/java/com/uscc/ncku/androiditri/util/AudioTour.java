package com.uscc.ncku.androiditri.util;

import android.content.Context;
import android.media.MediaPlayer;

import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Danny on 2016/10/22.
 */
public class AudioTour extends MainActivity {
    private MediaPlayer mediaPlayer;
    private boolean isPause;
    private LinkedList<String> songList ;
    private Context context;
    private  int length,totalLength;
    private ArrayList<Integer> playlist = new ArrayList<>();
//save the context recievied via constructor in a local variable

    public AudioTour(Context context){
        this.context=context;

    }
    public void addPlayList(){

        playlist.add(R.raw.test);
        totalLength = mediaPlayer.getDuration();

    }
    public int getAudioLength(){

        return totalLength;

    }

    public void doPlay() throws IOException {

        mediaPlayer = MediaPlayer.create(context,playlist.get(0));
        try {

            mediaPlayer.seekTo(length);
            mediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }
    public void pausePlay() throws IOException {

        mediaPlayer.pause();
        length = mediaPlayer.getCurrentPosition();


    }
    public void release(){


        mediaPlayer.release();

    }

}
