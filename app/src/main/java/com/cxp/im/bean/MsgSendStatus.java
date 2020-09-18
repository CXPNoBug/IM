package com.cxp.im.bean;

/**
 * 文 件 名: MsgSendStatus
 * 创 建 人: CXP
 * 创建日期: 2020-09-18 14:25
 * 描    述: 消息发送状态
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public enum MsgSendStatus {

    DEFAULT,
    //发送中
    SENDING,
    //发送失败
    FAILED,
    //已发送
    SENT

}
