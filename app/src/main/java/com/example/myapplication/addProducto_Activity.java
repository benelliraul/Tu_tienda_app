package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Map;

import static com.example.myapplication.R.drawable.error;


public class addProducto_Activity extends AppCompatActivity {
    ImageView imagen_producto;
    Bitmap bitmap;
    EditText nombreProducto;
    EditText descripcionNuevoProducto;
    EditText precioProducto;
    String id_tienda;
    public addProducto_Activity ctx =this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_producto);

        imagen_producto = (ImageView) findViewById(R.id.imagen_producto);
        nombreProducto = (EditText)findViewById(R.id.nombre_editar);
        descripcionNuevoProducto = (EditText)findViewById(R.id.descripcionNuevoProd);
        precioProducto = findViewById(R.id.precioProducto);
        SharedPreferences sharedPref = getSharedPreferences("teinda_logueada",this.MODE_PRIVATE);
        id_tienda =sharedPref.getString("id_tienda","null");

    }


    public void cargar(View viev){cargar_imagen();}
    public void enviar (View view){
        accion();
    }
    public void accion(){
        final RequestQueue queue = Volley.newRequestQueue(ctx);
        String url_aceptarEditarPerfil = "https://benelliraul.pythonanywhere.com/nuevo_producto_app/"+ id_tienda;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url_aceptarEditarPerfil,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ctx, "Creando nuevo producto", Toast.LENGTH_SHORT).show();
                        ir_logueado(ctx.getCurrentFocus());
                        finish();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError errorr) {
                        Toast.makeText(ctx, "Error, la imagen supera los 100 mb", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams(){
                String imagen_nueva = convertir_img_string(bitmap);
                Map<String, String> params = new HashMap<String, String>();
                params.put("producto", nombreProducto.getText().toString());
                params.put("descripcion", descripcionNuevoProducto.getText().toString());
                params.put("precio", precioProducto.getText().toString());
                params.put("imagen_b64",imagen_nueva);

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void cargar_imagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Elija la aplicacion, la imagen debe ser menor a 100 mb."),10);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Uri path =data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Picasso.with(addProducto_Activity.this)
                    .load(path)
                    .error(error)
                    .fit()
                    .centerInside()
                    .into(imagen_producto);
        }
    }

    private String convertir_img_string(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte= array.toByteArray();
        String imagenStrin = Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenStrin;
    }
    public void ir_logueado(View viewv){
        Intent logueado = new Intent(this,usuario_logeado.class);
        startActivity(logueado);
        finish();
    }
}
