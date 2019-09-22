package com.arnoldmanuel.cookpedia.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.arnoldmanuel.cookpedia.R;

import java.util.List;

public class IngredientesAdapter extends
        RecyclerView.Adapter<IngredientesAdapter.IngredienteViewHolder> {

    private List<String> textos;
    private LayoutInflater layoutInflater;

    public IngredientesAdapter(Context context, List<String> textos) {
        this.layoutInflater = LayoutInflater.from(context);
        this.textos = textos;
    }

    @NonNull
    @Override
    public IngredientesAdapter.IngredienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(R.layout.ingrediente_item, parent, false);
        return new IngredienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientesAdapter.IngredienteViewHolder holder, int position) {
        holder.ingredienteEditText.setText(textos.get(position));
    }

    @Override
    public int getItemCount() {
        return textos.size();
    }

    public class IngredienteViewHolder extends RecyclerView.ViewHolder {

        public TextInputEditText ingredienteEditText;
        TextInputLayout ingredienteInputText;

        ImageView ingredienteItemDelete;

        public IngredienteViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredienteEditText = itemView.findViewById(R.id.ingrediente_edit_text);
            ingredienteEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textos.set(getAdapterPosition(), s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            ingredienteInputText = itemView.findViewById(R.id.ingrediente_text_field);

            ingredienteItemDelete = itemView.findViewById(R.id.ingrediente_item_delete_icon);

            ingredienteItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialAlertDialogBuilder(layoutInflater.getContext())
                            .setMessage("¿Estas seguro de que quieres borrar este ingrediente?")
                            .setNegativeButton("Cancelar", null)
                            .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = getAdapterPosition();
                                    textos.remove(position);
                                    notifyItemRemoved(position);
                                }
                            }).show();
                }
            });
        }

    }
}
