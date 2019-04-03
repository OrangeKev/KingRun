package com.king.run.activity.circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.king.run.R;
import com.king.run.activity.other.HomeActivity;
import com.king.run.activity.statistics.adapter.MyFgAdapter;
import com.king.run.base.BaseFragment;
import com.king.run.view.PublishDialog;

import net.arvin.pictureselector.entities.ImageEntity;
import net.arvin.pictureselector.utils.PSConfigUtil;
import net.arvin.pictureselector.utils.PSConstanceUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：shuizi_wade on 2017/8/18 16:28
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.fragment_circle)
public class CircleFragment extends BaseFragment {
    @ViewInject(R.id.tab_layout)
    private TabLayout tabLayout;
    @ViewInject(R.id.view_pager)
    private ViewPager viewPager;
    private HotFragment hotFragment;
    private LatestFragment latestFragment;
    private AttentionFragment attentionFragment;
    private List<Fragment> frags;
    private String[] tabNames;
    private ArrayList<ImageEntity> selectedImages = new ArrayList<>();
    private static final int AVATAR_CODE = 40;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        hotFragment = new HotFragment();
        latestFragment = new LatestFragment();
        attentionFragment = new AttentionFragment();
        frags = new ArrayList<>();
        frags.add(hotFragment);
        frags.add(latestFragment);
        frags.add(attentionFragment);
        tabNames = new String[]{getString(R.string.hot), getString(R.string.latest), getString(R.string.attention)};
        MyFgAdapter fgAdapter = new MyFgAdapter(
                getChildFragmentManager(), frags, tabNames);
        viewPager.setAdapter(fgAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Event(value = {R.id.ly_publish})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.ly_publish:
                new PublishDialog(getActivity(), new PublishDialog.ClickListener() {
                    @Override
                    public void text() {
                        jumpActvity(PublishActivity.class);
                    }

                    @Override
                    public void picture() {
//                        jumpActvity(SelfCameraActivity.class, "publish");
                        open();
                    }

                    @Override
                    public void video() {
                        ((HomeActivity) getActivity()).checkVoiceStorgeCamera();
                    }
                }).show();
                break;
        }
    }

    public void tekeVideo() {
        jumpActvity(TakeVideoActivity.class, "circle");
    }

    public void initLocation() {
        if (null != hotFragment)
            hotFragment.initByGotLocationPermission();
    }

    private void open() {
        PSConfigUtil.getInstance().setMaxCount(9);
        PSConfigUtil.getInstance().showSelector(getActivity(), AVATAR_CODE, selectedImages);
    }

//    @Override
//    protected void gotCameraStorageAudioPermissionResult(boolean isGrant) {
//        super.gotCameraStorageAudioPermissionResult(isGrant);
//        if (isGrant)
//            jumpActvity(TakeVideoActivity.class, "circle");
//        else
//            senToa(R.string.permission_camera_storage_audio);
//    }
//
//    @Override
//    protected void gotCameraStoragePermissionResult(boolean isGrant) {
//        super.gotCameraStoragePermissionResult(isGrant);
//        if (isGrant)
//            jumpActvity(TakeVideoActivity.class, "circle");
//        else senToa(R.string.permission_camera_storage);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK) return;
        switch (requestCode) {
            case AVATAR_CODE:
                selectedImages = data.getParcelableArrayListExtra(PSConstanceUtil.PASS_SELECTED);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("fileList", selectedImages);
                jumpBundleActvity(PublishActivity.class, bundle);
                break;
        }
    }
}
