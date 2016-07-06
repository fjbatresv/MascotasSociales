package com.jmbsystems.fjbatresv.mascotassociales.main.DI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageStorage;
import com.jmbsystems.fjbatresv.mascotassociales.main.MainInteractor;
import com.jmbsystems.fjbatresv.mascotassociales.main.MainInteractorImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.main.MainPresenter;
import com.jmbsystems.fjbatresv.mascotassociales.main.MainPresenterImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.main.MainRepository;
import com.jmbsystems.fjbatresv.mascotassociales.main.MainRepositoryImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.main.ui.MainView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by javie on 30/06/2016.
 */
@Module
public class MainModule {
    private MainView view;

    public MainModule(MainView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    MainView providesMainView() {
        return this.view;
    }

    @Provides @Singleton
    MainPresenter providesMainPresenter(MainView view, EventBus eventBus, MainInteractor interactor) {
        return new MainPresenterImplementation(eventBus, interactor, view);
    }

    @Provides @Singleton
    MainInteractor providesMainInteractor(MainRepository repository) {
        return new MainInteractorImplementation(repository);
    }

    @Provides @Singleton
    MainRepository providesMainRepository(EventBus eventBus, FirebaseApi firebase) {
        return new MainRepositoryImplementation(eventBus, firebase);
    }
}
