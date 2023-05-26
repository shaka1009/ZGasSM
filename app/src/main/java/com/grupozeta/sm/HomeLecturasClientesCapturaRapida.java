package com.grupozeta.sm;

import static com.grupozeta.sm.Utils.Ficheros.saveFile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.grupozeta.sm.config.Opciones;
import com.grupozeta.sm.includes.PopupError;
import com.grupozeta.sm.includes.PopupLecturaRapida;
import com.grupozeta.sm.includes.Toolbar;
import com.grupozeta.sm.models.CuentaCliente;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeLecturasClientesCapturaRapida extends AppCompatActivity {

    ArrayList<CuentaCliente> mCuentasCliente;
    LinearLayout llDatosCliente;
    LinearLayout llLogo;
    TextView tvCuenta;
    TextView tvNombre;
    TextView tvDomicilio;
    TextView tvInteriores;
    TextView tvLecturaAntigua;
    EditText etLecturaNueva;
    TextView tvFecha;
    ImageView ivLogo;
    Button btnSiguiente;
    int x;
    private LottieAnimationView success;
    PopupLecturaRapida mPopup;
    private PopupError mPopupError;

    String idCuentaTanque;
    String numCalle;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_lecturas_clientes_captura_rapida);
        Toolbar.show(this, true);

        getDatosIntent();
        declaraciones();
        escuchadores();
        mostrarDatos();
    }

    private void declaraciones() {
        llDatosCliente = findViewById(R.id.llDatosCliente);
        tvCuenta = findViewById(R.id.tvCuenta);
        tvNombre = findViewById(R.id.tvNombre);
        tvDomicilio = findViewById(R.id.tvDomicilio);
        tvInteriores = findViewById(R.id.tvInteriores);
        tvLecturaAntigua = findViewById(R.id.tvLecturaAntigua);
        etLecturaNueva = findViewById(R.id.etLecturaNueva);
        tvFecha = findViewById(R.id.tvFecha);
        ivLogo = findViewById(R.id.ivLogo);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        success = findViewById(R.id.success);
        llLogo = findViewById(R.id.llLogo);
        x = 0;

        mPopup = new PopupLecturaRapida(this, getApplicationContext(), findViewById(R.id.popupLecturaRapida));
        mPopupError = new PopupError(this, getApplicationContext(), findViewById(R.id.popupError));
    }

    private void escuchadores() {
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valDatos();
            }
        });

        llDatosCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtil.hideKeyboard(HomeLecturasClientesCapturaRapida.this); //ESCONDER TECLADO
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            if (isOpen)
                llLogo.setVisibility(View.GONE);
            else
                llLogo.setVisibility(View.VISIBLE);
        });

        mPopup.popupConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCuentasCliente.get(x).setLectura_nueva(Integer.parseInt(etLecturaNueva.getText().toString()));
                x++;
                mostrarDatos();
                mPopup.hidePopup();

                saveFile(HomeLecturasClientesCapturaRapida.this, mCuentasCliente, idCuentaTanque, numCalle);
            }
        });
    }

    private void valDatos() {
        if(etLecturaNueva.getText().toString().equals(""))
        {
            mPopupError.setPopupError("Error: debes ingresar una lectura.");
        }
        else if(Integer.parseInt(etLecturaNueva.getText().toString()) < mCuentasCliente.get(x).getLectura())
        {
            mPopupError.setPopupError("Error: la lectura nueva no puede ser menor a la lectura anterior.");
        }
        else if(mCuentasCliente.get(x).getLectura() == Integer.parseInt(etLecturaNueva.getText().toString()))
        {
            mPopup.setPopup("La lectura es igual a la anterior, ¿Estás seguro?");
        }
        else if(mCuentasCliente.get(x).getId_estatus().equals("C") && (Integer.parseInt(etLecturaNueva.getText().toString())>mCuentasCliente.get(x).getLectura()))
        {
            mPopup.setPopup("Cliente cancelado, la lectura nueva subió, ¿Estás seguro?");
        }
        else if(mCuentasCliente.get(x).getLectura() + Opciones.consumoMinimo == Integer.parseInt(etLecturaNueva.getText().toString())) //Subió 1 metro cúbico - ID 3
        {
            mPopup.setPopup("La lectura nueva solo subió 1 mt. cúbico, ¿Estás seguro?");
        }
        else if(((Integer.parseInt(etLecturaNueva.getText().toString()) - mCuentasCliente.get(x).getLectura()))>((mCuentasCliente.get(x).getPromedio() * Opciones.excedeConsumo) + mCuentasCliente.get(x).getPromedio())) //LECTURA SOBREPASA LÍMITE - ID4
        {
            mPopup.setPopup("La lectura nueva sobrepasa el " + ((int) (Opciones.excedeConsumo*100)) + "%, ¿Estás seguro?" +
                    "\n\nEste caso necesita validarse con un administrador.");
        }
        else
        {
            mCuentasCliente.get(x).setLectura_nueva(Integer.parseInt(etLecturaNueva.getText().toString()));
            x++;
            mostrarDatos();
            saveFile(HomeLecturasClientesCapturaRapida.this, mCuentasCliente, idCuentaTanque, numCalle);
        }
    }

    private void getDatosIntent()
    {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        mCuentasCliente = (ArrayList<CuentaCliente>) args.getSerializable("ARRAYLIST");
        idCuentaTanque = intent.getStringExtra("idCuentaTanque");
        numCalle = intent.getStringExtra("numCalle");
    }

    @SuppressLint("SetTextI18n")
    public void mostrarDatos()
    {
        while(x<mCuentasCliente.size())
        {
            if(mCuentasCliente.get(x).getLectura_nueva()!=-1)
            {
                x++;
            }
            else
                break;
        }

        if(x+1>mCuentasCliente.size())
        {
            UIUtil.hideKeyboard(HomeLecturasClientesCapturaRapida.this); //ESCONDER TECLADO
            btnSiguiente.setVisibility(View.GONE);
            llDatosCliente.setVisibility(View.GONE);
            success.setVisibility(View.VISIBLE);
            success.playAnimation();
            mHandler.postDelayed(mRunnable, 1000);
        }
        else {
            etLecturaNueva.setText("");
            llDatosCliente.setVisibility(View.VISIBLE);
            tvCuenta.setText(Integer.toString(mCuentasCliente.get(x).getCuenta()));
            tvNombre.setText(mCuentasCliente.get(x).getNombre_cliente());
            tvDomicilio.setText(mCuentasCliente.get(x).getNombre_calle());
            tvInteriores.setText(mCuentasCliente.get(x).getInteriores());
            tvLecturaAntigua.setText("Lectura Anterior: " + Integer.toString(mCuentasCliente.get(x).getLectura()));
            etLecturaNueva.requestFocus();
            tvFecha.setText(mCuentasCliente.get(x).getUltima_fecha());
            ivLogo.setImageResource(mCuentasCliente.get(x).getDir());
        }
    }


    int mTimeLimit = 0;
    private Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mTimeLimit < 5) { // SEGUNDOS
                mTimeLimit++;
                mHandler.postDelayed(mRunnable, 1000);
            }
            else
            {
                Toast.makeText(HomeLecturasClientesCapturaRapida.this, "FIN DEL TIMER.", Toast.LENGTH_SHORT).show();
                mHandler.removeCallbacks(mRunnable);
                sendDatos();
                finish();
            }
        }
    };


    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }

        super.onDestroy();
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
        sendDatos();
        finish();
    }
    //BACK PRESS

    public void sendDatos()
    {
        Intent resultIntent = new Intent();
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)mCuentasCliente);
        resultIntent.putExtra("BUNDLE",args);
        setResult(RESULT_OK, resultIntent);
    }
}