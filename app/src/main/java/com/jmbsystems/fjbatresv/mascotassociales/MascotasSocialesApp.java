package com.jmbsystems.fjbatresv.mascotassociales;

import android.app.Application;
import android.content.Intent;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.jmbsystems.fjbatresv.mascotassociales.domain.DI.DomainModule;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseEventListenerCallback;
import com.jmbsystems.fjbatresv.mascotassociales.libs.DI.LibsModule;
import com.jmbsystems.fjbatresv.mascotassociales.login.DI.DaggerLoginComponent;
import com.jmbsystems.fjbatresv.mascotassociales.login.DI.LoginComponent;
import com.jmbsystems.fjbatresv.mascotassociales.login.DI.LoginModule;
import com.jmbsystems.fjbatresv.mascotassociales.login.ui.LoginActivity;
import com.jmbsystems.fjbatresv.mascotassociales.login.ui.LoginView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by javie on 5/07/2016.
 */
public class MascotasSocialesApp extends Application {

    private DomainModule domainModule;
    private MascotasSocialesAppModule mascotasSocialesAppModule;

    @Override
    public void onCreate() {
        initFabric();
        super.onCreate();
        initFacebook();
        initFirebase();
        initModule();
    }

    private void initModule() {
        mascotasSocialesAppModule = new MascotasSocialesAppModule(this);
        domainModule = new DomainModule();
    }

    private void initFirebase() {
        Firebase.setAndroidContext(this);
    }

    private void initFabric() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET
        );
        Fabric.with(this, new Twitter(authConfig));
    }

    private void initFacebook() {
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
    }

    public void logoutFacebook(){
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this, LoginActivity.class).setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK
        ));
    }

    public void logoutTwitter(){
        Twitter.logOut();
        startActivity(new Intent(this, LoginActivity.class).setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK
        ));
    }

    public LoginComponent getLoginComponent(LoginView view){
        return DaggerLoginComponent.builder()
                .domainModule(domainModule)
                .libsModule(new LibsModule(null))
                .mascotasSocialesAppModule(mascotasSocialesAppModule)
                .loginModule(new LoginModule(view))
                .build();
    }
}
