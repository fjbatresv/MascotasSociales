package com.jmbsystems.fjbatresv.mascotassociales.chat.DI;

import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesAppModule;
import com.jmbsystems.fjbatresv.mascotassociales.chat.ui.ChatActivity;
import com.jmbsystems.fjbatresv.mascotassociales.domain.DI.DomainModule;
import com.jmbsystems.fjbatresv.mascotassociales.libs.DI.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by javie on 7/07/2016.
 */
@Singleton
@Component(modules = {MascotasSocialesAppModule.class, DomainModule.class, LibsModule.class, ChatModule.class})
public interface ChatComponent {
    void inject(ChatActivity activity);
}
