package com.cxp.im.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cxp.im.utils.AppUtils;

public class Message implements MultiItemEntity {

    public static final int TYPE_SEND_TEXT = 1; //发送文本消息
    public static final int TYPE_RECEIVE_TEXT = 2; //接收文本消息
    public static final int TYPE_SEND_IMAGE = 3; //发送图片消息
    public static final int TYPE_RECEIVE_IMAGE = 4; //接收图片消息
    public static final int TYPE_SEND_VIDEO = 5; //发送视频消息
    public static final int TYPE_RECEIVE_VIDEO = 6; //接收视频消息
    public static final int TYPE_SEND_FILE = 7; //发送文件消息
    public static final int TYPE_RECEIVE_FILE = 8; //接收文件消息
    public static final int TYPE_SEND_AUDIO = 9; //发送音频消息
    public static final int TYPE_RECEIVE_AUDIO = 10; //接收音频消息

    private String uuid;
    private String msgId;
    private MsgType msgType;
    private MsgBody body;
    private MsgSendStatus sentStatus;
    private String senderId;
    private String targetId;
    private long sentTime;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public MsgBody getBody() {
        return body;
    }

    public void setBody(MsgBody body) {
        this.body = body;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public MsgSendStatus getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(MsgSendStatus sentStatus) {
        this.sentStatus = sentStatus;
    }


    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    @Override
    public int getItemType() {
        boolean isSend = senderId.equals(AppUtils.mSenderId);
        if (MsgType.TEXT == msgType) {
            return isSend ? TYPE_SEND_TEXT : TYPE_RECEIVE_TEXT;
        } else if (MsgType.IMAGE == msgType) {
            return isSend ? TYPE_SEND_IMAGE : TYPE_RECEIVE_IMAGE;
        } else if (MsgType.VIDEO == msgType) {
            return isSend ? TYPE_SEND_VIDEO : TYPE_RECEIVE_VIDEO;
        } else if (MsgType.FILE == msgType) {
            return isSend ? TYPE_SEND_FILE : TYPE_RECEIVE_FILE;
        } else if (MsgType.AUDIO == msgType) {
            return isSend ? TYPE_SEND_AUDIO : TYPE_RECEIVE_AUDIO;
        }
        return 0;
    }
}
