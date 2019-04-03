package com.king.run.view;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 作者：水子wade on 2018/1/24 23:04
 * 邮箱：674618016@qq.com
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer{
    private static final float MAX_SCALE = 1.2f;
    private static final float MIN_SCALE = 1.0f;//0.85f

    @Override
    public void transformPage(View page, float position) {

        if (position<=1){

            float scaleFactor =  MIN_SCALE+(1-Math.abs(position))*(MAX_SCALE-MIN_SCALE);

            page.setScaleX(scaleFactor);

            if(position>0){
                page.setTranslationX(-scaleFactor*2);
            }else if(position<0){
                page.setTranslationX(scaleFactor*2);
            }
            page.setScaleY(scaleFactor);
        }else {

            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        }
    }

}