package com.cxp.im.fingerprint;

/**
 * 文 件 名: FingerprintCallback
 * 创 建 人: CXP
 * 创建日期: 2020-09-27 10:41
 * 描    述: 验证结果回调，供使用者调用
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public interface FingerprintCallback {

    /**
     * 无指纹硬件或者指纹硬件不可用
     */
    void onHwUnavailable();

    /**
     * 未添加指纹
     */
    void onNoneEnrolled();

    /**
     * 验证成功
     */
    void onSucceeded();

    /**
     * 验证失败
     */
    void onFailed();

    /**
     * 密码登录
     */
    void onUsepwd();

    /**
     * 取消验证
     */
    void onCancel();
}
