package com.cxp.im.fingerprint;

import android.app.Activity;

import androidx.annotation.NonNull;

/**
 * 文 件 名: FingerprintVerifyManager
 * 创 建 人: CXP
 * 创建日期: 2020-09-27 10:39
 * 描    述: 指纹验证管理
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class FingerprintVerifyManager {

    public FingerprintVerifyManager(Builder builder) {
        IFingerprint fingerprint;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            fingerprint = FingerprintAndrM.newInstance();
        } else {
            //Android 6.0 以下官方未开放指纹识别，某些机型自行支持的情况暂不做处理
            builder.callback.onHwUnavailable();
            return;
        }
        //检测指纹硬件是否存在或者是否可用，若false，不再弹出指纹验证框
        if (!fingerprint.canAuthenticate(builder.context, builder.callback)) {
            return;
        }
        /**
         * 设定指纹验证框的样式
         */

        fingerprint.authenticate(builder.context,  builder.callback);
    }

    /**
     * UpdateAppManager的构建器
     */
    public static class Builder {
        /*必选字段*/
        private Activity context;
        private FingerprintCallback callback;


        /**
         * 构建器
         *
         * @param activity
         */
        public Builder(@NonNull Activity activity) {
            this.context = activity;
        }

        /**
         * 指纹识别回调
         *
         * @param callback
         */
        public Builder callback(@NonNull FingerprintCallback callback) {
            this.callback = callback;
            return this;
        }

        /**
         * 开始构建
         *
         * @return
         */
        public FingerprintVerifyManager build() {
            return new FingerprintVerifyManager(this);
        }
    }
}
