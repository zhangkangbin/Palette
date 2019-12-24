package com.z.palettedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.z.palettedemo.R;

import java.util.List;

public class ThemeListAdapter extends RecyclerView.Adapter<ThemeListAdapter.ViewHolder> {
    private List<String> image;

    public ThemeListAdapter(List<String> image) {
        this.image = image;
    }


    private Context context;

    public void setSelectImage(View.OnClickListener selectImage) {
        this.selectImage = selectImage;
    }

    View.OnClickListener selectImage;

    @NonNull
    @Override
    public ThemeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();

        if (viewType == 0) {
            return new ThemeListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_theme_select_image, viewGroup, false));
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.head_theme_list, viewGroup, false);

            view.findViewById(R.id.themeImageReference).setOnClickListener(v -> {
                if (selectImage != null) {
                    selectImage.onClick(v);
                }
            });
            return new ThemeListAdapter.ViewHolder(view);

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 0;
        }

    }

   @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //注意，这里的position返回的是item在recyclerview中的位置，不是item的数据在数据列表中的位置，是把header和footer算进去的
                    if (position==0) {
                        return gridManager.getSpanCount();
                    }else {
                        return  1;
                    }

                }
            });
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ThemeListAdapter.ViewHolder viewHolder, int i) {

        if (i != 0) {
            Glide.with(context).load(image.get(i)).into(viewHolder.mView);
        }


    }

    @Override
    public int getItemCount() {
        return image.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView.findViewById(R.id.image);
        }
    }

}

