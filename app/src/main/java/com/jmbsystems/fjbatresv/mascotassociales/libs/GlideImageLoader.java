package com.jmbsystems.fjbatresv.mascotassociales.libs;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageLoader;


/**
 * Created by javie on 14/06/2016.
 */
public class GlideImageLoader implements ImageLoader {
    private RequestManager glideRequestManager;

    public GlideImageLoader(RequestManager glideRequestManager) {
        this.glideRequestManager = glideRequestManager;
    }

    public GlideImageLoader(Context context) {
        this.glideRequestManager = Glide.with(context);
    }

    @Override
    public void load(ImageView imgAvatar, String s) {
        glideRequestManager
                .load(s)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .override(700, 700)
                .into(imgAvatar);
    }
}
