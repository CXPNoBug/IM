package com.cxp.im.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cxp.im.other.SerializableMap;

import java.util.Map;

/**
 * 文 件 名: BaseActivity
 * 创 建 人: CXP
 * 创建日期: 2020-09-17 20:28
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置状态栏
//        ImmersionBar.with(this)
//                .statusBarColor(R.color.new_color_000000) //状态栏颜色，不写默认透明色
//                .autoStatusBarDarkModeEnable(true,0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
//                .init();
    }

    /**
     * 页面跳转
     */
    public void startActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    //页面跳转
    public void startActivity(Class cls, Map<String, Object> map) {
        Intent intent = new Intent(this, cls);
        SerializableMap myMap = new SerializableMap();
        myMap.setMap(map);
        Bundle bundle = new Bundle();
        bundle.putSerializable("map", myMap);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
