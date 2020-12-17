package com.panasonic.cdd.imagesegmentationlib;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.dailystudio.app.utils.BitmapUtils;
import com.panasonic.cdd.imagesegmentationlib.ml.DeeplabInterface;
import com.panasonic.cdd.imagesegmentationlib.ml.DeeplabModel;
import com.panasonic.cdd.imagesegmentationlib.ml.ImageUtils;

import java.io.IOException;

public class ImageSegmentationModule {

    public static Bitmap[] segment(Context context, Bitmap originalBitmap) {
        DeeplabInterface deeplabInterface = DeeplabModel.getInstance();
        deeplabInterface.initialize(context);
        final boolean vertical = originalBitmap.getHeight() > originalBitmap.getWidth();
        Resources resources = context.getResources();
        final int dw = resources.getDimensionPixelSize(vertical ? R.dimen.image_width_v : R.dimen.image_width_h);
        final int dh = resources.getDimensionPixelSize(vertical ? R.dimen.image_height_v : R.dimen.image_height_h);
        int sampleSize = calculateSampleSize(originalBitmap.getWidth(), originalBitmap.getHeight(), dw, dh);
        int newWidth = originalBitmap.getWidth() / sampleSize;
        int newHeight = originalBitmap.getHeight() / sampleSize;
        Bitmap bitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
        final int w = bitmap.getWidth();
        final int h = bitmap.getHeight();
        float resizeRatio = (float) deeplabInterface.getInputSize() / Math.max(bitmap.getWidth(), bitmap.getHeight());
        int rw = Math.round(w * resizeRatio);
        int rh = Math.round(h * resizeRatio);
        Bitmap resized = ImageUtils.tfResizeBilinear(bitmap, rw, rh);
        Bitmap mask = deeplabInterface.segment(resized);
        Log.d("TAG", "segment: " + (mask != null));
//        Bitmap cropped = null;
//        if (mask != null) {
//            mask = BitmapUtils.createClippedBitmap(mask, (mask.getWidth() - rw) / 2, (mask.getHeight() - rh) / 2, rw, rh);
//            mask = BitmapUtils.scaleBitmap(mask, w, h);
//            cropped = cropBitmapWithMask(bitmap, mask);
//        }
        bitmap.recycle();
        bitmap = null;
        resized.recycle();
        resized = null;
        System.gc();
        return new Bitmap[] {originalBitmap, mask};
    }

    public static Bitmap[] segment(Context context, Uri uri) throws IOException {
        if (!DeeplabModel.getInstance().isInitialized()) {
            DeeplabModel.getInstance().initialize(context);
        }
        Bitmap originalBitmap = decodeFile(context, uri);
        return segment(context, originalBitmap);
    }

    public static Bitmap decodeFile(Context context, Uri uri) throws IOException {
        if (Build.VERSION.SDK_INT >= 28) {
            ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), uri);
            return ImageDecoder.decodeBitmap(source);
        } else {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        }
    }

    private static Bitmap cropBitmapWithMask(Bitmap original, Bitmap mask) {
        if (original == null
                || mask == null) {
            return null;
        }

        final int w = original.getWidth();
        final int h = original.getHeight();
        if (w <= 0 || h <= 0) {
            return null;
        }

        Bitmap cropped = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(cropped);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(original, 0, 0, null);
        canvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);

        return cropped;
    }

    private static int calculateSampleSize(int width, int height, int reqWidth, int reqHeight) {
        if (reqWidth > width && reqHeight > height) {
            return 1;
        }
        int inSampleSize = 1;
        int w, h;
        do {
            inSampleSize += 1;
            w = width / inSampleSize;
            h = height / inSampleSize;
        } while (reqWidth <= w && reqHeight <= h);
        return inSampleSize;
    }
}
