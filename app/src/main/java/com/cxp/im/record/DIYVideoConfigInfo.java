package com.cxp.im.record;

/**
 * @author maolubin
 * @Date 2019/7/12 18:04
 * @Description diy彩铃视频端能力下发参数真实配置
 * @Phone 15888806386
 */


public class DIYVideoConfigInfo {
    private String titleDes;       //选择视频界面头部显示文字
    private int minSecond; //最小录制时长(秒)
    private int maxSecond;   //最大录制时长(秒)
    private int bitRate;     //录制比特率
    private int mRecordWidth;  //录制分辨率宽
    private int mRecordHeight;     //录制分辨率高
    private boolean needCompress; //0 需要 1 不需要
    private int mCompressWidth;     //压缩分辨率宽
    private int mCompressHeight;     //压缩分辨率高
    private int compressBitRate; // 压缩比特率
    private int compressQuality; // 压缩质量，低中高123

    public int getMinSecond() {
        return minSecond;
    }

    public void setMinSecond(int minSecond) {
        this.minSecond = minSecond;
    }

    public int getMaxSecond() {
        return maxSecond;
    }

    public void setMaxSecond(int maxSecond) {
        this.maxSecond = maxSecond;
    }


    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getmRecordWidth() {
        return mRecordWidth;
    }

    public void setmRecordWidth(int mRecordWidth) {
        this.mRecordWidth = mRecordWidth;
    }

    public int getmRecordHeight() {
        return mRecordHeight;
    }

    public void setmRecordHeight(int mRecordHeight) {
        this.mRecordHeight = mRecordHeight;
    }


    public boolean isNeedCompress() {
        return needCompress;
    }

    public void setNeedCompress(boolean needCompress) {
        this.needCompress = needCompress;
    }

    public int getmCompressWidth() {
        return mCompressWidth;
    }

    public void setmCompressWidth(int mCompressWidth) {
        this.mCompressWidth = mCompressWidth;
    }

    public int getmCompressHeight() {
        return mCompressHeight;
    }

    public void setmCompressHeight(int mCompressHeight) {
        this.mCompressHeight = mCompressHeight;
    }

    public int getCompressBitRate() {
        return compressBitRate;
    }

    public void setCompressBitRate(int compressBitRate) {
        this.compressBitRate = compressBitRate;
    }

    public int getCompressQuality() {
        return compressQuality;
    }

    public void setCompressQuality(int compressQuality) {
        this.compressQuality = compressQuality;
    }

    public String getTitleDes() {
        return titleDes;
    }

    public void setTitleDes(String titleDes) {
        this.titleDes = titleDes;
    }

}
