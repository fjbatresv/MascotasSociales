package com.jmbsystems.fjbatresv.mascotassociales.chat.DI;

import android.content.Context;

import com.jmbsystems.fjbatresv.mascotassociales.chat.ChatInteractor;
import com.jmbsystems.fjbatresv.mascotassociales.chat.ChatInteractorImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.chat.ChatPresenter;
import com.jmbsystems.fjbatresv.mascotassociales.chat.ChatPresenterImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.chat.ChatRepository;
import com.jmbsystems.fjbatresv.mascotassociales.chat.ChatRepositoryImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.chat.ui.ChatView;
import com.jmbsystems.fjbatresv.mascotassociales.chat.ui.adapters.ChatAdapter;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.ChatMessage;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by javie on 7/07/2016.
 */
@Module
public class ChatModule {
    private ChatView view;

    public ChatModule(ChatView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    ChatView providesChatView(){
        return this.view;
    }

    @Provides
    @Singleton
    ChatAdapter providesChatAdapter(List<ChatMessage> messages, Context context, ImageLoader imageLoader){
        return new ChatAdapter(messages, context, imageLoader);
    }

    @Provides
    @Singleton
    List<ChatMessage> providesMessagesList(){
        return new ArrayList<ChatMessage>();
    }

    @Provides
    @Singleton
    ChatPresenter providesChatPresenter(ChatView view, EventBus bus, ChatInteractor interactor){
        return new ChatPresenterImplementation(view, bus, interactor);
    }

    @Provides
    @Singleton
    ChatInteractor providesChatInteractor(ChatRepository repo){
        return new ChatInteractorImplementation(repo);
    }

    @Provides
    @Singleton
    ChatRepository providesChatRepository(EventBus bus, FirebaseApi api){
        return new ChatRepositoryImplementation(bus, api);
    }

}
