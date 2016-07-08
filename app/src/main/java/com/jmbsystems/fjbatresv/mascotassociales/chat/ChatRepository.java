package com.jmbsystems.fjbatresv.mascotassociales.chat;

/**
 * Created by javie on 7/07/2016.
 */
public interface ChatRepository {
    void sendMessage(String msg);

    void subscribe();
    void unSubscribe();
    void destroyListener();
}
