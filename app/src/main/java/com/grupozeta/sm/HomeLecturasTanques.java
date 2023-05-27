package com.grupozeta.sm;

import static com.grupozeta.sm.Utils.Ficheros.saveFile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.grupozeta.sm.adapters.CuentaClienteListAdapter;
import com.grupozeta.sm.adapters.TanqueListAdapter;
import com.grupozeta.sm.config.Opciones;
import com.grupozeta.sm.includes.PopupLectura;
import com.grupozeta.sm.includes.PopupTanque;
import com.grupozeta.sm.includes.Toolbar;
import com.grupozeta.sm.models.CuentaCliente;
import com.grupozeta.sm.models.Tanque;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.ArrayList;

public class HomeLecturasTanques extends AppCompatActivity {

    TanqueListAdapter tanqueListAdapter;
    RecyclerView rvTanques;
    ArrayList<Tanque> mTanques;
    String idCuentaTanque;

    PopupTanque mPopupTanque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_lecturas_tanques);
        Toolbar.show(this, true);

        declaraciones();
        escuchadores();
        getDatosIntent();
        adapter();
    }

    private void escuchadores() {
        mPopupTanque.popupConfirmar.setOnClickListener(v -> new Thread(() -> {

            runOnUiThread(() ->
            {
                if(mPopupTanque.getPorcentaje() == -1)
                {
                    mPopupTanque.setError("Error: debes ingresar un porcentaje.");
                }

                else if(mPopupTanque.getPorcentaje() > 100)
                {
                    mPopupTanque.setError("Error: el porcentaje no puede ser mayor al 100%.");
                }

                else
                {
                    mTanques.get(mPopupTanque.getPosition()).setPorcentaje_nuevo(mPopupTanque.getPorcentaje());
                    saveFile(HomeLecturasTanques.this, mTanques, idCuentaTanque);
                    mPopupTanque.hidePopupCerrarSesion();
                }

                tanqueListAdapter.updateData();
            });
        }).start());
    }

    private void adapter() {
        tanqueListAdapter = new TanqueListAdapter(mTanques, HomeLecturasTanques.this);

        tanqueListAdapter.setOnItemClickListener(new TanqueListAdapter.OnItemClickListener() {
            @Override
            public void onLongClick(int position) {
                mPopupTanque.setPopup(mTanques.get(position), position);
            }
        });

        rvTanques.setHasFixedSize(true);
        rvTanques.setLayoutManager(new LinearLayoutManager(HomeLecturasTanques.this));
        rvTanques.setAdapter(tanqueListAdapter);
    }


    private void declaraciones() {
        mTanques = new ArrayList<Tanque>();
        rvTanques = findViewById(R.id.rvTanques);
        mPopupTanque = new PopupTanque(this, getApplicationContext(), findViewById(R.id.popupTanque));
    }

    private void getDatosIntent() {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        mTanques = (ArrayList<Tanque>) args.getSerializable("ARRAYLIST");
        idCuentaTanque = intent.getStringExtra("idCuentaTanque");
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