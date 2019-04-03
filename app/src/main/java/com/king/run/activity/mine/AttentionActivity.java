package com.king.run.activity.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.run.R;
import com.king.run.activity.circle.CircleFragment;
import com.king.run.activity.mine.fragment.AttentionCircleFragment;
import com.king.run.activity.mine.fragment.UserFragment;
import com.king.run.activity.posture.PostureFragment;
import com.king.run.activity.sport.SportFragment;
import com.king.run.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_attention)
public class AttentionActivity extends BaseActivity {

    @ViewInject(R.id.title_start)
    TextView title_start;
    @ViewInject(R.id.title_end)
    TextView title_end;
    @ViewInject(R.id.view_start)
    View view_start;
    @ViewInject(R.id.title_iv_back)
    ImageView iv_back;
    @ViewInject(R.id.view_end)
    View view_end;
    private int mIndex;
    private Fragment[] mFragments;
    private FragmentTransaction transaction;
    private UserFragment userFragment;
    private AttentionCircleFragment attentionCircleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initTitleBar();
    }

    private void initViews() {
        view_start.setVisibility(View.VISIBLE);
        title_start.setTextColor(ContextCompat.getColor(context, R.color.black));
        view_end.setVisibility(View.GONE);
        title_end.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray));
        transaction = getSupportFragmentManager().beginTransaction();
        userFragment = new UserFragment();
        attentionCircleFragment = new AttentionCircleFragment();
        mFragments = new Fragment[]{userFragment, attentionCircleFragment};
        transaction.add(R.id.container, userFragment);
        transaction.commit();
        setIndexSelected(0);
    }

    @Event(value = {R.id.lv_start, R.id.lv_end, R.id.title_iv_back})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.lv_start:
                view_start.setVisibility(View.VISIBLE);
                title_start.setTextColor(ContextCompat.getColor(context, R.color.black));
                view_end.setVisibility(View.GONE);
                title_end.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray));
                setIndexSelected(0);
                break;
            case R.id.lv_end:
                view_start.setVisibility(View.GONE);
                title_start.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray));
                view_end.setVisibility(View.VISIBLE);
                title_end.setTextColor(ContextCompat.getColor(context, R.color.black));
                setIndexSelected(1);
                break;
            case R.id.title_iv_back:
                finish();
                break;
        }
    }

    public void setIndexSelected(int index) {
        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.container, mFragments[index]).show(mFragments[index]);
        } else {
            ft.show(mFragments[index]);
        }
        ft.commit();
        //再次赋值
        mIndex = index;
    }
}
