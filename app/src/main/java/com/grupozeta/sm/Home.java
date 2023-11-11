package com.grupozeta.sm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.grupozeta.sm.includes.PopupCerrarSesion;
import com.grupozeta.sm.includes.SnackbarError;
import com.grupozeta.sm.includes.Toolbar;
import com.grupozeta.sm.models.Usuario;

public class Home extends AppCompatActivity {

    final public static String API_LINK = "http://10.21.115.10:8080/ApiSM/";

    public static Usuario mUsuario;

    NavigationView navigationView;
    private DrawerLayout drawer;

    private boolean pressButton;

    private PopupCerrarSesion mPopupCerrarSesion;

    private TextView tvSaludo;

    CardView cvLecturas;

    private ConnectivityManager cm;

    private CoordinatorLayout snackbar;
    SnackbarError snackbarError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar.showHome(this, true);

        inicializar();
        escuchadores();
        listenner_drawer();

        //startActivity(new Intent(Home.this , HomeLecturas.class));
        //finish();
    }

    private void escuchadores() {
        cvLecturas.setOnClickListener(view -> {
            new Thread(() -> {
                if(pressButton)
                    return;
                else pressButton = true;

                if(isConnected())
                    startActivity(new Intent(Home.this , HomeLecturas.class));
                else {
                    runOnUiThread(() -> {
                        snackbarError.show("No hay conexión a internet.");
                    });
                }
                SleepButton();
            }).start();
        });


        mPopupCerrarSesion.popupCerrarSesionSalir.setOnClickListener(v -> new Thread(() -> {
            mPopupCerrarSesion.hidePopupCerrarSesion();
            if(pressButton)
                return;
            else pressButton = true;


            try{
                deleteFile("Acc_App");
            }catch(Exception ignored){}

            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
            SleepButton();
            finish();
        }).start());
    }

    private boolean isConnected(){
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void inicializar() {
        //MENÚ DESLIZANTE
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        pressButton = false;

        mPopupCerrarSesion = new PopupCerrarSesion(this, getApplicationContext(), findViewById(R.id.popupError));

        tvSaludo = findViewById(R.id.tvSaludo);
        try {
            tvSaludo.setText(mUsuario.getNombreCompleto());
        }catch(Exception e)
        {

        }


        cvLecturas = findViewById(R.id.cvLecturas);

        cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);


        snackbar = findViewById(R.id.snackbar_layout);
        snackbarError = new SnackbarError(snackbar, this);
    }

    private void SleepButton()
    {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pressButton = false;
        }).start();
    }







    @SuppressLint("NonConstantResourceId")
    private void listenner_drawer() {
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(item -> {

            if(item.getItemId() == R.id.menu_perfil)
            {
                if(!pressButton)
                {
                    pressButton = true;
                    startActivity(new Intent(Home.this, HomePerfil.class));
                    SleepButton();
                }
            }
            else if(item.getItemId() == R.id.acerca_de_app)
            {
                if(!pressButton)
                {
                    pressButton = true;
                    startActivity(new Intent(Home.this, HomeAcerca.class));
                    SleepButton();
                }
            }
            else if(item.getItemId() == R.id.cerrar_sesion)
            {
                if(!pressButton){
                    pressButton = true;
                    mPopupCerrarSesion.setPopupCerrarSesion(mUsuario.getNombre());
                    SleepButton();
                }
            }

            drawer.closeDrawers();
            return false;
        });
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(Gravity.LEFT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.moveTaskToBack(true); //Minimizar
    }
}