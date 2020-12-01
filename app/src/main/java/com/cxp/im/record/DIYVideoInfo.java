package com.cxp.im.record;

import java.io.Serializable;

/**
 * @author maolubin
 * @Date 2019/7/12 18:04
 * @Description diy彩铃视频端能力下发参数
 * @Phone 15888806386
 */


public class DIYVideoInfo implements Serializable{
    private String shootRes;        //拍摄分辨率
    private String leastSecond; //最小录制时长(秒)
    private String maxSecond;   //最大录制时长(秒)
    private String needCompress; //0 需要 1 不需要
    private String titleDes;       //选择视频界面头部显示文字
    private String compressRes;     //压缩级别

    public String getLeastSecond() {
        return leastSecond;
    }

    public void setLeastSecond(String leastSecond) {
        this.leastSecond = leastSecond;
    }

    public String getMaxSecond() {
        return maxSecond;
    }

    public void setMaxSecond(String maxSecond) {
        this.maxSecond = maxSecond;
    }

    public String getNeedCompress() {
        return needCompress;
    }

    public void setNeedCompress(String needCompress) {
        this.needCompress = needCompress;
    }

    public String getTitleDes() {
        return titleDes;
    }

    public void setTitleDes(String titleDes) {
        this.titleDes = titleDes;
    }

    public String getCompressRes() {
        return compressRes;
    }

    public void setCompressRes(String compressRes) {
        this.compressRes = compressRes;
    }

    public String getShootRes() {
        return shootRes;
    }

    public void setShootRes(String shootRes) {
        this.shootRes = shootRes;
    }
}
