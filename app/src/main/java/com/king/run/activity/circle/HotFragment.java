package com.king.run.activity.circle;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.king.run.R;
import com.king.run.activity.circle.adapter.CircleInfoAdapter;
import com.king.run.activity.circle.adapter.HorLvAdapter;
import com.king.run.activity.circle.model.CircleInfo;
import com.king.run.activity.circle.model.HotCircleResult;
import com.king.run.activity.circle.model.Moment;
import com.king.run.activity.circle.model.PageResult;
import com.king.run.activity.other.HomeActivity;
import com.king.run.baidumap.LocManage;
import com.king.run.base.BaseFragment;
import com.king.run.util.PicassoUtil;
import com.king.run.util.PrefName;
import com.king.run.util.PreferencesUtils;
import com.king.run.util.Url;
import com.king.run.util.Utils;
import com.king.run.view.HorizontalListView;
import com.lwkandroid.rcvadapter.holder.RcvHolder;
import com.lwkandroid.rcvadapter.listener.RcvItemViewClickListener;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_hot)
public class HotFragment extends BaseFragment {
    private HorLvAdapter horLvAdapter;
    HorizontalListView hor_lv;
    ImageView iv_top;
    @ViewInject(R.id.swipRefresh_circle)
    SwipeRefreshLayout swipRefresh_circle;
    @ViewInject(R.id.recyclerView_circle)
    RecyclerView recyclerView_circle;
    //    @ViewInject(R.id.ly_comment)
//    LinearLayout ly_comment;
//    @ViewInject(R.id.et_comment)
//    EditText et_comment;
//    @ViewInject(R.id.btn_comment)
//    Button btn_comment;
    private List<Moment> moments = new ArrayList<>();
    private View headerView;
    private CircleInfoAdapter mAdapter;
    private List<CircleInfo> list = new ArrayList<>();
    private LocManage mLocManage;
    private double lat, lng;
    private static final double LOCATION_CHANGE_DISTINCTION_LAT = 0.00009797;//10米
    private static final double LOCATION_CHANGE_DISTINCTION_LNG = 0.00009000;//10米
    private int isFirst = 2;
    private int deletePos;
    //    private boolean firstLoc = true;
    private static final int INFO_DETAILS = 111;
    private static final int USER_INFO_DETAILS = 121;
    private static final int HOT_CIRCLE_LIST = 123;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSign();
        initHeader();
        initViews();
    }

    private void initSign() {
        mLocManage = new LocManage(getActivity());
        ((HomeActivity) getActivity()).initLocation();
    }

    /**
     * 获取定位权限后的初始化
     */
    public void initByGotLocationPermission() {
        showDia("加载中...");
        getLocation();
    }

    /**
     * 自动获取定位信息
     */
    private void getLocation() {
        mLocManage.getLocation(gotLocCallBack);
    }

    private void initHeader() {
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_circle_hot_header, null);
        iv_top = headerView.findViewById(R.id.iv_top);
        hor_lv = headerView.findViewById(R.id.hor_lv);
        headerView.findViewById(R.id.ly_circle_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpBundleActvityforResult(HotCircleActivity.class, null, HOT_CIRCLE_LIST, "hot");
            }
        });
        hor_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", list.get(i).getId());
                jumpBundleActvity(CircleDetailsActivity.class, bundle);
            }
        });
    }

    private void initViews() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_top.getLayoutParams();
        params.width = Utils.getDisplayWidth(getActivity());
        params.height = (int) (Utils.getDisplayWidth(getActivity()) * 0.43);
        iv_top.setLayoutParams(params);
        iv_top.setBackgroundResource(R.mipmap.hot);
        horLvAdapter = new HorLvAdapter(getActivity());
        hor_lv.setAdapter(horLvAdapter);
        initCircleAdapter();
    }

    @Event(value = {})
    private void getEvent(View view) {
        switch (view.getId()) {
        }
    }

    private void initCircleAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new CircleInfoAdapter(getActivity(), moments);
        recyclerView_circle.setLayoutManager(layoutManager);
        mAdapter.addHeaderView(headerView);
        mAdapter.enableLoadMore(false);
        mAdapter.enableItemShowingAnim(true);
        recyclerView_circle.setAdapter(mAdapter);
        swipRefresh_circle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListInfo();
                isFirst = 2;
            }
        });

        mAdapter.setOnItemClickListener(new RcvItemViewClickListener<Moment>() {
            @Override
            public void onItemViewClicked(RcvHolder holder, Moment circleInfo, int position) {
//                senToa(position + ";" + moments.size());
                Bundle bundle = new Bundle();
                bundle.putSerializable("moment", moments.get(position - 1));
                jumpBundleActvityforResult(InfoDetailsActivity.class, bundle, INFO_DETAILS);
            }
        });
        mAdapter.setDel(new CircleInfoAdapter.DeleteInterface() {
            @Override
            public void delete(final Moment circleInfo, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.hint).setMessage(R.string.sure_to_delete).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePos = position;
                        RequestParams params = new RequestParams(Url.DELETE_MOMENT_URL + circleInfo.getId());
                        httpPost("delete", params);
                    }
                }).setNegativeButton(R.string.cancel, null).create().show();

            }
        });
        mAdapter.setOnItemAvatarClickListener(new CircleInfoAdapter.ItemAvatarClickInterface() {
            @Override
            public void onAvatarClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", moments.get(position - 1).getUserId());
                jumpBundleActvityforResult(UserInfoDetailsActivity.class, bundle, USER_INFO_DETAILS);
            }
        });
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "list":
                moments.clear();
                HotCircleResult hotCircleResult = JSON.parseObject(result, HotCircleResult.class);
                if (hotCircleResult.getData().getBannerPic().size() > 0) {
                    PicassoUtil.displayImage(iv_top, Url.BASE_URL + hotCircleResult.getData().getBannerPic().get(0), getActivity());
                }
                moments = hotCircleResult.getData().getMoment();
                mAdapter.addDatas(moments);
                mAdapter.notifyDataSetChanged();
                mAdapter.enableLoadMore(true, new RcvLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequest() {
                        loadMoreDatas();
                    }
                });
                list = hotCircleResult.getData().getHotCircle();
                if (list.size() > 5) {
                    list = list.subList(0, 5);
                }
                horLvAdapter.setList(list);
                horLvAdapter.notifyDataSetChanged();
                swipRefresh_circle.setRefreshing(false);
                iv_top.setFocusableInTouchMode(true);
                iv_top.requestFocus();
                break;
            case "page":
                PageResult pageResult = JSON.parseObject(result, PageResult.class);
                if (null != pageResult.getData() && !pageResult.getData().isEmpty()) {
                    moments.addAll(pageResult.getData());
//                    if (pageResult.getData().size() < 10) {
//                        mAdapter.notifyLoadMoreSuccess(pageResult.getData(), false);
//                        isFirst = 2;
//                    } else
                    mAdapter.notifyLoadMoreSuccess(pageResult.getData(), true);
                } else {
                    mAdapter.notifyLoadMoreSuccess(new ArrayList<Moment>(), false);
                    isFirst = 2;
                }
                break;
            case "delete":
                moments.remove(deletePos - 1);
                mAdapter.addDatas(moments);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void error(String result, String type) {
        super.error(result, type);
        switch (type) {
            case "page":
                mAdapter.notifyLoadMoreSuccess(new ArrayList<Moment>(), false);
                break;
        }
    }

    /**
     * 获取百度经纬度回调
     */
    private LocManage.GotLocCallBackShort gotLocCallBack = new LocManage.GotLocCallBackShort() {

        @Override
        public void onGotLocShort(double latitude, double longitude, BDLocation location) {
            if (latitude == 0.0d || longitude == 0.0d) {
                return;
            } else {
                if (isLocationChange(latitude, longitude)) {
                    PreferencesUtils.putString(getActivity(), PrefName.PREF_LAST_LAT, latitude + "");
                    PreferencesUtils.putString(getActivity(), PrefName.PREF_LAST_LNG, longitude + "");
                    lat = latitude;
                    lng = longitude;
                    getListInfo();
                    mLocManage.stopLocation();
                    isFirst = 2;
                }
            }
        }

        @Override
        public void onGotLocShortFailed(String location) {
            //目前不会执行到这里
            hideDia();
        }
    };

    private void getListInfo() {
        mAdapter.enableLoadMore(false);
        RequestParams params = new RequestParams(Url.HOT_CIRCLE_MAIN_URL);
        params.addBodyParameter("lng", lng + "");
        params.addBodyParameter("lat", lat + "");
        httpGetNoDia(params, "list");
    }

    /**
     * 判断位置是否发生变化，如果没有变化则不更新地址信息，防止选择地址后，地址又变化的问题
     *
     * @param lat1
     * @param lng1
     * @return
     */
    private boolean isLocationChange(double lat1, double lng1) {
        return Math.abs(lat1 - lat) > LOCATION_CHANGE_DISTINCTION_LAT
                || Math.abs(lng1 - lng) > LOCATION_CHANGE_DISTINCTION_LNG;
    }

    private void loadMoreDatas() {
        RequestParams params = new RequestParams(Url.PAGE_CIRCLE_URL);
        params.addBodyParameter("lng", lng + "");
        params.addBodyParameter("lat", lat + "");
        params.addBodyParameter("page", isFirst + "");
        params.addBodyParameter("type", "0");
        httpGetNoDia(params, "page");
        isFirst++;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK)
            return;
        switch (requestCode) {
            case INFO_DETAILS:
                getListInfo();
                break;
        }
    }
}
