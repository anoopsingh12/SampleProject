package com.sampleapp.samplearticleapp.model;

import java.util.List;

/**
 * Model containing results object
 */
public class ResponseModel {
    private String title;
    private String byLine;
    private String published_date;
    private List<MediaModel> mediaModelList;

    /**
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title to set
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return byLine
     */
    public String getByLine() {
        return byLine;
    }

    /**
     * Set the byLine to set
     * @param byLine
     */
    public void setByLine(String byLine) {
        this.byLine = byLine;
    }

    /**
     *
     * @return published_date
     */
    public String getPublished_date() {
        return published_date;
    }

    /**
     * Set the published_date to set
     * @param published_date
     */
    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    /**
     *
     * @return mediaModelList
     */
    public List<MediaModel> getMediaModelList() {
        return mediaModelList;
    }

    /**
     * Set the mediaModelList to set
     * @param mediaModelList
     */
    public void setMediaModelList(List<MediaModel> mediaModelList) {
        this.mediaModelList = mediaModelList;
    }
}
