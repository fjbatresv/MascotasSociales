package com.jmbsystems.fjbatresv.mascotassociales.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesApp;
import com.jmbsystems.fjbatresv.mascotassociales.R;
import com.jmbsystems.fjbatresv.mascotassociales.domain.Util;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.jmbsystems.fjbatresv.mascotassociales.login.LoginPresenter;
import com.jmbsystems.fjbatresv.mascotassociales.main.ui.MainActivity;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {
    @Bind(R.id.container)
    LinearLayout container;
    @Bind(R.id.fbLogin)
    LoginButton fbLogin;
    @Bind(R.id.twLogin)
    TwitterLoginButton twLogin;
    @Bind(R.id.facebookOpt)
    RelativeLayout fbOpt;
    @Bind(R.id.twitterOpt)
    RelativeLayout twOpt;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.progressBar2)
    ProgressBar progressBar2;
    @Bind(R.id.mainLogin)
    Button mainLogin;
    @Bind(R.id.mainLoginFields)
    RelativeLayout mainLoginFields;
    @Bind(R.id.mainOpt)
    RelativeLayout mainOpt;
    @Bind(R.id.txtEmail)
    EditText txtEmail;
    @Bind(R.id.txtPassword)
    EditText txtPassword;

    private CallbackManager callbackManager;
    private MascotasSocialesApp app;

    @Inject
    LoginPresenter presenter;
    @Inject
    Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        app = (MascotasSocialesApp) getApplication();
        initInjection();
        presenter.onCreate();
        handleTwLogin();
        handleFbLogin();
        presenter.validLogin();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mainLoginFields.getVisibility() == View.VISIBLE){
            presenter.handleMainLogin(false);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void validSession(Map<String, Object> options){
        if (AccessToken.getCurrentAccessToken() != null){
            Session.getInstancia().setSessionType(Session.SESSION_FACEBOOK);
            Object value = options.get("profileImageURL");
            Session.getInstancia().setImage(String.valueOf(value));
            value = options.get("id");
            Session.getInstancia().setUsername(String.valueOf(value));
            value = options.get("displayName");
            Session.getInstancia().setNombre(String.valueOf(value));
        } else if (Twitter.getSessionManager().getActiveSession() != null){
            Session.getInstancia().setSessionType(Session.SESSION_TWITTER);
            Object value = options.get("profileImageURL");
            Session.getInstancia().setImage(String.valueOf(value));
            value = options.get("username");
            Session.getInstancia().setUsername(String.valueOf(value));
            value = options.get("displayName");
            Session.getInstancia().setNombre(String.valueOf(value));
        } else {
            Session.getInstancia().setSessionType(Session.SESSION_LOCAL);
            Object value = options.get("email");
            Session.getInstancia().setImage(String.valueOf(util.getavatarUrl(String.valueOf(value))));
            Session.getInstancia().setUsername(String.valueOf(value));
            Session.getInstancia().setNombre(String.valueOf(value));
        }
    }

    private void initInjection() {
        app.getLoginComponent(this).inject(this);
    }

    private void handleTwLogin() {
        twLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.e("twlogin", "success");
                Map<String, String> options = new HashMap<String, String>();
                TwitterSession session = Twitter.getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                Log.e("twlogin", "data taken");
                options.put("oauth_token", authToken.token);
                options.put("oauth_token_secret", authToken.secret);
                options.put("user_id", String.valueOf(session.getUserId()));
                Log.e("twlogin", "data setted");
                presenter.twSignin(options);
            }

            @Override
            public void failure(TwitterException exception) {
                //getLocalizedMessage nos da un error segun la localizacion del usuario(idioma).
                Snackbar.make(
                        container,
                        String.format(
                                getString(R.string.login_error_message),
                                exception.getLocalizedMessage()
                        ),
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    @OnClick(R.id.mainLogin)
    public void handleMainLogin(){
        presenter.handleMainLogin(true);
    }

    private void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
        ));
    }

    private void handleFbLogin() {
        callbackManager = CallbackManager.Factory.create();
        fbLogin.setPublishPermissions(Arrays.asList("publish_actions"));
        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("fblogin", "succes");
                presenter.fbSignin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {Log.e("fblogin", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("fblogin", error.getLocalizedMessage());
                Snackbar.make(container, String.format(
                        getString(R.string.login_error_message),
                        error.getLocalizedMessage()
                ), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("activity", "result get ");
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twLogin.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btnSignup)
    public void handleMainLoginCancel(){
        presenter.mainSignup(txtEmail.getText().toString(), txtPassword.getText().toString());
    }

    @OnClick(R.id.btnSignin)
    public void  handleSignIn(){
        presenter.mainSignin(txtEmail.getText().toString(), txtPassword.getText().toString());
    }

    @Override
    public void handleSocialNetworks(boolean mostrar) {
        int visible = View.VISIBLE;
        if (!mostrar){
            visible = View.GONE;
        }
        fbOpt.setVisibility(visible);
        twOpt.setVisibility(visible);
    }

    @Override
    public void handleProgressbar(boolean mostrar, int barra) {
        int visible = View.VISIBLE;
        int inVisible = View.GONE;
        if (barra == 1){
            if (mostrar){
                progressBar.setVisibility(visible);
            }else {
                progressBar.setVisibility(inVisible);
            }
        }else {
            if (mostrar){
                progressBar2.setVisibility(visible);
                mainOpt.setVisibility(inVisible);
                fbOpt.setVisibility(inVisible);
                twOpt.setVisibility(inVisible);
            }else{
                progressBar2.setVisibility(inVisible);
                mainOpt.setVisibility(visible);
                fbOpt.setVisibility(visible);
                twOpt.setVisibility(visible);
            }
        }
    }

    @Override
    public void handleMainLoginFields(boolean mostrar) {
        int visible = View.VISIBLE;
        int inVisible = View.GONE;
        if (mostrar){
            mainLoginFields.setVisibility(visible);
            mainLogin.setVisibility(inVisible);
            mainOpt.setBackgroundColor(getResources().getColor(R.color.icons));
        }else{
            mainLoginFields.setVisibility(inVisible);
            mainLogin.setVisibility(visible);
            mainOpt.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void signInSuccess() {
        navigateToMainScreen();
    }

    @Override
    public void signInError(String error) {
        Log.e("error login", error);
        Snackbar.make(container, String.format(getString(R.string.login_error_message), error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void signUpSuccess() {
        Snackbar.make(container, getString(R.string.login_signup_success_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void signUpError(String error) {
        Snackbar.make(container, String.format(getString(R.string.login_signup_error_message), error), Snackbar.LENGTH_LONG).show();
    }
}
