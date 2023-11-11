package com.grupozeta.sm;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.grupozeta.sm.adapters.CuentaTanqueListAdapter;
import com.grupozeta.sm.includes.PopupError;
import com.grupozeta.sm.includes.SnackbarError;
import com.grupozeta.sm.includes.Toolbar;
import com.grupozeta.sm.models.Calle;
import com.grupozeta.sm.models.CuentaTanque;
import com.grupozeta.sm.models.FilePorcentajesNuevos;
import com.grupozeta.sm.models.Tanque;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeLecturas extends AppCompatActivity {

    EditText etCuentaTanque;
    Button btnBuscar;
    boolean btnSendPress;
    private PopupError mPopupError;
    private ProgressBar pbLogin;
    SnackbarError snackbarError;
    private CoordinatorLayout snackbar;
    RequestQueue mQueue;
    CardView cvCuentaTanque;
    TextView tvCuenta;
    TextView tvDomicilio;
    TextView tvInteriores;
    TextView tvDescripcion;
    TextView tvCantidad;
    TextView tvFecha;
    RecyclerView rvCuentasTanque;
    CuentaTanqueListAdapter cuentaTanqueListAdapter;
    ArrayList<Tanque> mTanques;
    CardView cvPorcentajeTanque;
    CuentaTanque mCuentaTanques;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_lecturas);

        Toolbar.show(this, true);

        inicializar();
        escuchadores();

    }
    private void inicializar() {
        btnBuscar = findViewById(R.id.btnBuscar);
        etCuentaTanque = findViewById(R.id.etCuentaTanque);
        btnSendPress = false;
        mPopupError = new PopupError(this, getApplicationContext(), findViewById(R.id.popupError));
        pbLogin = findViewById(R.id.pbLogin);
        snackbar = findViewById(R.id.snackbar_layout);
        snackbarError = new SnackbarError(snackbar, this);
        mQueue = Volley.newRequestQueue(this);
        cvCuentaTanque = findViewById(R.id.cvCuentaTanque);
        tvCuenta = findViewById(R.id.tvCuenta);
        tvDomicilio = findViewById(R.id.tvDomicilio);
        tvInteriores = findViewById(R.id.tvInteriores);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvCantidad = findViewById(R.id.tvCantidad);
        tvFecha = findViewById(R.id.tvFecha);
        rvCuentasTanque = findViewById(R.id.rvCuentasTanque);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCuentasTanque.setLayoutManager(linearLayoutManager);
        mTanques = new ArrayList<Tanque>();
        cvPorcentajeTanque = findViewById(R.id.cvPorcentajeTanque);
    }
    private void escuchadores() {
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnSendPress)
                    return;
                else btnSendPress = true;

                runOnUiThread(() -> loadingVisible(true));

                new Thread(() -> {
                    UIUtil.hideKeyboard(HomeLecturas.this); //ESCONDER TECLADO

                    if (etCuentaTanque.length() == 0)
                    {
                        runOnUiThread(() -> {
                            mostrarCV(false);
                            mPopupError.setPopupError("Ingresa una Cuenta Tanque.");
                            btnSendPress = false;
                            runOnUiThread(() -> loadingVisible(false));
                        });
                    }
                    else if(Integer.parseInt(etCuentaTanque.getText().toString()) < 500000 || Integer.parseInt(etCuentaTanque.getText().toString())>600000 )
                    {
                        runOnUiThread(() -> {
                            mostrarCV(false);
                            mPopupError.setPopupError("Ingresa una Cuenta Tanque válida.");
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
                    else {
                        new Thread(() ->
                        {
                            webServiceCuentaTanque(etCuentaTanque.getText().toString());
                        }).start();
                    }
                }).start();

            }
        });

        cvPorcentajeTanque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeLecturas.this, HomeLecturasTanques.class);

                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)mTanques);
                intent.putExtra("BUNDLE",args);
                intent.putExtra("idCuentaTanque", Integer.toString(mCuentaTanques.getId_cuenta()));
                ActivityResultLauncher.launch(intent);
            }
        });
    }

    androidx.activity.result.ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    Bundle args = intent.getBundleExtra("BUNDLE");
                    /*
                    mCuentasCliente = (ArrayList<CuentaCliente>) args.getSerializable("ARRAYLIST");
                    imprimirDatos();
                    cuentaClienteListAdapter.updateData();
                    recarga_datos();*/
                }
            });

    private void webServiceCuentaTanque(String cuentaTanque)
    {
        String url = Home.API_LINK + "Lecturas/CuentaTanque/" + cuentaTanque;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            CuentaTanque mCuentaTanque = new CuentaTanque(response.get("id_cuenta").toString(),
                                    response.get("cuenta").toString(),
                                    response.get("nombre_calle").toString(),
                                    response.get("nombre_colonia").toString(),
                                    response.get("codigo_postal").toString(),
                                    response.get("exterior1").toString(),
                                    response.get("exterior2").toString(),
                                    response.get("interior").toString(),
                                    response.get("descripcion").toString());

                            JSONArray arr = response.getJSONArray("calles");

                            for (int x = 0; x < arr.length(); x++) {
                                JSONObject object1 = arr.getJSONObject(x);
                                Calle mCalle = new Calle(object1.getString("num_calle"), object1.getString("nombre_calle"));
                                mCuentaTanque.addCalle(mCalle);
                            }

                            mCuentaTanques = mCuentaTanque;
                            JSONArray arr1 = response.getJSONArray("mTanques");

                            mTanques = new ArrayList<Tanque>();

                            for (int x = 0; x < arr1.length(); x++) {
                                JSONObject object1 = arr1.getJSONObject(x);
                                System.out.println("DEPURACION" + object1.getString("id_tanque"));
                                Tanque mTanque = new Tanque(object1.getString("id_tanque"), object1.getString("descripcion"), object1.getString("capacidad"), object1.getString("numero_sap"), object1.getString("porcentaje_actual"));
                               mTanques.add(mTanque);
                            }

                            leerFichero();
/*

    TOMAR SOLO 1 VALOR
                            //List<String> list = new ArrayList<String>();
                            for (int i=0; i<arr.length(); i++) {
                                //list.add( arr.getString(i) );
                                //System.out.println("DEPURACION:" + arr.getString(i));
                                mCuentaTanque.addCalle(arr.getString(i));
                            }
                            //String[] stringArray = list.toArray(new String[list.size()]);
*/

                            if(mCuentaTanque.getIdCuenta().equals("0"))
                            {
                                runOnUiThread(() -> {
                                    mostrarCV(false);
                                    mPopupError.setPopupError("La Cuenta Tanque no es válida.");
                                    btnSendPress = false;
                                    runOnUiThread(() -> loadingVisible(false));
                                });
                            }
                            else
                            {
                                loadDatos(mCuentaTanque);
                            }

                        } catch (JSONException e) {
                            runOnUiThread(() -> {
                                snackbarError.show("No hay conexión con el servidor.");
                                System.out.println("DEPURACION" + e);
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

    private void leerFichero() {

        JSONArray jsonArray = new JSONArray();

        try {
            FileInputStream read = openFileInput("TAN_" + mCuentaTanques.getIdCuenta() + "_" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "_" + Calendar.getInstance().get(Calendar.YEAR));
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

    @SuppressLint("SetTextI18n")
    private void loadDatos(CuentaTanque mCuentaTanque) {
        runOnUiThread(() -> {
            tvCuenta.setText(mCuentaTanque.getCuenta());
            tvDomicilio.setText(mCuentaTanque.getNombre_calle() + ", " + mCuentaTanque.getExterior1() + ", " + mCuentaTanque.getExterior2() + ", " + mCuentaTanque.getInterior());
            tvInteriores.setText(mCuentaTanque.getNombre_colonia() + ", CP: " + mCuentaTanque.getCodigo_postal() );
            tvDescripcion.setText(mCuentaTanque.getDescripcion());
            mostrarCV(true);

            tvFecha.setText("");
            tvCantidad.setText("Cantidad: " + Integer.toString(mTanques.size()));
        });

        cuentaTanqueListAdapter = new CuentaTanqueListAdapter(mCuentaTanque.getCalles(), HomeLecturas.this, mCuentaTanque.getCuenta());

        cuentaTanqueListAdapter.setOnItemClickListener(new CuentaTanqueListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(HomeLecturas.this, HomeLecturasClientes.class);
                intent.putExtra("idCuentaTanque", mCuentaTanque.getIdCuenta());
                intent.putExtra("numCalle", mCuentaTanque.getCalles().get(position).getNumCalle());
                startActivity(intent);
            }
        });



        rvCuentasTanque.setHasFixedSize(true);
        rvCuentasTanque.setLayoutManager(new LinearLayoutManager(HomeLecturas.this));
        rvCuentasTanque.setAdapter(cuentaTanqueListAdapter);

        runOnUiThread(() -> loadingVisible(false));
        btnSendPress = false;
        cvCuentaTanque.setVisibility(View.VISIBLE);
    }

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void loadingVisible(boolean b)
    {
        if(b)
        {
            btnBuscar.setVisibility(View.INVISIBLE);
            pbLogin.setVisibility(View.VISIBLE);
            etCuentaTanque.setEnabled(false);
        }
        else
        {
            btnBuscar.setVisibility(View.VISIBLE);
            pbLogin.setVisibility(View.INVISIBLE);
            etCuentaTanque.setEnabled(true);
        }
    }

    private void mostrarCV(boolean b)
    {
        if(b)
        {
            cvPorcentajeTanque.setVisibility(View.VISIBLE);
            cvCuentaTanque.setVisibility(View.VISIBLE);
            rvCuentasTanque.setVisibility(View.VISIBLE);
        }

        else {
            cvPorcentajeTanque.setVisibility(View.GONE);
            cvCuentaTanque.setVisibility(View.GONE);
            rvCuentasTanque.setVisibility(View.GONE);
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