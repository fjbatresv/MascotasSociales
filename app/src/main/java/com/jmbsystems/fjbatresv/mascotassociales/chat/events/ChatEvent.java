package com.jmbsystems.fjbatresv.mascotassociales.chat.events;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.ChatMessage;

/**
 * Created by javie on 7/07/2016.
 */
public class ChatEvent {
    private ChatMessage message;
    private String error;

    public ChatEvent() {
    }

    public ChatEvent(ChatMessage message, String error) {
        this.message = message;
        this.error = error;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
