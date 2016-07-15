package com.jmbsystems.fjbatresv.mascotassociales.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesApp;
import com.jmbsystems.fjbatresv.mascotassociales.R;
import com.jmbsystems.fjbatresv.mascotassociales.chat.ChatPresenter;
import com.jmbsystems.fjbatresv.mascotassociales.chat.ui.adapters.ChatAdapter;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.ChatMessage;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageLoader;
import com.jmbsystems.fjbatresv.mascotassociales.main.ui.MainActivity;

import org.w3c.dom.Text;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements ChatView{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.loggedAvatar)
    CircleImageView loggedAvatar;
    @Bind(R.id.loggedName)
    TextView loggedName;
    @Bind(R.id.container)
    RelativeLayout container;
    @Bind(R.id.messageRecyclerView)
    RecyclerView messageRecyclerView;
    @Bind(R.id.txtMessage)
    EditText txtMessage;
    @Bind(R.id.btnSendMessage)
    ImageButton btnSendMessage;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    ChatPresenter presenter;
    @Inject
    ChatAdapter adapter;
    @Inject
    ImageLoader imageLoader;

    private MascotasSocialesApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        app = (MascotasSocialesApp ) getApplication();
        app.validSessionInit();
        setupInjection();
        setupToolbar();
        presenter.onCreate();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupToolbar() {
        loggedName.setText(Session.getInstancia().getNombre());
        imageLoader.load(loggedAvatar, Session.getInstancia().getImage());
        setSupportActionBar(toolbar);
    }

    private void setupInjection() {
        app.getChatComponent(this).inject(this);
    }

    @OnClick(R.id.btnSendMessage)
    public void sendMessage(){
        presenter.sendMessage(txtMessage.getText().toString());
    }

    @Override
    public void onMessageReceived(ChatMessage msg) {
        adapter.add(msg);
        messageRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
        txtMessage.setText(null);
    }

    @Override
    public void loading(boolean accion) {
        int visible = View.VISIBLE;
        int inVisible = View.GONE;
        if (accion){
            messageRecyclerView.setVisibility(inVisible);
            progressBar.setVisibility(visible);
        }else{
            messageRecyclerView.setVisibility(visible);
            progressBar.setVisibility(inVisible);
        }
    }
}
