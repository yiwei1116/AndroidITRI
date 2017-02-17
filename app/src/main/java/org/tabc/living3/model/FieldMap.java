package org.tabc.living3.model;

/**
 * Created by n500 on 2017/1/19.
 */

public class FieldMap {

    public static final String FIELD_MAP_ID = "field_map_id";
    public static final String NAME = "name";
    public static final String NAME_EN = "name_en";
    public static final String PROJECT_ID = "project_id";
    public static final String INTRODUCTION = "introduction";
    public static final String PHOTO = "photo";
    public static final String PHOTO_VERTICAL = "photo_vertical";
    public static final String MAP_SVG = "map_svg";
    public static final String MAP_SVG_EN = "map_svg_en";
    public static final String MAP_BG = "map_bg";

    private int fieldMapId;
    private String name;
    private String nameEn;
    private int projectId;
    private String introduction;
    private String photo;
    private String photoVertical;
    private String mapSvg;
    private String mapSvgEn;
    private String mapBg;

    public int getFieldMapId() {
        return fieldMapId;
    }

    public void setFieldMapId(int fieldMapId) {
        this.fieldMapId = fieldMapId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoVertical() {
        return photoVertical;
    }

    public void setPhotoVertical(String photoVertical) {
        this.photoVertical = photoVertical;
    }

    public String getMapSvg() {
        return mapSvg;
    }

    public void setMapSvg(String mapSvg) {
        this.mapSvg = mapSvg;
    }

    public String getMapSvgEn() {
        return mapSvgEn;
    }

    public void setMapSvgEn(String mapSvgEn) {
        this.mapSvgEn = mapSvgEn;
    }

    public String getMapBg() {
        return mapBg;
    }

    public void setMapBg(String mapBg) {
        this.mapBg = mapBg;
    }
}
