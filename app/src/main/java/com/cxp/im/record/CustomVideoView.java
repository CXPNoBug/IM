package com.cxp.im.record;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * @author maolubin
 * @Date 2019/7/9 15:43
 * @Description 全屏播放view,原生的不支持
 * @Phone 15888806386
 */
public class CustomVideoView extends VideoView {

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }
}
