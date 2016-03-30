package com.brandonjenniges.cats;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    Context context;
    OnItemClickListener itemClickListener;

    public MainActivityAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return PlaceHolderData.placeList().size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Cat cat = PlaceHolderData.placeList().get(position);

        holder.catName.setText(cat.getName());
        Picasso.with(context).load(cat.getImageResourceId(context)).into(holder.catImage);

        Bitmap photo = BitmapFactory.decodeResource(context.getResources(), cat.getImageResourceId(context));

        Palette.from(photo).generate(palette -> {
            int bgColor = palette.getMutedColor(ContextCompat.getColor(context, android.R.color.black));
            holder.catNameHolder.setBackgroundColor(bgColor);
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.mainHolder) LinearLayout catHolder;
        @Bind(R.id.cat_name_holder_ll) LinearLayout catNameHolder;
        @Bind(R.id.cat_name_tv) TextView catName;
        @Bind(R.id.catImage) ImageView catImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            catHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(itemView, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.itemClickListener = mItemClickListener;
    }

}