package com.brandonjenniges.journie;

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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    Context context;
    OnItemClickListener itemClickListener;
    ArrayList<Cat>cats;

    public MainActivityAdapter(Context context, ArrayList<Cat> cats) {
        this.context = context;
        this.cats = cats;
    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Cat cat = cats.get(position);

        holder.catName.setText(cat.getName());
        Picasso.with(context).load(cat.getImageResourceId(context)).into(holder.catImage);

        Bitmap photo = BitmapFactory.decodeResource(context.getResources(), cat.getImageResourceId(context));

        Palette.from(photo).generate(palette -> {
            int bgColor = palette.getMutedColor(ContextCompat.getColor(context, android.R.color.black));
            holder.catNameHolder.setBackgroundColor(bgColor);
        });
    }

    public void setCats(ArrayList<Cat> cats) {
        this.cats = cats;
        notifyDataSetChanged();
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