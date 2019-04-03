package com.king.run.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.DisplayMetrics;


import com.king.run.base.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA. User: weiguo.ren Date: 13-11-7 Time: 上午9:20 To
 * change this template use File | Settings | File Templates.
 */
public class ImageTool {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;

    /** */
    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
    //把白色转换成透明
    public static Bitmap convertWhitPng(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());
        Canvas canvas = new Canvas(output);
//        canvas.drawARGB(0, 255, 255, 255);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setXfermode(new PorterDuffXfermode(Mode.DARKEN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
//        bitmap.recycle();
        return output;
    }
    /**
     * 获取视频缩略图
     *
     * @param videoPath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /** */
    /**
     * 去色同时加圆角
     *
     * @param bmpOriginal 原图
     * @param pixels      圆角弧度
     * @return 修改后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
        return toRoundCorner(toGrayscale(bmpOriginal), pixels);
    }

    /** */
    /**
     * 把图片变成圆角
     *
     * @param bitmap   需要修改的图片
     * @param //pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /** */
    /**
     * 使圆角功能支持BitampDrawable
     *
     * @param bitmapDrawable
     * @param pixels
     * @return
     */
    public static Drawable drawableToRoundCorner(Drawable bitmapDrawable,
                                                 int pixels) {
        Bitmap bitmap = ((BitmapDrawable) bitmapDrawable).getBitmap();
        bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
        return bitmapDrawable;
    }

    /**
     * 读取路径中的图片，然后将其转化为缩放后的bitmap
     *
     * @param path
     */
    public static void saveBefore(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int be = (int) (options.outHeight / (float) 200);
        if (be <= 0)
            be = 1;
        options.inSampleSize = 2; // 图片长宽各缩小二分之一
        // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + " " + h);
        // savePNG_After(bitmap,path);
        saveJPGE_After(bitmap, path);
    }

    /**
     * 保存图片为PNG
     *
     * @param bitmap
     * @param name
     */
    public static void savePNG_After(Bitmap bitmap, String name) {
        File file = new File(name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片为JPEG
     *
     * @param bitmap
     * @param path
     */
    public static void saveJPGE_After(Bitmap bitmap, String path) {
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 水印
     *
     * @param //bitmap
     * @return
     */
    public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    /**
     * 图片合成
     *
     * @return
     */
    public static Bitmap potoMix(int direction, Bitmap... bitmaps) {
        if (bitmaps.length <= 0) {
            return null;
        }
        if (bitmaps.length == 1) {
            return bitmaps[0];
        }
        Bitmap newBitmap = bitmaps[0];
        // newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
        for (int i = 1; i < bitmaps.length; i++) {
            newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
        }
        return newBitmap;
    }

    private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second,
                                                 int direction) {
        if (first == null) {
            return null;
        }
        if (second == null) {
            return first;
        }
        int fw = first.getWidth();
        int fh = first.getHeight();
        int sw = second.getWidth();
        int sh = second.getHeight();
        Bitmap newBitmap = null;
        if (direction == LEFT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, sw, 0, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == RIGHT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, fw, 0, null);
        } else if (direction == TOP) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, sh, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == BOTTOM) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, 0, fh, null);
        }
        return newBitmap;
    }

    /**
     * 将Bitmap转换成指定大小
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    /**
     * Drawable 转 Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmapByBD(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }

    /**
     * Bitmap 转 Drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawableByBD(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    /**
     * Bitmap 转 Drawable 并保持尺寸
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Activity activity, Bitmap bitmap) {
//        // 设置bitmap转成drawable后尺寸不变
        Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
        return drawable;
    }

    /**
     * byte[] 转 bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * bitmap 转 byte[]
     *
     * @param bm
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * @param inputStream
     * @return 返回 byte[]
     * @throws Exception
     */
    public static byte[] read(InputStream inputStream) throws Exception {
        ByteArrayOutputStream arrayBuffer = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(b)) != -1) {
            arrayBuffer.write(b, 0, len);
        }
        inputStream.close();
        arrayBuffer.close();
        return arrayBuffer.toByteArray();
    }

    /**
     * Bytes2Drawable
     *
     * @param *b
     * @return
     */
    public static Drawable Bytes2Drawable(InputStream inputStream) {
        try {
            byte[] b = read(inputStream);
            Bitmap bitmap = null;
            if (b.length != 0) {
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                return new BitmapDrawable(bitmap);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap BytesToBitmap(InputStream inputStream) {
        try {
            byte[] b = read(inputStream);
            Bitmap bitmap = null;
            if (b.length != 0) {
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                return bitmap;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap resize(Bitmap bitmap, int size) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(size, size, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, size, size);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        int srcSize = (bitmap.getWidth() < bitmap.getHeight()) ? bitmap
                .getWidth() : bitmap.getHeight();
        final Rect srcRect = new Rect(0, 0, srcSize, srcSize);
        canvas.drawBitmap(bitmap, srcRect, rect, paint);
        return output;
    }

    /**
     * @param bitmap
     * @param size   短边缩放后的尺寸
     * @return
     */
    public static Bitmap resizeByMaxSize(Bitmap bitmap, int size) {
        if (bitmap == null) {
            return null;
        }
        float rate = ((float) bitmap.getWidth() / (float) bitmap.getHeight());
        int width;
        int height;
        if (rate < 1) {
            width = (int) (size * rate);
            height = size;
        } else {
            width = size;
            height = (int) (size / rate);
        }
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, size, size);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        int srcSize = (bitmap.getWidth() < bitmap.getHeight()) ? bitmap
                .getWidth() : bitmap.getHeight();
        final Rect srcRect = new Rect(0, 0, srcSize, srcSize);
        canvas.drawBitmap(bitmap, srcRect, rect, paint);
        return output;
    }

    public static Bitmap getAppStoreIcon(Bitmap bitmap, int size, float roundPx) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(size, size, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, size, size);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        int srcSize = (bitmap.getWidth() < bitmap.getHeight()) ? bitmap
                .getWidth() : bitmap.getHeight();
        final Rect srcRect = new Rect(0, 0, srcSize, srcSize);
        canvas.drawBitmap(bitmap, srcRect, rect, paint);
        return output;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null) {
            return null;
        }
        int size = (bitmap.getWidth() < bitmap.getHeight()) ? bitmap.getWidth()
                : bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(size, size, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();

        roundPx = (float) size / 4;
        final Rect rect = new Rect(0, 0, size, size);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            float radius = bitmap.getWidth() < bitmap.getHeight() ? bitmap
                    .getWidth() / 2 : bitmap.getHeight() / 2;
            float centerX = bitmap.getWidth() / 2;
            float centerY = bitmap.getHeight() / 2;

            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            // final RectF rectF = new RectF(rect);
            // // final float roundPx = bitmap.getWidth() / 2;
            // final float roundPx = 50;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(centerX, centerY, radius, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        } catch (OutOfMemoryError e) {
            System.gc();
            System.runFinalization();
            return getRoundedBitmap(bitmap);
        }

    }

    /**
     * 以最省内存的方式读取本地资源的图片
     * 这种方式需要在hdpi和mdpi，ldpi中配置相应的图片资源
     * 否则在不同分辨率的机器上会显示同样大小的
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static Bitmap getRoundedBitmapWithIcon(Bitmap bitmap, Bitmap icon) {
        if (bitmap == null) {
            return null;
        }
        float radius = bitmap.getWidth() < bitmap.getHeight() ? bitmap
                .getWidth() / 2 : bitmap.getHeight() / 2;
        float centerX = bitmap.getWidth() / 2;
        float centerY = bitmap.getHeight() / 2;

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // final RectF rectF = new RectF(rect);
        // // final float roundPx = bitmap.getWidth() / 2;
        // final float roundPx = 50;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(centerX, centerY, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
        canvas.drawBitmap(icon, rect, rect, paint);
        return output;
    }

    public static Bitmap getRoundedColorWithIcon(int size, int color,
                                                 Bitmap icon) {

        float radius = size / 2;
        float centerX = size / 2;
        float centerY = size / 2;

        Bitmap output = Bitmap.createBitmap(size, size, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(centerX, centerY, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
        canvas.drawBitmap(icon, centerX - icon.getWidth() / 2,
                centerY - icon.getHeight() / 2, paint);
        return output;
    }

    public static Bitmap getFullCycle(int size, int color) {
        if (size <= 0) {
            return null;
        }

        float radius = size / 2;
        float centerX = size / 2;
        float centerY = size / 2;

        Bitmap output = Bitmap.createBitmap(size, size, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawCircle(centerX, centerY, radius, paint);
        return output;
    }

    public static Bitmap getRoundedBlodBitmap(Bitmap bitmap, int blodWidth) {
        if (bitmap == null) {
            return null;
        }
        int size = (bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth()
                : bitmap.getHeight()) + blodWidth * 2;

        float radius = size / 2;

        Bitmap roundBitmap = getRoundedBitmap(bitmap);

        Bitmap output = Bitmap.createBitmap(size, size, Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);
        final Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawCircle(radius, radius, radius, paint);
        canvas.drawBitmap(roundBitmap, blodWidth, blodWidth, paint);

        return output;
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap sbmp;
        int radius = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth() / 2
                : bitmap.getHeight() / 2;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            sbmp = bitmap;

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        // 第三个参数减去的数值为白边的宽度.
        canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2,
                sbmp.getWidth() / 2 - 6, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

    public static final Bitmap grey(Drawable drawable) {
        Bitmap bitmap = drawableToBitmapByBD(drawable);
        return grey(bitmap);
    }

    public static final Bitmap grey(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }

    public static Bitmap compressImage(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap adaptive(Context context, Bitmap bitmap) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int scalX = metrics.widthPixels;
        int scalY = metrics.heightPixels;
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();// 获取资源位图的宽
        int height = bitmap.getHeight();// 获取资源位图的高
        float w = scalX / bitmap.getWidth();
        float h = scalY / bitmap.getHeight();
        matrix.postScale(w, h);// 获取缩放比例
        // 根据缩放比例获取新的位图
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static StateListDrawable newSelector(Context context, Bitmap bitmap, int idNormal,
                                                int idPressed, int idFocused, int idUnable) {
//		RenderScript rs=null;
//		try{
//			rs=RenderScript.create(topActivity_);
//			Allocation input=Allocation.createFromBitmap(rs,bitmap,
//					Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);
//			Allocation output=Allocation.createTyped(rs,input.getType());
//			ScriptIntrinsicBlur blur=ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//			blur.setInput(input);
//			blur.setRadius(mr);
//			blur.forEach(output);
//			output.copyTo(bitmap);
//		}catch (Exception e){
//			FastBulur s;
//		}
        Drawable normal = idNormal == -1 ? null : context.getResources()
                .getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources()
                .getDrawable(idPressed);
        Drawable focused = idFocused == -1 ? null : context.getResources()
                .getDrawable(idFocused);
        Drawable unable = idUnable == -1 ? null : context.getResources()
                .getDrawable(idUnable);
        return newSelector(context, normal, pressed, focused, unable);
    }

    public static StateListDrawable newSelector(Context context,
                                                Drawable normal, Drawable pressed, Drawable focused, Drawable unable) {
        StateListDrawable bg = new StateListDrawable();
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed,
                android.R.attr.state_enabled}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled,
                android.R.attr.state_focused}, focused);
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_focused}, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_window_focused}, unable);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, normal);
        return bg;
    }



    /**
     * 生成一个指定大小和颜色的圆点
     *
     * @param size
     * @param color
     * @return
     */
    public static Bitmap getRoundedColorBitmap(int size, @ColorInt int color) {

        float radius = size / 2;
        float centerX = size / 2;
        float centerY = size / 2;

        Bitmap output = Bitmap.createBitmap(size, size, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(centerX, centerY, radius, paint);
        return output;
    }



    /**
     * .9png 根据指定大小生成图片
     *
     * @param sharpDrRes
     * @param width
     * @param heigth
     * @return
     */
    public static Bitmap ninePngToBitmap(@DrawableRes int sharpDrRes, int width, int heigth) {
        NinePatchDrawable drawable = (NinePatchDrawable) MyApplication.getContext().getResources().getDrawable(sharpDrRes);
        drawable.setBounds(0, 0, width, heigth);

        Bitmap bitmap = Bitmap.createBitmap(width,
                heigth, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 使用sharp截取bitmap
     *
     * @param bitmap
     * @param sharpBitmap
     * @return
     */
    public static Bitmap cutBySharpBitmap(Bitmap bitmap, Bitmap sharpBitmap) {

        Bitmap output = Bitmap.createBitmap(sharpBitmap.getWidth(), sharpBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = 0xff424242;
        Paint paint = new Paint();

        Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawBitmap(sharpBitmap, rect, rect, paint);
        //原图片和形状应是保持一致大小使不变形
        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, srcRect, rect, paint);
        return output;
    }
    /**
     * 使用sharp截取bitmap，bitmap过大时仅截取中间部分
     *
     * @param bitmap
     * @param sharpBitmap
     * @return
     */
    public static Bitmap cutBySharpBitmapCrop(Bitmap bitmap, Bitmap sharpBitmap) {

        Bitmap output = Bitmap.createBitmap(sharpBitmap.getWidth(), sharpBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = 0xff424242;
        Paint paint = new Paint();

        Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawBitmap(sharpBitmap, rect, rect, paint);

        //处理两个图片的大小关系,生成srcRect
        //bitmap比sharp大时显示中间一块
        //bitmap比sharp小时从左上角开始显示
        int left = Math.max(0, (bitmap.getWidth() - sharpBitmap.getWidth()) / 2);
        int top = Math.max(0, (bitmap.getHeight() - sharpBitmap.getHeight()) / 2);
        int right = left + Math.min(sharpBitmap.getWidth(), bitmap.getWidth());
        int bottom = top + Math.min(sharpBitmap.getHeight(), bitmap.getHeight());

        Rect srcRect = new Rect(left, top, right, bottom);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, srcRect, rect, paint);
        return output;
    }
}
