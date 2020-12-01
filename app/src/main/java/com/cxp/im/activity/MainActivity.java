package com.cxp.im.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.cxp.im.R;
import com.cxp.im.fingerprint.FingerprintCallback;
import com.cxp.im.fingerprint.FingerprintVerifyManager;
import com.cxp.im.utils.CrashHandler;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

public class MainActivity extends BaseActivity {


    private String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "jnn.mp4";

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
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

//        MMKV mmkv = MMKV.mmkvWithID("MyData", MMKV.MULTI_PROCESS_MODE);
//        Log.d("CXP_LOG",mmkv.decodeString("name"));

    }

    public void clickLis(View view) {
        switch (view.getId()) {
            case R.id.main_bt1:
                //会话页面
                startActivity(ChatActivity.class);
                break;
            case R.id.main_bt2:
                //会话页面
                Intent intent = new Intent(this, VideoActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
                break;
            case R.id.main_bt3:
                //视频录制
                startActivity(RecordVideoActivity.class);
                break;
            case R.id.main_bt4:
                //短信群发
                startActivity(SmsGroupSendListActivity.class);
                break;
            case R.id.main_bt5:
                //指纹解锁
                FingerprintVerifyManager.Builder builder = new FingerprintVerifyManager.Builder(MainActivity.this);
                builder.callback(fingerprintCallback)
                        .build();
                break;
        }
    }

    private FingerprintCallback fingerprintCallback = new FingerprintCallback() {
        @Override
        public void onSucceeded() {
            Toast.makeText(MainActivity.this, getString(R.string.fingerprint_verify_success), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            Toast.makeText(MainActivity.this, getString(R.string.fingerprint_verify_failed), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUsepwd() {
            Toast.makeText(MainActivity.this, getString(R.string.fingerprint_usepwd), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, getString(R.string.fingerprint_cancel), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onHwUnavailable() {
            Toast.makeText(MainActivity.this, getString(R.string.fingerprint_finger_hw_unavailable), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNoneEnrolled() {
            //弹出提示框，跳转指纹添加页面
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.fingerprint_tip))
                    .setMessage(getString(R.string.fingerprint_finger_add))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.fingerprint_finger_add_confirm), ((DialogInterface dialog, int which) -> {
                        Intent intent = new Intent();
                        String pcgName = "com.android.settings";
                        String clsName = "com.android.settings.fingerprint.FingerprintSettingsActivity";
                        ComponentName componentName = new ComponentName(pcgName, clsName);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setComponent(componentName);
                        startActivity(intent);
                    }
                    ))
                    .setPositiveButton(getString(R.string.fingerprint_cancel), ((DialogInterface dialog, int which) -> {
                        dialog.dismiss();
                    }
                    ))
                    .create().show();
        }

    };


}
