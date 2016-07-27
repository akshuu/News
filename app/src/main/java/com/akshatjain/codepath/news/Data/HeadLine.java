package com.akshatjain.codepath.news.Data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akshatjain on 7/27/16.
 */
public class HeadLine {

    @SerializedName("main")
    public String main;

    @SerializedName("print_headline")
    public String printHeadline;

    @Override
    public String toString() {
        return "HeadLine{" +
                "main='" + main + '\'' +
                ", printHeadline='" + printHeadline + '\'' +
                '}';
    }
}
