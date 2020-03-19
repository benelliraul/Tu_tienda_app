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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_producto);

        imagen_producto = (ImageView) findViewById(R.id.imagen_producto);
        Button botonEnviarProducto;
        Button botonCancelar;
        final addProducto_Activity ctx = this;
        SharedPreferences sharedPref = getSharedPreferences("teinda_logueada",this.MODE_PRIVATE);
        final String id_tienda =sharedPref.getString("id_tienda","null");
        final SharedPreferences.Editor editor = sharedPref.edit();


        botonEnviarProducto =  (Button) findViewById(R.id.enviarNuevoProducto);
        botonCancelar = (Button) findViewById(R.id.cancelarEnviarProd);

        final TextView nombreProducto = (TextView) findViewById(R.id.nom_nuevo_producto);
        final TextView descripcionNuevoProducto = (TextView) findViewById(R.id.descripcionNuevoProd);
        final TextView precioProducto = (TextView) findViewById(R.id.precioProducto);

        final String[] url_imagen = {""};

        botonEnviarProducto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final RequestQueue queue = Volley.newRequestQueue(addProducto_Activity.this);
                String url_aceptarEditarPerfil = "https://benelliraul.pythonanywhere.com/nuevo_producto_app/"+ id_tienda;
                Toast.makeText(ctx,url_aceptarEditarPerfil,Toast.LENGTH_LONG).show();

                StringRequest postRequest = new StringRequest(Request.Method.POST, url_aceptarEditarPerfil,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(ctx, "Creando nuevo producto", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError errorr) {
                                Toast.makeText(ctx, "Error al crear producto "+ errorr, Toast.LENGTH_SHORT).show();
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
            };
        });
    }

    public void cargar(View viev){cargar_imagen();}

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

}
