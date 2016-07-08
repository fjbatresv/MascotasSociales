package com.jmbsystems.fjbatresv.mascotassociales.chat;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.jmbsystems.fjbatresv.mascotassociales.chat.events.ChatEvent;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.ChatMessage;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.ChatUser;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;

/**
 * Created by javie on 7/07/2016.
 */
public class ChatRepositoryImplementation implements ChatRepository {
    private EventBus bus;
    private FirebaseApi api;
    private ChildEventListener listener;

    public ChatRepositoryImplementation(EventBus bus, FirebaseApi api) {
        this.bus = bus;
        this.api = api;
    }

    @Override
    public void sendMessage(String msg) {
        ChatMessage message = new ChatMessage(
                msg,
                new ChatUser(
                        Session.getInstancia().getNombre(),
                        Session.getInstancia().getUsername(),
                        Session.getInstancia().getImage()),
                true);
        Firebase chat = api.getChatMessage();
        chat.push().setValue(message);
    }

    @Override
    public void subscribe() {
        if (listener == null){
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    message.setSentByMe(Session.getInstancia().getUsername().equals(message.getSender().getUsername()));
                    bus.post(new ChatEvent(message, null));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(FirebaseError firebaseError) {}
            };
        }
        api.getChatMessage().addChildEventListener(listener);
    }

    @Override
    public void unSubscribe() {
        api.getChatMessage().removeEventListener(listener);
    }

    @Override
    public void destroyListener() {
        listener = null;
    }
}
