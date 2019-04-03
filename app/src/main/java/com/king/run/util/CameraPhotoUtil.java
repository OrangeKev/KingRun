package com.king.run.util;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * 作者：shuizi_wade on 2017/10/19 16:18
 * 邮箱：674618016@qq.com
 */
public class CameraPhotoUtil {
    public static final int IMAGE = 123;
    public static final int CAMERA = 124;
    public static String imageUrl;
    public static Uri imageUri;

    /**
     * 打开相册的方法
     */
    public static void openAlbum(Context context) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        ((Activity) context).startActivityForResult(intent, IMAGE);
    }

    /**
     * 拍照获取图片
     **/
    public static String take_photo(Context context) {
        //创建File对象，用于存储拍照后的图片
        imageUrl = context.getExternalCacheDir() + File.separator + System.currentTimeMillis() + ".jpg";
        File outputImage = new File(imageUrl);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(context, "com.king.run.takephoto.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        ((Activity) context).startActivityForResult(intent, CAMERA);
        return imageUrl;
    }
    /**
     * 拍照获取图片
     **/
    public static Uri take_photo_uri(Context context) {
        //创建File对象，用于存储拍照后的图片
        imageUrl = context.getExternalCacheDir() + File.separator + System.currentTimeMillis() + ".jpg";
        File outputImage = new File(imageUrl);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(context, "com.king.run.takephoto.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        ((Activity) context).startActivityForResult(intent, CAMERA);
        return imageUri;
    }

    public static String getRealImagePath(Intent data, Context context) {
        String imagePath = null;
        Uri uri = data.getData();
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type) == true) {
                    imagePath = "${Environment.getExternalStorageDirectory()}/${split[1]}";
                }
            } else if (isDownloadsDocument(uri)) {
                //DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                imagePath = getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                //MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                imagePath = getDataColumn(context, contentUri, selection, selectionArgs);
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equalsIgnoreCase(uri.getScheme()) == true) {
            // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                imagePath = uri.getLastPathSegment();
            imagePath = getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme()) == true) {
            // File
            imagePath = uri.getPath();
        } else if (DocumentsContract.isDocumentUri(context, uri) == false) {
            getImagePath(context, uri, data);
        }
        Log.d("", imagePath);
        return imagePath;
    }

    /**
     * 获取图片路径
     */
    public static String getImagePath(Context context, Uri uri, Intent data) {
        Uri selectedImage = data.getData();
        //Log.e(TAG, selectedImage.toString());
        if (selectedImage != null) {
            String uriStr = selectedImage.toString();
            String path = uriStr.substring(10, uriStr.length());
            if (path.startsWith("com.sec.android.gallery3d")) {
                Log.e("Method selectImage", "It's auto backup pic path:" + selectedImage.toString());
                return null;
            }
        }
        final String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        String mImgPath;
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            mImgPath = picturePath;
            cursor.close();
        } else {
            mImgPath = selectedImage.getPath();
        }

        return mImgPath;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
            if (cursor == null) {
                return uri.getPath();
            }
        } finally {
            if (cursor != null)
                cursor.close();
//            return null;
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     *            *
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     *            *
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     *            *
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    //content://com.android.providers.media.documents/document/image%3A48974

    /**
     * @param uri The Uri to check.
     *            *
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
