package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.graphics.Bitmap.*;
import static com.example.myapplication.R.drawable.error;

public class Crear_tienda extends AppCompatActivity {
    ImageView imagen;
    EditText nombre;
    EditText direccion;
    EditText correo;
    EditText celular;
    EditText contrasena;
    Bitmap bitmap;
    Crear_tienda ctx = this;
    String latitud_actual, longitud_actual;
    String nombre_nuevo;
    String direccion_nueva ;
    String celular_nuevo ;
    String correo_nuevo ;
    String contrasena_nueva;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tienda);
        imagen = (ImageView) findViewById(R.id.imagen_cargada);
        nombre = (EditText) findViewById(R.id.Nombre_ingresado);
        direccion = (EditText)findViewById(R.id.Direccion_ingresada);
        celular = (EditText)findViewById(R.id.Telefono_ingresado);
        correo = (EditText)findViewById(R.id.Correo_ingresado);
        contrasena = (EditText)findViewById(R.id.Contrasena_ingresada);

    }

    public void onClick(View view) {
        cargar_imagen();
    }

    private void cargar_imagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Elija la aplicacion"),10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Uri path =data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
            } catch (IOException e){
                e.printStackTrace();
            }
            Picasso.with(Crear_tienda.this)
                    .load(path)
                    .error(error)
                    .fit()
                    .centerInside()
                    .into(imagen);
        }
    }

    public void Envair_datos(View view){
        SharedPreferences sharedPref = getSharedPreferences("teinda_logueada", ctx.MODE_PRIVATE);
        latitud_actual = sharedPref.getString("latitud_usuario", "-39.0000");
        longitud_actual = sharedPref.getString("longitud_usuario", "-57.84773");
        Envair_los_datos();
        Toast.makeText(ctx, "Se están enviando los datos", Toast.LENGTH_SHORT).show();
    }

    private void Envair_los_datos() {
        if(nombre.getText()!=null && direccion.getText() != null && celular != null && correo != null && contrasena != null){
            RequestQueue queue = Volley.newRequestQueue(this);
            String url_form="https://benelliraul.pythonanywhere.com/formulario_app";
            StringRequest request = new StringRequest(Request.Method.POST, url_form, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if(response.equals("El nombre ya está registrado")||(response.equals("El correo ya está registrado"))) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }else{
                        SharedPreferences sharedPref = getSharedPreferences("teinda_logueada", ctx.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        String url_img = "https://benelliraul.pythonanywhere.com/static/img_tiendas/portada" +nombre_nuevo + ".jpg";
                        editor.putString("nombre", nombre_nuevo);
                        editor.putString("celular", celular_nuevo);
                        editor.putString("correo", correo_nuevo);
                        editor.putString("direccion", direccion_nueva);
                        editor.putString("imagen", url_img);
                        editor.putString("id_tienda", response);
                        editor.apply();
                        editor.commit();
                        ir_a_logeado(ctx.getCurrentFocus());
                        finish();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"La imagen supera los 100 mb",Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    nombre_nuevo = nombre.getText().toString();
                    direccion_nueva = direccion.getText().toString();
                    celular_nuevo = celular.getText().toString();
                    correo_nuevo = correo.getText().toString();
                    contrasena_nueva= contrasena.getText().toString();
                    String imagen_nueva = convertir_img_string(bitmap);

                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("nombre",nombre_nuevo);
                    parametros.put("direccion",direccion_nueva);
                    parametros.put("celular",celular_nuevo);
                    parametros.put("correo",correo_nuevo);
                    parametros.put("contrasena",contrasena_nueva);
                    parametros.put("imagen_b64",imagen_nueva);
                    parametros.put("latitud",latitud_actual);
                    parametros.put("longitud",longitud_actual);
                    return parametros;
                }
            };
            queue.add(request);

        }

    }

    private void ir_a_logeado(View view) {
        Intent ir_a_loguead = new Intent (view.getContext(),usuario_logeado.class);
        startActivity(ir_a_loguead);
    }

    private String convertir_img_string(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG,75,array);
        byte[] imagenByte= array.toByteArray();
        String imagenStrin = Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenStrin;
    }


}
