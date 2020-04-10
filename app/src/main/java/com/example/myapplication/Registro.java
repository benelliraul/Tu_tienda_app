package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Registro extends AppCompatActivity {
    private Button boton_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        final Registro ctx = this;
        SharedPreferences sharedPref = getSharedPreferences("teinda_logueada",this.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        boton_login = (Button) findViewById(R.id.iniciar_sesion);
        boton_login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                final RequestQueue queue = Volley.newRequestQueue(Registro.this);

                String url_login = "https://benelliraul.pythonanywhere.com/usuario_app";
                final EditText nombre_ingresado = (EditText) findViewById(R.id.usuario_login);
                final EditText contrasena_ingresada = (EditText) findViewById(R.id.contrasena_login);
                final String nombre = nombre_ingresado.getText().toString();
                final String contrasena = contrasena_ingresada.getText().toString();
                //String  tag_string_req = "string_req";
                Map<String, String> params = new HashMap<String, String>();
                params.put("nombre",nombre);
                params.put("contrasena",contrasena);
                JSONObject jsonObj = new JSONObject(params);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url_login, jsonObj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String nombre_tienda = response.getString("nombre_tienda");
                            String celular_tienda = response.getString("contacto");
                            String correo_tienda = response.getString("correo_electronico");
                            String direccion_tienda = response.getString("direccion_tienda");
                            String categoria_tienda = response.getString("categoria_tienda");
                            String id_tienda = response.getString("id_tienda");
                            String url_imagen = "https://benelliraul.pythonanywhere.com" + response.getString("imagen_portada_tienda");
                            String url_img = url_imagen.replaceAll("\\\\", "");

                            editor.putString("nombre", nombre_tienda);
                            editor.putString("celular", celular_tienda);
                            editor.putString("correo", correo_tienda);
                            editor.putString("direccion", direccion_tienda);
                            editor.putString("categoria", categoria_tienda);
                            editor.putString("imagen", url_img);
                            editor.putString("id_tienda",id_tienda);
                            editor.apply();
                            editor.commit();
                            if(id_tienda.equals("crear")){
                                ir_a_inico(ctx.getCurrentFocus());
                            }else {
                                usuario_log(ctx.getCurrentFocus());
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Ocurrio un error, intente nuevamente ",
                                    Toast.LENGTH_LONG).show();
                        }
                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error al recibir los datos", Toast.LENGTH_SHORT).show();
                    }
            });
                queue.add(jsonObjReq);


            }
        }

        );

    }

    private void ir_a_inico(View view) {
        Intent intetn = new Intent (this,inicioActivity.class);
        Toast.makeText(this, "Usuario o contraseña no validos", Toast.LENGTH_SHORT).show();
        startActivity(intetn);
        finish();
    }
    public void usuario_log (View view) {
        Intent logueado = new Intent(view.getContext(), usuario_logeado.class);
        startActivity(logueado);
        finish();
    }
}