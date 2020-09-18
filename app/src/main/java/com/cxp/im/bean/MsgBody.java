package com.cxp.im.bean;

import java.io.Serializable;

/**
 * 文 件 名: MsgBody
 * 创 建 人: CXP
 * 创建日期: 2020-09-18 14:26
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class MsgBody implements Serializable {

    private MsgType localMsgType;

    public MsgType getLocalMsgType() {
        return localMsgType;
    }

    public void setLocalMsgType(MsgType localMsgType) {
        this.localMsgType = localMsgType;
    }
}
