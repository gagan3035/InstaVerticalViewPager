package com.example.gagan.lbbtask.adapter;

/**
 * Created by Gagan on 5/28/2016.
 * this is the pager adapter to show the images received from Instagram
 * account.
 * Images are shown using Picasso library.
 */
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.gagan.lbbtask.R;
import com.example.gagan.lbbtask.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class InstaVerticalPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<Picture> mPictures=  new ArrayList<>();

    public InstaVerticalPagerAdapter(Context context, List<Picture> pictures) {
        mContext = context;
        mPictures = pictures;

    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_instagram_image, collection, false);
        ImageView instaImageView = (ImageView) layout.findViewById(R.id.insta_image_view);
        Picture currentPicture = mPictures.get(position);
        Picasso.with(mContext).load(currentPicture.getURL()).into(instaImageView);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mPictures.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public void addData(List<Picture> Pictures) {
        mPictures.addAll(Pictures);
    }
}
