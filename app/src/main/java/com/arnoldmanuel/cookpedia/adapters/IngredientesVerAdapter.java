package com.arnoldmanuel.cookpedia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnoldmanuel.cookpedia.R;

import java.util.List;

public class IngredientesVerAdapter extends
        RecyclerView.Adapter<IngredientesVerAdapter.IngredienteVerViewHolder> {
    private List<String> textos;
    private LayoutInflater layoutInflater;

    public IngredientesVerAdapter(Context context, List<String> textos) {
        this.layoutInflater = LayoutInflater.from(context);
        this.textos = textos;
    }

    @NonNull
    @Override
    public IngredienteVerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(R.layout.ingrediente_ver_item, parent, false);
        return new IngredientesVerAdapter.IngredienteVerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredienteVerViewHolder holder, int position) {
        holder.textoIngrediente.setText(textos.get(position));
    }

    @Override
    public int getItemCount() {
        return textos.size();
    }

    public class IngredienteVerViewHolder extends RecyclerView.ViewHolder {

        public TextView textoIngrediente;

        public IngredienteVerViewHolder(@NonNull View itemView) {
            super(itemView);
            textoIngrediente = itemView.findViewById(R.id.itemTextView);

        }
    }
}
