package com.sampleapp.samplearticleapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.sampleapp.samplearticleapp.R;
import com.sampleapp.samplearticleapp.model.MediaMetadata;
import com.sampleapp.samplearticleapp.model.ResponseModel;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<ResponseModel> articleList;
    private ImageLoader mImageLoader;
    private Context context;
    private View itemView;
    private String largeImage = "";
    private String description;

    public RecyclerAdapter(List<ResponseModel> responseModelList, Context context) {
        this.articleList = responseModelList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recycler_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            ResponseModel responseModel = articleList.get(position);
            holder.authorName.setText(responseModel.getByLine());
            holder.publishedDate.setText(responseModel.getPublished_date());
            holder.title.setText(responseModel.getTitle());

            String imageUrl = "";

            for (MediaMetadata mediaMetadata : responseModel.getMediaModelList().get(0).getMediaMetadataList()) {
                if (mediaMetadata.getFormat().equals("Standard Thumbnail")) {
                    imageUrl = mediaMetadata.getUrl();
                }
            }
            ImageLoader.ImageCache imageCache = new BitmapLruCache();
            mImageLoader = new ImageLoader(Volley.newRequestQueue(context), imageCache);
            holder.mItemImageView.setImageUrl(imageUrl, mImageLoader);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ResponseModel responseModel = articleList.get(position);
                    for (MediaMetadata mediaMetadata : responseModel.getMediaModelList().get(0).getMediaMetadataList()) {
                        if(mediaMetadata.getFormat().equals("square640")){
                            largeImage = mediaMetadata.getUrl();
                        }
                    }
                    description = responseModel.getMediaModelList().get(0).getCaption();

                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("largeImage", largeImage);
                    intent.putExtra("description", description);
                    context.startActivity(intent);
                }
            });
        } catch (Exception ex) {
            Log.i("onBindViewHolder", "Exception in onBindViewHolder of RecyclerAdapter");
        }

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, authorName, publishedDate;
        public NetworkImageView mItemImageView;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            authorName = (TextView) view.findViewById(R.id.author_name);
            publishedDate = (TextView) view.findViewById(R.id.published_date);
            mItemImageView = (NetworkImageView) view.findViewById(R.id.item_image_view);
        }
    }
}
