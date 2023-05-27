package com.grupozeta.sm.includes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grupozeta.sm.R;
import com.grupozeta.sm.models.CuentaCliente;
import com.grupozeta.sm.models.Tanque;

public class PopupTanque {

    Context context;
    Context contextApp;
    View error;
    boolean pressButton;

    private Dialog popupLectura;
    public Button popupCancelar, popupConfirmar;
    private TextView tvCuerpo, tvError;

    private EditText etPorcentaje;

    private LinearLayout llError;

    public int getPosition() {
        return position;
    }

    int position;

    boolean toggleAdvertencia;
    int toggleIdAdvertencia;

    public int getToggleIdAdvertencia() {
        return toggleIdAdvertencia;
    }

    public void setToggleIdAdvertencia(int toggleIdAdvertencia) {
        this.toggleIdAdvertencia = toggleIdAdvertencia;
    }

    public int getPorcentaje()
    {
        try
        {
            return Integer.parseInt(etPorcentaje.getText().toString());
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public PopupTanque(Context context, Context contextApp, View error) {
        this.context = context;
        this.contextApp = contextApp;
        this.error = error;
        pressButton = false;
        declaration();
    }

    private void declaration() {
        ///popup cerrar sesion
        popupLectura = new Dialog(context);
        popupLectura.setContentView(R.layout.popup_tanque);
        popupLectura.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupConfirmar = popupLectura.findViewById(R.id.popupSi);
        popupCancelar = popupLectura.findViewById(R.id.popupNo);
        tvCuerpo = popupLectura.findViewById(R.id.tvCuerpo);
        tvError = popupLectura.findViewById(R.id.tvError);
        llError = popupLectura.findViewById(R.id.llError);
        etPorcentaje = popupLectura.findViewById(R.id.etPorcentaje);
    }

    @SuppressLint("SetTextI18n")
    public void setPopup(Tanque Tanque, int position)
    {
        etPorcentaje.setText("");
        etPorcentaje.requestFocus();
        tvError.setText("");
        llError.setVisibility(View.GONE);
        popupConfirmar.setText("CONFIRMAR");
        toggleAdvertencia = false;

        /*
        tvCuerpo.setText("Cuenta: " + mCuentaCliente.getCuenta() + "\n\n" +
                mCuentaCliente.getNombre_calle() + "\n" +
                mCuentaCliente.getInteriores() + "\n\n" +
                "Lectura Anterior: " + mCuentaCliente.getLectura()+ "\n" +
                "Ingresa la nueva lectura."
        );

         */


        this.position = position;
        popupCancelar.setOnClickListener(v -> popupLectura.dismiss());
        popupLectura.show();
    }

    public boolean isToggleAdvertencia() {
        return toggleAdvertencia;
    }


    public void setError(String error)
    {
        //popupConfirmar.setText("SÍ");
        tvError.setText(error);
        llError.setVisibility(View.VISIBLE);
        toggleAdvertencia = true;
        toggleIdAdvertencia=0;
    }

    public void hidePopupCerrarSesion()
    {
        popupLectura.dismiss();
    }
}
