package com.jmbsystems.fjbatresv.mascotassociales.domain;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.twitter.sdk.android.Twitter;

import java.util.Map;


/**
 * Created by javie on 28/06/2016.
 */
public class FirebaseApi {
    private Firebase firebase;
    private Firebase chat;
    private Firebase photo;
    private ChildEventListener photosEventListener;

    public FirebaseApi(Firebase firebase) {
        this.firebase = firebase;
        this.photo = firebase.getRoot().child("fotos");
        this.chat = firebase.getRoot().child("chat");
    }

    public void cheackForData(final FirebaseActionListenerCallback callback){
        photo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    callback.onSuccess();
                }else{
                    callback.onError(null);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                callback.onError(firebaseError);
            }
        });
    }

    public void subscribe(final FirebaseEventListenerCallback callback){
        if (photosEventListener == null){
            photosEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    callback.onChildAdded(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    callback.onChildRemoved(dataSnapshot);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    callback.onCancelled(firebaseError);
                }
            };
            photo.addChildEventListener(photosEventListener);
        }
    }

    public void unSubscribe(){
        if (photosEventListener != null){
            photo.removeEventListener(photosEventListener);
        }
    }

    public String create(){
        return photo.push().getKey();
    }

    public void update(Photo Photo){
        this.photo.child(Photo.getId()).setValue(Photo);
    }

    public void remove(Photo Photo, FirebaseActionListenerCallback callback){
        this.photo.child(Photo.getId()).removeValue();
        callback.onSuccess();
    }

    public String getAuthEmail(){
        String email = null;
        if (firebase.getAuth() != null){
            Map<String, Object> providerData = firebase.getAuth().getProviderData();
            email = providerData.get("email").toString();
        }
        return email;
    }

    public Map<String, Object> viewAuthData(){
        Map<String, Object> data = firebase.getAuth().getProviderData();
        for (Map.Entry<String, Object> entry : data.entrySet()){
            Log.e(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return data;
    }

    public void logout(){
        firebase.unauth();
        if (Session.getInstancia().getSessionType() != Session.SESSION_LOCAL){
            if (AccessToken.getCurrentAccessToken() != null){
                LoginManager.getInstance().logOut();
            }else if (Twitter.getSessionManager().getActiveSession() != null){
                Twitter.logOut();
            }
        }
    }

    public void login(String email, String password,  final FirebaseActionListenerCallback callback){
        firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                callback.onSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                callback.onError(firebaseError);
            }
        });
    }

    public void loginFacebook(AccessToken token, final FirebaseActionListenerCallback callback){
        if (token != null){
            this.firebase.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    callback.onSuccess();
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    callback.onError(firebaseError);
                }
            });
        }
    }

    public void loginTwitter(Map<String, String> options, final FirebaseActionListenerCallback callback){
        this.firebase.authWithOAuthToken("twitter", options, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                callback.onSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                callback.onError(firebaseError);
            }
        });
    }

    public void signup(String email, String password,  final FirebaseActionListenerCallback callback){
        firebase.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                callback.onSuccess();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                callback.onError(firebaseError);
            }
        });
    }

    public void checkForSession(FirebaseActionListenerCallback callback){
        if(firebase.getAuth() != null){
            callback.onSuccess();
        }else{
            callback.onError(null);
        }
    }

    public Firebase getChatMessage(){
        return chat;
    }
}
