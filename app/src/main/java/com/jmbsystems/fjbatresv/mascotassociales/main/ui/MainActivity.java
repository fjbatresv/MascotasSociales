package com.jmbsystems.fjbatresv.mascotassociales.main.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesApp;
import com.jmbsystems.fjbatresv.mascotassociales.R;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.jmbsystems.fjbatresv.mascotassociales.libs.GlideImageLoader;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageLoader;
import com.jmbsystems.fjbatresv.mascotassociales.login.ui.LoginActivity;
import com.jmbsystems.fjbatresv.mascotassociales.main.MainPresenter;
import com.jmbsystems.fjbatresv.mascotassociales.photo.ui.PhotoActivity;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.ui.PhotoListActivity;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.ui.PhotoMapActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @OnClick(R.id.fab)
    public void takePhoto(){
        startActivity(
                new Intent(this, PhotoActivity.class)
                .putExtra(PhotoActivity.ORIGEN, NOMBRE_ORIGEN)
        );
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
