package com.cxp.im.bean;

/**
 * 文 件 名: AudioMsgBody
 * 创 建 人: CXP
 * 创建日期: 2020-09-18 14:32
 * 描    述: 音频消息内容
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class AudioMsgBody extends FileMsgBody {

    //语音消息长度,单位：秒。
    private long duration;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
