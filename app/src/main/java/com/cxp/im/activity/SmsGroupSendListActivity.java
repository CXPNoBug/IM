package com.cxp.im.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cxp.im.R;
import com.cxp.im.adapter.SmsGroupSendListAdapter;
import com.cxp.im.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文 件 名: SmsGroupSendListActivity
 * 创 建 人: CXP
 * 创建日期: 2020-09-24 18:15
 * 描    述: 短信群发
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SmsGroupSendListActivity extends BaseActivity {

    @BindView(R.id.new_head_title)
    TextView mNewHeadTitle;
    @BindView(R.id.new_head_right_tv)
    TextView mNewHeadRightTv;
    @BindView(R.id.sms_group_send_list_recyclerview)
    RecyclerView mSmsGroupSendListRecyclerview;

    private Context mContext;
    private List<Map<String, Object>> mDatas;
    private SmsGroupSendListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_group_send_list);
        ButterKnife.bind(this);

        mContext = this;

        mNewHeadRightTv.setVisibility(View.VISIBLE);
        mNewHeadTitle.setText("短信群发");
        mNewHeadRightTv.setText("发短信");

        mDatas = new ArrayList<>();

        //初始化数据
        initDatas();

        mAdapter = new SmsGroupSendListAdapter(mContext, mDatas, new OnItemClickListener() {
            @Override
            public void itemClickListener(View view, int position) {
                //短信详情
                startActivity(SmsDetailActivity.class,mDatas.get(position));
            }
        });
        mSmsGroupSendListRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mSmsGroupSendListRecyclerview.setAdapter(mAdapter);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        Map<String,Object> map=new HashMap<>();
        map.put("people","张三");
        map.put("content","明天把计划发给我");
        map.put("time","下午2:20");
        mDatas.add(map);
        Map<String,Object> map1=new HashMap<>();
        map1.put("people","李四");
        map1.put("content","明天把计划发给我");
        map1.put("time","下午2:20");
        mDatas.add(map1);
        Map<String,Object> map2=new HashMap<>();
        map2.put("people","王五");
        map2.put("content","明天把计划发给我");
        map2.put("time","下午2:20");
        mDatas.add(map2);
    }

    @OnClick(R.id.new_head_back_ll)
    public void onViewClicked() {
        finish();
    }
}
