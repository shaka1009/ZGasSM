package com.grupozeta.sm.Utils;

import android.content.Context;
import android.os.Environment;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.grupozeta.sm.models.CuentaCliente;
import com.grupozeta.sm.models.FileLecturasNuevas;

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
            removeFile(context, idCuentaTanque);
        }catch (Exception ignored){}

//NO SIRVE

        /*
        try
        {
            File root = new File("/tempLecturas/");
            if (!root.exists()) {
                root.mkdir();
            }
        }
        catch (Exception e){}*/

        try
        {
            FileOutputStream conf = context.openFileOutput("LEC_" + idCuentaTanque + "_" + numCalle + "_"+ (Calendar.getInstance().get(Calendar.MONTH)+1) + "_" + Calendar.getInstance().get(Calendar.YEAR), Context.MODE_PRIVATE);
            conf.write(json.getBytes());
            conf.close();
        }catch (Exception ignored){}
    }

    public static void removeFile(Context context, String id)
    {
        try{
            context.deleteFile("LEC_" + id + "_" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "_" + Calendar.getInstance().get(Calendar.YEAR));
        }catch(Exception ignored){}
    }

}
