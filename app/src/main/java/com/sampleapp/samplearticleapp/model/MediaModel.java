package com.sampleapp.samplearticleapp.model;

import java.util.List;

/**
 * Model for media type Array
 */
public class MediaModel {
    private String type;
    private String caption;
    private String copyright;
    private List<MediaMetadata> mediaMetadataList;

    /**
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type to set
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Set the caption to set
     * @param caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     *
     * @return copyright
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * set the copyright to set
     * @param copyright
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     *
     * @return mediaMetadataList
     */
    public List<MediaMetadata> getMediaMetadataList() {
        return mediaMetadataList;
    }

    /**
     * Set the list of mediaMetadataList
     * @param mediaMetadataList
     */
    public void setMediaMetadataList(List<MediaMetadata> mediaMetadataList) {
        this.mediaMetadataList = mediaMetadataList;
    }
}
