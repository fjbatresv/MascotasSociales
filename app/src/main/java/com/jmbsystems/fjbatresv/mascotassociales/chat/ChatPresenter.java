package com.jmbsystems.fjbatresv.mascotassociales.chat;

import com.jmbsystems.fjbatresv.mascotassociales.chat.events.ChatEvent;

/**
 * Created by javie on 7/07/2016.
 */
public interface ChatPresenter {
    void onCreate();
    void onDestroy();

    void sendMessage(String msg);
    void onEventMeinThread(ChatEvent event);
}
