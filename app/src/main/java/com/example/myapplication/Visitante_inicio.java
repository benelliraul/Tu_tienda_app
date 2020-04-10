package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Visitante_inicio extends AppCompatActivity {
    Visitante_inicio ctx = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitante_inicio);
        Button ver_todas = (Button) findViewById(R.id.ver_todas_tiendas);
        Button busca = (Button) findViewById(R.id.buscar_por_contenido);
        Button buscar_cercanas = (Button) findViewById(R.id.buscar_cercanas);
        Button registrse = (Button) findViewById(R.id.crear_tienda);
        Button login = (Button) findViewById(R.id.iniciar_sesion);

    }
    public void ver_todas_tiendas (View view){
        Intent ver_todas = new Intent(this,Visitante.class);
        ver_todas.putExtra("url","https://benelliraul.pythonanywhere.com/visitantes_app");
        startActivity(ver_todas);

    }
    public void registrarse_desde_visitante (View view){
        Intent registrarse = new Intent (this,Datos_gps.class);
        registrarse.putExtra("actividad_siguiente", "crear_tienda");
        startActivity(registrarse);
    }
    public void login_desde_visitante (View view){
        Intent login = new Intent(this, Registro.class);
        startActivity(login);
        finish();
    }
    public void buscar_cerca_intent ( View view) {
        Intent buscar_cerca = new Intent(this, Datos_gps.class);
        buscar_cerca.putExtra("actividad_siguiente", "buscar_cerca");
        startActivity(buscar_cerca);
    }
    public void buscar_intent (View view){
        Intent buscar = new Intent(this,Buscar.class);
        startActivity(buscar);
    }


}
