package com.arnoldmanuel.cookpedia.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arnoldmanuel.cookpedia.CrearRecetaActivity;
import com.arnoldmanuel.cookpedia.R;
import com.arnoldmanuel.cookpedia.VerRecetaActivity;
import com.arnoldmanuel.cookpedia.model.RecetaConIngredientesConPasos;

import java.util.ArrayList;
import java.util.List;

public class RecetasAdapter extends RecyclerView.Adapter<RecetasAdapter.RecetaViewHolder> {

    private List<RecetaConIngredientesConPasos> recetas = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private OnRecetaListener onRecetaListener;

    public RecetasAdapter(Context context, OnRecetaListener onRecetaListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.onRecetaListener = onRecetaListener;
    }

    @NonNull
    @Override
    public RecetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater
                .inflate(R.layout.receta_item, parent, false);
        return new RecetaViewHolder(itemView, onRecetaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecetaViewHolder holder, int position) {
        RecetaConIngredientesConPasos recetaActual = recetas.get(position);
        if (recetaActual.receta.getImagen() != null) {
            holder.imagenReceta.setImageBitmap(BitmapFactory.decodeFile(recetaActual.receta.getImagen()));
        }
        holder.tituloReceta.setText(recetaActual.receta.getTitulo());
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    public void setRecetas(List<RecetaConIngredientesConPasos> recetas) {
        this.recetas = recetas;
        notifyDataSetChanged();
    }

    public List<RecetaConIngredientesConPasos> getRecetas() {
        return this.recetas;
    }

    public class RecetaViewHolder extends RecyclerView.ViewHolder {

        ImageView imagenReceta;
        TextView tituloReceta;
        OnRecetaListener onRecetaListener;

        public RecetaViewHolder(@NonNull View itemView, final OnRecetaListener onRecetaListener) {
            super(itemView);
            imagenReceta = itemView.findViewById(R.id.card_view_image);
            tituloReceta = itemView.findViewById(R.id.card_view_image_title);
            this.onRecetaListener = onRecetaListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecetaListener.onRecetaClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnRecetaListener {
        void onRecetaClick(int position);
    }
}
