package com.nullexcom.picture.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class AutoHeightImageView extends androidx.appcompat.widget.AppCompatImageView {

    public AutoHeightImageView(Context context) {
        super(context);
    }

    public AutoHeightImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoHeightImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        if (drawable == null) return;
        if (!(drawable instanceof BitmapDrawable)) return;
        Rect bounds = drawable.getBounds();
        adjustHeight(bounds.width(), bounds.height());
        invalidate();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (bm == null) return;
        adjustHeight(bm.getWidth(), bm.getHeight());
    }

    private void adjustHeight(int width, int height) {
        int newHeight = height * getWidth() / width;
        setMeasuredDimension(getWidth(), newHeight);
    }
}
