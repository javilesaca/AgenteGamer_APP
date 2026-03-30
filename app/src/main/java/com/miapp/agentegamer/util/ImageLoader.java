package com.miapp.agentegamer.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.miapp.agentegamer.R;

public class ImageLoader {

    public static void load(ImageView view, String url) {

        if (view == null) return;

        if (url == null || url.isEmpty()) {
            view.setImageResource(R.drawable.placeholder_game);
            return;
        }
        Glide.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.placeholder_game)
                .error(R.drawable.placeholder_game)
                .centerCrop()
                .into(view);
    }
}
