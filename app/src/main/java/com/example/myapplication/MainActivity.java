package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.R.drawable.error;


public class MainActivity extends AppCompatActivity {
    //<action android:name="android.intent.action.MAIN" />
    String id_actual ="";
    /*
    @Override
    public  void onRestart() {
        super.onRestart();
        evaluar_id();
    }

     */
    MainActivity ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //final MainActivity ctx = this;
        //ir_a_inicio(ctx.getCurrentFocus());
        Intent intent = new Intent(ctx,inicioActivity.class);
        startActivity(intent);
    }
}


    /*
    private void ir_a_inicio(View view) {
        Intent intent = new Intent(ctx,inicioActivity.class);
        startActivity(intent);
    }

    public void ir_a_logueado(View view){
        Intent login = new Intent(ctx,Registro.class);
        startActivity(login);
    }
    private void evaluar_id() {
        SharedPreferences sharedPref = getSharedPreferences("teinda_logueada",this.MODE_PRIVATE);
        String id_actual =sharedPref.getString("id_tienda","null");
        if (id_actual.equals("crear")){
            ir_a_inicio(ctx.getCurrentFocus());

        }else {
            ir_a_logueado(ctx.getCurrentFocus());
        }
    }
}

     */



