package com.king.run.activity.mine;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import com.king.run.R;
import com.king.run.activity.mine.adapter.MinePicAdapter;
import com.king.run.activity.mine.model.UserInfo;
import com.king.run.activity.music.model.MusicDetails;
import com.king.run.activity.music.model.MusicInfo;
import com.king.run.base.BaseFragment;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.PreferencesUtils;
import com.king.run.util.Url;
import com.king.run.view.HorizontalListView;
import com.orhanobut.logger.Logger;

import org.xml.sax.SAXException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * 作者：shuizi_wade on 2017/8/18 16:27
 * 邮箱：674618016@qq.com
 */
@ContentView(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {
    @ViewInject(R.id.title_iv_back)
    ImageView iv_back;
    @ViewInject(R.id.hor_lv)
    HorizontalListView hor_lv;
    @ViewInject(R.id.iv_avatar)
    ImageView iv_avatar;
    @ViewInject(R.id.tv_attention_num)
    TextView tv_attention_num;
    @ViewInject(R.id.tv_fans_num)
    TextView tv_fans_num;
    @ViewInject(R.id.tv_collect_num)
    TextView tv_collect_num;
    @ViewInject(R.id.tv_name_pic)
    TextView tv_pic_size;
    @ViewInject(R.id.tv_user_name)
    TextView tv_user_name;
    private MinePicAdapter adapter;
    private static final int EDIT_INFO_CODE = 222;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        adapter = new MinePicAdapter(getActivity());
        hor_lv.setAdapter(adapter);
        iv_back.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        httpMyInfo();
    }

    private void httpMyInfo() {
        RequestParams params = new RequestParams(Url.MY_INFO_URL);
        params.addBodyParameter("token", PrefName.getToken(getActivity()));
        httpGetNoDia(params, "myInfo");
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "myInfo":
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ReductoCondSSK.ttf");
                UserInfo userInfo = JSON.parseObject(result, UserInfo.class);
                PreferencesUtils.putString(getActivity(), PrefName.NICK_NAME, userInfo.getData().getNickName());
                PreferencesUtils.putString(getActivity(), PrefName.AVATAR, userInfo.getData().getAvator());
                adapter.setAlbums(userInfo.getData().getAlbums());
                adapter.notifyDataSetChanged();
                tv_user_name.setText(userInfo.getData().getNickName());
                tv_attention_num.setTypeface(typeface);
                tv_attention_num.setText(userInfo.getData().getFollow() + "");
                tv_fans_num.setTypeface(typeface);
                tv_fans_num.setText(userInfo.getData().getFollower() + "");
                tv_collect_num.setTypeface(typeface);
                tv_collect_num.setText(userInfo.getData().getCollect() + "");
                tv_pic_size.setText("我的相册(" + userInfo.getData().getAlbums().size() + ")");
                PicassoUtil.displayImage(iv_avatar, userInfo.getData().getAvator(), getActivity());
                break;
        }
    }

    @Event(value = {R.id.ly_avatar, R.id.ly_attention, R.id.ly_fans, R.id.ly_collect
            , R.id.ly_train_remind, R.id.ly_my_dynamic, R.id.ly_third})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.ly_avatar:
                jumpBundleActvityforResult(EditInfoActivity.class, null, EDIT_INFO_CODE);
                break;
            case R.id.ly_attention:
                jumpActvity(AttentionActivity.class);
                break;
            case R.id.ly_fans:
                jumpActvity(FansActivity.class);
                break;
            case R.id.ly_collect:
                jumpActvity(CollectActivity.class);
                break;
            case R.id.ly_train_remind:
                jumpActvity(TrainRemindActivity.class);
                break;
            case R.id.ly_my_dynamic:
                jumpActvity(MyDynamicListActivity.class);
                break;
            case R.id.ly_third:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK) return;
        switch (requestCode) {
            case EDIT_INFO_CODE:
                httpMyInfo();
                break;
        }
    }
}
