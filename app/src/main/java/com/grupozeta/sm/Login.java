package com.grupozeta.sm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.grupozeta.sm.includes.PopupError;
import com.grupozeta.sm.includes.SnackbarError;
import com.grupozeta.sm.models.Usuario;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;

public class Login extends AppCompatActivity {

    private LinearLayout llEmpecemos;

    private ProgressBar pbLogin;

    SnackbarError snackbarError;
    private CoordinatorLayout snackbar;
    EditText etUsuario, etPassword;

    private PopupError mPopupError;

    Button btnLogin;

    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializar();
        escuchadores();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isConnected())
            snackbarError.show("No hay conexión a internet.");
    }



    private boolean btnSendPress = false;


    private void escuchadores() {
        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            if (isOpen)
                llEmpecemos.setVisibility(View.GONE);
            else
                llEmpecemos.setVisibility(View.VISIBLE);
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnSendPress)
                    return;
                else btnSendPress = true;

                runOnUiThread(() -> loadingVisible(true));

                new Thread(() -> {
                    UIUtil.hideKeyboard(Login.this); //ESCONDER TECLADO


                    //VALIDACIONES
                    if (etUsuario.length() == 0)
                    {
                        runOnUiThread(() -> {
                            mPopupError.setPopupError("Ingresa un usuario.");
                            btnSendPress = false;
                            runOnUiThread(() -> loadingVisible(false));
                        });
                    }
                    else if (etPassword.length() == 0)
                    {
                        runOnUiThread(() -> {
                            mPopupError.setPopupError("Ingresa una contraseña.");
                            btnSendPress = false;
                            runOnUiThread(() -> loadingVisible(false));
                        });
                    }
                    else if(!isConnected())
                    {
                        runOnUiThread(() -> {
                            snackbarError.show("No hay conexión a internet.");
                            btnSendPress = false;
                            runOnUiThread(() -> loadingVisible(false));
                        });
                    }
                    {
                        new Thread(() ->
                        {
                            webServiceLogin(etUsuario.getText().toString(), etPassword.getText().toString());
                        }).start();
                    }


                }).start();


            }
        });
    }

    private void webServiceLogin(String usuario, String password) {
        String url = Home.API_LINK + "Login/"+usuario+"/" + password;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //JSONArray jsonArray = response.getJSONArray("userId")

                        try {
                            Usuario mUsuario = new Usuario(response.get("id_usuario").toString(), usuario, password, response.get("id_estatususr").toString(), response.get("nombre").toString(),response.get("appaterno").toString(),response.get("apmaterno").toString(),response.get("nomina").toString(), response.get("id_rol").toString());

                            if(mUsuario.getId_usuario()==0)
                            {
                                runOnUiThread(() -> {
                                    mPopupError.setPopupError("Usuario o contraseña incorrecta.");
                                    btnSendPress = false;
                                    runOnUiThread(() -> loadingVisible(false));
                                });
                            }
                            else if(mUsuario.getId_rol()!=253 || mUsuario.getId_estatususr()!=1)
                            {
                                runOnUiThread(() -> {
                                    mPopupError.setPopupError("No tienes permisos para acceder.");
                                    btnSendPress = false;
                                    runOnUiThread(() -> loadingVisible(false));
                                });
                            }
                            else if(mUsuario.getId_usuario() != 0)
                            {
                                //GRABAR DATOS
                                saveData(mUsuario);
                            }

                        } catch (JSONException e) {
                            runOnUiThread(() -> {
                                snackbarError.show("No hay conexión con el servidor.");
                                btnSendPress = false;
                                runOnUiThread(() -> loadingVisible(false));
                            });
                        }


                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                runOnUiThread(() -> {
                    snackbarError.show("No hay conexión con el servidor.");
                    btnSendPress = false;
                    runOnUiThread(() -> loadingVisible(false));
                });
            }
        });

        mQueue.add(request);
    }

    private void saveData(Usuario mUsuario) {
        Toast.makeText(this, "Bienvenido: "  + mUsuario.getNombre() + " " + mUsuario.getAppaterno()  + " " + mUsuario.getApmaterno(), Toast.LENGTH_SHORT).show();

        try{
            //DELETE FILE
            try{
                deleteFile("Acc_App");
            }catch(Exception ignored){}
            //

            FileOutputStream conf = openFileOutput("Acc_App", Context.MODE_PRIVATE);

            conf.write(mUsuario.getString().getBytes());
            conf.close();
        }
        catch(Exception ignored){}

        Home.mUsuario = mUsuario;

        startActivity(new Intent(Login.this, Home.class));
        finish();
    }




    private void loadingVisible(boolean b)
    {
        if(b)
        {
            btnLogin.setVisibility(View.INVISIBLE);
            pbLogin.setVisibility(View.VISIBLE);
            etUsuario.setEnabled(false);
            etPassword.setEnabled(false);
        }
        else
        {
            btnLogin.setVisibility(View.VISIBLE);
            pbLogin.setVisibility(View.INVISIBLE);
            etUsuario.setEnabled(true);
            etPassword.setEnabled(true);
        }
    }

    private void inicializar() {

        llEmpecemos = findViewById(R.id.llEmpecemos);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        mPopupError = new PopupError(this, getApplicationContext(), findViewById(R.id.popupError));

        snackbar = findViewById(R.id.snackbar_layout);
        snackbarError = new SnackbarError(snackbar, this);

        pbLogin = findViewById(R.id.pbLogin);

        mQueue = Volley.newRequestQueue(this);
    }


    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}