package com.jmbsystems.fjbatresv.mascotassociales.enitites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by javie on 7/07/2016.
 */
@JsonIgnoreProperties({"sentByMe"})
public class ChatMessage {
    private String msg;
    private ChatUser sender;
    private boolean sentByMe;

    public ChatMessage() {
    }

    public ChatMessage(String message, ChatUser sender, boolean sentByMe) {
        this.msg = message;
        this.sender = sender;
        this.sentByMe = sentByMe;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public ChatUser getSender() {
        return sender;
    }

    public void setSender(ChatUser sender) {
        this.sender = sender;
    }

    public boolean isSentByMe() {
        return sentByMe;
    }

    public void setSentByMe(boolean sentByMe) {
        this.sentByMe = sentByMe;
    }

    @Override
    public boolean equals(Object obj){
        boolean equals = false;
        if(obj instanceof  ChatMessage){
            ChatMessage recipe = (ChatMessage)obj;
            equals = this.sender.equals(recipe.getSender())
                    && this.msg.equals(recipe.getMessage())
                    && this.sentByMe == recipe.isSentByMe();
        }
        return equals;
    }
}
