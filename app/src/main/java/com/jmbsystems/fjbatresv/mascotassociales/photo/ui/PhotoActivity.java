package com.jmbsystems.fjbatresv.mascotassociales.photo.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    @Bind(R.id.btnPhotoSave)
    Button btnPhotoSave;

    @Inject
    PhotoPresenter presenter;

    private ImageLoader imageLoader;

    public final static String ORIGEN = "origen";

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
        app.validSessionInit();
        setupInjection();
        setupToolbar();
        setupGoogleApiClient();
        presenter.onCreate();
        tackePicture();
    }

    private void setupToolbar() {
        loggedName.setText(Session.getInstancia().getNombre());
        imageLoader.load(loggedAvatar, Session.getInstancia().getImage());
        setSupportActionBar(toolbar);
    }

    private void setupInjection() {
        app.getPhotoComponent(this).inject(this);
        imageLoader = new GlideImageLoader(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @OnClick(R.id.btnPhotoCancel)
    public void back(){
        Class origen = MainActivity.class;
        switch (getIntent().getStringExtra(ORIGEN)){
            case MainActivity.NOMBRE_ORIGEN:
                origen = MainActivity.class;
                break;
            case PhotoListActivity.NOMBRE_ORIGEN:
                origen = PhotoListActivity.class;
                break;
            case PhotoMapActivity.NOMBRE_ORIGEN:
                origen = PhotoMapActivity.class;
                break;
        }
        startActivity(new Intent(this, origen).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK
        ));
    }

    @OnClick(R.id.btnPhotoSave)
    public void savePhoto(){
        if (path.getText().toString() != null && !path.getText().toString().isEmpty()){
            presenter.uploadPhoto(lastKnownLocation, path.getText().toString(), new Photo(tags.getText().toString(),
                    comentario.getText().toString()));
        }
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
            lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
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
            }
        }else{
            resolvingError = true;
            GoogleApiAvailability
                    .getInstance()
                    .getErrorDialog(this, connectionResult.getErrorCode(), REQUEST_RESOLVE_ERROR)
                    .show();
        }
    }//MAPS END

    //TAKE PHOTO
    @OnClick(R.id.fab)
    public void tackePicture(){
        //Esto seria null si el dispositivo no tiene camara o algo
        Intent chooserIntent = null;
        List<Intent> intentList = new ArrayList<Intent>();
        //Elegir imagen de la galeria
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //Tomar fotografia
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra("return-data", true);
        File foto = getFile();
        if (foto != null){
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
            if (cameraIntent.resolveActivity(getPackageManager()) != null){
                intentList = addIntentToList(intentList, cameraIntent);
            }
        }
        if(pickIntent.resolveActivity(getPackageManager()) != null){
            intentList = addIntentToList(intentList, pickIntent);
        }
        if (intentList.size() > 0){
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1)
                    , getString(R.string.main_message_picture_resource));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }
        startActivityForResult(chooserIntent, REQUEST_PICTURE);
    }

    private File getFile(){
        File foto = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            foto = File.createTempFile(imageFileName, ".jpg", storageDir);
            fotoPath = foto.getAbsolutePath();
        } catch (IOException e) {
            showSnackBar(getString(R.string.main_error_dispatch_camera));
        }
        return foto;
    }

    private List<Intent> addIntentToList(List<Intent> list, Intent intent){
        //Esto recibe las aplicaciones que pueden recibir el intent
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo){
            String packageName = resolveInfo.activityInfo.packageName;
            Intent target = new Intent(intent);
            target.setPackage(packageName);
            list.add(target);
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE){
            if (resultCode == RESULT_OK){
                boolean fromCamera = (data == null || data.getData() == null);
                if (fromCamera){
                    addToGallery();
                }else{
                    fotoPath = getRealPathFromUri(data.getData());
                }
                app.validSessionInit();
                path.setText(fotoPath);
                btnPhotoSave.setVisibility(View.VISIBLE);
            }
        }
    }

    private String getRealPathFromUri(Uri data) {
        String result = null;
        Cursor cursor = getContentResolver().query(data, null, null, null, null);
        if (cursor == null){
            result = data.getPath();
        }else{
            if (data.toString().contains("mediaKey")){
                cursor.close();
                try {
                    File file = File.createTempFile("tempImg", ".jpg", getCacheDir());
                    InputStream input = getContentResolver().openInputStream(data);
                    OutputStream output = new FileOutputStream(file);
                    try{
                        byte[] buffer = new byte[ 4* 1024];
                        int read;
                        while ((read = input.read(buffer)) != -1){
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                        result = file.getAbsolutePath();
                    } finally {
                        output.close();
                        input.close();
                    }
                } catch (IOException e) {
                }
            } else {
                cursor.moveToFirst();
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(dataColumn);
                cursor.close();
            }
        }
        return result;
    }

    private void addToGallery() {
        Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
        File file = new File(fotoPath);
        Uri contentUri = Uri.fromFile(file);
        mediaScan.setData(contentUri);
        sendBroadcast(mediaScan);
    }
    //Take Photo end
}
