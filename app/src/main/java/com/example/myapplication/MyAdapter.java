package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.myapplication.R.drawable.error;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<producto> mDataset;
    private Context ctx;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView tarjeta;
        public TextView nombre_producto;
        public TextView descripcion_producto;
        public TextView precio_producto;
        public ImageView imagen_producto;
        public MyViewHolder(View v) {
            super(v);
            tarjeta = (CardView) v.findViewById(R.id.la_tarjeta);
            nombre_producto =(TextView) v.findViewById(R.id.nombre_prod);
            descripcion_producto = (TextView) v.findViewById(R.id.descripcion_prod);
            precio_producto = (TextView) v.findViewById(R.id.precio_prod);
            imagen_producto = (ImageView) v.findViewById(R.id.imagen_prod);
        }
    }

    public MyAdapter(ArrayList<producto> myDataset, Context context) {
        mDataset = myDataset;
        ctx = context ;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);


        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nombre_producto.setText(mDataset.get(position).getNombre_prod());
        holder.descripcion_producto.setText(mDataset.get(position).getDescripcion_prod());
        holder.precio_producto.setText(mDataset.get(position).getPrecio_rod());
        try {
            Picasso.with(ctx)
                    .load(mDataset.get(position).getRuta_imagen())
                    .resize(230,230)
                    .error(error)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(holder.imagen_producto);
        } catch (Exception e) {
            Toast.makeText(ctx, "Error:  "+ e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
