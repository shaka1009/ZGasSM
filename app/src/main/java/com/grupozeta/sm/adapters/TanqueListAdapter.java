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
import com.grupozeta.sm.models.Tanque;

import java.util.ArrayList;
import java.util.List;


public class TanqueListAdapter extends RecyclerView.Adapter<TanqueListAdapter.ViewHolder> {

    private ArrayList<Tanque> mData;
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

    public TanqueListAdapter(ArrayList<Tanque> itemList, Context context)
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
        View view = mInflater.inflate(R.layout.rv_tanques, null);
        return new ViewHolder(view, mListener);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        holder.bindData(mData.get(position));
        holder.setIsRecyclable(false);
    }

    public void setItems(ArrayList<Tanque> items)
    {
        mData = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout llPorcentajeNuevo;
        TextView tvDescripcion, tvCapacidad, tvPorcentajeActual, tvPorcentajeNuevo;
        ImageView ivLogo;

        ViewHolder(View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvCapacidad = itemView.findViewById(R.id.tvCapacidad);
            tvPorcentajeActual = itemView.findViewById(R.id.tvPorcentajeActual);
            tvPorcentajeNuevo = itemView.findViewById(R.id.tvPorcentajeNuevo);
            llPorcentajeNuevo = itemView.findViewById(R.id.llPorcentajeNuevo);
            ivLogo = itemView.findViewById(R.id.ivLogo);

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
        void bindData(final Tanque item)
        {

            tvDescripcion.setText("Tanque #" + item.getDescripcion());
            tvCapacidad.setText("Capacidad: " + item.getCapacidad() + " Lts.");
            tvPorcentajeActual.setText("Porcentaje anterior: " + item.getPorcentaje_actual() + "%");

            if(item.getPorcentaje_nuevo()!=-1)
            {
                tvPorcentajeNuevo.setTextColor(ContextCompat.getColor(context, R.color.VerdeClaro));
                tvPorcentajeNuevo.setText(" " + item.getPorcentaje_nuevo() + "%");
                llPorcentajeNuevo.setVisibility(View.VISIBLE);
            }

        }

    }
}
