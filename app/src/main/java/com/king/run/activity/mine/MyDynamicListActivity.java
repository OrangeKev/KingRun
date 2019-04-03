package com.king.run.activity.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.circle.InfoDetailsActivity;
import com.king.run.activity.circle.adapter.CircleInfoAdapter;
import com.king.run.activity.circle.model.CircleInfo;
import com.king.run.activity.circle.model.Moment;
import com.king.run.activity.circle.model.PageResult;
import com.king.run.base.BaseActivity;
import com.king.run.util.PrefName;
import com.king.run.util.Url;
import com.lwkandroid.rcvadapter.holder.RcvHolder;
import com.lwkandroid.rcvadapter.listener.RcvItemViewClickListener;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_my_dynamic_list)
public class MyDynamicListActivity extends BaseActivity {
    @ViewInject(R.id.swipRefresh_circle)
    SwipeRefreshLayout swipRefresh_circle;
    @ViewInject(R.id.recyclerView_circle)
    RecyclerView recyclerView_circle;
    private CircleInfoAdapter mAdapter;
    private List<CircleInfo> list = new ArrayList<>();
    private List<Moment> moments = new ArrayList<>();
    private int page = 1;
    private static final int INFO_DETAILS = 111;
    private int deletePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
        loadMoreDatas();
        initCircleAdapter();
    }

    private void initViews() {
        title_tv_title.setText(R.string.my_dynamic);
    }

    private void initCircleAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mAdapter = new CircleInfoAdapter(context, moments);
        recyclerView_circle.setLayoutManager(layoutManager);
        recyclerView_circle.setAdapter(mAdapter);
        mAdapter.enableLoadMore(false);
        swipRefresh_circle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadMoreDatas();
            }
        });

        mAdapter.setOnItemClickListener(new RcvItemViewClickListener<Moment>() {
            @Override
            public void onItemViewClicked(RcvHolder holder, Moment circleInfo, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("moment", moments.get(position));
                jumpBundleActvityforResult(InfoDetailsActivity.class, bundle, INFO_DETAILS);
            }
        });
//        mAdapter.setOnItemAvatarClickListener(new CircleInfoAdapter.ItemAvatarClickInterface() {
//            @Override
//            public void onAvatarClick(int position) {
//                Bundle bundle = new Bundle();
//                bundle.putString("userId", moments.get(position).getUserId());
//                jumpBundleActvityforResult(UserInfoDetailsActivity.class, bundle, USER_INFO_DETAILS);
//            }
//        });
        mAdapter.setDel(new CircleInfoAdapter.DeleteInterface() {
            @Override
            public void delete(final Moment circleInfo, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

    private void loadMoreDatas() {
        RequestParams params = new RequestParams(Url.PAGE_CIRCLE_URL);
        params.addBodyParameter("lng", PrefName.getPrefLastLng(context));
        params.addBodyParameter("lat", PrefName.getPrefLastLat(context));
        params.addBodyParameter("page", page + "");
        params.addBodyParameter("type", "2");
        params.addBodyParameter("typeId", PrefName.getUserId(context));
        httpGet(params, "page");
        page++;
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        switch (type) {
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
                moments.remove(deletePos);
                mAdapter.addDatas(moments);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}
