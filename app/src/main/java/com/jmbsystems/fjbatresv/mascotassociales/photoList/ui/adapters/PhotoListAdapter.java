package com.jmbsystems.fjbatresv.mascotassociales.photoList.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmbsystems.fjbatresv.mascotassociales.R;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by javie on 6/07/2016.
 */
public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {
    private List<Photo> fotos;
    private ImageLoader imageLoader;
    private OnItemClickListener listener;

    public PhotoListAdapter(List<Photo> fotos, ImageLoader imageLoader, OnItemClickListener listener) {
        this.fotos = fotos;
        this.imageLoader = imageLoader;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_photo_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo current = fotos.get(position);
        holder.setOnItemClickListener(current, listener);
        imageLoader.load(holder.avatar, current.getAvatar());
        imageLoader.load(holder.img, current.getUrl());
        holder.txtUser.setText(current.getNombre());
        if (current.getComentario() != null){
            holder.comentario.setText(current.getComentario());
        }else{
            holder.comentario.setVisibility(View.GONE);
        }
        if (current.getTags() != null){
            holder.tags.setText(current.getTags());
        }else{
            holder.tags.setVisibility(View.GONE);
        }
        if (current.isPublishedByMe()){
            holder.imgDelete.setVisibility(View.VISIBLE);
        } else {
            holder.imgDelete.setVisibility(View.GONE);
        }
    }

    public void addPhoto(Photo foto){
        fotos.add(0, foto);
        notifyDataSetChanged();
    }

    public void removePhoto(Photo foto){
        fotos.remove(foto);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.txtUser)
        TextView txtUser;
        @Bind(R.id.avatar)
        CircleImageView avatar;
        @Bind(R.id.img)
        ImageView img;
        @Bind(R.id.comentario)
        TextView comentario;
        @Bind(R.id.tags)
        TextView tags;
        @Bind(R.id.imgShare)
        ImageButton imgShare;
        @Bind(R.id.imgDelete)
        ImageButton imgDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        //Esto le da acciones a cada objeto en la vista.
        public void setOnItemClickListener(final Photo foto, final OnItemClickListener listener){
            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onShareClick(foto, img);
                }
            });
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClick(foto);
                }
            });
        }
    }
}
