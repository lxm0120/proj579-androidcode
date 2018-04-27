package com.example.monster.proje579code;

/**
 * Created by usama on 4/15/18.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.GraphRequest;
import com.squareup.picasso.Picasso;


/**
 * Created by usama on 4/14/18.
 */

public class PhotosAdapter extends BaseAdapter {
    private final Context mContext;
    private final Photo[] photos;

    // 1
    public PhotosAdapter(Context context, Photo[] photos) {
        this.mContext = context;
        this.photos = photos;
    }

    // 2
    @Override
    public int getCount() {
        return photos.length;
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Photo photo = photos[position];

        // view holder pattern
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.photos_linear_layout, null);

            final ImageView imageViewCoverArt = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
            final ImageView imageViewFavorite = (ImageView)convertView.findViewById(R.id.imageview_favorite);

            final ViewHolder viewHolder = new ViewHolder(imageViewCoverArt, imageViewFavorite);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
//        viewHolder.imageViewCoverArt.setImageResource(book.getImageResource());
        Picasso.with(mContext).load(photo.getImageUrl()).into(viewHolder.imageViewCoverArt);

        viewHolder.imageViewFavorite.setImageResource(photo.getIsFavorite() ? R.drawable.star_enabled : R.drawable.star_disabled);

        return convertView;
    }
    private class ViewHolder {

        private final ImageView imageViewCoverArt;
        private final ImageView imageViewFavorite;

        public ViewHolder( ImageView imageViewCoverArt, ImageView imageViewFavorite) {

            this.imageViewCoverArt = imageViewCoverArt;
            this.imageViewFavorite = imageViewFavorite;
        }
    }
}
