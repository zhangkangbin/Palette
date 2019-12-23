package com.z.palettedemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.z.palettedemo.R;

public class ColorSeekBar extends FrameLayout {

    public ColorSeekBar(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ColorSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    SeekBar colorCount;
    TextView colorCountText;
    int mProgress=6;

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.color_seek_bar, null, false);
        colorCount = view.findViewById(R.id.colorCount);
        colorCountText = view.findViewById(R.id.colorCountText);

        this.colorCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress = progress;
                colorCountText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addView(view);
    }


    public int getProgress() {
        return mProgress;
    }
}
