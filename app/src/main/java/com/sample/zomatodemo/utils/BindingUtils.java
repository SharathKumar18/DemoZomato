package com.sample.zomatodemo.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sample.zomatodemo.R;

public class BindingUtils {

    @BindingAdapter("setImage")
    public static void setImageView(final ImageView imageView, String url) {
        if(imageView!=null){
            Glide.with(imageView.getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_action_restuarant)
                    .error(R.drawable.ic_action_restuarant)
                    .into(imageView);
        }
    }
}
