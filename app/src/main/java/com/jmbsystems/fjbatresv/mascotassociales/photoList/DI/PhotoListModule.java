package com.jmbsystems.fjbatresv.mascotassociales.photoList.DI;

import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageLoader;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.PhotoListInteractor;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.PhotoListInteractoriImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.PhotoListPresenter;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.PhotoListPresenterImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.PhotoListRepository;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.PhotoListRepositoryImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.ui.PhotoListView;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.ui.adapters.OnItemClickListener;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.ui.adapters.PhotoListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by javie on 7/07/2016.
 */
@Module
public class PhotoListModule {
    private PhotoListView view;
    private OnItemClickListener listener;

    public PhotoListModule(PhotoListView view, OnItemClickListener listener) {
        this.view = view;
        this.listener = listener;
    }

    @Provides
    @Singleton
    PhotoListView providesPhotoListView(){
        return this.view;
    }

    @Provides
    @Singleton
    OnItemClickListener providesOnItemClickListener(){
        return this.listener;
    }

    @Provides
    @Singleton
    PhotoListPresenter providesPhotoListPresenter(EventBus bus, PhotoListView view, PhotoListInteractor interactor){
        return new PhotoListPresenterImplementation(bus, view, interactor);
    }

    @Provides
    @Singleton
    PhotoListInteractor providesPhotoListInteractor(PhotoListRepository repository){
        return new PhotoListInteractoriImplementation(repository);
    }

    @Provides
    @Singleton
    PhotoListRepository providesPhotoListRepository(FirebaseApi api, EventBus bus){
        return new PhotoListRepositoryImplementation(api, bus);
    }

    @Singleton
    @Provides
    PhotoListAdapter providesPhotoListAdapter(List<Photo> fotos, ImageLoader imageLoader, OnItemClickListener listener){
        return new PhotoListAdapter(fotos, imageLoader, listener);
    }

    @Singleton
    @Provides
    List<Photo> providesPhotos(){
        return new ArrayList<Photo>();
    }
}
