package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Datos_gps extends AppCompatActivity {
    Datos_gps ctx = this;
    String actividad_siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_gps);

        actividad_siguiente = getIntent().getExtras().getString("actividad_siguiente","no");

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }else{
            locationStart();
        }

    }
    public void registrar_lat_long(Location loc){
        SharedPreferences sharedPref = getSharedPreferences("teinda_logueada", ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Double latitud= loc.getLatitude();
        Double longitud = loc.getLongitude();
        editor.putString("latitud_usuario", latitud.toString());
        editor.putString("longitud_usuario", longitud.toString());
        editor.apply();
        editor.commit();
    }
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Datos_gps.Localizacion Local = new Datos_gps.Localizacion();
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
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    public class Localizacion implements LocationListener {
        Datos_gps localizar;
        public Datos_gps getMainActivity() {

            return localizar;
        }
        public void setMainActivity(Datos_gps localizar) {

            this.localizar = localizar;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onLocationChanged(Location loc) {
            Context applicationContext = localizar.getApplicationContext();
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            registrar_lat_long(loc);
            actividad_siguiente(ctx.getCurrentFocus());
            localizar.getMainLooper().quit();
            finish();


        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //Toast.makeText(ctx, "No es posible obtener ubicacion, active el GPS....", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(ctx, "Obteniendo datos de ubcacion...", Toast.LENGTH_SHORT).show();
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

    public void actividad_siguiente(View view){
        if (actividad_siguiente.equals("buscar_cerca")) {
            Intent buscar_cerca = new Intent(this,Buscar_cerca.class);
            startActivity(buscar_cerca);

        }else{
            if (actividad_siguiente.equals("crear_tienda")) {
                Intent crear_tienda = new Intent(this,Crear_tienda.class);
                startActivity(crear_tienda);

            }else {
                Intent inicio = new Intent (this, inicioActivity.class);
                startActivity(inicio);

            }
        }
    }
}
