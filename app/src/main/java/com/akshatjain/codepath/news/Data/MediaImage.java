package com.akshatjain.codepath.news.Data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akshatjain on 7/27/16.
 */
public class MediaImage {

    @SerializedName("url")
    public String url;

    public int width;

    public int height;

    public String type;

    public String subtype;

    @Override
    public String toString() {
        return "MediaImage{" +
                "url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", type='" + type + '\'' +
                ", subtype='" + subtype + '\'' +
                '}';
    }
}
