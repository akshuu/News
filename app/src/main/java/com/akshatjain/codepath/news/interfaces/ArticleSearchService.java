package com.akshatjain.codepath.news.interfaces;

import com.akshatjain.codepath.news.Data.MainResponse;
import com.akshatjain.codepath.news.Data.Response;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by akshatjain on 7/27/16.
 */
public interface ArticleSearchService {

    @GET("/svc/search/v2/articlesearch.json?api-key=85a8d638b48c483f9f9727987aacdf1f")
    public Call<MainResponse> listArticles(@Query("q") String query);
}
