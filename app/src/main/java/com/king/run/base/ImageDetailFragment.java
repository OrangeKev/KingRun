package com.king.run.base;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.king.run.R;
import com.king.run.util.ImageTool;
import com.king.run.util.PicassoUtil;
import com.king.run.util.StringUtil;
import com.king.run.util.Utils;
import com.king.run.view.photoView.PhotoViewAttacher;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.File;

public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;
    private String preUrl = null;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    public static ImageDetailFragment newInstance(String imageUrl,
                                                  String preUrl) {
        ImageDetailFragment f = newInstance(imageUrl);
        f.setPreUrl(preUrl);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url")
                : null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment,
                container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if (!mImageUrl.contains("http")) {
//            // 本地文件
//            try {
//                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
//                        mImageUrl));
//                mImageView.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            return;
//        }
        showThumbnailImage();
        showImage();
    }

    public void setPreUrl(String preUrl) {
        this.preUrl = preUrl;
    }

    private void showThumbnailImage() {
        if (StringUtil.isBlank(preUrl))
            return;
        if (!preUrl.startsWith("http://")
                && !preUrl.startsWith("https://"))
            preUrl = "file://" + mImageUrl;
//        Util.printVerbose("加载小图:" + preUrl);
        PicassoUtil.loadBitmap(preUrl, new PicassoUtil.OnGetBitmap() {
            @Override
            public void onGet(Bitmap source) {
//                Util.printVerbose("加载小图成功:" + preUrl);
                if (!hasImageLoaded) {
                    Bitmap bitmap = ImageTool.convertWhitPng(source);
//                    source.recycle();
                    mImageView.setImageBitmap(bitmap);
                    mAttacher.update();
//                    Util.printVerbose("显示小图:" + preUrl);
                }
            }

//            @Override
//            public void onFail() {
////                Util.printVerbose("显示小图失败:" + mImageUrl);
//            }
        });
    }

    private void showImage() {
        progressBar.setVisibility(View.VISIBLE);
        if (StringUtil.isBlank(mImageUrl))
            return;
//        Util.printVerbose("加载大图:" + mImageUrl);
        RequestCreator creator;
        if (mImageUrl.startsWith("http://")
                || mImageUrl.startsWith("https://"))
            creator = Picasso.with(getActivity()).load(mImageUrl);
        else {
            if (!mImageUrl.startsWith("file://"))
                mImageUrl = "file://" + mImageUrl;
            creator = Picasso.with(getActivity()).load(new File(mImageUrl));
        }
        creator.transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap bitmap = ImageTool.convertWhitPng(source);
                source.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return mImageUrl;
            }
        });
        creator.into(mImageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                mAttacher.update();
                setImageHadLoaded(true);
                progressBar.setVisibility(View.GONE);
//                Util.printVerbose("加载大图成功:" + mImageUrl);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                mAttacher.update();
                progressBar.setVisibility(View.GONE);
//                Util.printVerbose("加载大图失败:" + mImageUrl);
            }
        });
    }

    /**
     * 标记大图是否加载完成，如果未完成，小图获取成功后先显示小图
     */
    private boolean hasImageLoaded = false;

    private synchronized void setImageHadLoaded(boolean b) {
        hasImageLoaded = b;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //不知道为什么：https://github.com/chrisbanes/PhotoView/issues/229
        mAttacher.cleanup();
    }
}
