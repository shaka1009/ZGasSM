package com.grupozeta.sm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.grupozeta.sm.includes.PopupCerrarSesion;
import com.grupozeta.sm.includes.Toolbar;

public class HomePerfil extends AppCompatActivity {

    TextView tvNomina;
    TextView tvNombre;

    TextView tvPuesto;

    boolean pressButton;
    Button btnCerrarSesion;

    private PopupCerrarSesion mPopupCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_perfil);
        Toolbar.show(this, true);

        declaraciones();
        escuchadores();
    }

    private void escuchadores() {
        btnCerrarSesion.setOnClickListener(view->
        {
            if(pressButton)
                return;
            else pressButton = true;

            mPopupCerrarSesion.setPopupCerrarSesion(Home.mUsuario.getNombre());

            SleepButton();
        });

        mPopupCerrarSesion.popupCerrarSesionSalir.setOnClickListener(v -> new Thread(() -> {
            mPopupCerrarSesion.hidePopupCerrarSesion();
            if(pressButton)
                return;
            else pressButton = true;

            try{
                deleteFile("Acc_App");
            }catch(Exception ignored){}

            Intent intent = new Intent(HomePerfil.this, Login.class);
            startActivity(intent);
            SleepButton();
            finish();
        }).start());
    }

    @SuppressLint("SetTextI18n")
    private void declaraciones() {

        tvPuesto = findViewById(R.id.tvPuesto);
        tvNomina = findViewById(R.id.tvNomina);
        tvNombre = findViewById(R.id.tvNombre);

        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        mPopupCerrarSesion = new PopupCerrarSesion(this, getApplicationContext(), findViewById(R.id.popupError));

        pressButton = false;

        if(Home.mUsuario.getId_rol()==253)
            tvPuesto.setText("Administrador Servicio Medido");
        else {
            tvPuesto.setText("Toma Lecturas");
        }


        tvNomina.setText(Integer.toString(Home.mUsuario.getNomina()));
        tvNombre.setText(Home.mUsuario.getNombreCompleto());
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

    @SuppressLint("RtlHardcoded")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            backPress();
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