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

public class PopupLecturaRapida {

    Context context;
    Context contextApp;
    View error;
    boolean pressButton;

    private Dialog popupLectura;
    public Button popupCancelar, popupConfirmar;
    private TextView tvCuerpo, tvError;

    private EditText etLectura;

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

    public int getLectura()
    {
        try
        {
            return Integer.parseInt(etLectura.getText().toString());
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public PopupLecturaRapida(Context context, Context contextApp, View error) {
        this.context = context;
        this.contextApp = contextApp;
        this.error = error;
        pressButton = false;
        declaration();
    }

    private void declaration() {
        ///popup cerrar sesion
        popupLectura = new Dialog(context);
        popupLectura.setContentView(R.layout.popup_lectura_rapida);
        popupLectura.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupConfirmar = popupLectura.findViewById(R.id.popupSi);
        popupCancelar = popupLectura.findViewById(R.id.popupNo);
        tvCuerpo = popupLectura.findViewById(R.id.tvCuerpo);
        tvError = popupLectura.findViewById(R.id.tvError);
        llError = popupLectura.findViewById(R.id.llError);
        etLectura = popupLectura.findViewById(R.id.etLectura);
    }

    @SuppressLint("SetTextI18n")
    public void setPopup(String error)
    {
        tvError.setText(error);
        llError.setVisibility(View.VISIBLE);
        popupConfirmar.setText("CONFIRMAR");
        popupCancelar.setOnClickListener(v -> popupLectura.dismiss());
        popupLectura.show();
    }

    public boolean isToggleAdvertencia() {
        return toggleAdvertencia;
    }


    public void hidePopup()
    {
        popupLectura.dismiss();
    }
}
