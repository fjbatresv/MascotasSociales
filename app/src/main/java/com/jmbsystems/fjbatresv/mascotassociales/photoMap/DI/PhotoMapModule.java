package com.jmbsystems.fjbatresv.mascotassociales.photoMap.DI;

import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.PhotoMapInteractor;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.PhotoMapInteractorImplemtation;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.PhotoMapPresenter;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.PhotoMapPresenterImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.PhotoMapRepository;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.PhotoMapRepositoryImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.ui.PhotoMapView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by javie on 7/07/2016.
 */
@Module
public class PhotoMapModule {
    private PhotoMapView view;

    public PhotoMapModule(PhotoMapView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    PhotoMapView providesPhotoMapView(){
        return this.view;
    }

    @Singleton
    @Provides
    PhotoMapPresenter providesPhotoMapPresenter(EventBus bus, PhotoMapView view, PhotoMapInteractor interactor){
        return new PhotoMapPresenterImplementation(bus, view, interactor);
    }

    @Singleton
    @Provides
    PhotoMapInteractor providesPhotoMapInteractor(PhotoMapRepository repo){
        return new PhotoMapInteractorImplemtation(repo);
    }

    @Singleton
    @Provides
    PhotoMapRepository providesPhotoMapRepository(EventBus bus, FirebaseApi api){
        return new PhotoMapRepositoryImplementation(bus, api);
    }
}
