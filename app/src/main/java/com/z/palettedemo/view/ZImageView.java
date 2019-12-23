package com.z.palettedemo.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class ZImageView extends AppCompatImageView {
    public ZImageView(Context context) {
        super(context);
    }

    public ZImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ZImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
