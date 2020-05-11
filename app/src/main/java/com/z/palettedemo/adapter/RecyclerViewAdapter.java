package com.z.palettedemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.z.palettedemo.R;

import java.util.List;

/**
 * @author zhangkb
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
  //  private List<Palette.Swatch> swatchList;
    private List<PaletteColorsBean> paletteColorsBeans;

    public RecyclerViewAdapter(List<PaletteColorsBean> paletteColorsBeans) {
        this.paletteColorsBeans = paletteColorsBeans;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecyclerViewAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_main, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int i) {

        viewHolder.mView.setBackgroundColor(paletteColorsBeans.get(i).getColor().getRgb());
        viewHolder.mText.setText(paletteColorsBeans.get(i).getColorText());
    }

    @Override
    public int getItemCount() {
        return paletteColorsBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mView;
        private TextView mText;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView.findViewById(R.id.image);
            mText = itemView.findViewById(R.id.text);
        }
    }

}

