package com.akshatjain.codepath.news.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.akshatjain.codepath.news.Constants;
import com.akshatjain.codepath.news.Data.MainResponse;
import com.akshatjain.codepath.news.Data.Response;
import com.akshatjain.codepath.news.R;
import com.akshatjain.codepath.news.interfaces.ArticleSearchService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Gson gson = new GsonBuilder()
                .setDateFormat(Constants.ISO_FORMAT)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.NYTIMES_URL)

                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ArticleSearchService service = retrofit.create(ArticleSearchService.class);

        Call<MainResponse> responseCall = service.listArticles();
        responseCall.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, retrofit2.Response<MainResponse> response) {
                Log.d("NYTIME","response == " + response.body().response);
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Log.d("NYTIME","onFailure : response == " + t.toString());
            }
        });
    }
}
