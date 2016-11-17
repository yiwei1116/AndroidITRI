package com.uscc.ncku.androiditri.util;

import android.media.MediaPlayer;
import android.view.View;

import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.ArrayList;

/**
 * Created by 振凱 on 10月22日.
 *
 * This class store each equipment information in Equipment fragment
 */
public class EquipmentTabInformation {
    // current equipment tab view
    private View v;

    // device ID
    private int deviceId;

    // equipment title
    private String title;

    // is equipment has video or photo
    private boolean isVideo;
    private boolean isPhoto;

    // photo
    private ArrayList<String> equipPhoto;
    private int equipPhotoIndex;
    private String videoID;

    // equipment information
    private String textContent;
    // equipment information font size
    private int fontSize;

    // for media play
    private MediaPlayer mediaPlayer;
    private int playList;
    private int playLength;

    // youtube fragment
    private YouTubePlayerFragment youTubePlayerFragment;

    // for company information
    private String companyTitleImage;
    private String companyTitleText;
    private String companyName;
    private String companyWebsite;
    private String companyPhone;
    private String companyLocation;
    private String companyQRcode;

    // constructor
    public EquipmentTabInformation() {
        this.equipPhoto = new ArrayList<String>();
        // initial photo index at positon one
        this.equipPhotoIndex = 0;
        this.fontSize = 18;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyTitleImage() {
        return companyTitleImage;
    }

    public void setCompanyTitleImage(String companyTitleImage) {
        this.companyTitleImage = companyTitleImage;
    }

    public String getCompanyTitleText() {
        return companyTitleText;
    }

    public void setCompanyTitleText(String companyTitleText) {
        this.companyTitleText = companyTitleText;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isPhoto() {
        return isPhoto;
    }

    public void setPhoto(boolean photo) {
        isPhoto = photo;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public int getPlayLength() {
        return playLength;
    }

    public void setPlayLength(int playLength) {
        this.playLength = playLength;
    }

    public int getPlayList() {
        return playList;
    }

    public void setPlayList(int playList) {
        this.playList = playList;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public View getView() {
        return v;
    }

    public void setView(View v) {
        this.v = v;
    }

    public ArrayList<String> getEquipPhoto() {
        return equipPhoto;
    }

    public String getEquipPhotoFirst() {
        return equipPhoto.get(0);
    }

    public void insertEquipPhoto(String name) {
        equipPhoto.add(name);
    }

    public int getEquipPhotoIndex() {
        return equipPhotoIndex;
    }

    public void setEquipPhotoIndex(int equipPhotoIndex) {
        this.equipPhotoIndex = equipPhotoIndex;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getCompanyQRcode() {
        return companyQRcode;
    }

    public void setCompanyQRcode(String companyQRcode) {
        this.companyQRcode = companyQRcode;
    }

    public YouTubePlayerFragment getYouTubePlayerFragment() {
        return youTubePlayerFragment;
    }

    public void setYouTubePlayerFragment(YouTubePlayerFragment youTubePlayerFragment) {
        this.youTubePlayerFragment = youTubePlayerFragment;
    }
}
