package com.grupozeta.sm.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.grupozeta.sm.R;
import com.grupozeta.sm.config.Opciones;
import com.grupozeta.sm.models.Calle;
import com.grupozeta.sm.models.CuentaCliente;

import java.util.ArrayList;
import java.util.List;


public class CuentaClienteListAdapter extends RecyclerView.Adapter<CuentaClienteListAdapter.ViewHolder> {

    private List<CuentaCliente> mData;
    private OnItemClickListener mListener;

    private LayoutInflater mInflater;
    private Context context;

    public void updateData() {
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        //void onClick(int position);

        void onLongClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CuentaClienteListAdapter(List<CuentaCliente> itemList, Context context)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.rv_cuentas_cliente, null);
        return new ViewHolder(view, mListener);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        holder.bindData(mData.get(position));
        holder.setIsRecyclable(false);
    }

    public void setItems(List<CuentaCliente> items)
    {
        mData = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout llLecturaNueva;
        TextView tvCuenta, tvNombre, tvCalle, tvInteriores, tvLectura, tvFecha, tvLecturaNueva;
        ImageView ivLogo;

        ViewHolder(View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            tvCuenta = itemView.findViewById(R.id.tvCuenta);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCalle = itemView.findViewById(R.id.tvCalle);
            tvInteriores = itemView.findViewById(R.id.tvInteriores);
            tvLectura = itemView.findViewById(R.id.tvLectura);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvLecturaNueva = itemView.findViewById(R.id.tvLecturaNueva);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            llLecturaNueva = itemView.findViewById(R.id.llLecturaNueva);

            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onClick(position);
                        }
                    }
                }
            });*/

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onLongClick(position);
                        }
                    }
                    return false;
                }
            });
        }

        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        void bindData(final CuentaCliente item)
        {
            tvCuenta.setText(Integer.toString(item.getCuenta()));
            tvNombre.setText(item.getNombre_cliente());
            tvCalle.setText(item.getNombre_calle());
            tvInteriores.setText(item.getInteriores());
            tvLectura.setText("Lectura Ant: " + Integer.toString(item.getLectura()));
            ivLogo.setImageResource(item.getDir());
            tvFecha.setText(item.getUltima_fecha());

            if(item.getLectura_nueva()!=-1)
            {
                if(item.getLectura() == item.getLectura_nueva()) //ES IGUAL
                {
                    tvLecturaNueva.setTextColor(ContextCompat.getColor(context, R.color.Azul));
                }
                else if(item.getId_estatus().equals("C") && (item.getLectura_nueva()>item.getLectura())) //Con contrato cancelado pero cambió %
                {
                   tvLecturaNueva.setTextColor(ContextCompat.getColor(context, R.color.Naranja));
                }
                else if(item.getLectura() + Opciones.consumoMinimo == item.getLectura_nueva()) //Subió 1 metro cúbico
                {
                    tvLecturaNueva.setTextColor(ContextCompat.getColor(context, R.color.Amarillo));
                }
                else if(((item.getLectura_nueva() - item.getLectura()))>((item.getPromedio() * Opciones.excedeConsumo) + item.getPromedio()))
                {
                    tvLecturaNueva.setTextColor(ContextCompat.getColor(context, R.color.Rojo));
                }
                else
                {
                    tvLecturaNueva.setTextColor(ContextCompat.getColor(context, R.color.VerdeClaro)); //Lecturas validadas
                }

                tvLecturaNueva.setText(Integer.toString(item.getLectura_nueva()));
                llLecturaNueva.setVisibility(View.VISIBLE);

            }

        }

    }
}
