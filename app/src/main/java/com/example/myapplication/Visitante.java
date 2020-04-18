package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class Visitante extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Adaptador_tiendas adaptador_tiendas;
    private RecyclerView.LayoutManager layoutManager;
    public Visitante ctx = Visitante.this;
    public TextView texto_1;
    public TextView texto_2;

    String url_tiendas = "https://benelliraul.pythonanywhere.com/visitantes_app";
    String valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitante);
        final ArrayList<Tiendas> mDataset = new ArrayList<>();
        SharedPreferences sharedPref = getSharedPreferences("teinda_logueada",this.MODE_PRIVATE);
        String id_actual =sharedPref.getString("id_tienda","null");
        String longitud = sharedPref.getString("longitud_usuario","no se che");
        valor = getIntent().getExtras().getString("url","no");
        direccion();
        texto_1 = (TextView) findViewById(R.id.texto_1);
        texto_2 = (TextView) findViewById(R.id.texto_2);


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonArrayRequest request = new JsonArrayRequest(url_tiendas, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject objeto = response.getJSONObject(i);
                        String nombre = objeto.getString("nombre_tienda");
                        String direccion = objeto.getString("direccion_tienda");
                        String categoria = objeto.getString("categoria_tienda");
                        String ruta = "https://benelliraul.pythonanywhere.com" + objeto.getString("imagen_portada_tienda");
                        String ruta_img = ruta.replaceAll("\\\\", "");
                        String id = objeto.getString("id_tienda");
                        String correo = objeto.getString("correo_electronico");
                        String celular = objeto.getString("contacto");
                        mDataset.add(new Tiendas(nombre, direccion, celular, correo, id, categoria, ruta_img, 0, 0));

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }iniciar_vista(mDataset);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "Error al cargar los datos", Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(request);


    }
    public void direccion(){
        if (valor != "no"){
            url_tiendas=valor;
        }else url_tiendas = "https://benelliraul.pythonanywhere.com/visitantes_app";
    }

    private void iniciar_vista(ArrayList<Tiendas> datastet) {

        Adaptador_tiendas mAdapter = new Adaptador_tiendas(datastet,ctx);
        recyclerView.setAdapter(mAdapter);
    }
    public void ir_a_localizacion (View view){
        Intent localizacion = new Intent(this, Visitante_inicio.class);
        startActivity(localizacion);
    }
    public void ir_a_logueado(View view){
        Intent login = new Intent(this,Registro.class);
        startActivity(login);
    }
    public void ir_a_crear (View view){
        Intent crear = new Intent(this,Datos_gps.class);
        crear.putExtra("actividad_siguiente", "crear_tienda");
        startActivity(crear);
    }
}
