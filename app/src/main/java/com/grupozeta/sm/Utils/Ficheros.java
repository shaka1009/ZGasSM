package com.grupozeta.sm.Utils;

import android.content.Context;
import android.os.Environment;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.grupozeta.sm.models.CuentaCliente;
import com.grupozeta.sm.models.FileLecturasNuevas;
import com.grupozeta.sm.models.FilePorcentajesNuevos;
import com.grupozeta.sm.models.Tanque;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Ficheros {

    public static void saveFile(Context context, ArrayList<CuentaCliente> mCuentasCliente, String idCuentaTanque, String numCalle)
    {
        ArrayList<FileLecturasNuevas> mCuentasClienteTemp = new ArrayList<FileLecturasNuevas>();

        for(int x=0; x<mCuentasCliente.size(); x++)
        {
            if(mCuentasCliente.get(x).getLectura_nueva() != -1)
            {
                mCuentasClienteTemp.add(new FileLecturasNuevas(mCuentasCliente.get(x).getId_cliente(), mCuentasCliente.get(x).getLectura_nueva()));
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(mCuentasClienteTemp);

        try
        {
            removeFile(context, idCuentaTanque, numCalle);
        }catch (Exception ignored){}


        try
        {
            FileOutputStream conf = context.openFileOutput("LEC_" + idCuentaTanque + "_" + numCalle + "_"+ (Calendar.getInstance().get(Calendar.MONTH)+1) + "_" + Calendar.getInstance().get(Calendar.YEAR), Context.MODE_PRIVATE);
            conf.write(json.getBytes());
            conf.close();
        }catch (Exception ignored){}
    }

    public static void saveFile(Context context, ArrayList<Tanque> mTanques, String idCuentaTanque)
    {
        ArrayList<FilePorcentajesNuevos> mPorcentajesTemp = new ArrayList<FilePorcentajesNuevos>();

        for(int x=0; x<mTanques.size(); x++)
        {
            if(mTanques.get(x).getPorcentaje_nuevo() != -1)
            {
                mPorcentajesTemp.add(new FilePorcentajesNuevos(mTanques.get(x).getId_tanque(), mTanques.get(x).getPorcentaje_nuevo()));
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(mPorcentajesTemp);

        try
        {
            removeFilePorcentaje(context, idCuentaTanque);
        }catch (Exception ignored){}

        try
        {
            FileOutputStream conf = context.openFileOutput("TAN_" + idCuentaTanque + "_"+ (Calendar.getInstance().get(Calendar.MONTH)+1) + "_" + Calendar.getInstance().get(Calendar.YEAR), Context.MODE_PRIVATE);
            conf.write(json.getBytes());
            conf.close();
        }catch (Exception ignored){}
    }

    public static void removeFile(Context context, String id, String numCalle)
    {
        try{
            context.deleteFile("LEC_" + id + "_" + numCalle + "_" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "_" + Calendar.getInstance().get(Calendar.YEAR));
        }catch(Exception ignored){}
    }


    public static void removeFilePorcentaje(Context context, String id)
    {
        try{
            context.deleteFile("TAN_" + id + "_" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "_" + Calendar.getInstance().get(Calendar.YEAR));
        }catch(Exception ignored){}
    }

}
