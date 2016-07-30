package com.akshatjain.codepath.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.article_item, parent, false);

        // Return a new holder instance
        Holder viewHolder = new Holder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Article article = articleList.get(position);
        TextView txtHeadline = holder.txtHeadline;
        ImageView thumbnail = holder.thumbnail;

        txtHeadline.setText(article.headline.main);
        MediaImage thumbnailImage = article.getThumbnail();
        if(thumbnailImage != null){
            String imageUrl = Constants.NYTIMES_SITE_URL + thumbnailImage.url;
            Glide.with(mContext).load(imageUrl)
                    .crossFade().into(holder.thumbnail);
        }else{
            thumbnail.setBackgroundResource(R.mipmap.ic_launcher);
            thumbnail.setAdjustViewBounds(true);
        }
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

//        @BindView(R.id.txtHeadline)
        public TextView txtHeadline;

//        @BindView(R.id.thumbnail)
        public ImageView thumbnail;

        public Holder(View itemView) {
            super(itemView);

            txtHeadline = (TextView) itemView.findViewById(R.id.txtHeadline);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
//            ButterKnife.bind(itemView);
        }
    }
}
