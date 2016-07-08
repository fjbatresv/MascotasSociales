package com.jmbsystems.fjbatresv.mascotassociales.chat.ui.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmbsystems.fjbatresv.mascotassociales.R;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.ChatMessage;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageLoader;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by javie on 7/07/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatMessage> messages;
    private Context context;
    private ImageLoader imageLoader;

    public ChatAdapter(List<ChatMessage> messages, Context context, ImageLoader imageLoader) {
        this.messages = messages;
        this.context = context;
        this.imageLoader = imageLoader;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage current = messages.get(position);
        String color = "1976D2";
        if(!current.isSentByMe()){
            color = "2196F3";
        }
        holder.chatChard.setCardBackgroundColor(Color.parseColor("#" + color));
        holder.message.setText(current.getMessage());
        holder.loggedName.setText(current.getSender().getNombre());
        imageLoader.load(holder.loggedAvatar, current.getSender().getImage());
    }

    public void add(ChatMessage msg){
        if(!messages.contains(msg)){
            messages.add(msg);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.message)
        TextView message;
        @Bind(R.id.loggedName)
        TextView loggedName;
        @Bind(R.id.loggedAvatar)
        CircleImageView loggedAvatar;
        @Bind(R.id.chatCard)
        CardView chatChard;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
