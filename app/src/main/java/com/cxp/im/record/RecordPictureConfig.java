package com.cxp.im.record;

import android.media.CamcorderProfile;
import android.text.TextUtils;


/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.lib.config
 * describe：PictureSelector Final Class
 * email：893855882@qq.com
 * data：2017/5/24
 */

public class RecordPictureConfig {

    public static LocalMediaFolder childLocalMediaFolder;
    public static String PATH_COMPRESS_VIDEO ;
    public final static String FC_TAG = "picture";
    public final static String EXTRA_RESULT_SELECTION = "extra_result_media";
    public final static String EXTRA_LOCAL_MEDIAS = "localMedias";
    public final static String EXTRA_PREVIEW_SELECT_LIST = "previewSelectList";
    public final static String EXTRA_SELECT_LIST = "selectList";
    public final static String EXTRA_POSITION = "position";
    public final static String EXTRA_MEDIA = "media";
    public final static String DIRECTORY_PATH = "directory_path";
    public final static String BUNDLE_CAMERA_PATH = "CameraPath";
    public final static String BUNDLE_ORIGINAL_PATH = "OriginalPath";
    public final static String EXTRA_BOTTOM_PREVIEW = "bottom_preview";
    public final static String EXTRA_CONFIG = "PictureSelectorConfig";
    public final static String IMAGE = "image";
    public final static String VIDEO = "video";


    public final static int UPDATE_FLAG = 2774;// 预览界面更新选中数据 标识
    public final static int CLOSE_PREVIEW_FLAG = 2770;// 关闭预览界面 标识
    public final static int PREVIEW_DATA_FLAG = 2771;// 预览界面图片 标识
    public final static int TYPE_ALL = 0;
    public final static int TYPE_IMAGE = 1;
    public final static int TYPE_VIDEO = 2;
    public final static int TYPE_AUDIO = 3;

    public static final int MAX_COMPRESS_SIZE = 100;
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;

    public final static int SINGLE = 1;
    public final static int MULTIPLE = 2;

    public final static int CHOOSE_REQUEST = 188;
    public final static int REQUEST_CAMERA = 909;
    public final static int READ_EXTERNAL_STORAGE = 0x01;
    public final static int CAMERA = 0x02;
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;
    private static final int DEFAULT_MIN_DURATION = 1;
    private static final int DEFAULT_MAX_DURATION = 15;
    private static final int DEFAULT_BITRATE = 2 * 1024 * 1024;
    private static final int DEFAULT_COMPRESS_BITRATE = 3 * 1024 * 1024;

    public static String getPathCompressVideo(){
        if (TextUtils.isEmpty(PATH_COMPRESS_VIDEO)){
            PATH_COMPRESS_VIDEO= RecordFileUtils.getDir()+ RecordFileUtils.VIDEO_DIR + "/compress";
        }
        return PATH_COMPRESS_VIDEO;
    }

    public static DIYVideoConfigInfo loadVideoConfig(DIYVideoInfo diyVideoInfo) {
        DIYVideoConfigInfo videoConfigInfo = new DIYVideoConfigInfo();
//        String shootRes = diyVideoInfo.getShootRes();
        String shootRes="2";
        String leastSecond = diyVideoInfo.getLeastSecond();
        String maxSecond = diyVideoInfo.getMaxSecond();
        String needCompress = diyVideoInfo.getNeedCompress();
        String compressRes = diyVideoInfo.getCompressRes();
        String titleDes = diyVideoInfo.getTitleDes();

/*        String shootRes = "1";
        String leastSecond = "7";
        String maxSecond = "45";
        String needCompress = "0";
        String compressRes = "1";
        String titleDes = "视频彩铃只支持7~45s长度内视频，您可选择视频进行编辑。";*/

        if (TextUtils.isEmpty(shootRes)) {
            videoConfigInfo.setmRecordWidth(DEFAULT_WIDTH);
            videoConfigInfo.setmRecordHeight(DEFAULT_HEIGHT);
            videoConfigInfo.setBitRate(DEFAULT_BITRATE);
        } else {
            if (shootRes.equals("1")) {
                videoConfigInfo.setmRecordWidth(DEFAULT_WIDTH);
                videoConfigInfo.setmRecordHeight(DEFAULT_HEIGHT);
                videoConfigInfo.setBitRate(DEFAULT_BITRATE);
            } else if (shootRes.equals("2")) {
                videoConfigInfo.setmRecordWidth(1280);
                videoConfigInfo.setmRecordHeight(720);
                videoConfigInfo.setBitRate(3 * 1024 * 1024);
            } else if (shootRes.equals("3")) {
                videoConfigInfo.setmRecordWidth(1920);
                videoConfigInfo.setmRecordHeight(1080);
                videoConfigInfo.setBitRate(8 * 1024 * 1024);
            } else {
                videoConfigInfo.setmRecordWidth(DEFAULT_WIDTH);
                videoConfigInfo.setmRecordHeight(DEFAULT_HEIGHT);
                videoConfigInfo.setBitRate(DEFAULT_BITRATE);
            }

        }

        if (TextUtils.isEmpty(leastSecond)) {
            videoConfigInfo.setMinSecond(DEFAULT_MIN_DURATION);
        } else {
            videoConfigInfo.setMinSecond(Integer.valueOf(leastSecond));
        }

        if (TextUtils.isEmpty(maxSecond)) {
            videoConfigInfo.setMaxSecond(DEFAULT_MAX_DURATION);
        } else {
            videoConfigInfo.setMaxSecond(Integer.valueOf(maxSecond));
        }

        if (TextUtils.isEmpty(needCompress)) {
            videoConfigInfo.setNeedCompress(true);
        } else {
            if (needCompress.equals("0")) {
                videoConfigInfo.setNeedCompress(true);
            } else {
                videoConfigInfo.setNeedCompress(false);
            }
        }

      /*  if (TextUtils.isEmpty(bitRate)) {
            videoConfigInfo.setBitRate(DEFAULT_BITRATE);
        } else {
            CamcorderProfile profile = getCamcorderProfile(bitRate);
            if (profile != null) {
                videoConfigInfo.setBitRate(profile.videoBitRate);
                MyLogUtil.LogI("mao", "CamcorderProfile mPreviewWidth = " + profile.videoFrameWidth + "|mPreviewHeight = " + profile.videoFrameHeight
                        + "|videoBitRate="+ profile.videoBitRate + "|videoFrameRate = " + profile.videoFrameRate);
            }else {
                if (bitRate.equals("1")) {
                    videoConfigInfo.setBitRate(DEFAULT_BITRATE);
                } else if (bitRate.equals("2")) {
                    videoConfigInfo.setBitRate(12000000);
                } else if(bitRate.equals("3")){
                    videoConfigInfo.setBitRate(17000000);
                }else {
                    videoConfigInfo.setBitRate(DEFAULT_BITRATE);
                }
            }
        }*/


        if (TextUtils.isEmpty(compressRes)) {
            videoConfigInfo.setmCompressWidth(DEFAULT_WIDTH);
            videoConfigInfo.setmCompressHeight(DEFAULT_HEIGHT);
            videoConfigInfo.setCompressBitRate(DEFAULT_COMPRESS_BITRATE);
        } else {
            if (compressRes.equals("1")) {
                videoConfigInfo.setmCompressWidth(DEFAULT_WIDTH);
                videoConfigInfo.setmCompressHeight(DEFAULT_HEIGHT);
                videoConfigInfo.setCompressBitRate(DEFAULT_COMPRESS_BITRATE);
            } else if (compressRes.equals("2")) {
                videoConfigInfo.setmCompressWidth(1280);
                videoConfigInfo.setmCompressHeight(720);
                videoConfigInfo.setCompressBitRate(3 * 1024 * 1024);
            } else if (compressRes.equals("3")) {
                videoConfigInfo.setmCompressWidth(1920);
                videoConfigInfo.setmCompressHeight(1080);
                videoConfigInfo.setCompressBitRate(4 * 1024 * 1024);
            } else {
                videoConfigInfo.setmCompressWidth(DEFAULT_WIDTH);
                videoConfigInfo.setmCompressHeight(DEFAULT_HEIGHT);
                videoConfigInfo.setCompressBitRate(DEFAULT_COMPRESS_BITRATE);
            }
        }


        if (!TextUtils.isEmpty(titleDes)) {
            videoConfigInfo.setTitleDes(titleDes);
        }

        return videoConfigInfo;
    }

    private static CamcorderProfile getCamcorderProfile(String bitRate) {
        CamcorderProfile profile = null;
        if (bitRate.equals("1")) {
            if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P))
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        } else if (bitRate.equals("2")) {
            if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P))
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
        } else if (bitRate.equals("3")) {
            if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P))
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);
        }

        return profile;
    }
}
