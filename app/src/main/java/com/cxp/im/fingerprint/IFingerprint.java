package com.cxp.im.fingerprint;

import android.app.Activity;
import android.content.Context;

/**
 * 文 件 名: IFingerprint
 * 创 建 人: CXP
 * 创建日期: 2020-09-27 10:42
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public interface IFingerprint {

    /**
     * 检测指纹硬件是否可用，及是否添加指纹
     * @param context
     * @param callback
     * @return
     */
    boolean canAuthenticate(Context context, FingerprintCallback callback);

    /**
     * 初始化并调起指纹验证
     *
     * @param context
     * @param callback
     */
    void authenticate(Activity context, FingerprintCallback callback);
}
