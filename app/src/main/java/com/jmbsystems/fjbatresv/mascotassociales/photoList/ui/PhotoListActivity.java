package com.jmbsystems.fjbatresv.mascotassociales.photoList.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesApp;
import com.jmbsystems.fjbatresv.mascotassociales.R;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageLoader;
import com.jmbsystems.fjbatresv.mascotassociales.main.ui.MainActivity;
import com.jmbsystems.fjbatresv.mascotassociales.photo.ui.PhotoActivity;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.PhotoListPresenter;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.ui.adapters.OnItemClickListener;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.ui.adapters.PhotoListAdapter;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PhotoListActivity extends AppCompatActivity implements PhotoListView, OnItemClickListener {
    @Bind(R.id.container)
    RelativeLayout container;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.loggedAvatar)
    CircleImageView loggedAvatar;
    @Bind(R.id.loggedName)
    TextView loggedName;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.reclyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;


    @Inject
    PhotoListPresenter presenter;
    @Inject
    ImageLoader imageLoader;
    @Inject
    PhotoListAdapter adapter;

    private MascotasSocialesApp app;

    public final static String NOMBRE_ORIGEN = "photoList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        ButterKnife.bind(this);
        app = (MascotasSocialesApp) getApplication();
        app.validSessionInit();
        setupInjection();
        setupToolbar();
        setUpRecyclerView();
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @OnClick(R.id.fab)
    public void takePhoto(){
        startActivity(
                new Intent(this, PhotoActivity.class)
                        .putExtra(PhotoActivity.ORIGEN, NOMBRE_ORIGEN)
        );
    }

    private void setupInjection() {
        app.getPhotoListComponent(this, this).inject(this);
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adapter);
    }

    private void setupToolbar() {
        loggedName.setText(Session.getInstancia().getNombre());
        imageLoader.load(loggedAvatar, Session.getInstancia().getImage());
        setSupportActionBar(toolbar);
    }

    @Override
    public void onError(String error) {
        loggedName.setText(Session.getInstancia().getNombre());
        imageLoader.load(loggedAvatar, Session.getInstancia().getImage());
        Snackbar.make(container, error, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void toggleContent(boolean mostrar) {
        int visible = View.VISIBLE;
        if (!mostrar){
            visible = View.GONE;
        }
        fab.setVisibility(visible);
        recyclerView.setVisibility(visible);
    }

    @Override
    public void toggleProgress(boolean mostrar) {
        int visible = View.VISIBLE;
        if (!mostrar){
            visible = View.GONE;
        }
        progressBar.setVisibility(visible);
    }

    @Override
    public void addPhoto(Photo foto) {
        adapter.addPhoto(foto);
    }

    @Override
    public void removePhoto(Photo foto) {
        adapter.removePhoto(foto);
    }

    @Override
    public void onShareClick(Photo foto, ImageView img) {
        Bitmap bitmap = ((GlideBitmapDrawable)img.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, null, null);
        Uri uri = Uri.parse(path);
        Intent share = new Intent(Intent.ACTION_SEND)
                .setType("image/jpeg")
                .putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, getString(R.string.photolist_message_share)));
    }

    @Override
    public void onDeleteClick(Photo foto) {
        presenter.removePhoto(foto);
    }
}
