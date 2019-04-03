package com.king.run.activity.circle;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.king.run.R;
import com.king.run.activity.circle.adapter.CircleNameListAdapter;
import com.king.run.activity.circle.model.CircleInfo;
import com.king.run.activity.circle.model.CircleInfoResult;
import com.king.run.base.BaseActivity;
import com.king.run.util.Url;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_add_circle_list)
public class AddCircleListActivity extends BaseActivity {
    @ViewInject(R.id.lv)
    ListView lv;
    private CircleNameListAdapter adapter;
    private List<CircleInfo> list = new ArrayList<>();
    @ViewInject(R.id.lv_no_circle)
    RelativeLayout lv_no_circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
        getAttentionCircle();
    }

    private void initViews() {
        title_tv_title.setText(R.string.add_circle_no_add);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("circleInfo", list.get(i));
                setResultAct(bundle);
                finish();
            }
        });
    }

    private void getAttentionCircle() {
        RequestParams params = new RequestParams(Url.USER_ATTENTION_URL);
        httpGet(params, "userAttention");
    }

    @Override
    public void success(String result, String type) {
        super.success(result, type);
        CircleInfoResult circleInfoResult = JSON.parseObject(result, CircleInfoResult.class);
        list = circleInfoResult.getData();
        if (null != list && !list.isEmpty()) {
            lv.setVisibility(View.VISIBLE);
            lv_no_circle.setVisibility(View.INVISIBLE);
            adapter = new CircleNameListAdapter(context, list);
            lv.setAdapter(adapter);
        }else {
            lv.setVisibility(View.INVISIBLE);
            lv_no_circle.setVisibility(View.VISIBLE);
        }
    }
}
