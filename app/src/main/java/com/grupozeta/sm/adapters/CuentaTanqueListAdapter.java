package com.grupozeta.sm.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.grupozeta.sm.R;
import com.grupozeta.sm.models.Calle;

import java.util.List;


public class CuentaTanqueListAdapter extends RecyclerView.Adapter<CuentaTanqueListAdapter.ViewHolder> {

    private List<Calle> mData;
    private OnItemClickListener mListener;
    private LayoutInflater mInflater;
    private Context context;

    String cuenta;


    public interface OnItemClickListener {
        //void onItemClick(int position);

        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CuentaTanqueListAdapter(List<Calle> itemList, Context context, String cuenta)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.cuenta = cuenta;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.rv_cuentas_tanque, null);
        return new ViewHolder(view, mListener);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<Calle> items)
    {
        mData = items;
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCuenta, tvCalle, tvCantidad, tvEliminar;
        ImageView ivLogo;

        ViewHolder(View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            tvCuenta = itemView.findViewById(R.id.tvCuenta);
            tvCalle = itemView.findViewById(R.id.tvCalle);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvEliminar = itemView.findViewById(R.id.tvEliminar);

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
            });

        }
        @SuppressLint("SetTextI18n")
        void bindData(final Calle item)
        {
            tvCuenta.setText("Cuenta: " + cuenta);
            tvCalle.setText("Calle: " + item.getNombreCalle());
            tvCantidad.setText("");
            //ivLogo.setImageBitmap(item.getFoto());

            tvEliminar.setVisibility(View.GONE);
        }
    }



}
