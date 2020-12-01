package com.cxp.im.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.cxp.im.R;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;

public class RecordButton extends AppCompatButton {


    public RecordButton(Context context) {
        super(context);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private String mFile = "";


    private OnFinishedRecordListener finishedListener;
    /**
     * 最短录音时间
     **/
    private int MIN_INTERVAL_TIME = 1000;
    /**
     * 最长录音时间
     **/
    private int MAX_INTERVAL_TIME = 1000 * 15;

    /**
     * 倒计时
     */
    private int COUNT_DOWN = 1000 * 10;

    private static View view;

    private LinearLayout mRootLl;
    private TextView mStateDate;
    private TextView mStateTV;
    private TextView mStateTime;
    private ImageView mStateIV;

    private MediaRecorder mRecorder;
    private ObtainDecibelThread mThread;
    private Handler volumeHandler;

    private boolean isSend;

    private long startCountTime;//实时刷新的时间

    private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

    private Handler mHandler = new Handler();

    private CountDownTimer mCountDownTimer;

    private float y;

    private boolean isCancel;


    public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
        finishedListener = listener;
    }


    private static long startTime;
    private Dialog recordDialog;
    private static int[] res = {R.drawable.ic_volume_0, R.drawable.ic_volume_1, R.drawable.ic_volume_2,
            R.drawable.ic_volume_3, R.drawable.ic_volume_4, R.drawable.ic_volume_5, R.drawable.ic_volume_6
            , R.drawable.ic_volume_7, R.drawable.ic_volume_8};


    @SuppressLint("HandlerLeak")
    private void init() {
        volumeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == -100) {
                    stopRecording();
                    recordDialog.dismiss();
                } else if (msg.what != -1) {
                    mStateIV.setImageResource(res[msg.what]);
                }
            }
        };
    }

    private AnimationDrawable anim;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        y = event.getY();
        if (mStateTV != null && mStateIV != null && y < 0) {
            isCancel=true;
            mStateIV.setVisibility(VISIBLE);
            mStateDate.setVisibility(INVISIBLE);
            mStateTime.setVisibility(GONE);
            mStateTV.setText("松开手指,取消发送");
            mStateIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_cancel));
        } else if (mStateTV != null) {
//            if (System.currentTimeMillis() - startTime-1000 > MAX_INTERVAL_TIME - COUNT_DOWN) {
//                mStateIV.setVisibility(GONE);
//                mStateDate.setVisibility(INVISIBLE);
//                mStateTime.setVisibility(VISIBLE);
//            } else {
//                mStateTime.setVisibility(GONE);
//                mStateDate.setVisibility(VISIBLE);
//            }
            mStateTV.setText("手指上滑,取消发送");
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isSend = false;
                mFile = getContext().getFilesDir() + "/" + "voice_" + System.currentTimeMillis() + ".mp3";

                setText("松开发送");
                initDialogAndStartRecord();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.setText("按住录音");
                if (y >= 0 && (System.currentTimeMillis() - startTime <= MAX_INTERVAL_TIME)) {
                    Logger.d("结束录音:");
                    if (!isSend) {
                        finishRecord();
                    }

                } else if (y < 0) {  //当手指向上滑，会cancel
                    if (!isSend) {
                        cancelRecord();
                    }
                }
                break;
        }

        return true;
    }

    /**
     * 初始化录音对话框 并 开始录音
     */
    private void initDialogAndStartRecord() {
        startTime = System.currentTimeMillis();
        recordDialog = new Dialog(getContext(), R.style.like_toast_dialog_style);
        // view = new ImageView(getContext());
        view = View.inflate(getContext(), R.layout.dialog_record, null);
        mRootLl = view.findViewById(R.id.rc_audio_ll);
        mStateDate = view.findViewById(R.id.rc_audio_state_date);
        mStateIV = view.findViewById(R.id.rc_audio_state_image);
        mStateTime = view.findViewById(R.id.rc_audio_state_time);
        mStateTV = view.findViewById(R.id.rc_audio_state_text);
        mStateIV.setImageDrawable(getResources().getDrawable(R.drawable.anim_mic));


        anim = (AnimationDrawable) mStateIV.getDrawable();
        anim.start();
        mStateIV.setVisibility(View.VISIBLE);
        //mStateIV.setImageResource(R.drawable.ic_volume_1);
        mStateTV.setVisibility(View.VISIBLE);
        mStateTV.setText("手指上滑,取消发送");
        recordDialog.setContentView(view, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        recordDialog.setOnDismissListener(onDismiss);
        WindowManager.LayoutParams lp = recordDialog.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        startRecording();
        recordDialog.show();

    }

    /**
     * 放开手指，结束录音处理
     */
    private void finishRecord() {

        isSend = true;

        controllDateOpen(false);

        isCancel=false;


        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }

        long intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            Logger.d("录音时间太短");
            mStateIV.setVisibility(VISIBLE);
            mStateDate.setVisibility(INVISIBLE);
            mStateTime.setVisibility(GONE);
            volumeHandler.sendEmptyMessageDelayed(-100, 500);
            //view.setBackgroundResource(R.drawable.ic_voice_cancel);
            mStateIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_wraning));
            mStateTV.setText("录音时间太短");
            anim.stop();
            File file = new File(mFile);
            file.delete();
        /*    stopRecording();
            recordDialog.dismiss();*/
            return;
        } else {
            stopRecording();
            recordDialog.dismiss();
        }
        Logger.d("录音完成的路径:" + mFile);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mFile);
            mediaPlayer.prepare();
            mediaPlayer.getDuration();
            Logger.d("获取到的时长:" + mediaPlayer.getDuration() / 1000);
        } catch (Exception e) {

        }

        File amrFile = new File(mFile);
        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File convertedFile) {
                // So fast? Love it!
                if (finishedListener != null)
                    finishedListener.onFinishedRecord(convertedFile.getPath(), mediaPlayer.getDuration() / 1000);
            }
            @Override
            public void onFailure(Exception error) {
                // Oops! Something went wrong
            }
        };
        AndroidAudioConverter.with(getContext())
                // Your current audio file
                .setFile(amrFile)

                // Your desired audio format
                .setFormat(AudioFormat.MP3)

                // An callback to know when conversion is finished
                .setCallback(callback)

                // Start conversion
                .convert();

    }

    /**
     * 取消录音对话框和停止录音
     */
    public void cancelRecord() {

        isSend = true;

        controllDateOpen(false);

        isCancel=false;

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }

        stopRecording();
        recordDialog.dismiss();
        File file = new File(mFile);
        file.delete();
    }

    //获取类的实例
    // ExtAudioRecorder extAudioRecorder; //压缩的录音（WAV）

    /**
     * 执行录音操作
     */
    //int num = 0 ;
    private void startRecording() {


        if (mHandler != null) {
            mHandler.postDelayed(runnable, 1000);
        }

        controllDateOpen(true);

        if (mRecorder != null) {
            mRecorder.reset();
        } else {
            mRecorder = new MediaRecorder();
        }
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File file = new File(mFile);
        Logger.d("创建文件的路径:" + mFile);
        Logger.d("文件创建成功:" + file.exists());
        mRecorder.setOutputFile(mFile);
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            Logger.d("preparestart异常,重新开始录音:" + e.toString());
            e.printStackTrace();
            mRecorder.release();
            mRecorder = null;
            startRecording();
        }
       /* mThread = new  ObtainDecibelThread();
        mThread.start();*/
    }


    private void stopRecording() {

        if (mThread != null) {
            mThread.exit();
            mThread = null;
        }
        if (mRecorder != null) {
            try {
                mRecorder.stop();//停止时没有prepare，就会报stop failed
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
            } catch (RuntimeException pE) {
                pE.printStackTrace();
            } finally {
                if (recordDialog.isShowing()) {
                    recordDialog.dismiss();
                }
            }
        }

    }

    private class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            Logger.d("检测到的分贝001:");
            while (running) {
                if (mRecorder == null || !running) {
                    break;
                }
                // int x = recorder.getMaxAmplitude(); //振幅
                int db = mRecorder.getMaxAmplitude() / 600;
                Logger.d("检测到的分贝002:" + mRecorder);
                if (db != 0 && y >= 0) {


                    int f = (int) (db / 5);
                    if (f == 0)
                        volumeHandler.sendEmptyMessage(0);
                    else if (f == 1)
                        volumeHandler.sendEmptyMessage(1);
                    else if (f == 2)
                        volumeHandler.sendEmptyMessage(2);
                    else if (f == 3)
                        volumeHandler.sendEmptyMessage(3);
                    else if (f == 4)
                        volumeHandler.sendEmptyMessage(4);
                    else if (f == 5)
                        volumeHandler.sendEmptyMessage(5);
                    else if (f == 6)
                        volumeHandler.sendEmptyMessage(6);
                    else
                        volumeHandler.sendEmptyMessage(7);
                }

                volumeHandler.sendEmptyMessage(-1);
                if (System.currentTimeMillis() - startTime > 20000) {
                    finishRecord();
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private DialogInterface.OnDismissListener onDismiss = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            stopRecording();
        }
    };

    public interface OnFinishedRecordListener {
        public void onFinishedRecord(String audioPath, int time);
    }


    /**
     * 倒计时显示
     */
    private void countDown() {

        //COUNT_DOWN+50 +50预防误差
        mCountDownTimer = new CountDownTimer(COUNT_DOWN+50, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                //计时
                long count = millisUntilFinished / 1000;
                Log.d("CXP_LOG",""+millisUntilFinished);
                mStateTime.setText("" + count);
            }

            @Override
            public void onFinish() {
                //完成
                if (!isSend) {
                    finishRecord();
                }
            }
        };
        mCountDownTimer.start();
    }

    /**
     * 计时器
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startCountTime += 1000;

            if (startCountTime <= (MAX_INTERVAL_TIME - COUNT_DOWN)) {
                if (mStateDate != null) {
                    mStateDate.setText(sdf.format(new Date(startCountTime)));
                }
                mHandler.postDelayed(runnable, 1000);
            } else {
                controllDateOpen(false);
                //倒计时
                countDown();
            }
        }
    };


    /**
     * 控制时间开关
     */
    private void controllDateOpen(boolean open) {
        if (open) {
            mStateDate.setText("00:00");
            mStateDate.setVisibility(VISIBLE);
            mStateIV.setVisibility(VISIBLE);
            mStateTime.setVisibility(GONE);
            mStateTV.setVisibility(VISIBLE);
        } else {

            if (mHandler != null) {
                mHandler.removeCallbacks(runnable);
            }

            startCountTime = 0;
            mStateDate.setVisibility(INVISIBLE);
            if (!isCancel ) {
                mStateIV.setVisibility(GONE);
                mStateTime.setVisibility(VISIBLE);
            } else {
                mStateIV.setVisibility(VISIBLE);
                mStateTime.setVisibility(GONE);
            }
            mStateTV.setVisibility(VISIBLE);
        }
    }

}
