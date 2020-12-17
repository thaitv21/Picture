package com.nullexcom.picture.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RotatedImageView extends androidx.appcompat.widget.AppCompatImageView {

    private int rotation = 0;
    private int centerX;
    private int centerY;
    private Point[] points = new Point[]{
            new Point(),
            new Point(),
            new Point(),
            new Point()
    };

    public RotatedImageView(@NonNull Context context) {
        super(context);
    }

    public RotatedImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RotatedImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint paint = new Paint();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        centerX = width / 2;
        centerY = height / 2;
        points[0].set(0, 0);
        points[1].set(width, 0);
        points[2].set(width, height);
        points[3].set(0, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        double alpha = calculateOriginalAngle();
        double rotationInRadius = calculateRotationInRadius();
        points[0] = calculatePosition(Math.PI - alpha - rotationInRadius);
        points[1] = calculatePosition(alpha - rotationInRadius);
        points[2] = calculatePosition(2 * Math.PI - alpha - rotationInRadius);
        points[3] = calculatePosition(Math.PI + alpha - rotationInRadius);

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(20);
//        for (Point point: points) {
//            canvas.drawPoint(point.x, point.y, paint);
//        }
//        canvas.drawLine(points[0].x, points[0].y, width, points[0].y, paint);
//        canvas.drawLine(0, points[1].y, points[1].x, points[1].y, paint);
//        canvas.drawLine(0, points[2].y, points[2].x, points[2].y, paint);
//        canvas.drawLine(points[3].x, points[3].y, width, points[3].y, paint);
        if (points[0].y > points[1].y) {
            canvas.drawLine(0, points[1].y, points[1].x, points[1].y, paint);
            canvas.drawLine(points[0].x, 0, points[0].x, height, paint);
        } else {
            canvas.drawLine(points[0].x, points[0].y, width, points[0].y, paint);
            canvas.drawLine(points[1].x, points[1].y, points[1].x, height, paint);
        }
        if (points[2].y > points[3].y) {
            canvas.drawLine(0, points[2].y, points[2].x, points[2].y, paint);
            canvas.drawLine(points[3].x, 0, points[3].x, points[3].y, paint);
        } else {
            canvas.drawLine(points[3].x, points[3].y, width, points[3].y, paint);
            canvas.drawLine(points[2].x, 0, points[2].x, points[2].y, paint);
        }
        invalidate();
    }

    private double calculateOriginalAngle() {
        int width = getWidth();
        int height = getHeight();
        return Math.atan(height * 1f / width);
    }

    private Point calculatePosition(double angle) {
        int w = getWidth() / 2;
        int h = getHeight() / 2;
        double r = Math.sqrt(w*w + h*h);
        double x = centerX + r*Math.cos(angle);
        double y = centerY - r*Math.sin(angle);
        return new Point((int) x, (int) y);
    }

    private double calculateRotationInRadius() {
        return rotation * Math.PI / 180;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        invalidate();
    }
}
