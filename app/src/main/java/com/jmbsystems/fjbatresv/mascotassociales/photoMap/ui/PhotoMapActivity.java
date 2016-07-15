package com.jmbsystems.fjbatresv.mascotassociales.photoMap.ui;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesApp;
import com.jmbsystems.fjbatresv.mascotassociales.R;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageLoader;
import com.jmbsystems.fjbatresv.mascotassociales.main.ui.MainActivity;
import com.jmbsystems.fjbatresv.mascotassociales.photo.ui.PhotoActivity;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.PhotoMapPresenter;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PhotoMapActivity extends AppCompatActivity implements PhotoMapView,
        OnMapReadyCallback, GoogleMap.InfoWindowAdapter, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    @Bind(R.id.container)
    RelativeLayout container;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.loggedName)
    TextView loggedName;
    @Bind(R.id.loggedAvatar)
    CircleImageView loggedAvatar;
    @Bind(R.id.wrapperMap)
    FrameLayout wrapperMap;

    @Inject
    ImageLoader imageLoader;
    @Inject
    PhotoMapPresenter presenter;

    private GoogleMap googleMap;
    private HashMap<Marker, Photo> markers;
    private MascotasSocialesApp app;
    private Location lastKnownLocation;
    private GoogleApiClient apiClient;
    private boolean resolvingError = false;
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    private final static int REQUEST_PICTURE = 1;
    private static final int REQUEST_RESOLVE_ERROR = 0;
    public final static String NOMBRE_ORIGEN = "photoMap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_map);
        ButterKnife.bind(this);
        app = (MascotasSocialesApp) getApplication();
        app.validSessionInit();
        markers = new HashMap<Marker, Photo>();
        setupInjection();
        presenter.onCreate();
        setupToolbar();
        setupGoogleApiClient();
        setupFragment();
    }

    @OnClick(R.id.fab)
    public void takePhoto(){
        startActivity(
                new Intent(this, PhotoActivity.class)
                        .putExtra(PhotoActivity.ORIGEN, NOMBRE_ORIGEN)
        );
    }

    private void setupFragment() {
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupToolbar() {
        loggedName.setText(Session.getInstancia().getNombre());
        imageLoader.load(loggedAvatar, Session.getInstancia().getImage());
        setSupportActionBar(toolbar);
    }

    private void setupInjection() {
        app.getPhotoMapComponent(this).inject(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void addPhoto(Photo foto) {
        LatLng location = new LatLng(foto.getLatitude(), foto.getLongitud());
        Marker marker = this.googleMap.addMarker(new MarkerOptions().position(location));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 6));
        markers.put(marker, foto);
    }

    @Override
    public void removePhoto(Photo foto) {
        for (Map.Entry<Marker, Photo> opt : markers.entrySet()) {
            Photo fotoA = opt.getValue();
            Marker markerA = opt.getKey();
            if (fotoA.getId().equals(foto.getId())) {
                markerA.remove();
                markers.remove(markerA);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onPhotosError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void loading(boolean accion) {
        int visible = View.VISIBLE;
        int inVisible = View.GONE;
        if (accion) {
            fab.setVisibility(inVisible);
            wrapperMap.setVisibility(inVisible);
            progressBar.setVisibility(visible);
        } else {
            fab.setVisibility(visible);
            wrapperMap.setVisibility(visible);
            progressBar.setVisibility(inVisible);
        }
    }

    private void setupGoogleApiClient() {
        if (apiClient == null) {
            apiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
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
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, PERMISSIONS_REQUEST_LOCATION);
            }
            return;
        }
        setLastLocation();
    }

    private void setLastLocation() {
        if (LocationServices.FusedLocationApi.getLocationAvailability(apiClient).isLocationAvailable()){
            this.googleMap.setMyLocationEnabled(true);
        } else {
            showSnackBar(getString(R.string.main_error_location_not_available));
        }
    }

    private void showSnackBar(String string) {
        Snackbar.make(container, string, Snackbar.LENGTH_LONG).show();
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
            }
        }else{
            resolvingError = true;
            GoogleApiAvailability
                    .getInstance()
                    .getErrorDialog(this, connectionResult.getErrorCode(), REQUEST_RESOLVE_ERROR)
                    .show();
        }
    }//MAPS END

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        presenter.subscribe();
        this.googleMap.setInfoWindowAdapter(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, PERMISSIONS_REQUEST_LOCATION);
            }
            return;
        }
        this.googleMap.setMyLocationEnabled(true);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = this.getLayoutInflater().inflate(R.layout.map_info_window, null);
        Photo fotoA = markers.get(marker);
        CircleImageView imgAvatar = (CircleImageView)view.findViewById(R.id.loggedAvatar);
        TextView txtUser = (TextView) view.findViewById(R.id.loggedName);
        ImageView imgMain = (ImageView) view.findViewById(R.id.img);

        imageLoader.load(imgAvatar, fotoA.getAvatar());
        imageLoader.load(imgMain, fotoA.getUrl());
        txtUser.setText(fotoA.getNombre());

        return view;
    }
}
