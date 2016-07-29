package com.akshatjain.codepath.news.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akshatjain.codepath.news.Constants;
import com.akshatjain.codepath.news.Data.Article;
import com.akshatjain.codepath.news.Data.MainResponse;
import com.akshatjain.codepath.news.Data.Response;
import com.akshatjain.codepath.news.R;
import com.akshatjain.codepath.news.adapter.ArticleAdapter;
import com.akshatjain.codepath.news.adapter.ItemClickSupport;
import com.akshatjain.codepath.news.adapter.SpacesItemDecoration;
import com.akshatjain.codepath.news.interfaces.ArticleSearchService;
import com.akshatjain.codepath.news.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.articlesView)
    RecyclerView articlesView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    ArticleAdapter mAdapter;
    ArrayList<Article> mArticles;
    ArticleSearchService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);
        articlesView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));

        articlesView.addItemDecoration(new SpacesItemDecoration(16));

        ItemClickSupport.addTo(articlesView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // do it
                Toast.makeText(NewsActivity.this,"Clicked : " + position, Toast.LENGTH_LONG).show();
                Article article = mArticles.get(position);
                String url =article.webUrl;
                Intent webActivity = new Intent(NewsActivity.this,WebViewActivity.class);
                webActivity.putExtra("URL",url);
                startActivity(webActivity);
            }
        });
        Gson gson = new GsonBuilder()
                .setDateFormat(Constants.ISO_FORMAT)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.NYTIMES_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        service = retrofit.create(ArticleSearchService.class);

//        getArticles();
    }

    private void getArticles(String query) {

        if(Utils.isNetworkAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            Call<MainResponse> responseCall = service.listArticles(query);
            responseCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, retrofit2.Response<MainResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("NYTIME", "response == " + response.body().response);
                    mArticles = response.body().response.articles;
                    mAdapter = new ArticleAdapter(mArticles, NewsActivity.this);
                    articlesView.setAdapter(mAdapter);
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("NYTIME", "onFailure : response == " + t.toString());
                }
            });
        }else{
            Toast.makeText(this,"No Internet connection. Please try again...",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("NYTIME","onStop() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("NYTIME","onSaveInstanceState() called");
//        outState.putSerializable("ArticleList",mArticles);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("NYTIME","onResume() called");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("NYTIME","onRestoreInstanceState() called");
//        mArticles = (ArrayList<Article>) savedInstanceState.getSerializable("ArticleList");
//        Log.d("NYTIME","onRestoreInstanceState() : " + mAdapter.getItemCount());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_list, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // UX
//        int searchImgId = android.support.v7.appcompat.R.id.search_button;
//        ImageView v = (ImageView) searchView.findViewById(searchImgId);
////        v.setImageResource(R.drawable.search_btn);
//        // Customize searchview text and hint colors
//        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
//        EditText et = (EditText) searchView.findViewById(searchEditId);
//        et.setTextColor(Color.BLACK);
//        et.setHintTextColor(Color.BLACK);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                Log.d("NyTimes","Query == " + query);
                getArticles(query);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
