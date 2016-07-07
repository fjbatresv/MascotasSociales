package com.jmbsystems.fjbatresv.mascotassociales.photoMap;

import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.events.PhotoMapEvent;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.ui.PhotoMapView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by javie on 7/07/2016.
 */
public class PhotoMapPresenterImplementation implements PhotoMapPresenter {
    private EventBus bus;
    private PhotoMapView view;
    private PhotoMapInteractor interactor;

    public PhotoMapPresenterImplementation(EventBus bus, PhotoMapView view, PhotoMapInteractor interactor) {
        this.bus = bus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        interactor.unSubscribe();
        view = null;
        bus.unRegister(this);
    }

    @Override
    public void subscribe() {
        if (view != null){
            view.loading(true);
        }
        interactor.subscribe();
    }

    @Override
    @Subscribe
    public void onEventMainThread(PhotoMapEvent event) {
        if (view != null){
            view.loading(false);
        }
        if (event.getError() != null){
            String error = event.getError();
            if (error.isEmpty()){
                error = "No hay fotografias disponibles";
            }
            view.onPhotosError(error);
        }else{
            switch (event.getType()){
                case PhotoMapEvent.READ_EVENT:
                    view.addPhoto(event.getFoto());
                    break;
                case PhotoMapEvent.DELETE_EVENT:
                    view.removePhoto(event.getFoto());
            }
        }
    }
}
