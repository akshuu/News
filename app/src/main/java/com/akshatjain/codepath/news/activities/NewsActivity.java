package com.akshatjain.codepath.news.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akshatjain.codepath.news.Constants;
import com.akshatjain.codepath.news.Data.Article;
import com.akshatjain.codepath.news.Data.MainResponse;
import com.akshatjain.codepath.news.R;
import com.akshatjain.codepath.news.adapter.ArticleAdapter;
import com.akshatjain.codepath.news.adapter.EndlessRecyclerViewScrollListener;
import com.akshatjain.codepath.news.adapter.ItemClickSupport;
import com.akshatjain.codepath.news.adapter.SpacesItemDecoration;
import com.akshatjain.codepath.news.fragment.SearchDialogFragment;
import com.akshatjain.codepath.news.interfaces.ArticleSearchService;
import com.akshatjain.codepath.news.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity implements SearchDialogFragment.AdvanceSearchQuery{

    public static final String SEARCH = "Search";
    public static final String BEGIN_DATE = "BeginDate";
    public static final String SORT_ORDER = "SortOrder";
    public static final String FACETS = "Facets";

    @BindView(R.id.articlesView)
    RecyclerView articlesView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    ArticleAdapter mAdapter;
    ArrayList<Article> mArticles;
    ArticleSearchService service;
    int mPage =0 ;
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);

//         TODO : Remove this to enable dynamic sizing
//        articlesView.setHasFixedSize(true);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        articlesView.setLayoutManager(staggeredGridLayoutManager);

        articlesView.addItemDecoration(new SpacesItemDecoration(16));

        articlesView.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d("NYTIME", "query == " + mQuery + ", page ==" + page);

                getArticles(mQuery,page);
            }
        });
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

    private void getArticles(String query, final int page, String beginDate, String sortOrder, String facets) {

        if(Utils.isNetworkAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            Log.d("NYTIME", "fetching page == " + page);

            Map<String, String> queryMap = new HashMap<>();
            if(query != null)
                queryMap.put("q", query);
            queryMap.put("page", String.valueOf(page));
            if(beginDate != null)
                queryMap.put("begin_date", beginDate);

            if(sortOrder != null){
                queryMap.put("sort",sortOrder);
            }

            if(facets != null && facets.length() > 0){
                queryMap.put("fq",facets);
            }

            Log.d("NYTIME", "query Map == " + queryMap.toString());

            Call<MainResponse> responseCall = service.listArticles(queryMap);
            responseCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, retrofit2.Response<MainResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("NYTIME", "response == " + response.body().response);
                    ArrayList<Article> tmpArticles = response.body().response.articles;

                    int size = mArticles.size();
                    mArticles.addAll(tmpArticles);
                    if(mAdapter == null) {
                        Log.d("NYTIME", "new Adapter == " + mArticles.size());
                        mAdapter = new ArticleAdapter(mArticles, NewsActivity.this);
                        articlesView.setAdapter(mAdapter);

                        mAdapter.notifyItemInserted(0);
                        articlesView.scrollToPosition(0);
                    }else{
                        int curSize = mAdapter.getItemCount();
                        Log.d("NYTIME", "updating items in range == " + curSize + ", " + tmpArticles.size());
                        mAdapter.notifyItemRangeInserted(curSize,tmpArticles.size());

                    }
                    mPage = page;
                    Log.d("NYTIME", "mPage == " + mPage);
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("NYTIME", "onFailure : response == " + t.toString());
                    Toast.makeText(NewsActivity.this,"Unable to fetch the Articles. Please try again...",Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(this,"No Internet connection. Please try again...",Toast.LENGTH_LONG).show();
        }
    }

    private void getArticles(String query, final int page) {

        SharedPreferences pref = getSharedPreferences(SEARCH,MODE_PRIVATE);
        String beginDate = pref.getString(BEGIN_DATE,null);
        String sortOrder  = pref.getString(SORT_ORDER,null);
        String facets = pref.getString(FACETS,null);

        getArticles(query, page,beginDate,sortOrder,facets);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("NYTIME","onStop() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("NYTIME","onResume() called");
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
                if(mAdapter != null)
                    mAdapter.notifyItemRangeRemoved(0,mArticles.size()-1);
                mAdapter = null;
                mArticles = new ArrayList<Article>();
                mPage = 0;
                mQuery = query;
                getArticles(query, mPage);
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
        if (id == R.id.action_advance) {
            showEditDialog();
            return true;
        }

        if(id == R.id.action_clear){
            SharedPreferences pref = getSharedPreferences(SEARCH,MODE_PRIVATE);
            pref.edit().clear().apply();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateSearchQuery(@Nullable String date, String sortOrder, @Nullable String facets) {
        Log.d("NYTime","Search query == " + date  +" , sortOrder = " + sortOrder +" , facets = " + facets);
        if(mAdapter != null)
            mAdapter.notifyItemRangeRemoved(0,mArticles.size()-1);
        mAdapter = null;
        mArticles = new ArrayList<Article>();
        mPage = 0;
        SharedPreferences pref = getSharedPreferences(SEARCH,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(BEGIN_DATE,date);
        editor.putString(SORT_ORDER,sortOrder);
        editor.putString(FACETS,facets);
        editor.apply();

        getArticles(mQuery,mPage,date,sortOrder,facets);
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SearchDialogFragment searchDialogFragment = new SearchDialogFragment();
        searchDialogFragment.show(fm, "SearchDialogFragment");
    }

}
