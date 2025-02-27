package com.example.esquiv2;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MiContenedor> implements View.OnClickListener {

    Context contexto;
    ArrayList<Estacion> listaDatos;
    View.OnClickListener escuchador;
    int adapterPosition;

    public Adaptador(Context contexto, ArrayList<Estacion> listaDatos, View.OnClickListener escuchador) {
        this.contexto = contexto;
        this.listaDatos = listaDatos;
        this.escuchador = escuchador;
    }

    @NonNull
    @Override
    public MiContenedor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vItem = inflador.inflate(R.layout.item_layout, parent, false);
        vItem.setOnClickListener(this);
        return new Adaptador.MiContenedor(vItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MiContenedor holder, int position) {
        Estacion estacion = listaDatos.get(position);
        holder.nombre.setText(estacion.getNombre());
        holder.cordillera.setText(estacion.getCordillera());
        holder.n_remontes.setText(String.valueOf(estacion.getN_remontes())); //IMPORTANTE todos los números con el String.valueOf()
        holder.km_pistas.setText(String.valueOf(estacion.getKm_pistas()));
        holder.imagen.setImageResource(R.drawable.esqui);
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    @Override
    public void onClick(View view) {
        escuchador.onClick(view);
    }

    public class MiContenedor extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
    {
        TextView nombre, cordillera, n_remontes, km_pistas;
        ImageView imagen;

        public MiContenedor(View itemView)
        {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            cordillera = itemView.findViewById(R.id.cordillera);
            n_remontes = itemView.findViewById(R.id.remontes);
            km_pistas = itemView.findViewById(R.id.km);
            imagen = itemView.findViewById(R.id.imagen);
            itemView.setOnCreateContextMenuListener(this);

            itemView.setOnLongClickListener(v -> {
                adapterPosition = getAdapterPosition();
                return false;
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
        {
            contextMenu.add(getAdapterPosition(), 122, 0, "BORRAR");
        }
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }
}
