package com.jmbsystems.fjbatresv.mascotassociales.photo;

import android.location.Location;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.main.events.MainEvent;
import com.jmbsystems.fjbatresv.mascotassociales.photo.events.PhotoEvent;
import com.jmbsystems.fjbatresv.mascotassociales.photo.ui.PhotoView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by javie on 6/07/2016.
 */
public class PhotoPresenterImplementation implements PhotoPresenter {
    private EventBus bus;
    private PhotoInteractor interactor;
    private PhotoView view;

    public PhotoPresenterImplementation(EventBus bus, PhotoInteractor interactor, PhotoView view) {
        this.bus = bus;
        this.interactor = interactor;
        this.view = view;
    }

    @Override
    public void onCreate() {
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        bus.unRegister(this);
    }

    @Override
    public void uploadPhoto(Location location, String path, Photo photo) {
        view.handleProgressBar(true);
        interactor.execute(location, path, photo);
    }

    @Override
    @Subscribe
    public void onEventMainThread(PhotoEvent event) {
        if (view != null){
            switch (event.getType()){
                case PhotoEvent.UPLOAD_INIT:
                    view.onUploadInit();
                    break;
                case PhotoEvent.UPLOAD_COMPLETE:
                    view.handleProgressBar(false);
                    view.onUploadComplete();
                    break;
                case PhotoEvent.UPLOAD_ERROR:
                    view.handleProgressBar(false);
                    view.onUploadError(event.getError());
                    break;
            }
        }
    }
}
