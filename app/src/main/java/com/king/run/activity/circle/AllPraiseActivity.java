package com.king.run.activity.circle;

import android.os.Bundle;
import android.widget.ListView;

import com.king.run.R;
import com.king.run.activity.circle.adapter.AllPraiseAdapter;
import com.king.run.activity.circle.model.LikeUser;
import com.king.run.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

@ContentView(R.layout.activity_all_praise)
public class AllPraiseActivity extends BaseActivity {
    private List<LikeUser> list;
    @ViewInject(R.id.lv)
    ListView lv;
    private AllPraiseAdapter allPraiseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initViews();
    }

    private void initViews() {
        list = (List<LikeUser>) getIntent().getExtras().getSerializable("list");
        title_tv_title.setText("赞");
        allPraiseAdapter = new AllPraiseAdapter(context, list);
        lv.setAdapter(allPraiseAdapter);
    }
}
