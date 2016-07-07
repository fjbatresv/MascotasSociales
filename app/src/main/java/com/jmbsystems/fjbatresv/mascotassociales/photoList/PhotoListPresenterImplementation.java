package com.jmbsystems.fjbatresv.mascotassociales.photoList;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.events.PhotoListEvent;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.ui.PhotoListView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by javie on 6/07/2016.
 */
public class PhotoListPresenterImplementation implements PhotoListPresenter {
    private EventBus bus;
    private PhotoListView view;
    private PhotoListInteractor interactor;

    public PhotoListPresenterImplementation(EventBus bus, PhotoListView view, PhotoListInteractor interactor) {
        this.bus = bus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        bus.register(this);
        if (view != null){
            view.toggleProgress(true);
            view.toggleContent(false);
        }
        interactor.subscribe();
    }

    @Override
    public void onDestroy() {
        interactor.unSubscribe();
        view = null;
        bus.unRegister(this);
    }

    @Override
    public void removePhoto(Photo foto) {
        interactor.removePhoto(foto);
    }

    @Override
    @Subscribe
    public void onEventMainThread(PhotoListEvent event) {
        if (view != null){
            view.toggleProgress(false);
            view.toggleContent(true);
        }
            if (event.getError() != null){
                String error = event.getError();
                if (error.isEmpty()){
                    error = "No hay fotografias";
                }
                view.onError(error);
            }else{
                switch (event.getType()){
                    case PhotoListEvent.READ_EVENT:
                        view.addPhoto(event.getFoto());
                        break;
                    case PhotoListEvent.DELETE_EVENT:
                        view.removePhoto(event.getFoto());
                        break;
                }
            }
    }
}
