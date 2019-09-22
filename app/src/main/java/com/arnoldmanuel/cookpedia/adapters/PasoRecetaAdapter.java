package com.arnoldmanuel.cookpedia.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.arnoldmanuel.cookpedia.CrearRecetaActivity;
import com.arnoldmanuel.cookpedia.R;
import com.arnoldmanuel.cookpedia.viewmodels.CrearRecetaViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.arnoldmanuel.cookpedia.CrearRecetaActivity.SELECCIONA_IMAGEN_PASO;

public class PasoRecetaAdapter extends
        RecyclerView.Adapter<PasoRecetaAdapter.PasoRecetaViewHolder> {

    private List<String> textos;
    private List<Bitmap[]> imagenes;
    private LayoutInflater layoutInflater;
    public int lastAdapterPosition = 0;
    public int lastImagePosition = 0;

    public PasoRecetaAdapter(Context context, List<String> textos, List<Bitmap[]> imagenes) {
        this.layoutInflater = LayoutInflater.from(context);
        this.textos = textos;
        this.imagenes = imagenes;
    }

    @NonNull
    @Override
    public PasoRecetaAdapter.PasoRecetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(R.layout.paso_item, parent, false);
        return new PasoRecetaAdapter.PasoRecetaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasoRecetaAdapter.PasoRecetaViewHolder holder, int position) {
        holder.pasoRecetaEditText.setText(textos.get(position));
        holder.pasoTextNumber.setText(String.valueOf(position+1));
        for (int i = 0; i < 3 ; i++) {
            if (imagenes.get(position)[i] != null) {
                ((ImageView)holder.cardsImagePasoFoto[i].getChildAt(0)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                ((ImageView)holder.cardsImagePasoFoto[i].getChildAt(0))
                        .setImageBitmap((imagenes.get(position)[i]));
            }
            else {
                ((ImageView)holder.cardsImagePasoFoto[i].getChildAt(0)).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ((ImageView)holder.cardsImagePasoFoto[i].getChildAt(0)).setImageResource(R.drawable.ic_camera);
            }
        }

    }

    @Override
    public int getItemCount() {
        return textos.size();
    }

    public class PasoRecetaViewHolder extends RecyclerView.ViewHolder {

        public TextView pasoTextNumber;
        TextInputEditText pasoRecetaEditText;
        ImageView pasoRecetaItemDelete;
        CardView[] cardsImagePasoFoto = new CardView[3];

        public PasoRecetaViewHolder(@NonNull View itemView) {
            super(itemView);

            pasoTextNumber = itemView.findViewById(R.id.paso_text_number);
            pasoRecetaEditText = itemView.findViewById(R.id.paso_edit_text);
            pasoRecetaEditText.addTextChangedListener(new TextWatcher() {
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

            pasoRecetaItemDelete = itemView.findViewById(R.id.paso_item_delete_icon);
            cardsImagePasoFoto[0] = itemView.findViewById(R.id.card_image_paso_foto1);
            cardsImagePasoFoto[1] = itemView.findViewById(R.id.card_image_paso_foto2);
            cardsImagePasoFoto[2] = itemView.findViewById(R.id.card_image_paso_foto3);

            pasoRecetaItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialAlertDialogBuilder(layoutInflater.getContext())
                            .setMessage("¿Estas seguro de que quieres borrar este paso?")
                            .setNegativeButton("Cancelar", null)
                            .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = getAdapterPosition();
                                    textos.remove(position);
                                    imagenes.remove(position);
                                    notifyDataSetChanged();
                                }
                            }).show();
                }
            });
            cardsImagePasoFoto[0].setOnClickListener(clickImageChooser);
            cardsImagePasoFoto[1].setOnClickListener(clickImageChooser);
            cardsImagePasoFoto[2].setOnClickListener(clickImageChooser);
        }

        View.OnClickListener clickImageChooser = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                lastAdapterPosition = getAdapterPosition();
                lastImagePosition = Arrays.asList(cardsImagePasoFoto).indexOf((CardView) v);
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                ((Activity) layoutInflater.getContext()).startActivityForResult(
                        Intent.createChooser(i, "Selecciona la imagen"),
                        SELECCIONA_IMAGEN_PASO);
            }
        };

    }


}
