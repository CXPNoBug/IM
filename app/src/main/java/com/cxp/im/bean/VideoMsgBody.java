package com.cxp.im.bean;

/**
 * 文 件 名: VideoMsgBody
 * 创 建 人: CXP
 * 创建日期: 2020-09-18 16:00
 * 描    述: 视频消息内容
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class VideoMsgBody extends FileMsgBody {

    //视频消息长度
    private long duration;
    //高度
    private int height;
    //宽度
    private int width;


    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
