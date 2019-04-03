package com.king.run.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.king.run.R;
import com.king.run.base.MyApplication;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * 作者：shuizi_wade on 2016/11/2 18:22
 * 邮箱：674618016@qq.com
 */

public class PicassoUtil {
    private static Picasso mPicasso = null;
    private static Context mContext;

    private final static String CACHE_DIR_NAME = "picasso";
    private final static long CACHE_MAX_SIZE = 200 * 1024 * 1024;

    public static Picasso Picasso() {
        if (mPicasso == null) {
            mContext = MyApplication.getContext();

            OkHttpClient okHttpClient = getUnsafeOkHttpClient();
            File cacheDir = new File(Utils.getCacheFilePath() + File.separator + CACHE_DIR_NAME);
            okHttpClient.setCache(new Cache(cacheDir, CACHE_MAX_SIZE));

            Picasso.setSingletonInstance(new Picasso.Builder(mContext).
                    downloader(new OkHttpDownloader(okHttpClient))
                    .build());
            mPicasso = Picasso.with(mContext);
        }
        return mPicasso;
    }

    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;

                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 显示头像，图片显示为圆形
     * 默认加载旧头像，随后删除缓存，刷新头像
     *
     * @param imageView
     * @param photoUrl
     */
    public static void displayPhotoClearCache(final ImageView imageView, String photoUrl) {
        if (imageView == null)
            return;
        final Drawable noPhoto = mContext.getResources().getDrawable(R.mipmap.ic_launcher_app);
        if (StringUtil.isBlank(photoUrl)) {
            imageView.setImageDrawable(noPhoto);
            return;
        }
        final String url = checkUrl(photoUrl);

        //显示图片的transformer
        final Transformation photoTransformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap bitmap = ImageTool.getRoundedBitmap(source);
                source.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return url;
            }
        };

        Picasso().load(url)
                .placeholder(noPhoto)
                .error(noPhoto)
                .transform(photoTransformation)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                //使用已显示的图片做为加载中的图片
                                refreshImageWithoutCache(imageView, url, imageView.getDrawable(), noPhoto, photoTransformation);
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshImageWithoutCache(imageView, url, null, noPhoto, photoTransformation);
                            }
                        });
                    }
                });
    }

    /**
     * 显示头像，图片显示为圆形
     * 加载过程显示圆形圈
     *
     * @param imageView
     * @param photoUrl
     */
    public static void displayPhoto(ImageView imageView, String photoUrl) {
        displayPhoto(imageView, photoUrl, R.mipmap.ic_launcher_app, R.mipmap.ic_launcher_app);
    }

    /**
     * 显示头像，图片显示为圆形
     * 加载过程显示圆形圈
     *
     * @param imageView
     * @param photoUrl
     * @param failedResId 加载失败显示的头像
     */
    public static void displayPhoto(ImageView imageView, String photoUrl,
                                    @DrawableRes int loadingResId, @DrawableRes int failedResId,
                                    final boolean noColor) {
        if (imageView == null)
            return;
        if (StringUtil.isBlank(photoUrl)) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(failedResId));
            return;
        }
        final String url = checkUrl(photoUrl);
        Picasso().load(url)
                .placeholder(mContext.getResources().getDrawable(loadingResId))
                .error(mContext.getResources().getDrawable(failedResId))
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        Bitmap bitmap;
                        if (noColor)
                            bitmap = ImageTool.getRoundedBitmap(ImageTool.toGrayscale(source));
                        else
                            bitmap = ImageTool.getRoundedBitmap(source);
                        source.recycle();
                        return bitmap;
                    }

                    @Override
                    public String key() {
                        return url;
                    }
                }).into(imageView);

    }

    public static void displayPhoto(ImageView imageView, String photoUrl, @DrawableRes int loadingResId, @DrawableRes int failedResId) {
        displayPhoto(imageView, photoUrl, loadingResId, failedResId, false);
    }


    private static String checkUrl(String photoUrl) {
        if (StringUtil.isBlank(photoUrl))
            return "http://unknow";
        else if (photoUrl.startsWith("http://") || photoUrl.startsWith("https://"))
            return photoUrl;
        else
            return "file://" + photoUrl;
    }

    /**
     * 显示图片
     *
     * @param imageView
     * @param imageUrl
     */
    public static void displayImage(ImageView imageView, String imageUrl, Context context) {
        mContext = context;
        displayImage(imageView, imageUrl,
                context.getResources().getDrawable(R.mipmap.common_loading_bg),
                context.getResources().getDrawable(R.mipmap.common_loading_bg));
    }

    public static void displayImageSquare(ImageView imageView, String imageUrl, Context context) {
        mContext = context;
        displayImageSquare(imageView, imageUrl,
                context.getResources().getDrawable(R.mipmap.common_loading_bg),
                context.getResources().getDrawable(R.mipmap.common_loading_bg));
    }

    public static void displayImageDim(ImageView imageView, String imageUrl, Context context) {
        mContext = context;
        displayImageDim(imageView, imageUrl,
                context.getResources().getDrawable(R.mipmap.common_loading_bg),
                context.getResources().getDrawable(R.mipmap.common_loading_bg));
    }

    public static void displayImageDim(ImageView imageView, int resId, Context context) {
        mContext = context;
        displayImageDim(imageView, resId,
                context.getResources().getDrawable(R.mipmap.common_loading_bg),
                context.getResources().getDrawable(R.mipmap.common_loading_bg));
    }

    /**
     * 显示图片
     *
     * @param imageView
     * @param photoUrl
     * @param failed
     */
    public static void displayImage(ImageView imageView, String photoUrl,
                                    @DrawableRes int loading, @DrawableRes int failed, Context context) {
        displayImage(imageView, photoUrl,
                context.getResources().getDrawable(loading),
                context.getResources().getDrawable(failed));
    }

    public static void displayImage(ImageView imageView, String photoUrl, Drawable loading, Drawable failed) {
        if (imageView == null)
            return;
        if (StringUtil.isBlank(photoUrl)) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.common_loading_bg));
            return;
        }
        final String url = checkUrl(photoUrl);
        //如果imageview已经加载的图片的url与当前相同，则使用已显示的图片做为加载中显示的图片
        if (url.equals(imageView.getTag()))
            if (imageView.getDrawable() != null)
                loading = imageView.getDrawable();
        imageView.setTag(url);

        Picasso().load(url)
                .error(failed)
                .placeholder(loading)
                .into(imageView);
    }

    public static void displayImageSquare(ImageView imageView, String photoUrl, Drawable loading, Drawable failed) {
        if (imageView == null)
            return;
        if (StringUtil.isBlank(photoUrl)) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.common_loading_bg));
            return;
        }
        final String url = checkUrl(photoUrl);
        //如果imageview已经加载的图片的url与当前相同，则使用已显示的图片做为加载中显示的图片
        if (url.equals(imageView.getTag()))
            if (imageView.getDrawable() != null)
                loading = imageView.getDrawable();
        imageView.setTag(url);

        Picasso().load(url)
                .error(failed)
                .resize(100, 100)
                .placeholder(loading)
                .into(imageView);
    }

    public static void displayImageDim(ImageView imageView, String photoUrl, Drawable loading, Drawable failed) {
        if (imageView == null)
            return;
        if (StringUtil.isBlank(photoUrl)) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.common_loading_bg));
            return;
        } else if (photoUrl.equals("1")) {
            Picasso().load(R.mipmap.train_share_pic_4)
                    .error(failed).resize(100, 100)
                    .placeholder(loading)
                    .transform(new BlurTransformation(mContext))
                    .into(imageView);
//            imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.train_share_pic_4));
            return;
        }
        final String url = checkUrl(photoUrl);
        //如果imageview已经加载的图片的url与当前相同，则使用已显示的图片做为加载中显示的图片
        if (url.equals(imageView.getTag()))
            if (imageView.getDrawable() != null)
                loading = imageView.getDrawable();
        imageView.setTag(url);

        Picasso().load(url)
                .error(failed)
                .resize(100, 100)
                .placeholder(loading)
                .transform(new BlurTransformation(mContext))
                .into(imageView);
    }

    public static void displayImageDim(ImageView imageView, int resId, Drawable loading, Drawable failed) {
        if (imageView == null)
            return;
        Picasso().load(resId)
                .error(failed).resize(100, 100)
                .placeholder(loading)
                .transform(new BlurTransformation(mContext))
                .into(imageView);
//            imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.train_share_pic_4));
//            return;

//        final String url = checkUrl(photoUrl);
//        //如果imageview已经加载的图片的url与当前相同，则使用已显示的图片做为加载中显示的图片
//        if (url.equals(imageView.getTag()))
//            if (imageView.getDrawable() != null)
//                loading = imageView.getDrawable();
//        imageView.setTag(url);
//
//        Picasso().load(url)
//                .error(failed)
//                .resize(100, 100)
//                .placeholder(loading)
//                .transform(new BlurTransformation(mContext))
//                .into(imageView);
    }

    public static void displayDrawable(ImageView imageView, @DrawableRes int resId) {
        if (imageView == null)
            return;
        Picasso().load(resId).into(imageView);
    }

    /**
     * 显示图片，并强制刷新缓存
     *
     * @param imageView
     * @param imageUrl
     */
    public static void displayImageClearCache(final ImageView imageView, final String imageUrl,
                                              Drawable loading, final Drawable failed) {

        if (imageView == null)
            return;
        if (StringUtil.isBlank(imageUrl)) {
            imageView.setImageDrawable(failed);
            return;
        }
        final String url = checkUrl(imageUrl);
        Picasso().load(url)
                .placeholder(loading)
                .error(failed)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                //使用已显示的图片做为加载中的图片
                                refreshImageWithoutCache(imageView, url, imageView.getDrawable(), failed, null);
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshImageWithoutCache(imageView, url, null, failed, null);
                            }
                        });
                    }
                });
    }

    private static void refreshImageWithoutCache(ImageView imageView, String photoUrl,
                                                 Drawable loading, Drawable failed,
                                                 Transformation transformation) {
        if (imageView == null)
            return;
        if (StringUtil.isBlank(photoUrl)) {
            imageView.setImageDrawable(failed);
            return;
        }
        final String url = checkUrl(photoUrl);

        Picasso().invalidate(url);
        RequestCreator creator = Picasso().load(url)
//                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(failed);
        creator.placeholder(loading);
        if (transformation != null)
            creator.transform(transformation);
        creator.into(imageView);
    }


    public interface OnGetBitmap {
        void onGet(Bitmap bitmap);
    }

    /**
     * 显示图片
     *
     * @param photoUrl
     * @param onGetBitmap
     * @return
     */
    public static void loadBitmap(final String photoUrl, final OnGetBitmap onGetBitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = Picasso().load(photoUrl).get();
                    if (onGetBitmap != null)
                        MyApplication.doInMain(new Runnable() {
                            @Override
                            public void run() {
                                onGetBitmap.onGet(bitmap);
                            }
                        });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 模糊
     *
     * @author jia
     */
    public static class BlurTransformation implements Transformation {

        RenderScript rs;

        public BlurTransformation(Context context) {
            super();
            rs = RenderScript.create(context);
        }

        @SuppressLint("NewApi")
        @Override
        public Bitmap transform(Bitmap bitmap) {
            // 创建一个Bitmap作为最后处理的效果Bitmap
            Bitmap blurredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            // 分配内存
            Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
            Allocation output = Allocation.createTyped(rs, input.getType());

            // 根据我们想使用的配置加载一个实例
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);

            // 设置模糊半径
            script.setRadius(10);

            //开始操作
            script.forEach(output);

            // 将结果copy到blurredBitmap中
            output.copyTo(blurredBitmap);

            //释放资源
            bitmap.recycle();

            return blurredBitmap;
        }

        @Override
        public String key() {
            return "blur";
        }
    }
}
