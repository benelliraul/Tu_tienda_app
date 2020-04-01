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
    boolean b = true;
    Double latitud_actual;
    Double longitud_actual;
    Boolean estado = new Boolean(b);
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
        latitud_actual =0.0;
        longitud_actual=0.0;
        nombre = (EditText) findViewById(R.id.Nombre_ingresado);
        direccion = (EditText)findViewById(R.id.Direccion_ingresada);
        celular = (EditText)findViewById(R.id.Telefono_ingresado);
        correo = (EditText)findViewById(R.id.Correo_ingresado);
        contrasena = (EditText)findViewById(R.id.Contrasena_ingresada);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }


    }
    public void registrar_lat_long(Location loc){
        latitud_actual=loc.getLatitude();
        longitud_actual=loc.getLongitude();
        SharedPreferences sharedPref = getSharedPreferences("teinda_logueada", ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Double latitud= loc.getLatitude();
        Double longitud = loc.getLongitude();
        editor.putString("latitud_usuario", latitud.toString());
        editor.putString("longitud_usuario", longitud.toString());
        editor.apply();
        editor.commit();
    }
    public boolean cambiar(boolean bol){
        if (!bol) {bol=true;
        }
        return bol;
    }
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Crear_tienda.Localizacion Local = new Crear_tienda.Localizacion();
        Looper looper = null;


        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local,looper);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local,looper);
        //mensaje1.setText("Localización agregada");
        //mensaje2.setText("");
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    //mensaje2.setText("Mi direccion es: \n"
                            //+ DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        Crear_tienda localizar;
        public Crear_tienda getMainActivity() {
            return localizar;
        }
        public void setMainActivity(Crear_tienda localizar) {
            this.localizar = localizar;
        }
        @Override
        public void onLocationChanged(Location loc) {
            localizar.getApplicationContext();
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();

            registrar_lat_long(loc);

            //guardar_datos(latitud.toString(),longitud.toString());
            //Toast.makeText(Localizar.this, "lat: " + latitud.toString(), Toast.LENGTH_LONG).show();

            //mensaje1.setText(Text);
            estado = cambiar(estado);
            this.localizar.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //mensaje1.setText("Obteniendo datos de GPS....");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //mensaje1.setText("GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
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
        if (latitud_actual != 0.0){Envair_los_datos();
            Toast.makeText(ctx, "se estan enviando los datos", Toast.LENGTH_SHORT).show();
        }else {
            intentar_enviar();
        }
    }
    public void intentar_enviar(){
        if (latitud_actual != 0.0){Envair_los_datos();
            Toast.makeText(ctx, "se estan enviando los datos", Toast.LENGTH_SHORT).show();
        }else {
            try {
                Toast.makeText(ctx, "Aún no se recibieron los datos de localizacion de su tienda, lo intentaremom nuevamente en 5 segundos", Toast.LENGTH_LONG).show();
                Thread.sleep(3000);
                intentar_enviar();
            } catch (InterruptedException e) {
                Toast.makeText(ctx, "Ocurrió un error", Toast.LENGTH_SHORT).show();
            }
        }    }

    private void Envair_los_datos() {
        if(nombre.getText()!=null && direccion.getText() != null && celular != null && correo != null && contrasena != null){
            RequestQueue queue = Volley.newRequestQueue(this);
            String url_form="http://benelliraul.pythonanywhere.com/formulario_app";
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
                    parametros.put("latitud",latitud_actual.toString());
                    parametros.put("longitud",longitud_actual.toString());


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
