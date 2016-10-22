package com.uscc.ncku.androiditri.util;

import android.media.MediaPlayer;
import android.view.View;

/**
 * Created by 振凱 on 10月22日.
 *
 * This class store each equipment information in Equipment fragment
 */
public class EquipmentTabInformation {
    // current equipment tab view
    private View v;

    // equipment title
    private String title;

    // is equipment has video or photo
    private boolean isVideo;
    private boolean isPhoto;

    // photo
    private int[] equipPhoto;
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

    // for company information
    private int companyTitleImage;
    private String companyTitleText;
    private String companyName;
    private String companyWebsite;
    private String companyPhone;
    private String companyLocation;
    private int companyQRcode;

    // constructor
    public EquipmentTabInformation() {
        this.equipPhotoIndex = 0;
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

    public int getCompanyQRcode() {
        return companyQRcode;
    }

    public void setCompanyQRcode(int companyQRcode) {
        this.companyQRcode = companyQRcode;
    }

    public int getCompanyTitleImage() {
        return companyTitleImage;
    }

    public void setCompanyTitleImage(int companyTitleImage) {
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

    public int[] getEquipPhoto() {
        return equipPhoto;
    }

    public void setEquipPhoto(int[] equipPhoto) {
        this.equipPhoto = equipPhoto;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }
}
