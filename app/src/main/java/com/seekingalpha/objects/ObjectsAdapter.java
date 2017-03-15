package com.seekingalpha.objects;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.seekingalpha.R;
import com.seekingalpha.data.Object;
import com.seekingalpha.utils.Utils;

import java.util.List;

public class ObjectsAdapter extends RecyclerView.Adapter<ObjectsAdapter.ViewHolder> {

    public static final String ERROR = "Error";
    private List<Object> objects;
    private final OnItemClickListener listener;

    public ObjectsAdapter(List<Object> objects, OnItemClickListener listener) {
        this.objects = objects;
        this.listener = listener;
    }

    public void addAll(List<Object> objects) {
        this.objects.addAll(objects);
        notifyDataSetChanged();
    }

    @Override
    public ObjectsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_object, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Object object = objects.get(position);
        Context context = holder.image.getContext();

        Glide.with(context)
                .load(object.getImageUrl())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.image);

        String name = object.getName();
        String description = object.getDescription();
        holder.name.setText(Utils.isEmpty(name) ? ERROR : name);
        holder.description.setText(Utils.isEmpty(description) ? ERROR : description);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder, position, object);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.image.setTransitionName(context.getString(R.string.transition_image) + position);
            holder.name.setTransitionName(context.getString(R.string.transition_name)+position);
            holder.description.setTransitionName(context.getString(R.string.transition_description)+position);
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;
        public TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.row_image);
            name = (TextView)itemView.findViewById(R.id.row_name);
            description = (TextView)itemView.findViewById(R.id.row_description);
        }
    }

    interface OnItemClickListener {
        void onItemClick(ViewHolder viewHolder, int position, Object item);
    }
}
