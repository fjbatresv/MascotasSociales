package com.jmbsystems.fjbatresv.mascotassociales.login.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import com.jmbsystems.fjbatresv.mascotassociales.login.LoginPresenter;
import com.jmbsystems.fjbatresv.mascotassociales.login.LoginPresenterImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.main.MainActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        app = (MascotasSocialesApp) getApplication();
        if (AccessToken.getCurrentAccessToken() != null ||
                Twitter.getSessionManager().getActiveSession() != null){
            //navigateToMainScreen();
        }
        initInjection();
        presenter.onCreate();
        handleTwLogin();
        handleFbLogin();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void initInjection() {
        app.getLoginComponent(this).inject(this);
    }

    private void handleTwLogin() {
        twLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Map<String, String> options = new HashMap<String, String>();
                TwitterSession session = Twitter.getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                options.put("oauth_token", authToken.token);
                options.put("oauth_token_secret", authToken.secret);
                options.put("user_id", String.valueOf(session.getUserId()));
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
                presenter.fbSignin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
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
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twLogin.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.mainLoginCancel)
    public void handleMainLoginCancel(){
        presenter.handleMainLogin(false);
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
    public void handleProgressbar(boolean mostrar) {
        int visible = View.VISIBLE;
        if (!mostrar){
            visible = View.GONE;
        }
        progressBar.setVisibility(visible);
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
        Snackbar.make(container, String.format(getString(R.string.login_error_message), error), Snackbar.LENGTH_LONG).show();
    }
}
