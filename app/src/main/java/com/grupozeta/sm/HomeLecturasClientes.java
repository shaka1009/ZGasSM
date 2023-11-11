package com.grupozeta.sm;

import static com.grupozeta.sm.Utils.Ficheros.saveFile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.grupozeta.sm.adapters.CuentaClienteListAdapter;
import com.grupozeta.sm.config.Opciones;
import com.grupozeta.sm.includes.PopupError;
import com.grupozeta.sm.includes.PopupLectura;
import com.grupozeta.sm.includes.SnackbarError;
import com.grupozeta.sm.includes.Toolbar;
import com.grupozeta.sm.models.CuentaCliente;
import com.grupozeta.sm.models.ClienteLecturas;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;


import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;


public class HomeLecturasClientes extends AppCompatActivity {

    ArrayList<CuentaCliente> mCuentasCliente;

    RequestQueue mQueue;
    RequestQueue mQueue2;
    JsonArrayRequest sendLecturas;
    JSONArray jsonArray;

    RecyclerView rvCuentasCliente;

    CuentaClienteListAdapter cuentaClienteListAdapter;

    ScrollView svCuentaCliente;
    ProgressBar pbClientes;

    SnackbarError snackbarError;
    private CoordinatorLayout snackbar;

    PopupLectura mPopupLectura;
    private PopupError mPopupError;

    String idCuentaTanque;
    String numCalle;

    Button btnFinalizar;

    ArrayList<ClienteLecturas> sendClienteLecturas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_lecturas_clientes);
        Toolbar.show(this, true, "");

        idCuentaTanque = getIntent().getStringExtra("idCuentaTanque");
        numCalle = getIntent().getStringExtra("numCalle");

        //idCuentaTanque = "1902448";
        //numCalle = "80319";

        declaraciones();
        escuchadores();
        webService(idCuentaTanque, numCalle);



    }

    private void escuchadores() {
        mPopupLectura.popupConfirmar.setOnClickListener(v -> new Thread(() -> {

            runOnUiThread(() ->
            {
                if(mPopupLectura.getLectura() == -1)
                {
                    mPopupLectura.setError("Error: debes ingresar una lectura.");
                }
                else if(mPopupLectura.getLectura() < mCuentasCliente.get(mPopupLectura.getPosition()).getLectura() )
                {
                    mPopupLectura.setError("Error: la lectura nueva no puede ser menor a la lectura anterior.");
                }
                else if(mCuentasCliente.get(mPopupLectura.getPosition()).getLectura() == mPopupLectura.getLectura()) //ES IGUAL - ID:1
                {
                    if(mPopupLectura.isToggleAdvertencia() && mPopupLectura.getToggleIdAdvertencia()==1)
                    {
                        mCuentasCliente.get(mPopupLectura.getPosition()).setLectura_nueva(mPopupLectura.getLectura());
                        saveFile(HomeLecturasClientes.this, mCuentasCliente, idCuentaTanque, numCalle);
                        mPopupLectura.hidePopupCerrarSesion();
                    }
                    else {
                        mPopupLectura.setError("Advertencia: La lectura es igual a la anterior, ¿Estás seguro?");
                        UIUtil.hideKeyboard(HomeLecturasClientes.this); //ESCONDER TECLADO
                        mPopupLectura.setToggleIdAdvertencia(1);
                    }
                }
                else if(mCuentasCliente.get(mPopupLectura.getPosition()).getId_estatus().equals("C") && (mPopupLectura.getLectura()>mCuentasCliente.get(mPopupLectura.getPosition()).getLectura())) //Con contrato cancelado pero cambió % - ID 2
                {
                    if(mPopupLectura.isToggleAdvertencia() && mPopupLectura.getToggleIdAdvertencia()==2)
                    {
                        mCuentasCliente.get(mPopupLectura.getPosition()).setLectura_nueva(mPopupLectura.getLectura());
                        saveFile(HomeLecturasClientes.this, mCuentasCliente, idCuentaTanque, numCalle);
                        mPopupLectura.hidePopupCerrarSesion();
                    }
                    else
                    {
                        mPopupLectura.setError("Advertencia: Cliente cancelado, la lectura nueva subió, ¿Estás seguro?");
                        UIUtil.hideKeyboard(HomeLecturasClientes.this); //ESCONDER TECLADO
                        mPopupLectura.setToggleIdAdvertencia(2);
                    }
                }
                else if(mCuentasCliente.get(mPopupLectura.getPosition()).getLectura() + Opciones.consumoMinimo == mPopupLectura.getLectura()) //Subió 1 metro cúbico - ID 3
                {
                    if(mPopupLectura.isToggleAdvertencia() && mPopupLectura.getToggleIdAdvertencia()==3)
                    {
                        mCuentasCliente.get(mPopupLectura.getPosition()).setLectura_nueva(mPopupLectura.getLectura());
                        saveFile(HomeLecturasClientes.this, mCuentasCliente, idCuentaTanque, numCalle);
                        mPopupLectura.hidePopupCerrarSesion();
                    }
                    else
                    {
                        mPopupLectura.setError("Advertencia: La lectura nueva solo subió 1 mt. cúbico, ¿Estás seguro?");
                        UIUtil.hideKeyboard(HomeLecturasClientes.this); //ESCONDER TECLADO
                        mPopupLectura.setToggleIdAdvertencia(3);
                    }
                }
                else if(((mPopupLectura.getLectura() - mCuentasCliente.get(mPopupLectura.getPosition()).getLectura()))>((mCuentasCliente.get(mPopupLectura.getPosition()).getPromedio() * Opciones.excedeConsumo) + mCuentasCliente.get(mPopupLectura.getPosition()).getPromedio())) //LECTURA SOBREPASA LÍMITE - ID4
                {
                    if(mPopupLectura.isToggleAdvertencia() && mPopupLectura.getToggleIdAdvertencia()==4)
                    {
                        mCuentasCliente.get(mPopupLectura.getPosition()).setLectura_nueva(mPopupLectura.getLectura());
                        saveFile(HomeLecturasClientes.this, mCuentasCliente, idCuentaTanque, numCalle);
                        mPopupLectura.hidePopupCerrarSesion();
                    }
                    else
                    {
                        mPopupLectura.setError("Advertencia: la lectura nueva sobrepasa el " + ((int) (Opciones.excedeConsumo*100)) + "%, ¿Estás seguro?" +
                                "\nEste caso necesita validarse con un administrador.");
                        UIUtil.hideKeyboard(HomeLecturasClientes.this); //ESCONDER TECLADO
                        mPopupLectura.setToggleIdAdvertencia(4);
                    }
                }
                else
                {
                    mCuentasCliente.get(mPopupLectura.getPosition()).setLectura_nueva(mPopupLectura.getLectura());
                    saveFile(HomeLecturasClientes.this, mCuentasCliente, idCuentaTanque, numCalle);
                    mPopupLectura.hidePopupCerrarSesion();
                }

                cuentaClienteListAdapter.updateData();
            });
        }).start());


        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlSendLecturas = Home.API_LINK + "Lecturas/Insertar/" + idCuentaTanque;
                sendClienteLecturas = new ArrayList<ClienteLecturas>();
                Gson gson = new Gson();
                String json = new String();

                //Valida que todos los datos estén capturados.
                for(int x=0; x<mCuentasCliente.size();x++)
                {
                    if(mCuentasCliente.get(x).getLectura_nueva()==-1)
                    {
                        //Toast.makeText(HomeLecturasClientes.this, "Faltan datos por capturar.", Toast.LENGTH_SHORT).show();
                        //return;
                    }
                    else
                        sendClienteLecturas.add(new ClienteLecturas(mCuentasCliente.get(x).getId_cliente(),mCuentasCliente.get(x).getLectura_nueva()));
                }

                try {
                    jsonArray = new JSONArray(gson.toJson(sendClienteLecturas));
                    json = gson.toJson(sendClienteLecturas);
                    System.out.println("DEPURACION - JSONARRAY" + jsonArray);
                    System.out.println("DEPURACION - JSONSTRING" + json);
                } catch (JSONException e) {
                    Toast.makeText(HomeLecturasClientes.this, "Error al enviar lecturas.", Toast.LENGTH_SHORT).show();
                }
                
                webServiceInsertar(urlSendLecturas, json, jsonArray);
                
            }
        });
    }
/*
    private void webServiceInsertar(String urlSendLecturas, String json) {

        //URL http://10.21.115.10:8080/ApiSM/Lecturas/Insertar

        // user object that we need to send
        JSONObject userJson = new JSONObject();
        // body of the request
        JSONObject body = new JSONObject();

        try {
            // Put user attributes in a JSONObject
            userJson.put("id_cliente", 1858274);
            userJson.put("lectura_nueva", 20);
            // Put user JSONObject inside of another JSONObject which will be the body of the request
            body.put("user", userJson);


            System.out.println("Object: " + userJson);
            System.out.println("Body: " + body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                urlSendLecturas,
                userJson,
                response -> {
                    // Handle response

                }, e -> {
            // handle error
        }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        };

        mQueue2.add(jsonObjectRequest);
    }
*/

    private void webServiceInsertar(String urlSendLecturas, String json, JSONArray jsonArray) {

        //URL http://10.21.115.10:8080/ApiSM/Lecturas/Insertar

        // body of the request
        JSONObject body = new JSONObject();

        try {
            // Put user attributes in a JSONObject
            System.out.println("jsonArray: " + jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.POST,
                urlSendLecturas,
                jsonArray,
                response -> {
                    // Handle response

                }, e -> {
            // handle error
        }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        };

        mQueue2.add(jsonObjectRequest);
    }


    private void declaraciones() {
        mCuentasCliente = new ArrayList<CuentaCliente>();
        mQueue = Volley.newRequestQueue(this);
        mQueue2 = Volley.newRequestQueue(this);

        rvCuentasCliente = findViewById(R.id.rvCuentasCliente);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCuentasCliente.setLayoutManager(linearLayoutManager);

        svCuentaCliente = findViewById(R.id.svCuentaCliente);
        pbClientes = findViewById(R.id.pbClientes);

        snackbar = findViewById(R.id.snackbar_layout);
        snackbarError = new SnackbarError(snackbar, this);

        mPopupLectura = new PopupLectura(this, getApplicationContext(), findViewById(R.id.popupLectura));
        mPopupError = new PopupError(this, getApplicationContext(), findViewById(R.id.popupError));

        btnFinalizar = findViewById(R.id.btnFinalizar);
    }

    private void webService(String idCuentaTanque, String numCalle) {
        String url = Home.API_LINK + "Lecturas/CuentaCliente/" + idCuentaTanque + "/" + numCalle;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0;i<response.length();i++) {
                        JSONObject object=response.getJSONObject(i);
                        mCuentasCliente.add(new CuentaCliente(object.getString("id_cliente"), object.getString("cuenta"), object.getString("num_calle"), object.getString("nombre_calle"), object.getString("exterior1"), object.getString("exterior2"), object.getString("interior"), object.getString("id_estatus"), object.getString("nombre_cliente"), object.getString("lectura"), object.getString("ultima_fecha"), object.getString("promedio")));
                    }

                    leerFichero();

                    getMenuInflater().inflate(R.menu.menu_toolbar_captura_rapida, menu); //MOSTRAR

                    imprimirDatos();

                }catch (Exception e)
                {
                    runOnUiThread(() -> {
                        snackbarError.show("No hay conexión con el servidor." + e);
                    });
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                runOnUiThread(() -> {
                    snackbarError.show("No hay conexión con el servidor.");
                });
            }
        });





        mQueue.add(request);

    }

    private void leerFichero() {

        JSONArray jsonArray = new JSONArray();

        try {
            FileInputStream read = openFileInput("LEC_" + idCuentaTanque + "_" + numCalle + "_" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "_" + Calendar.getInstance().get(Calendar.YEAR));
            int size = read.available();
            byte[] buffer = new byte[size];
            read.read(buffer);
            read.close();
            String json = new String(buffer);

            jsonArray = new JSONArray(json);

        } catch (Exception ignored) {}

        ArrayList<ClienteLecturas> mCuentasClienteTemp = new ArrayList<ClienteLecturas>();

        try {
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject object=jsonArray.getJSONObject(i);
                mCuentasClienteTemp.add(new ClienteLecturas(Integer.parseInt(object.getString("id_cliente")), Integer.parseInt(object.getString("lectura_nueva"))));
            }
        }catch (Exception e){}


        for(int x=0; x<mCuentasClienteTemp.size();x++)
        {
            for(int y=0; y<mCuentasCliente.size();y++)
            {
                if(mCuentasCliente.get(y).getId_cliente() == mCuentasClienteTemp.get(x).getId_cliente())
                {
                    mCuentasCliente.get(y).setLectura_nueva(mCuentasClienteTemp.get(x).getLectura_nueva());
                    break;
                }
            }
        }
    }

    private void imprimirDatos() {

        cuentaClienteListAdapter = new CuentaClienteListAdapter(mCuentasCliente, HomeLecturasClientes.this);

        cuentaClienteListAdapter.setOnItemClickListener(new CuentaClienteListAdapter.OnItemClickListener() {
            @Override
            public void onLongClick(int position) {
                Toast.makeText(HomeLecturasClientes.this, "" + mCuentasCliente.get(position).getPromedio(), Toast.LENGTH_SHORT).show();
                mPopupLectura.setPopup(mCuentasCliente.get(position), position);
            }
        });

        rvCuentasCliente.setHasFixedSize(true);
        rvCuentasCliente.setLayoutManager(new LinearLayoutManager(HomeLecturasClientes.this));
        rvCuentasCliente.setAdapter(cuentaClienteListAdapter);

        pbClientes.setVisibility(View.INVISIBLE);
        svCuentaCliente.setVisibility(View.VISIBLE);
    }



    ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            Bundle args = intent.getBundleExtra("BUNDLE");
            mCuentasCliente = (ArrayList<CuentaCliente>) args.getSerializable("ARRAYLIST");
            imprimirDatos();
            cuentaClienteListAdapter.updateData();
            recarga_datos();
        }
    });

    private void recarga_datos() {
        Toast.makeText(this, "Datos actualizados.", Toast.LENGTH_SHORT).show();
    }


    Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            backPress();
        }
        else if(item.getItemId() == R.id.btnCapturaRapida)
        {
            Intent intent = new Intent(this, HomeLecturasClientesCapturaRapida.class);

            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST",(Serializable)mCuentasCliente);
            intent.putExtra("BUNDLE",args);
            intent.putExtra("idCuentaTanque", idCuentaTanque);
            intent.putExtra("numCalle", numCalle);

            ActivityResultLauncher.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        backPress();
    }

    private void backPress() {
        finish();
    }
    //BACK PRESS
}