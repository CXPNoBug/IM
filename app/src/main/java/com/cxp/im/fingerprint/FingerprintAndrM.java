package com.cxp.im.fingerprint;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import com.cxp.im.R;

/**
 * 文 件 名: FingerprintAndrM
 * 创 建 人: CXP
 * 创建日期: 2020-09-27 10:51
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintAndrM implements IFingerprint {

    private final String TAG = FingerprintAndrM.class.getName();
    private Activity context;

    private static FingerprintAndrM fingerprintAndrM;
    //指纹验证框
    private static FingerprintDialog fingerprintDialog;
    //指向调用者的指纹回调
    private FingerprintCallback fingerprintCallback;

    //用于取消扫描器的扫描动作
    private CancellationSignal cancellationSignal;
    //指纹加密
    private static FingerprintManagerCompat.CryptoObject cryptoObject;
    //Android 6.0 指纹管理
    private FingerprintManagerCompat fingerprintManagerCompat;


    public static FingerprintAndrM newInstance() {
        if (fingerprintAndrM == null) {
            synchronized (FingerprintAndrM.class) {
                if (fingerprintAndrM == null) {
                    fingerprintAndrM = new FingerprintAndrM();
                }
            }
        }
        //指纹加密，提前进行Cipher初始化，防止指纹认证时还没有初始化完成
        try {
            cryptoObject = new FingerprintManagerCompat.CryptoObject(new CipherHelper().createCipher());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fingerprintAndrM;
    }

    /**
     * 指纹验证框按键监听
     */
    private FingerprintDialog.OnDialogActionListener dialogActionListener = new FingerprintDialog.OnDialogActionListener() {
        @Override
        public void onUsepwd() {
            if (fingerprintCallback != null)
                fingerprintCallback.onUsepwd();
        }

        @Override
        public void onCancle() {//取消指纹验证，通知调用者
            if (fingerprintCallback != null)
                fingerprintCallback.onCancel();
        }

        @Override
        public void onDismiss() {//验证框消失，取消指纹验证
            if (cancellationSignal != null && !cancellationSignal.isCanceled())
                cancellationSignal.cancel();
        }
    };

    /**
     * 指纹验证结果回调
     */
    private FingerprintManagerCompat.AuthenticationCallback authenticationCallback = new FingerprintManagerCompat.AuthenticationCallback() {

        //指纹验证多次错误的时候回调方法，多次尝试都失败了的时候，errString为错误信息
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            //errMsgId==5时，在OnDialogActionListener的onCancle回调中处理；！=5的报错，才需要显示在指纹验证框中。
            if (errMsgId != 5)
                fingerprintDialog.setTip(errString.toString(), R.color.fingerprint_color_FF5555);
        }

        //当指纹验证失败的时候会回调此方法
        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
            fingerprintDialog.setTip(helpString.toString(), R.color.fingerprint_color_FF5555);
        }

        //在认证期间遇到可恢复的错误时调用,比如手指移到太快指纹传感器可能只采集了部分的信息
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            fingerprintDialog.setTip(context.getString(R.string.fingerprint_verify_success), R.color.fingerprint_color_82C785);
            fingerprintCallback.onSucceeded();
            fingerprintDialog.dismiss();
        }

        //当验证的指纹成功时会回调此函数
        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            fingerprintDialog.setTip(context.getString(R.string.fingerprint_verify_failed), R.color.fingerprint_color_FF5555);
            fingerprintCallback.onFailed();
        }
    };

    /*
     * 在 Android Q，Google 提供了 Api BiometricManager.canAuthenticate() 用来检测指纹识别硬件是否可用及是否添加指纹
     * 不过尚未开放，标记为"Stub"(存根)
     * 所以暂时还是需要使用 Andorid 6.0 的 Api 进行判断
     * */
    @Override
    public boolean canAuthenticate(Context context, FingerprintCallback fingerprintCallback) {
        /*
         * 硬件是否支持指纹识别
         * */
        if (!FingerprintManagerCompat.from(context).isHardwareDetected()) {
            fingerprintCallback.onHwUnavailable();
            return false;
        }
        //是否已添加指纹
        if (!FingerprintManagerCompat.from(context).hasEnrolledFingerprints()) {
            fingerprintCallback.onNoneEnrolled();
            return false;
        }
        return true;
    }

    @Override
    public void authenticate(Activity context, FingerprintCallback callback) {
        this.context = context;
        this.fingerprintCallback = callback;
        //Android 6.0 指纹管理 实例化
        fingerprintManagerCompat = FingerprintManagerCompat.from(context);

        //取消扫描，每次取消后需要重新创建新示例
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(() -> fingerprintDialog.dismiss());

        //调起指纹验证
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, authenticationCallback, null);
        //指纹验证框
        fingerprintDialog = FingerprintDialog.newInstance().setActionListener(dialogActionListener);
        fingerprintDialog.show(context.getFragmentManager(), TAG);
    }
}
