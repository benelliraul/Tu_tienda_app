package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.myapplication.R.drawable.error;

public class usuario_logeado extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public usuario_logeado ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_logeado);
        final ArrayList<producto> mDataset = new ArrayList<>();
        final TextView ver_url = (TextView) findViewById(R.id.url_imagen_tienda);
        final TextView direccion = (TextView) findViewById(R.id.direccion_dinamica);
        final TextView correo = (TextView) findViewById(R.id.correo_dinamico);
        final TextView nombre = (TextView) findViewById(R.id.nombre_dinamico);
        final TextView celular = (TextView) findViewById(R.id.celular_dinamico);
        final ImageView imagen_tienda = (ImageView) findViewById(R.id.imagen_tienda);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_prod);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        SharedPreferences sharedPref = getSharedPreferences("teinda_logueada",this.MODE_PRIVATE);
        String id_actual =sharedPref.getString("id_tienda","1");
        final String url = "https://benelliraul.pythonanywhere.com/lista_productos/"+id_actual;
        String nombre_rec=sharedPref.getString("nombre","null");
        String direccion_rec =sharedPref.getString("direccion","null");
        String correo_rec=sharedPref.getString("correo","null");
        String celular_rec=sharedPref.getString("celular","null");
        String ruta_imagen=sharedPref.getString("imagen","null");
        String categoria_rec=sharedPref.getString("categoria","null");
        ver_url.setText(ruta_imagen);
        nombre.setText(nombre_rec);
        direccion.setText(direccion_rec);
        correo.setText(correo_rec);
        celular.setText(celular_rec);
        Picasso.with(usuario_logeado.this)
                .load(ruta_imagen)
                .error(error)
                .fit()
                .centerInside()
                .into(imagen_tienda);
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject objeto = response.getJSONObject(i);
                        String nombre = objeto.getString("nombre_producto");
                        String precio = objeto.getString("precio_producto");
                        String id_producto = objeto.getString("id_producto");
                        String descripcion = objeto.getString("descripcion_producto");
                        String ruta = "https://benelliraul.pythonanywhere.com" + objeto.getString("imagen_producto");
                        String ruta_img = ruta.replaceAll("\\\\", "");
                        mDataset.add(new producto(nombre,precio,descripcion,ruta_img));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }iniciar_productos(mDataset);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });queue.add(request);


    }
    public void iniciar_productos(ArrayList<producto> dataset){
        MyAdapter myAdapter= new MyAdapter(dataset,ctx);
        recyclerView.setAdapter(myAdapter);
    }

    public void registro_activity(View view) {
        Intent agregar_prod = new Intent(this,addProducto_Activity.class);
        startActivity(agregar_prod);
    }
    public void ir_a_inicio(View view){
        Intent ir_a_inicio = new Intent (this,inicioActivity.class);
        startActivity(ir_a_inicio);
    }
}
