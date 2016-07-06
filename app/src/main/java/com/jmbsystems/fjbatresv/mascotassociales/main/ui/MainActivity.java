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
    private String fotoPath;
    private static final int REQUEST_PICTURE = 1;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE){
            Log.e("activityResult", "picture");
            if (resultCode == RESULT_OK){
                Log.e("activityResult", "ok");
                boolean fromCamera = (data == null || data.getData() == null);
                Log.e("activityResult", "camara: " + fromCamera);

                if (fromCamera){
                    Log.e("activityResult", "addGallery | " + fotoPath);
                    addToGallery();
                }else{
                    Log.e("activityResult", "realPathFromUri");
                    fotoPath = getRealPathFromUri(data.getData());
                }
                Log.e("activityResult", "uploadPhoto");
                startActivity(new Intent(this, PhotoActivity.class)
                        .putExtra(PhotoActivity.PHOTO_PATH, fotoPath));
                //presenter.uploadPhoto(lastKnownLocation, fotoPath);
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
                    Log.e("realPathUri", e.toString());
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


    @Override
    public void logout() {
        startActivity(new Intent(this, LoginActivity.class).setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK
        ));
    }


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
        Log.e("tackePicture", pickIntent.resolveActivity(getPackageManager()).toString());
        if(pickIntent.resolveActivity(getPackageManager()) != null){
            Log.e("tackePicture", "pickIntent");
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
            Log.e("getFile", e.toString());
        }
        return foto;
    }

    private void showSnackBar(String message){
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show();
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

    private void setupToolbar() {
        loggedName.setText(Session.getInstancia().getNombre());
        Log.e("avatar", Session.getInstancia().getImage());
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
