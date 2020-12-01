package com.cxp.im.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.cxp.im.R;

/**
 * 文 件 名: VideoActivity
 * 创 建 人: CXP
 * 创建日期: 2020-09-22 14:30
 * 描    述: 视频播放
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class VideoActivity extends BaseActivity {


    private VideoView mVideoView;


    private String path;

    private int mPositionWhenPaused = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        if (getIntent() != null) {
            path = getIntent().getStringExtra("path");
        }

        mVideoView = findViewById(R.id.video_view);

        mVideoView.setMediaController(new MediaController(this));

        setupVideo();
    }


    private void setupVideo() {
        //监听视频装载完成的事件
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                /**
                 * VIDEO_SCALING_MODE_SCALE_TO_FIT：适应屏幕显示
                 * VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING：充满屏幕显示，保持比例，如果屏幕比例不对，则进行裁剪
                 */
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                mVideoView.start();
            }
        });
        //播放完毕，重新开始播放
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //循环播放
//                mp.setLooping(true);
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                stopPlaybackVideo();
                return true;
            }
        });
        mVideoView.setVideoPath(path);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideoView.isPlaying()) {
            mVideoView.seekTo(mPositionWhenPaused);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView.canPause()) {
            mVideoView.pause();
            mPositionWhenPaused = mVideoView.getCurrentPosition();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaybackVideo();
    }

    private void stopPlaybackVideo() {
        try {
            mVideoView.stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
