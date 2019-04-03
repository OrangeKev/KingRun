package com.king.run.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.king.run.R;

import java.io.File;

/**
 * Email 674618016@qq.com
 * Created by shuiz_wade on 2017/7/5.
 */

public class ImageLoadUtil {

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .error(R.mipmap.ic_launcher)
                .transform(new GlideCircleTransform(context)).into(imageView);
    }

    public static void loadImage(Context context, File file, ImageView imageView) {
        Glide.with(context).load(file)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .transform(new GlideCircleTransform(context)).into(imageView);
    }
}
