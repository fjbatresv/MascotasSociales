package com.jmbsystems.fjbatresv.mascotassociales.main.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesApp;
import com.jmbsystems.fjbatresv.mascotassociales.R;
import com.jmbsystems.fjbatresv.mascotassociales.about.AboutActivity;
import com.jmbsystems.fjbatresv.mascotassociales.chat.ui.ChatActivity;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.jmbsystems.fjbatresv.mascotassociales.libs.GlideImageLoader;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageLoader;
import com.jmbsystems.fjbatresv.mascotassociales.login.ui.LoginActivity;
import com.jmbsystems.fjbatresv.mascotassociales.main.MainPresenter;
import com.jmbsystems.fjbatresv.mascotassociales.photo.ui.PhotoActivity;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.ui.PhotoListActivity;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.ui.PhotoMapActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements MainView{
    @Bind(R.id.container)
    LinearLayout container;
    @Bind(R.id.appbar)
    AppBarLayout appBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.loggedAvatar)
    CircleImageView loggedAvatar;
    @Bind(R.id.loggedName)
    TextView loggedName;

    ImageLoader imageLoader;
    @Inject
    MainPresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;

    private MascotasSocialesApp app;
    public final static String NOMBRE_ORIGEN = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        app = (MascotasSocialesApp) getApplication();
        app.validSessionInit();
        setupInjection();
        setupToolbar();
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupInjection() {
        app.getMainComponenet(this, this).inject(this);
        imageLoader = new GlideImageLoader(getApplicationContext());
    }



    @Override
    public void logout() {
        startActivity(new Intent(this, LoginActivity.class).setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK
        ));
    }

    @OnClick(R.id.photoList)
    public void photoList(){
        startActivity(new Intent(this, PhotoListActivity.class));
    }
    @OnClick(R.id.photoMap)
    public void photoMap(){
        startActivity(new Intent(this, PhotoMapActivity.class));
    }
    @OnClick(R.id.chat)
    public void chat(){
        startActivity(new Intent(this, ChatActivity.class));
    }
    @OnClick(R.id.about)
    public void about(){
        startActivity(new Intent(this, AboutActivity.class));
    }

    @OnClick(R.id.fab)
    public void takePhoto(){
        startActivity(
                new Intent(this, PhotoActivity.class)
                .putExtra(PhotoActivity.ORIGEN, NOMBRE_ORIGEN)
        );
    }

    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private void showSnackBar(String message){
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {
        loggedName.setText(Session.getInstancia().getNombre());
        imageLoader.load(loggedAvatar, Session.getInstancia().getImage());
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                presenter.logout();
                startActivity(new Intent(this, LoginActivity.class).setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK
                ));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
