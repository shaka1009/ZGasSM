package com.grupozeta.sm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.grupozeta.sm.models.Usuario;

import java.io.File;
import java.io.FileInputStream;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTheme(R.style.Theme_ServicioMedido_Loading);

        //Si existe usuario te manda al Home
        if(existDatos())
        {
            startActivity(new Intent(MainActivity.this, Home.class));
            finish();
        }
        //Si no existe usuario, va al Login.
        else {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
    }


    private boolean existDatos() {
        try {
            FileInputStream read = openFileInput("Acc_App");
            int size = read.available();
            byte[] buffer = new byte[size];
            read.read(buffer);
            read.close();
            String text = new String(buffer);
            StringTokenizer token = new StringTokenizer(text, "\n");

            Home.mUsuario = new Usuario(token.nextToken(),token.nextToken(),token.nextToken(), token.nextToken(), token.nextToken(), token.nextToken(), token.nextToken(), token.nextToken(), token.nextToken());

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}