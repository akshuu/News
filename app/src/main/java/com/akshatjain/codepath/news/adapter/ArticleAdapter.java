package com.akshatjain.codepath.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshatjain.codepath.news.Constants;
import com.akshatjain.codepath.news.Data.Article;
import com.akshatjain.codepath.news.Data.MediaImage;
import com.akshatjain.codepath.news.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akshatjain on 7/27/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.Holder>{


    private List<Article> articleList;
    private Context mContext;

    public ArticleAdapter(List<Article> articleList, Context mContext) {
        this.articleList = articleList;
        this.mContext = mContext;
    }

    @Override
    public void onViewRecycled(Holder holder) {
        super.onViewRecycled(holder);
        // Required to clear image when the view is recycled
        // See  : https://github.com/bumptech/glide/issues/710
        Glide.clear(holder.thumbnail);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.article_item, parent, false);

        // Return a new holder instance
        Holder viewHolder = new Holder(articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Article article = articleList.get(position);
        TextView txtHeadline = holder.txtHeadline;
        ImageView thumbnail = holder.thumbnail;

        txtHeadline.setText(article.headline.main);
        MediaImage thumbnailImage = article.getThumbnail();
        thumbnail.setImageDrawable(null);
        if(thumbnailImage != null){
            String imageUrl = Constants.NYTIMES_SITE_URL + thumbnailImage.url;
            Glide.with(mContext)
                    .load(imageUrl)
                    .centerCrop()
                    .dontAnimate()
                    .override(300,300)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.news_icon)
                    .into(thumbnail);
        }else{
            thumbnail.setImageResource(R.drawable.news_icon);

        }

        holder.itemView.setTag(article);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        @BindView(R.id.txtHeadline)
        public TextView txtHeadline;

        @BindView(R.id.thumbnail)
        public ImageView thumbnail;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
