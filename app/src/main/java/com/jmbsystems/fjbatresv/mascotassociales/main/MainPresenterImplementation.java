package com.jmbsystems.fjbatresv.mascotassociales.main;

import android.location.Location;

import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.main.events.MainEvent;
import com.jmbsystems.fjbatresv.mascotassociales.main.ui.MainView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by javie on 5/07/2016.
 */
public class MainPresenterImplementation implements MainPresenter {
    private EventBus bus;
    private MainInteractor interactor;
    private MainView view;

    public MainPresenterImplementation(EventBus bus, MainInteractor interactor, MainView view) {
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
    public void logout() {
        interactor.logout();
    }


    @Override
    @Subscribe
    public void onEventMainThread(MainEvent event) {
        if (view != null){
            switch (event.getType()){
                case MainEvent.LOGOUT:
                    view.logout();
                    break;
            }
        }
    }
}
