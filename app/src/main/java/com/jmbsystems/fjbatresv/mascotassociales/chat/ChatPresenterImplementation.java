package com.jmbsystems.fjbatresv.mascotassociales.chat;

import com.jmbsystems.fjbatresv.mascotassociales.chat.events.ChatEvent;
import com.jmbsystems.fjbatresv.mascotassociales.chat.ui.ChatView;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by javie on 7/07/2016.
 */
public class ChatPresenterImplementation implements ChatPresenter {
    private ChatView view;
    private EventBus bus;
    private ChatInteractor interactor;

    public ChatPresenterImplementation(ChatView view, EventBus bus, ChatInteractor interactor) {
        this.view = view;
        this.bus = bus;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        bus.register(this);
        interactor.subscribe();
        view.loading(true);
    }

    @Override
    public void onDestroy() {
        view = null;
        interactor.unSubscribe();
        interactor.destroyListener();
        bus.unRegister(this);
    }

    @Override
    public void sendMessage(String msg) {
        interactor.sendMessage(msg);
    }

    @Override
    @Subscribe
    public void onEventMeinThread(ChatEvent event) {
        if(view != null){
            view.onMessageReceived(event.getMessage());
            view.loading(false);
        }
    }
}
