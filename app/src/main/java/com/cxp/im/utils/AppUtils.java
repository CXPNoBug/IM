package com.cxp.im.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * 文 件 名: AppUtils
 * 创 建 人: CXP
 * 创建日期: 2020-09-18 15:29
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class AppUtils {

    public static final String VIDEO_CACHE_DIR = "/cache/videos";
    public static final String APP_DIR = "/cxp";

    public static final String mSenderId = "right";
    public static final String mTargetId = "left";

    /**
     * 判断字符串是否为空
     *
     * @param object
     * @return
     */
    public static boolean notIsEmpty(Object object) {
        boolean flag = false;
        if (object != null && object.toString().trim().length() != 0) {
            flag = true;
        }
        return flag;
    }

    private static long lastClickTime;
    public static boolean isFastDoubleClick(int interval) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < interval) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }


}
