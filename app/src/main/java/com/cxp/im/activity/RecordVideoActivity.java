package com.cxp.im.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.cxp.im.R;
import com.cxp.im.record.DIYVideoConfigInfo;
import com.cxp.im.record.DIYVideoInfo;
import com.cxp.im.record.AlbumNotifyHelper;
import com.cxp.im.record.CustomVideoView;
import com.cxp.im.record.RecordFileUtils;
import com.cxp.im.record.RecordPictureConfig;
import com.cxp.im.utils.AppUtils;
import com.cxp.im.utils.FileUtils;
import com.cxp.im.record.CompressDialog;
import com.iceteck.silicompressorr.SiliCompressor;
import com.luck.picture.lib.tools.ToastUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * 文 件 名: RecordVideoActivity
 * 创 建 人: CXP
 * 创建日期: 2020-10-14 11:12
 * 描    述: 录制视频
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class RecordVideoActivity extends BaseActivity {

    @BindView(R.id.record_video_videoview)
    VideoView mRecordVideoVideoview;
    @BindView(R.id.record_video_videoview_play)
    CustomVideoView mRecordVideoVideoviewPlay;
    @BindView(R.id.record_video_time)
    Chronometer mRecordVideoTime;
    @BindView(R.id.record_video_time_ll)
    LinearLayout mRecordVideoTimeLl;
    @BindView(R.id.record_video_progreebar)
    MaterialProgressBar mRecordVideoProgreebar;
    @BindView(R.id.record_video_desc)
    TextView mRecordVideoDesc;
    @BindView(R.id.record_video_switch_camera)
    ImageView mRecordVideoSwitchCamera;
    @BindView(R.id.record_video_video_ll)
    LinearLayout mRecordVideoVideoLl;
    @BindView(R.id.record_video_success_time)
    Chronometer mRecordVideoSuccessTime;
    @BindView(R.id.record_video_success_time_ll)
    LinearLayout mRecordVideoSuccessTimeLl;
    @BindView(R.id.record_video_success_fl)
    FrameLayout mRecordVideoSuccessFl;

    private boolean isVideo = true;

    // 成员变量
    private PowerManager.WakeLock mWakeLock;
    private MediaRecorder mMediarecorder;// 录制视频的类
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private int mCameraId = -1;// 摄像头(前置 or 后置)
    private boolean mEnableSwitchCamera = true;// 是否允许切换摄像头
    private int mCameraDegree;
    private int mRecordQuality = -1;// 录制质量
    private int mPreviewWidth = -1;// 预览的宽
    private int mPreviewHeight = -1;// 预览的高
    private int mFrameRate = -1;// 帧率
    private int mBitRate = -1;// 比特率
    private String mVideoFilePath;
    private String mImgFilePath;
    private long mFileSizeLimit;
    private int mMinDuration;// 最小时长
    private int mMaxDuration;// 最大时长
    private long mRecordDuration;// 录制时长
    private boolean mRecording = false;// 是否在录音中
    private boolean mPlaying = false;// 是否在播放中
    private long startRecordTime;
    private String fileName = "";
    private int mPositionWhenPaused = -1;
    private CompressDialog compressDialog;
    private DIYVideoConfigInfo diyVideoConfigInfo;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
        // 设置竖屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 选择支持半透明模式,在有surfaceview的activity中使用。
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_record_video);
        ButterKnife.bind(this);

        //初始化数据
        initData();

        /**
         * 控制这个surfaceView是否被放在另一个普通的surfaceView上面（但是仍然在窗口之后）。这个函数通常被用来将覆盖层至于一个多媒体层上面。

         这个函数必须在窗口被添加到窗口管理器之前设置。

         调用这个函数会使之前调用的setZOrderOnTop(boolean)无效。
         */
        mRecordVideoVideoviewPlay.setZOrderMediaOverlay(true);

        /**
         * 控制这个surfaceView是否被放在窗口顶层。通常,为了使它与绘图树整合,它被放在窗口之后。通过这个函数，你可以使SurfaceView被放在窗口顶层。这意味着它所在的窗口的其他内容都不可见。（注:可以设置surfaceView透明来使其他内容可见）

         这个函数必须在窗口被添加到窗口管理器之前设置。

         调用这个函数会使之前调用的setZOrderMediaOverlay(boolean)无效；
         */
        // mRecordVideoVideoview.setZOrderOnTop(true);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // 无摄像头设备
            Toast.makeText(this, "当前无摄像设备", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;// 默认为后置摄像头
            mEnableSwitchCamera = Camera.getNumberOfCameras() > 1;
        }
        mRecordVideoSwitchCamera.setVisibility(mEnableSwitchCamera ? View.VISIBLE : View.GONE);

        mRecordVideoProgreebar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mRecordVideoSwitchCamera.setVisibility(View.GONE);
                        startRecordTime = System.currentTimeMillis();
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - startRecordTime < 2000) {
                            isVideo = false;
                            mRecordVideoTime.stop();
                            mRecordVideoTimeLl.setVisibility(View.GONE);
                            mRecordVideoVideoLl.setVisibility(View.GONE);
                            mRecordVideoSuccessFl.setVisibility(View.VISIBLE);

                            String imgFileName = "IMG_" + new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + ".jpg";
                            mImgFilePath = RecordFileUtils.getDir().getAbsoluteFile() + RecordFileUtils.IMG_DIR + File.separator + imgFileName;

                            takePicture();
                        } else {
                            stopRecord(true);
                        }

                        break;
                }
                return true;
            }
        });

        mRecordVideoVideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
            }
        });

        mRecordVideoVideoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mRecordVideoVideoview.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
        }

        mRecordVideoVideoviewPlay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //播放完成
                mRecordVideoSuccessTime.stop();
            }
        });

        mRecordVideoVideoviewPlay.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (what != 1) {
                    mPlaying = false;
                    mRecordVideoSuccessTime.stop();
                    mRecordVideoVideoviewPlay.stopPlayback();
                    updateBottom();
                }
                return true;
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mRecordVideoVideoviewPlay.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        // video started
                        mRecordVideoVideoviewPlay.setBackgroundColor(Color.TRANSPARENT);
                        return true;
                    }
                    return false;
                }
            });
        }

        mRecordVideoVideoviewPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });

        //给计时器添加监听器，当计时到达15s时，要重置
        mRecordVideoTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {
                mRecordDuration = SystemClock.elapsedRealtime() - arg0.getBase();
                //加载进度
                loadProgress();
                if (mRecordDuration >= (mMaxDuration * 1000)) {
                    stopRecord(true);
                }
            }
        });

    }

    /**
     * 初始化数据
     */
    @SuppressLint("InvalidWakeLockTag")
    private void initData() {
        mFileSizeLimit = 52428800;
        DIYVideoInfo diyVideoInfo = new DIYVideoInfo();
        diyVideoConfigInfo = RecordPictureConfig.loadVideoConfig(diyVideoInfo);
        mPreviewWidth = diyVideoConfigInfo.getmRecordWidth();
        mPreviewHeight = diyVideoConfigInfo.getmRecordHeight();
        mMinDuration = diyVideoConfigInfo.getMinSecond();
        mMaxDuration = diyVideoConfigInfo.getMaxSecond();
        //帧数
        mFrameRate = 25;
        //比特率
        mBitRate = diyVideoConfigInfo.getBitRate();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "");
        mWakeLock.acquire();
        mSurfaceHolder = mRecordVideoVideoview.getHolder();// 取得holder
        mSurfaceHolder.setKeepScreenOn(true);// 屏幕常亮
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCamera();
                mSurfaceHolder = holder;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceHolder = holder;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @OnClick({R.id.record_video_again, R.id.record_video_switch_camera, R.id.record_video_success_back, R.id.record_video_success_again, R.id.record_video_success_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.record_video_again:
            case R.id.record_video_success_back:
                //返回
                back();
                break;
            case R.id.record_video_switch_camera:
                //切换摄像头
                if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    mCameraId =  Camera.CameraInfo.CAMERA_FACING_BACK;
                } else {
                    mCameraId =  Camera.CameraInfo.CAMERA_FACING_FRONT;
                }
                initCamera();
                mRecordVideoSwitchCamera.setSelected(!mRecordVideoSwitchCamera.isSelected());
                break;
            case R.id.record_video_success_again:
                //重拍
                startRecordTime = 0;

                if (isVideo) {
                    mPlaying = false;
                    mRecordVideoSuccessTime.stop();
                    mRecordVideoVideoviewPlay.stopPlayback();
                    updateBottom();

                    if (!TextUtils.isEmpty(mVideoFilePath)) {
                        FileUtils.deleteFile(mVideoFilePath);
                    }
                } else {
                    againCamera();

                    if (!TextUtils.isEmpty(mImgFilePath)) {
                        FileUtils.deleteFile(mImgFilePath);
                    }
                }
                break;
            case R.id.record_video_success_ok:
                //确认
                if (isVideo) {
                    if (!AppUtils.isFastDoubleClick(1000)) {
                        if (diyVideoConfigInfo.isNeedCompress()) {
                            File oFile = new File(mVideoFilePath);
                            float olength = oFile.length() / 1024f; // Size in KB
                            //小于20M不压缩
                            if (olength > 20 * 1024) {
                                goCompress();
                            } else {
                                Intent intent = getIntent();
                                intent.putExtra("videoFilePath", mVideoFilePath);
                                intent.putExtra("videoFileDuration", mRecordDuration);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        } else {
                            Intent intent = getIntent();
                            intent.putExtra("videoFilePath", mVideoFilePath);
                            intent.putExtra("videoFileDuration", mRecordDuration);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                } else {
                    if (!AppUtils.isFastDoubleClick(1000)) {
                        Intent intent = getIntent();
                        intent.putExtra("imgFilePath", mImgFilePath);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mRecordVideoSuccessFl.getVisibility() == View.VISIBLE) {

                if (isVideo) {
                    mPlaying = false;
                    mRecordVideoSuccessTime.stop();
                    mRecordVideoVideoviewPlay.stopPlayback();
                    updateBottom();
                } else {
                    againCamera();
                }
            } else {
                //返回
                back();
            }
        }
        return true;
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        if (null != mCamera) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        try {
            if (Build.VERSION.SDK_INT > 8) {
                mCamera = Camera.open(mCameraId);
            } else {
                mCamera = Camera.open();
            }

            mCamera.setPreviewDisplay(mSurfaceHolder);
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraId, info);

            Camera.Parameters parameters = mCamera.getParameters();
            parameters.set("orientation", "portrait");// 竖屏显示
            mCamera.setDisplayOrientation(90);

            // 获取视频支持的帧率
            boolean hasSupportRate = false;
            List<Integer> supportedFrameRates = parameters.getSupportedPreviewFrameRates();
            if (null != supportedFrameRates && supportedFrameRates.size() > 0) {
                Collections.sort(supportedFrameRates);
                for (int i = 0; i < supportedFrameRates.size(); i++) {
                    int supportRate = supportedFrameRates.get(i);
                    if (mFrameRate == supportRate) {
                        hasSupportRate = true;
                        break;
                    }
                }

                if (!hasSupportRate) {
                    // 帧率取中间
                    int frameRateLocation = supportedFrameRates.size() / 2;
                    if (supportedFrameRates.size() % 2 == 0) {
                        frameRateLocation = frameRateLocation - 1;
                    }
                    mFrameRate = supportedFrameRates.get(frameRateLocation);
                }
            }

            // 获取摄像头所有支持的分辨率
            List<Camera.Size> resolutionList = parameters.getSupportedPreviewSizes();
            if (null != resolutionList && resolutionList.size() > 0) {
                boolean hasSize = false;
                for (Camera.Size size : resolutionList) {
                    if (size.width == mPreviewWidth && size.height == mPreviewHeight) {
                        hasSize = true;
                        break;
                    }
                }
                // 如果不支持则设为中间的那个
                if (!hasSize) {
                    int sizeLocation = resolutionList.size() / 2;
                    if (resolutionList.size() % 2 == 0) {
                        sizeLocation = sizeLocation - 1;
                    }
                    Camera.Size previewSize = resolutionList.get(sizeLocation);
                    mPreviewWidth = previewSize.width;
                    mPreviewHeight = previewSize.height;
                }
            }
            parameters.setPreviewSize(mPreviewWidth, mPreviewHeight);

            //增加对聚焦模式的判断
            List<String> focusModesList = parameters.getSupportedFocusModes();
            if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            } else if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }

            mCamera.setParameters(parameters);
            mCamera.startPreview();

            if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mCameraDegree = 270;
            } else {
                mCameraDegree = 90;
            }

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("打开设备失败");
            builder.setMessage("相机可能被占用");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            releaseCamera();
        }
    }

    @Override
    protected void onResume() {
        if (mPlaying) {
            // Resume video player
            if (mPositionWhenPaused >= 0) {
                mRecordVideoVideoviewPlay.seekTo(mPositionWhenPaused);
                mPositionWhenPaused = -1;
//                if (ivPlay.isSelected())
//                    mRecordVideoVideoviewPlay.start();
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
   /*     if (mRecording) {
            stopRecord(false);
        }*/

/*        if (mPlaying) {
            // Stop video when the activity is pause.
            mPositionWhenPaused = mRecordVideoVideoviewPlay.getCurrentPosition();
            mRecordVideoVideoviewPlay.stopPlayback();
        }

        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mRecordVideoSuccessFl.getVisibility() == View.GONE) {

            if (mRecording) {
                stopRecord(false);
            }

            if (mPlaying) {
                // Stop video when the activity is pause.
                mPositionWhenPaused = mRecordVideoVideoviewPlay.getCurrentPosition();
                mRecordVideoSuccessTime.stop();
                mRecordVideoVideoviewPlay.stopPlayback();
            }

            if (mWakeLock != null) {
                mWakeLock.release();
                mWakeLock = null;
            }
        } else {
            mRecordVideoSuccessFl.setVisibility(View.GONE);
            mRecordVideoVideoLl.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        mRecordVideoVideoview = null;
        mRecordVideoVideoviewPlay = null;
        releaseRecoder();
        releaseCamera();
        if (compressDialog != null)
            compressDialog = null;
        super.onDestroy();
    }

    @SuppressLint("NewApi")
    private void startRecord() {
        if (mRecording) {
            return;
        }
        if (null == mCamera) {
            initCamera();
        }
        mCamera.unlock();
        mMediarecorder = new MediaRecorder();// 创建mediarecorder对象
        mMediarecorder.setCamera(mCamera);

        mMediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);// 设置音频采集方式 需要放在setOutputFormat之前,CAMCORDER麦克风
        mMediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 设置视频的采集方式设置录制视频源为Camera（相机），需要放在setOutputFormat之前
        mMediarecorder.setOrientationHint(mCameraDegree);

        boolean boo = false;
        if (!boo) {
            mMediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 设置录制完成后视频的封装格式THREE_GPP为3gp，MPEG_4为mp4
            mMediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);// 设置录制的音频编码，需要放在setOutputFormat之后
            mMediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// 设置录制的视频编码，需要放在setOutputFormat之后
            mMediarecorder.setVideoSize(mPreviewWidth, mPreviewHeight);// 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
            mMediarecorder.setVideoEncodingBitRate(mBitRate);// 设置录制的视频编码比特率
            if (mFrameRate != -1) {
                mMediarecorder.setVideoFrameRate(mFrameRate);// 设置视频的帧速率，必须放在设置编码和格式的后面，否则报错
            }
        }

        mMediarecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        // 设置文件大小限制
      /*  if (mFileSizeLimit > 0) {
            mMediarecorder.setMaxFileSize(mFileSizeLimit);
        }*/
        // 设置时长限制
        mMediarecorder.setMaxDuration(mMaxDuration * 1000 + 2000);
        // 设置视频文件输出的路径
        File cacheDirFile = new File(RecordFileUtils.getDir(), RecordFileUtils.VIDEO_DIR);
        RecordFileUtils.createDirFile(cacheDirFile.getPath());
        fileName = "video_" + System.currentTimeMillis() + ".mp4";
        mVideoFilePath = String.format("%s/%s", cacheDirFile, fileName);
        mMediarecorder.setOutputFile(mVideoFilePath);
        mMediarecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                stopRecord(false);
            }
        });
        mMediarecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if (MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED == what) {
                    ToastUtils.s(RecordVideoActivity.this, "录音文件时长已达上限");
                    stopRecord(true);
                } else if (MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED == what) {
                    ToastUtils.s(RecordVideoActivity.this, "录音文件内容已达上限");
                    stopRecord(true);
                }
            }
        });
        boolean isStart = false;
        try {
            // 准备录制
            mMediarecorder.prepare();
            // 开始录制
            mMediarecorder.start();
            isStart = true;
            startChronometerTick();
            mRecordVideoDesc.setVisibility(View.INVISIBLE);
            mRecordVideoTimeLl.setVisibility(View.VISIBLE);
            mRecording = true;
        } catch (Exception e) {
            if (isStart) {
                stopRecord(false);
            } else {
                // 释放资源
                releaseRecoder();
                releaseCamera();
                finish();
            }
        }
    }

    /**
     * 获取系统API提供的视频质量
     *
     * @return
     */
    private CamcorderProfile getCamcorderProfile() {
        CamcorderProfile profile = null;
        if (mRecordQuality == 0) {
            if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P))
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);
        } else if (mRecordQuality == 1) {
            if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P))
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
        } else if (mRecordQuality == 2) {
            if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P))
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        }

        return profile;
    }

    /**
     * 录制计时
     */
    private void startChronometerTick() {
        //设置起始时间和时间格式，然后开始计时
        mRecordDuration = 0;
        mRecordVideoTime.setBase(SystemClock.elapsedRealtime());
        mRecordVideoTime.setFormat("%s");
        mRecordVideoTime.start();
    }

    /**
     * 播放计时
     */
    private void startSuccessChronometerTick() {
        mRecordVideoSuccessTimeLl.setVisibility(View.VISIBLE);
        mRecordVideoSuccessTime.setBase(SystemClock.elapsedRealtime());
        mRecordVideoSuccessTime.setFormat("%s");
        mRecordVideoSuccessTime.start();
    }

    /**
     * 停止录制
     */
    private void stopRecord(boolean isNormalRecord) {
        if (mMediarecorder != null) {
            isVideo = true;
            try {
                // 设置后不会崩
                mMediarecorder.setOnInfoListener(null);
                mMediarecorder.setOnErrorListener(null);
                mMediarecorder.setPreviewDisplay(null);
                try {
                    mRecordVideoTime.stop();
                    // 停止录制
                    mMediarecorder.stop();
                    //重置
                    mMediarecorder.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 释放资源
                releaseRecoder();
                try {
                    if (null != mCamera) {
                        mCamera.reconnect();
                    }
                } catch (IOException e) {
                }
                mRecording = false;
                if (isNormalRecord) {
                    //插入相册
                    AlbumNotifyHelper.insertVideoToMediaStore(RecordVideoActivity.this, mVideoFilePath,
                            System.currentTimeMillis(), mPreviewWidth, mPreviewHeight, mRecordDuration);
                    //开启预览视频
                    playVideo();
                } else {
                    if (!TextUtils.isEmpty(mVideoFilePath)) {
                        try {
                            FileUtils.deleteFile(mVideoFilePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                isVideo = false;
                takePicture();
            }
        }
    }


    /**
     * 销毁相机
     */
    private void releaseCamera() {
        try {
            if (mCamera != null) {
                if (mRecording) {
                    mCamera.lock();
                }
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
        }
    }

    /**
     * 销毁录像
     */
    private void releaseRecoder() {
        try {
            if (mMediarecorder != null) {
                mMediarecorder.release();
                mMediarecorder = null;
            }
        } catch (Exception e) {
        }
    }

    /**
     * 播放录制完的视频
     */
    private void playVideo() {
        mPlaying = true;
        updateBottom();
        mRecordVideoVideoviewPlay.setVideoPath(mVideoFilePath);
        mRecordVideoVideoviewPlay.requestFocus();
        mRecordVideoVideoviewPlay.start();

        //开始计时
        startSuccessChronometerTick();
    }

    /**
     * 更新布局
     */
    private void updateBottom() {
        if (mPlaying) {
            mRecordVideoVideoviewPlay.setVisibility(View.VISIBLE);
            mRecordVideoSuccessFl.setVisibility(View.VISIBLE);
            mRecordVideoVideoLl.setVisibility(View.GONE);
        } else {
            mRecordVideoVideoviewPlay.setVisibility(View.GONE);
            mRecordVideoSuccessFl.setVisibility(View.GONE);
            mRecordVideoTimeLl.setVisibility(View.GONE);
            mRecordVideoSuccessTimeLl.setVisibility(View.GONE);
            mRecordVideoVideoLl.setVisibility(View.VISIBLE);
            mRecordVideoSwitchCamera.setVisibility(View.VISIBLE);
            mRecordVideoProgreebar.setProgress(0);
        }
    }

    /**
     * 录像进度
     */
    private void loadProgress() {
        if (mRecordDuration != 0) {

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            String result = numberFormat.format((float) mRecordDuration / 1000 / (float) 15 * 100);
            int progress = (int) Math.floor(Double.valueOf(result));
            if (progress >= 94) {
                progress = 100;
            }
            Log.d("CXP_LOG", "progress：" + progress);
            mRecordVideoProgreebar.setProgress(progress);
        }

    }

    /**
     * 拍照
     */
    private void takePicture() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                //保存图片
                saveImage(data);

                //销毁相机
                releaseCamera();
            }
        });
    }

    /**
     * 保存图片
     */
    private void saveImage(byte[] data) {
        new Thread(() -> {
            try {
                RecordFileUtils.createDirFile(RecordFileUtils.getDir().getAbsoluteFile() + RecordFileUtils.IMG_DIR);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap newBitmap = rotateBitmap(bitmap, 90);
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(mImgFilePath));
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();

                //插入相册
                AlbumNotifyHelper.insertImageToMediaStore(RecordVideoActivity.this, mImgFilePath,
                        System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return bmp;
    }

    /**
     * 重拍照片
     */
    private void againCamera() {
        mRecordVideoVideoLl.setVisibility(View.VISIBLE);
        mRecordVideoSwitchCamera.setVisibility(View.VISIBLE);
        mRecordVideoSuccessFl.setVisibility(View.GONE);

        initCamera();
    }

    /**
     * 返回
     */
    private void back() {
        try {
            if (mRecording) {
                if (!TextUtils.isEmpty(mVideoFilePath)) {
                    FileUtils.deleteFile(mVideoFilePath);
                }
                mRecording = false;
            }
            releaseRecoder();
            releaseCamera();
            //取消
            setResult(Activity.RESULT_CANCELED);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩
     */
    private void goCompress() {
        RecordFileUtils.createDirFile(RecordFileUtils.getDir().getAbsoluteFile() + RecordFileUtils.VIDEO_DIR + "/compress");
        if (!TextUtils.isEmpty(mVideoFilePath) && RecordFileUtils.isFileExist(mVideoFilePath)) {
            try {
                //  final long time = System.currentTimeMillis();
                //  MyLogUtil.LogI("mao", "DIY视频彩铃压缩开始= " + mFilePath + "|" + time);
                SiliCompressor.with(RecordVideoActivity.this).compressVideoWithProgress(true, mVideoFilePath, RecordPictureConfig.getPathCompressVideo(),
                        diyVideoConfigInfo.getmCompressWidth(), diyVideoConfigInfo.getmCompressHeight(),
                        diyVideoConfigInfo.getCompressQuality(), diyVideoConfigInfo.getCompressBitRate(), new SiliCompressor.CompressListener() {
                            @Override
                            public void onStart() {
                                showCompressDialog(false);
                                setCompressDialogProgress(0);
                            }


                            @Override
                            public void onSuccess(String compressFilePath) {
                                try {
                                    if (TextUtils.isEmpty(compressFilePath) || !RecordFileUtils.isFileExist(compressFilePath)) {
                                        showCompressDialog(true);
                                        return;
                                    }
                                    //  String fileSize = FileUtils.getFileSizeWithformat(compressFilePath);
                                    // String text = String.format(Locale.US, "%s\nName: %s\nSize: %s", "完成", compressFilePath, fileSize);
                                    // MyLogUtil.LogI("mao", "DIY视频彩铃压缩结束= " + compressFilePath + "|" + (System.currentTimeMillis() - time) + "|" + text);
                                    closeCompressDialog();
                                    Intent intent = getIntent();
                                    intent.putExtra("filePath", compressFilePath);
                                    intent.putExtra("fileDuration", mRecordDuration);
                                    setResult(Activity.RESULT_OK, intent);
                                    RecordVideoActivity.this.finish();
                                } catch (Exception e) {
                                    showCompressDialog(true);
                                    RecordFileUtils.deleteDirectory(RecordPictureConfig.getPathCompressVideo());
                                }
                            }

                            @Override
                            public void onFail() {
                                showCompressDialog(true);
                                RecordFileUtils.deleteDirectory(RecordPictureConfig.getPathCompressVideo());
                            }

                            @Override
                            public void onProgress(float percent) {
                                setCompressDialogProgress(percent);
                            }
                        });

            } catch (OutOfMemoryError e) {
                showCompressDialog(true);
                RecordFileUtils.deleteDirectory(RecordPictureConfig.getPathCompressVideo());
            } catch (Exception e) {
                showCompressDialog(true);
                RecordFileUtils.deleteDirectory(RecordPictureConfig.getPathCompressVideo());
            }
        } else {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }


    private void setCompressDialogProgress(float percent) {
        if (compressDialog != null) {
            //因为走不到100，所以直接进整100了
            if (percent > 99.8) {
                percent = 100;
            }
            compressDialog.setProgress((int) percent);
        }
    }

    private void showCompressDialog(boolean isFail) {
        if (compressDialog == null) {
            compressDialog = new CompressDialog(this, R.style.FullHeightDialog);
            compressDialog.setYesOnclickListener(new CompressDialog.onYesOnclickListener() {
                @Override
                public void onYesOnclick() {
                    compressDialog.dismiss();
                }
            });
        }
        compressDialog.show();
        compressDialog.showLayout(isFail);
    }

    private void closeCompressDialog() {
        if (compressDialog != null) {
            compressDialog.dismiss();
        }
    }
}
