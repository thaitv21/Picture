package com.nullexcom.picture.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MiddleProgressBar extends androidx.appcompat.widget.AppCompatSeekBar {

    public interface OnProgressChangedListener {
        void onProgressChanged(int progress, boolean fromUser);
    }

    private Paint linePaint = new Paint();
    private Paint progressPaint = new Paint();
    private final Rect mCanvasClipBounds = new Rect();
    private OnProgressChangedListener onProgressChangedListener;
    private int mode;

    public MiddleProgressBar(@NonNull Context context) {
        super(context);
        init();
    }

    public MiddleProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MiddleProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(dp(2));
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setColor(Color.GRAY);

        progressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setColor(Color.RED);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(dp(2));

        setMax(100);
        setProgress(50);
        super.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                int value = 2 * (progress - (getMax() / 2));
                if (onProgressChangedListener != null) {
                    onProgressChangedListener.onProgressChanged(value, true);
                }
            }
        });
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }

    public void setValue(int value) {
        int progress = value / 2 + (getMax() / 2);
        setProgress(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.getClipBounds(mCanvasClipBounds);
        int width = mCanvasClipBounds.width() - dp(5);
        int mProgressLineWidth = dp(2);
        int mProgressLineHeight = getHeight();
        int linesCount = 41;
        float mProgressLineMargin = (width - linesCount * mProgressLineWidth) * 1f /(linesCount - 1);
        float half = width / 2f;
        float start = dp(5) / 2f;
        float space = mProgressLineWidth + mProgressLineMargin;
        for (int i = 0; i < linesCount; i++) {
            float x = start + i * space + mProgressLineWidth / 2f;
            if (i < (linesCount / 4)) {
                linePaint.setAlpha((int) (255 * ((i + 1) / (float) (linesCount / 4))));
            } else if (i > (linesCount * 3 / 4)) {
                linePaint.setAlpha((int) (255 * ((linesCount - i) / (float) (linesCount / 4))));
            } else {
                linePaint.setAlpha(255);
            }
            if (i == linesCount / 2) {
                canvas.drawLine(x, 0, x, getHeight(), linePaint);
            } else {
                canvas.drawLine(x, mCanvasClipBounds.centerY() - mProgressLineHeight / 4.0f, x, mCanvasClipBounds.centerY() + mProgressLineHeight / 4.0f, linePaint);
            }
        }

        int progress = 2 * (getProgress() - (getMax() / 2));
        float percent = progress * 1f / getMax();
        float x = start + half + percent * half;
        canvas.drawLine(x, 0, x, getHeight(), progressPaint);
    }

    private int dp(int dimens) {
        return getContext().getResources().getDisplayMetrics().densityDpi * dimens / 160;
    }
}
