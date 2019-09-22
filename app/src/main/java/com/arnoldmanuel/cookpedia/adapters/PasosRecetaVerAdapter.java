package com.arnoldmanuel.cookpedia.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.arnoldmanuel.cookpedia.R;

import java.util.List;

public class PasosRecetaVerAdapter extends
        RecyclerView.Adapter<PasosRecetaVerAdapter.PasosRecetaVerViewHolder> {

    private List<String> textos;
    private List<String[]> imagenes;
    private LayoutInflater layoutInflater;

    public PasosRecetaVerAdapter(Context context, List<String> textos, List<String[]> imagenes) {
        this.textos = textos;
        this.imagenes = imagenes;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PasosRecetaVerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(R.layout.paso_ver_item, parent, false);
        return new PasosRecetaVerAdapter.PasosRecetaVerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasosRecetaVerViewHolder holder, int position) {
        holder.pasoRecetaViewText.setText(textos.get(position));
        holder.pasoTextNumber.setText(String.valueOf(position+1));
        for (int i = 0; i < 3 ; i++) {
            if (imagenes.get(position)[i] != null) {
                ImageView imageView = (ImageView)holder.cardsImagePasoFoto[i].getChildAt(0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(BitmapFactory.decodeFile(imagenes.get(position)[i]));
            }
            else {
                ((ImageView)holder.cardsImagePasoFoto[i].getChildAt(0)).setImageResource(R.drawable.ic_camera);
            }
        }
    }

    @Override
    public int getItemCount() {
        return textos.size();
    }

    public class PasosRecetaVerViewHolder extends RecyclerView.ViewHolder {

        public TextView pasoTextNumber;
        TextView pasoRecetaViewText;
        CardView[] cardsImagePasoFoto = new CardView[3];

        public PasosRecetaVerViewHolder(@NonNull View itemView) {
            super(itemView);
            pasoTextNumber = itemView.findViewById(R.id.paso_text_number);
            pasoRecetaViewText = itemView.findViewById(R.id.paso_text_view);
            cardsImagePasoFoto[0] = itemView.findViewById(R.id.card_image_paso_foto1);
            cardsImagePasoFoto[1] = itemView.findViewById(R.id.card_image_paso_foto2);
            cardsImagePasoFoto[2] = itemView.findViewById(R.id.card_image_paso_foto3);
        }
    }
}
