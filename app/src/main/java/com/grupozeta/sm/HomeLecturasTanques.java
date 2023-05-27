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
import com.grupozeta.sm.models.FilePorcentajesNuevos;
import com.grupozeta.sm.models.Tanque;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;

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

        leerFichero();
    }

    private void leerFichero() {

        JSONArray jsonArray = new JSONArray();

        try {
            FileInputStream read = openFileInput("TAN_" + idCuentaTanque + "_" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "_" + Calendar.getInstance().get(Calendar.YEAR));
            int size = read.available();
            byte[] buffer = new byte[size];
            read.read(buffer);
            read.close();
            String json = new String(buffer);

            jsonArray = new JSONArray(json);

        } catch (Exception ignored) {}

        ArrayList<FilePorcentajesNuevos> mTanquesTemp = new ArrayList<FilePorcentajesNuevos>();

        try {
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject object=jsonArray.getJSONObject(i);
                mTanquesTemp.add(new FilePorcentajesNuevos(Integer.parseInt(object.getString("id_tanque")), Integer.parseInt(object.getString("porcentaje_nuevo"))));
            }
        }catch (Exception e){}


        for(int x=0; x<mTanquesTemp.size();x++)
        {
            for(int y=0; y<mTanques.size();y++)
            {
                if(mTanques.get(y).getId_tanque() == mTanquesTemp.get(x).getId_tanque())
                {
                    mTanques.get(y).setPorcentaje_nuevo(mTanquesTemp.get(x).getPorcentaje_nuevo());
                    break;
                }
            }
        }

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