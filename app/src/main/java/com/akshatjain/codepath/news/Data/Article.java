package com.akshatjain.codepath.news.Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshatjain on 7/27/16.
 */
public class Article {

    @SerializedName("_id")
    public String id;

    @SerializedName("web_url")
    public String webUrl;

    public String snippet;

    @SerializedName("lead_paragraph")
    public String leadPara;

    public String source;

    @SerializedName("pub_date")
    public String publishDate;

    @SerializedName("headline")
    public HeadLine headline;

    @SerializedName("multimedia")
    public ArrayList<MediaImage> images;

    public MediaImage getThumbnail(){
        if(images != null || images.size() > 0){
            for(MediaImage image : images){
                if(image.subtype.equalsIgnoreCase("thumbnail")){
                    return image;
                }
            }
        }
        return null;
    }

    public String getThumbnailUrl(){
        if(images != null || images.size() > 0){
            for(MediaImage image : images){
                if(image.subtype.equalsIgnoreCase("thumbnail")){
                    return image.url;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", snippet='" + snippet + '\'' +
                ", leadPara='" + leadPara + '\'' +
                ", source='" + source + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", headline=" + headline.toString() +
                ", images=" + images.toString() +
                '}';
    }
}
