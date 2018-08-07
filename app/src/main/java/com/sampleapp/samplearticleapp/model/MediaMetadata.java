package com.sampleapp.samplearticleapp.model;

/**
 * Model for media meta-data
 */
public class MediaMetadata {
    private String url;
    private String format;
    private int height;
    private int width;

    /**
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the Image Url to set
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Set the format to set
     * @param format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set the height to set
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set the width to set
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }
}
