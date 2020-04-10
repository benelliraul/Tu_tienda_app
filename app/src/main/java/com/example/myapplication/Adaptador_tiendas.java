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

public class Adaptador_tiendas  extends RecyclerView.Adapter<Adaptador_tiendas.MyViewHolder>{
    private ArrayList<Tiendas> mDataset;
    private Context ctx;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView tarjeta;
        public TextView nombre_tienda;
        public TextView direccion_tienda;
        public TextView correo_tienda;
        public TextView celular_tienda;
        public TextView categoria_tienda;
        public ImageView imagen_tienda;
        public MyViewHolder(View v) {
            super(v);
            tarjeta = (CardView) v.findViewById(R.id.card_tienda);
            nombre_tienda =(TextView) v.findViewById(R.id.nombre_tienda);
            direccion_tienda = (TextView) v.findViewById(R.id.direccion_tienda);
            correo_tienda = (TextView) v.findViewById(R.id.correo_tienda);
            celular_tienda = (TextView) v.findViewById(R.id.celular_tienda);
            categoria_tienda = (TextView) v.findViewById(R.id.categoria_tienda); 
            imagen_tienda = (ImageView) v.findViewById(R.id.imagen_tienda);
        }
    }
    public Adaptador_tiendas(ArrayList<Tiendas> myDataset, Context context) {
        mDataset = myDataset;
        ctx = context ;
    }
    @Override
    public Adaptador_tiendas.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tarjeta_tienda, parent, false);


        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(Adaptador_tiendas.MyViewHolder holder, int position) {
        holder.nombre_tienda.setText(mDataset.get(position).getNombre());
        holder.direccion_tienda.setText(mDataset.get(position).getDireccion());
        holder.correo_tienda.setText(mDataset.get(position).getCorreo());
        holder.categoria_tienda.setText(mDataset.get(position).getCategoria());
        holder.celular_tienda.setText(mDataset.get(position).getCelular());
        try {
            Picasso.with(ctx)
                    .load(mDataset.get(position).getRuta_imagen())
                    .resize(230,230)
                    .error(error)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(holder.imagen_tienda);
        } catch (Exception e) {
            Toast.makeText(ctx, "Error: "+ e.toString(), Toast.LENGTH_LONG).show();
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


