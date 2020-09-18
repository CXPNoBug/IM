package com.cxp.im.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.cxp.im.R;
import com.cxp.im.utils.CrashHandler;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class MainActivity extends BaseActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                         Manifest.permission.CAMERA,
                         Manifest.permission.RECORD_AUDIO)
                .subscribe(granted -> {
                    if (granted) {
                        //权限开启成功

                        //崩溃捕获
                        CrashHandler catchHandler = CrashHandler.getInstance();
                        catchHandler.init(getApplicationContext());
                    } else {
                        //权限开启失败
                    }
                });

    }

    public void clickLis(View view) {
        switch (view.getId()) {
            case R.id.main_bt1:
                //会话页面
                startActivity(ChatActivity.class);
                break;
        }
    }
}
