package com.akshatjain.codepath.news.Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshatjain on 7/27/16.
 */
public class Response {

    @SerializedName("docs")
    public ArrayList<Article> articles;

    @Override
    public String toString() {

        StringBuilder returnString = new StringBuilder();
        for(Article article: articles)
            returnString.append(article.toString());
        return "Response{" +
                "articles=" + returnString +
                '}';
    }
}
