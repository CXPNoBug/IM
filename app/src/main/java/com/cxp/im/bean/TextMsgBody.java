package com.cxp.im.bean;

/**
 * 文 件 名: TextMsgBody
 * 创 建 人: CXP
 * 创建日期: 2020-09-18 15:59
 * 描    述: 文本消息内容
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class TextMsgBody extends MsgBody {

    private String message;
    private String extra;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public TextMsgBody() {
    }

    public TextMsgBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "TextMsgBody{" +
                "message='" + message + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}
