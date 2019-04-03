package com.king.run.activity.mine;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.mine.adapter.EditInfoPicAdapter;
import com.king.run.activity.mine.model.Albums;
import com.king.run.activity.mine.model.EditInfo;
import com.king.run.activity.other.CompleteHeightActivity;
import com.king.run.activity.other.CompleteSexActivity;
import com.king.run.activity.other.CompleteWeightActivity;
import com.king.run.activity.other.LoginActivity;
import com.king.run.activity.sport.walk.SelfCameraActivity;
import com.king.run.base.BaseActivity;
import com.king.run.model.BaseResult;
import com.king.run.util.ActivityManager;
import com.king.run.util.CameraPhotoUtil;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.PreferencesUtils;
import com.king.run.util.StringUtil;
import com.king.run.util.TimeDialog;
import com.king.run.util.Url;
import com.king.run.util.image.FileUtil;
import com.king.run.util.image.compressor.Compressor;
import com.king.run.view.BottomDialog;
import com.king.run.view.CircleImageView;
import com.king.run.view.DragGridView;
import com.lljjcoder.city_20170724.CityPickerView;
import com.lljjcoder.city_20170724.bean.CityBean;
import com.lljjcoder.city_20170724.bean.DistrictBean;
import com.lljjcoder.city_20170724.bean.ProvinceBean;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 作者：shuizi_wade on 2017/8/22 15:51
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.activity_edit_info)
public class EditInfoActivity extends BaseActivity {
    private static final int REQUEST_CODE = 0x11;
    private EditInfoPicAdapter adapter;
    @ViewInject(R.id.drag_view)
    DragGridView drag_view;
    private List<Albums> items;
    private List<Albums> newItems = new ArrayList<>();
    @ViewInject(R.id.iv_user_avatar)
    CircleImageView iv_user_avatar;
    @ViewInject(R.id.tv_user_name)
    TextView tv_user_name;
    @ViewInject(R.id.tv_sex)
    TextView tv_sex;
    @ViewInject(R.id.tv_height)
    TextView tv_height;
    @ViewInject(R.id.tv_weight)
    TextView tv_weight;
    @ViewInject(R.id.tv_birth)
    TextView tv_birth;
    @ViewInject(R.id.tv_location)
    TextView tv_location;
    //    private String imageUrl;
    private File compressedImage;
    private java.io.File actualImage;
    private File imgFile;
    private Uri photoUri;
    private String savaPath;
    private String fileDirName = "KingRun";
    private boolean isAvatar = false;
    private static final int NICKNAME_CODE = 100;
    private static final int SEX_CODE = 101;
    private static final int HEIGHT_CODE = 102;
    private static final int WEIGHT_CODE = 103;
    private static final int LOCATION_CODE = 104;
    private String nickName, avatarPath, height, weight, birth, location;
    private int sex;
    private StringBuilder deleteIds = new StringBuilder();
    private boolean isDrag = false;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
        initData();
        httpEditInfo();
    }

    private void httpEditInfo() {
        RequestParams params = new RequestParams(Url.Edit_INFO_URL);
        params.addBodyParameter("token", PrefName.getToken(context));
        httpGet(params, "editInfo");
    }

    private void initViews() {
        title_tv_title.setText(R.string.edit_info);
        title_tv_right.setText(R.string.save);
        title_tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams(Url.EDIT_AVATAR_NICKNAME);
                List<KeyValue> list = new ArrayList<>();
                for (int i = 0; i < newItems.size(); i++) {
                    list.add(new KeyValue("album", new File(newItems.get(i).getUrlPre())));
                }
                if (StringUtil.isNotBlank(avatarPath))
                    list.add(new KeyValue("file", new File(avatarPath)));
                MultipartBody body = new MultipartBody(list, "UTF-8");
                params.setRequestBody(body);
                // 使用multipart表单上传文件
                params.setMultipart(true);
                params.addBodyParameter("token", PrefName.getToken(context));
                params.addBodyParameter("nickName", nickName);
                params.addBodyParameter("sex", sex + "");
                params.addBodyParameter("height", height);
                params.addBodyParameter("weight", weight);
                params.addBodyParameter("year", birth);
                params.addBodyParameter("age", age + "");
                params.addBodyParameter("location", location);
                if (StringUtil.isNotBlank(deleteIds.toString())) {
                    params.addBodyParameter("delAlbumIds", deleteIds.toString());
                }
                if (isDrag) {
                    StringBuilder ord = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getId() != 0) {
                            ord.append(items.get(i).getId() + ":" + i + ",");
                        }
                    }
                    params.addBodyParameter("ord", ord.toString());
                }
                httpPost("edit", params);
            }
        });
    }

    private void initData() {
        File mFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), fileDirName);
        savaPath = mFile.getAbsolutePath();

        items = new ArrayList<>();
        adapter = new EditInfoPicAdapter(context);
        drag_view.setAdapter(adapter);
        drag_view.setOnItemChanageListener(new DragGridView.OnItemChanageListener() {
            @Override
            public void onChange(int from, int to) {
                if (from < to) {
                    for (int i = from; i < to; i++) {
                        Collections.swap(items, i, i + 1);
                    }
                } else if (from > to) {
                    for (int i = from; i > to; i--) {
                        Collections.swap(items, i, i - 1);
                    }
                }
                isDrag = true;
                adapter.notifyDataSetChanged();
            }
        });

        drag_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                if (items.size() < 8 && i == items.size()) {
                    isAvatar = false;
                    if (checkCameraStoragePermission()) {
                        openCameraStorage();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.notify).setMessage(R.string.sure_to_delete).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if (items.get(i).getUrlPre().contains("http:")) {
                                deleteIds.append(items.get(i).getId() + ",");
                            }
                            items.remove(i);
                            adapter.setAlbums(items);
                            adapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton(R.string.cancel, null).create().show();
                }
            }
        });
    }

    private void openCameraStorage() {
        new BottomDialog(context, new BottomDialog.ClickListener() {
            @Override
            public void openCamera() {
//                imageUrl = CameraPhotoUtil.take_photo(context);
                photoUri = CameraPhotoUtil.take_photo_uri(context);
//                jumpActvity(SelfCameraActivity.class, "avatar");
            }

            @Override
            public void openPicture() {
                CameraPhotoUtil.openAlbum(context);
            }
        }).show();
    }

    @Override
    protected void gotCameraStoragePermissionResult(boolean isGrant) {
        super.gotCameraStoragePermissionResult(isGrant);
        if (isGrant) {
            openCameraStorage();
        } else {
            senToa(R.string.permission_camera_storage);
        }
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "editInfo":
                EditInfo editInfo = JSON.parseObject(result, EditInfo.class);
                birth = editInfo.getData().getYear();
                nickName = editInfo.getData().getNickName();
                height = editInfo.getData().getHeight() + "";
                weight = editInfo.getData().getWeight() + "";
                sex = editInfo.getData().getSex();
                location = editInfo.getData().getLocation();
                PreferencesUtils.putString(context, PrefName.NICK_NAME, nickName);
                PreferencesUtils.putString(context, PrefName.HEIGHT, height);
                PreferencesUtils.putString(context, PrefName.WEIGHT, weight);
                PreferencesUtils.putInt(context, PrefName.SEX, sex);
                tv_birth.setText(birth + "年");
                tv_user_name.setText(nickName);
                tv_height.setText(height + "cm");
                tv_sex.setText(sex == 0 ? "男" : "女");
                tv_weight.setText(weight + "kg");
                tv_location.setText(location);
                PicassoUtil.displayImage(iv_user_avatar, editInfo.getData().getAvator(), context);
                items = editInfo.getData().getAlbums();
                adapter.setAlbums(items);
                adapter.notifyDataSetChanged();
                break;
            case "edit":
                BaseResult baseResult = JSON.parseObject(result, BaseResult.class);
                senToa(baseResult.getMsg());
                setResultAct(null);
                finish();
                break;
        }
    }

    @Event({R.id.ly_modify, R.id.ly_nick_name, R.id.ly_sex, R.id.ly_height, R.id.ly_weight
            , R.id.ly_birth, R.id.ly_my_avatar, R.id.ly_location, R.id.btn_exit})
    private void getEvent(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ly_modify:
                jumpActvity(SafetyConfirmActivity.class);
                break;
            case R.id.ly_nick_name:
                bundle.putString("nickName", nickName);
                jumpBundleActvityforResult(ModifyUserNameActivity.class, bundle, NICKNAME_CODE);
                break;
            case R.id.ly_sex:
                bundle.putInt("sex", sex);
                jumpBundleActvityforResult(CompleteSexActivity.class, bundle, SEX_CODE);
                break;
            case R.id.ly_height:
                bundle.putString("height", height);
                jumpBundleActvityforResult(CompleteHeightActivity.class, bundle, HEIGHT_CODE);
                break;
            case R.id.ly_weight:
                bundle.putString("weight", weight);
                jumpBundleActvityforResult(CompleteWeightActivity.class, bundle, WEIGHT_CODE);
                break;
            case R.id.ly_birth:
                new TimeDialog(context, 1, Integer.parseInt(birth), new TimeDialog.myTimeListener() {
                    @Override
                    public void timeListener(String time) {
                        birth = time;
                        tv_birth.setText(birth + "年");
                        Calendar calendar = Calendar.getInstance();
                        int yearBirth = calendar.get(Calendar.YEAR);
                        age = yearBirth - Integer.parseInt(birth);
                    }
                }).show();
                break;
            case R.id.ly_my_avatar:
                isAvatar = true;
                if (checkCameraStoragePermission()) {
                    openCameraStorage();
                }
                break;
            case R.id.ly_location:
                CityPickerView cityPicker = new CityPickerView.Builder(context)
                        .textSize(20)
                        .title("选择地区")
                        .backgroundPop(0xa0000000)
                        .titleBackgroundColor("#ffffff")
                        .titleTextColor("#ffffff")
                        .confirTextColor("#000000")
                        .cancelTextColor("#61000000")
                        .province("江苏")
                        .city("常州")
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(true)
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .onlyShowProvinceAndCity(true)
                        .build();
                cityPicker.show();
                //监听方法，获取选择结果
                cityPicker.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        //返回结果
                        //ProvinceBean 省份信息
                        //CityBean     城市信息
                        //DistrictBean 区县信息
//                        senToa(province + ";" + city);
                        location = province + "" + city;
                        tv_location.setText(province + "" + city);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
            case R.id.btn_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PreferencesUtils.getPref(context).edit().remove(PrefName.TOKEN).commit();
                        ActivityManager.getInstance().finishAllActivity();
                        jumpActvity(LoginActivity.class);
                    }
                }).setNegativeButton(R.string.cancel, null).setMessage("确认退出？");
                builder.create().show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case CameraPhotoUtil.IMAGE:
                if (data == null) {
                    senToa("请选择图片!");
                    return;
                }
                try {
                    actualImage = FileUtil.from(this, data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (actualImage == null) {
                    senToa("请选择图片");
                } else {
                    compress();
                }
                break;
            case CameraPhotoUtil.CAMERA:
                if (data == null) {
                    try {
                        actualImage = FileUtil.from(this, photoUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (actualImage == null) {
                        senToa("请拍照");
                    } else {
                        compress();
                    }
                }
                break;
            case NICKNAME_CODE:
                nickName = data.getExtras().getString("nickName", "");
                tv_user_name.setText(nickName);
                break;
            case SEX_CODE:
                sex = data.getExtras().getInt("sex");
                tv_sex.setText(sex == 0 ? "男" : "女");
                break;
            case HEIGHT_CODE:
                height = data.getExtras().getString("height");
                tv_height.setText(height + "cm");
                break;
            case WEIGHT_CODE:
                weight = data.getExtras().getString("weight");
                tv_weight.setText(weight + "kg");
                break;
        }
    }


    private void addImg(String path) {
        Albums albums = new Albums();
        albums.setUrlPre(path);
        newItems.add(albums);
        items.add(albums);
        adapter.setAlbums(items);
        adapter.notifyDataSetChanged();
    }


    private void compress() {
        Luban.get(this)
                .load(actualImage)                     //传人要压缩的图片
                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调

                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        showDia("加载中...");
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        if (isAvatar) {
                            avatarPath = file.getAbsolutePath();
                            PicassoUtil.displayImage(iv_user_avatar, avatarPath, context);
                        } else
                            addImg(file.getAbsolutePath());
                        Log.e("xwk", "compressedImage path=" + file.getAbsolutePath());
                        Log.e("xwk", "压缩结果" + String.format("Size : %s",
                                FileUtil.getReadableFileSize(file.length())));
                        hideDia();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过去出现问题时调用
                        senToa(e.getMessage());
                    }
                }).launch();    //启动压缩
    }
}

