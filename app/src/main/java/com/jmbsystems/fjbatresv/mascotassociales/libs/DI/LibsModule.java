package com.jmbsystems.fjbatresv.mascotassociales.libs.DI;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.jmbsystems.fjbatresv.mascotassociales.libs.CloudinaryImageStorage;
import com.jmbsystems.fjbatresv.mascotassociales.libs.GlideImageLoader;
import com.jmbsystems.fjbatresv.mascotassociales.libs.GreenRobotEventBus;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageLoader;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;



/**
 * Created by javie on 15/06/2016.
 */
@Module
public class LibsModule {

    private Fragment fragment;

    public LibsModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Singleton
    @Provides
    public EventBus providesEventBus(org.greenrobot.eventbus.EventBus eventBus){
        return new GreenRobotEventBus(eventBus);
    }

    @Singleton
    @Provides
    public org.greenrobot.eventbus.EventBus providesLibraryEventBus(){
        return org.greenrobot.eventbus.EventBus.getDefault();
    }

    @Singleton
    @Provides
    public ImageLoader providesImageLoader(RequestManager manager){
        return new GlideImageLoader(manager);
    }

    @Singleton
    @Provides
    public RequestManager providesRequestManager(Fragment fragment){
        return Glide.with(fragment);
    }

    @Singleton
    @Provides
    public Fragment providesFragment(){
        return this.fragment;
    }

    @Singleton
    @Provides
    public ImageStorage providesImageStorage(Cloudinary cloudinary){
        return new CloudinaryImageStorage(cloudinary);
    }

    @Singleton
    @Provides
    public Cloudinary providesCloudinary(Context context){
        return new Cloudinary(Utils.cloudinaryUrlFromContext(context));
    }

}
