package com.king.run.activity.circle;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.circle.adapter.CircleInfoAdapter;
import com.king.run.activity.circle.adapter.MyAttentionAdapter;
import com.king.run.activity.circle.model.CircleInfo;
import com.king.run.activity.circle.model.CircleInfoResult;
import com.king.run.activity.circle.model.Moment;
import com.king.run.activity.circle.model.PageResult;
import com.king.run.activity.mine.model.FansInfo;
import com.king.run.base.BaseFragment;
import com.king.run.util.PrefName;
import com.king.run.util.Url;
import com.king.run.view.ListViewForScrollView;
import com.lwkandroid.rcvadapter.holder.RcvHolder;
import com.lwkandroid.rcvadapter.listener.RcvItemViewClickListener;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_attention)
public class AttentionFragment extends BaseFragment {
    @ViewInject(R.id.swipRefresh_circle)
    SwipeRefreshLayout swipRefresh_circle;
    @ViewInject(R.id.recyclerView_circle)
    RecyclerView recyclerView_circle;
    private CircleInfoAdapter mAdapter;
    private List<Moment> moments = new ArrayList<>();
    private View headerView;
    private MyAttentionAdapter myAttentionAdapter;
    private ListViewForScrollView lv;
    List<CircleInfo> circleInfos = new ArrayList<>();
    private int page = 1;
    private static final int INFO_DETAILS = 111;
    private static final int USER_INFO_DETAILS = 121;
    private static final int HOT_CIRCLE_LIST = 123;
    private int deletePos;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        getAttentionCircle();
    }

    private void initViews() {
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_circle_attention_header, null);
        lv = headerView.findViewById(R.id.lv_header);
        headerView.findViewById(R.id.ly_all_attention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpBundleActvityforResult(HotCircleActivity.class, null, HOT_CIRCLE_LIST, "attention");
            }
        });
        myAttentionAdapter = new MyAttentionAdapter(getActivity());
        lv.setAdapter(myAttentionAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", circleInfos.get(i).getId());
                jumpBundleActvity(CircleDetailsActivity.class, bundle);
            }
        });
        initCircleAdapter();
    }

    private void getAttentionCircle() {
        RequestParams params = new RequestParams(Url.USER_ATTENTION_URL);
        httpGet(params, "userAttention");
    }

    private void initCircleAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new CircleInfoAdapter(getActivity(), moments);
        recyclerView_circle.setLayoutManager(layoutManager);
        mAdapter.addHeaderView(headerView);
        recyclerView_circle.setAdapter(mAdapter);
        mAdapter.enableLoadMore(false);
        mAdapter.setFocus(true);
        swipRefresh_circle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getAttentionCircle();
            }
        });

        mAdapter.setOnItemClickListener(new RcvItemViewClickListener<Moment>() {
            @Override
            public void onItemViewClicked(RcvHolder holder, Moment circleInfo, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("moment", moments.get(position - 1));
                jumpBundleActvityforResult(InfoDetailsActivity.class, bundle, INFO_DETAILS);

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
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
            case "userAttention":
                CircleInfoResult circleInfoResult = JSON.parseObject(result, CircleInfoResult.class);
                circleInfos = circleInfoResult.getData();
                myAttentionAdapter.setList(circleInfos);
                myAttentionAdapter.notifyDataSetChanged();
                loadMoreDatas();
                break;
            case "page":
                PageResult pageResult = JSON.parseObject(result, PageResult.class);
                if (page == 2) {
                    moments.clear();
                    moments = pageResult.getData();
                    mAdapter.addDatas(moments);
                    mAdapter.notifyDataSetChanged();
                    mAdapter.enableLoadMore(true, new RcvLoadMoreListener() {
                        @Override
                        public void onLoadMoreRequest() {
                            loadMoreDatas();
                        }
                    });
                    swipRefresh_circle.setRefreshing(false);
                } else {
                    if (null != pageResult.getData() && !pageResult.getData().isEmpty()) {
                        moments.addAll(pageResult.getData());
                        if (pageResult.getData().size() < 10) {
                            mAdapter.notifyLoadMoreSuccess(pageResult.getData(), false);
                            page = 1;
                        } else {
                            mAdapter.notifyLoadMoreSuccess(pageResult.getData(), true);
                        }
                    } else {
                        mAdapter.notifyLoadMoreSuccess(new ArrayList<Moment>(), false);
                        page = 1;
                    }
                }
                break;
            case "delete":
                moments.remove(deletePos - 1);
                mAdapter.addDatas(moments);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void loadMoreDatas() {
        RequestParams params = new RequestParams(Url.PAGE_CIRCLE_URL);
        params.addBodyParameter("lng", PrefName.getPrefLastLng(getActivity()));
        params.addBodyParameter("lat", PrefName.getPrefLastLat(getActivity()));
        params.addBodyParameter("type", "4");
        params.addBodyParameter("page", page + "");
        httpGet(params, "page");
        page++;
    }
}
