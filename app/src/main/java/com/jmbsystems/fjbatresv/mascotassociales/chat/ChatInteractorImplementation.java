package com.jmbsystems.fjbatresv.mascotassociales.chat;

import com.jmbsystems.fjbatresv.mascotassociales.photo.PhotoRepositoryImplementation;

/**
 * Created by javie on 7/07/2016.
 */
public class ChatInteractorImplementation implements ChatInteractor {
    private ChatRepository repo;

    public ChatInteractorImplementation(ChatRepository repo) {
        this.repo = repo;
    }

    @Override
    public void sendMessage(String msg) {
        repo.sendMessage(msg);
    }

    @Override
    public void subscribe() {
        repo.subscribe();
    }

    @Override
    public void unSubscribe() {
        repo.unSubscribe();
    }

    @Override
    public void destroyListener() {
        repo.destroyListener();
    }
}
