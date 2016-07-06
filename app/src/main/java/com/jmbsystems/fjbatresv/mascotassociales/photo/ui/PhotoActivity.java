package com.jmbsystems.fjbatresv.mascotassociales.photo.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesApp;
import com.jmbsystems.fjbatresv.mascotassociales.R;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.jmbsystems.fjbatresv.mascotassociales.libs.GlideImageLoader;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageLoader;
import com.jmbsystems.fjbatresv.mascotassociales.main.ui.MainActivity;
import com.jmbsystems.fjbatresv.mascotassociales.photo.PhotoPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PhotoActivity extends AppCompatActivity implements PhotoView,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    @Bind(R.id.container)
    RelativeLayout container;
    @Bind(R.id.loggedName)
    TextView loggedName;
    @Bind(R.id.loggedAvatar)
    CircleImageView loggedAvatar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.path)
    EditText path;
    @Bind(R.id.tags)
    EditText tags;
    @Bind(R.id.comentario)
    EditText comentario;

    @Inject
    PhotoPresenter presenter;

    private ImageLoader imageLoader;

    public static final String PHOTO_PATH = "photoPath";

    private MascotasSocialesApp app;
    private String fotoPath;
    private Location lastKnownLocation;
    private GoogleApiClient apiClient;
    private boolean resolvingError = false;
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    private final static int REQUEST_PICTURE = 1;
    private static final int REQUEST_RESOLVE_ERROR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        app = (MascotasSocialesApp) getApplication();
        setupInjection();
        setupToolbar();
        setupGoogleApiClient();
        presenter.onCreate();
        path.setText(getIntent().getStringExtra(PHOTO_PATH));
    }

    private void setupToolbar() {
        loggedName.setText(Session.getInstancia().getNombre());
        Log.e("avatar", Session.getInstancia().getImage());
        imageLoader.load(loggedAvatar, Session.getInstancia().getImage());
        setSupportActionBar(toolbar);
    }

    private void setupInjection() {
        app.getPhotoComponent(this).inject(this);
        imageLoader = new GlideImageLoader(getApplicationContext());
    }

    @OnClick(R.id.btnPhotoCancel)
    public void back(){
        startActivity(new Intent(this, MainActivity.class).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK
        ));
    }

    @OnClick(R.id.btnPhotoSave)
    public void savePhoto(){
        presenter.uploadPhoto(lastKnownLocation, path.getText().toString(), new Photo(tags.getText().toString(),
                comentario.getText().toString()));
    }

    @Override
    protected void onStart() {
        apiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        apiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onUploadInit() {
        showSnackBar(getString(R.string.main_notice_upload_init));
    }

    private void showSnackBar(String mensaje) {
        Snackbar.make(container, mensaje, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onUploadComplete() {
        back();
    }

    @Override
    public void onUploadError(String error) {
        showSnackBar(error);
    }

    @Override
    public void handleProgressBar(boolean mostrar) {
        int visibility = View.VISIBLE;
        if (!mostrar){
            visibility = View.GONE;
        }
        progressBar.setVisibility(visibility);
    }

    //MAPS INICIO
    private void setupGoogleApiClient() {
        if (apiClient == null) {
            apiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
        Log.e("ApiClient", apiClient.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_LOCATION:{
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    setLastLocation();
                }
                return;
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, PERMISSIONS_REQUEST_LOCATION);
            }
            return;
        }
        setLastLocation();
    }

    private void setLastLocation() {
        if (LocationServices.FusedLocationApi.getLocationAvailability(apiClient).isLocationAvailable()){
            lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            showSnackBar(lastKnownLocation.toString());
        } else {
            showSnackBar(getString(R.string.main_error_location_not_available));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        apiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (resolvingError){
            return;
        } else if (connectionResult.hasResolution()){
            resolvingError = true;
            try {
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                Log.e("ConnectionFailed", e.toString());
            }
        }else{
            resolvingError = true;
            GoogleApiAvailability
                    .getInstance()
                    .getErrorDialog(this, connectionResult.getErrorCode(), REQUEST_RESOLVE_ERROR)
                    .show();
        }
    }//MAPS END
}
