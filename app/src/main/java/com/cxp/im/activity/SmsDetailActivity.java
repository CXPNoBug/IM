package com.cxp.im.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.cxp.im.R;
import com.cxp.im.other.SerializableMap;
import com.cxp.im.utils.AppUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文 件 名: SmsDetailActivity
 * 创 建 人: CXP
 * 创建日期: 2020-09-24 19:01
 * 描    述: 短信详情
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SmsDetailActivity extends BaseActivity {

    @BindView(R.id.new_head_title)
    TextView mNewHeadTitle;
    @BindView(R.id.sms_detail_people)
    TextView mSmsDetailPeople;
    @BindView(R.id.sms_detail_time)
    TextView mSmsDetailTime;
    @BindView(R.id.sms_detail_content)
    TextView mSmsDetailContent;
    private Map<String, Object> datas;

    private String mPeople;
    private String mContent;
    private String mTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        SerializableMap serializableMap = (SerializableMap) bundle.get("map");
        if (serializableMap != null) {
            datas = serializableMap.getMap();
            if (AppUtils.notIsEmpty(datas.get("people"))) {
                mPeople = (String) datas.get("people");
            }
            if (AppUtils.notIsEmpty(datas.get("content"))) {
                mContent = (String) datas.get("content");
            }
            if (AppUtils.notIsEmpty(datas.get("time"))) {
                mTime = (String) datas.get("time");
            }
        }

    }

    @OnClick({R.id.new_head_back_ll, R.id.sms_detail_again_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.new_head_back_ll:
                finish();
                break;
            case R.id.sms_detail_again_send:

                break;
        }
    }
}
