package com.king.run.activity.sport;

import android.content.Context;

import com.king.run.base.BaseFragment;

/**
 * Created by Administrator on 2017/12/6.
 */

public class BaseSportFragment extends BaseFragment {

    public interface SportListener{
        void currentWay(int type);
    }
    protected  SportListener sportListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sportListener = (SportListener) context;
    }
}
