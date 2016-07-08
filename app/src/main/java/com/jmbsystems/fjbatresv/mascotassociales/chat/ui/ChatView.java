package com.jmbsystems.fjbatresv.mascotassociales.chat.ui;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.ChatMessage;

/**
 * Created by javie on 7/07/2016.
 */
public interface ChatView {
    void onMessageReceived(ChatMessage msg);
    void loading(boolean accion);
}
