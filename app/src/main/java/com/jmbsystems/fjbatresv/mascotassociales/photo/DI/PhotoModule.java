package com.jmbsystems.fjbatresv.mascotassociales.photo.DI;

import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageStorage;
import com.jmbsystems.fjbatresv.mascotassociales.photo.PhotoInteractor;
import com.jmbsystems.fjbatresv.mascotassociales.photo.PhotoInteractorImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.photo.PhotoPresenter;
import com.jmbsystems.fjbatresv.mascotassociales.photo.PhotoPresenterImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.photo.PhotoRepository;
import com.jmbsystems.fjbatresv.mascotassociales.photo.PhotoRepositoryImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.photo.ui.PhotoView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by javie on 6/07/2016.
 */
@Module
public class PhotoModule {
    private PhotoView view;

    public PhotoModule(PhotoView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    PhotoRepository providesPhotoRepository(FirebaseApi api, ImageStorage storage, EventBus bus){
        return new PhotoRepositoryImplementation(api, storage, bus);
    }

    @Provides
    @Singleton
    PhotoInteractor providesPhotoInteractor(PhotoRepository repo){
        return new PhotoInteractorImplementation(repo);
    }

    @Provides
    @Singleton
    PhotoPresenter providesPhotoPresenter(EventBus bus, PhotoInteractor interactor, PhotoView view){
        return new PhotoPresenterImplementation(bus, interactor, view);
    }

    @Provides
    @Singleton
    PhotoView providesPhotoView(){
        return this.view;
    }
}
