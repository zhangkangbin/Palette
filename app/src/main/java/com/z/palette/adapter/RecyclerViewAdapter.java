package com.z.palette.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.z.palette.BitmapUtils;
import com.z.palette.R;

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

    Context context;

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context=viewGroup.getContext();
        return new RecyclerViewAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_main, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int i) {

        if(null!=paletteColorsBeans.get(i).getColor()){
            viewHolder.mView.setImageBitmap(null);
            viewHolder.mView.setBackgroundColor(paletteColorsBeans.get(i).getColor().getRgb());
            viewHolder.mText.setText(paletteColorsBeans.get(i).getColorText());
        }else {
            viewHolder.mText.setText(" ");
            viewHolder.mView.setImageBitmap(paletteColorsBeans.get(i).getBitmap());
        }
        viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(null!=paletteColorsBeans.get(i).getBitmap()){
                    BitmapUtils.saveImage(paletteColorsBeans.get(i).getBitmap(),context);
                    Toast.makeText(context,"保存成功！",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
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

