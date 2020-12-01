package com.cxp.im.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cxp.im.R;
import com.cxp.im.adapter.ChatAdapter;
import com.cxp.im.bean.AudioMsgBody;
import com.cxp.im.bean.FileMsgBody;
import com.cxp.im.bean.ImageMsgBody;
import com.cxp.im.bean.Message;
import com.cxp.im.bean.MsgSendStatus;
import com.cxp.im.bean.MsgType;
import com.cxp.im.bean.TextMsgBody;
import com.cxp.im.bean.VideoMsgBody;
import com.cxp.im.utils.AppUtils;
import com.cxp.im.utils.ChatUiHelper;
import com.cxp.im.utils.FileUtils;
import com.cxp.im.utils.PictureFileUtil;
import com.cxp.im.widget.MediaManager;
import com.cxp.im.widget.RecordButton;
import com.cxp.im.widget.StateButton;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.orhanobut.logger.Logger;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 1000;
    private static final int REQUEST_CODE_VEDIO = 1001;
    private static final int REQUEST_CODE_FILE = 1002;

    @BindView(R.id.rv_chat_list)
    RecyclerView mRvChatList;
    @BindView(R.id.swipe_chat)
    SmartRefreshLayout mSwipeChat;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.ivEmo)
    ImageView mIvEmo;
    @BindView(R.id.ivAdd)
    ImageView mIvAdd;
    @BindView(R.id.btn_send)
    StateButton mBtnSend;//发送按钮
    @BindView(R.id.ivAudio)
    ImageView mIvAudio;//录音图片
    @BindView(R.id.btnAudio)
    RecordButton mBtnAudio;//录音按钮
    @BindView(R.id.rlEmotion)
    LinearLayout mLlEmotion;//表情布局
    @BindView(R.id.llAdd)
    LinearLayout mLlAdd;//添加布局
    @BindView(R.id.llContent)
    LinearLayout mLlContent;
    @BindView(R.id.bottom_layout)
    RelativeLayout mBottomLayout;
    @BindView(R.id.new_head_title)
    TextView mNewHeadTitle;

    private ChatAdapter mAdapter;

    private ImageView ivAudio;

    private int mCurrentPosition = -1;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        //初始化试图
        initView();
    }

    /**
     * 初始化试图
     */
    private void initView() {
        ButterKnife.bind(this);

        mContext = this;

        mNewHeadTitle.setText("消息");

        mAdapter = new ChatAdapter(new ArrayList<Message>());
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);
        mRvChatList.setLayoutManager(mLinearLayout);
        mRvChatList.setAdapter(mAdapter);

        mSwipeChat.setEnableAutoLoadMore(false);
        mSwipeChat.setEnableRefresh(true);
        mSwipeChat.setEnableLoadMore(false);
        mSwipeChat.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //下拉刷新

                //下拉刷新模拟获取历史消息
                List<Message> mReceiveMsgList = new ArrayList<Message>();
                //构建文本消息
                Message mMessgaeText = getBaseReceiveMessage(MsgType.TEXT);
                TextMsgBody mTextMsgBody = new TextMsgBody();
                mTextMsgBody.setMessage("收到的消息");
                mMessgaeText.setBody(mTextMsgBody);
                mReceiveMsgList.add(mMessgaeText);
                //构建图片消息
                Message mMessgaeImage = getBaseReceiveMessage(MsgType.IMAGE);
                ImageMsgBody mImageMsgBody = new ImageMsgBody();
                mImageMsgBody.setThumbUrl("https://c-ssl.duitang.com/uploads/item/201208/30/20120830173930_PBfJE.thumb.700_0.jpeg");
                mMessgaeImage.setBody(mImageMsgBody);
                mReceiveMsgList.add(mMessgaeImage);
                //构建文件消息
                Message mMessgaeFile = getBaseReceiveMessage(MsgType.FILE);
                FileMsgBody mFileMsgBody = new FileMsgBody();
                mFileMsgBody.setDisplayName("收到的文件");
                mFileMsgBody.setSize(12);
                mMessgaeFile.setBody(mFileMsgBody);
                mReceiveMsgList.add(mMessgaeFile);
                mAdapter.addData(0, mReceiveMsgList);
                mSwipeChat.finishRefresh();
            }
        });
        mSwipeChat.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                //上拉加载
            }
        });

        //初始化聊天UI
        initChatUi();

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                final boolean isSend = mAdapter.getItem(position).getSenderId().equals(AppUtils.mSenderId);
                if (mCurrentPosition == position) {

                    if (MediaManager.isPlaying()) {
                        //释放语音
                        releaseSound(isSend);
                    } else {
                        //播放语音
                        playSound(isSend, view, position);
                    }
                } else {
                    if (mCurrentPosition != -1) {
                        //释放语音
                        releaseSound(isSend);
                    }
                    //播放语音
                    playSound(isSend, view, position);
                }
                mCurrentPosition = position;

            }
        });

    }

    /**
     * 播放语音
     */
    private void playSound(boolean isSend, View view, int position) {
        ivAudio = view.findViewById(R.id.ivAudio);
        MediaManager.reset();
        if (isSend) {
            ivAudio.setBackgroundResource(R.drawable.audio_animation_right_list);
        } else {
            ivAudio.setBackgroundResource(R.drawable.audio_animation_left_list);
        }
        AnimationDrawable drawable = (AnimationDrawable) ivAudio.getBackground();
        drawable.start();
        MediaManager.playSound(mContext, ((AudioMsgBody) mAdapter.getData().get(position).getBody()).getLocalPath(), new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (isSend) {
                    ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
                } else {
                    ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
                }
                MediaManager.release();
            }
        });
    }

    /**
     * 释放语音
     */
    private void releaseSound(boolean isSend) {
        if (isSend) {
            if (ivAudio != null) {
                ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
            }
        } else {
            if (ivAudio != null) {
                ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
            }
        }
        ivAudio = null;
        MediaManager.reset();
    }

    /**
     * 初始化聊天UI
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initChatUi() {
        final ChatUiHelper mUiHelper = ChatUiHelper.with(this);
        mUiHelper.bindContentLayout(mLlContent) //绑定整体界面布局
                .bindAudioIv(mIvAudio) //绑定语音图片点击事件
                .bindAudioBtn(mBtnAudio) //绑定语音按钮点击事件
                .bindToSendButton(mBtnSend) //绑定发送按钮
                .bindEditText(mEtContent) //绑定输入框
                .bindBottomLayout(mBottomLayout) //绑定底部布局
                .bindEmojiLayout(mLlEmotion) //绑定表情布局
                .bindAddLayout(mLlAdd) //绑定添加布局
                .bindToEmojiButton(mIvEmo) //绑定表情按钮点击事件
                .bindToAddButton(mIvAdd) //绑定底部加号按钮
                .bindEmojiData(); //绑定表情数据

        //点击空白区域关闭键盘
        mRvChatList.setOnTouchListener((view, event) -> {
            mUiHelper.hideBottomLayout(false);
            mUiHelper.hideSoftInput();
            mEtContent.clearFocus();
            mIvEmo.setImageResource(R.mipmap.ic_emoji);
            return false;
        });

        //点击空白区域关闭键盘
        mRvChatList.setOnTouchListener((view, event) -> {
            mUiHelper.hideBottomLayout(false);
            mUiHelper.hideSoftInput();
            mEtContent.clearFocus();
            mIvEmo.setImageResource(R.mipmap.ic_emoji);
            return false;
        });

        //录音结束回调
        mBtnAudio.setOnFinishedRecordListener((audioPath, time) -> {
            Logger.d("录音结束回调");
            File file = new File(audioPath);
            if (file.exists()) {
                sendAudioMessage(audioPath, time);
            }
        });
    }

    @OnClick({R.id.new_head_back_ll, R.id.btn_send, R.id.rlPhoto, R.id.rlVideo, R.id.rlFile, R.id.rlLocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.new_head_back_ll:
                finish();
                break;
            case R.id.btn_send:
                String content = mEtContent.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    sendTextMsg(content);
                    mEtContent.setText("");
                }
                break;
            case R.id.rlPhoto:
                //选择图片
                PictureFileUtil.openGalleryPic(ChatActivity.this, REQUEST_CODE_IMAGE);
                break;
            case R.id.rlVideo:
                //选择视频
                PictureFileUtil.openGalleryAudio(ChatActivity.this, REQUEST_CODE_VEDIO);
                break;
            case R.id.rlFile:
                //选择文件
                PictureFileUtil.openFile(ChatActivity.this, REQUEST_CODE_FILE);
                break;
            case R.id.rlLocation:
                break;
        }
    }

    //文本消息
    private void sendTextMsg(String hello) {
        final Message mMessgae = getBaseSendMessage(MsgType.TEXT);
        TextMsgBody mTextMsgBody = new TextMsgBody();
        mTextMsgBody.setMessage(hello);
        mMessgae.setBody(mTextMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        //模拟两秒后发送成功
        updateMsg(mMessgae);
    }

    //图片消息
    private void sendImageMessage(final LocalMedia media) {
        final Message mMessgae = getBaseSendMessage(MsgType.IMAGE);
        ImageMsgBody mImageMsgBody = new ImageMsgBody();
        mImageMsgBody.setThumbUrl(media.getCompressPath());
        mMessgae.setBody(mImageMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        //模拟两秒后发送成功
        updateMsg(mMessgae);
    }

    /**
     * 发送语音消息
     */
    private void sendAudioMessage(final String path, int time) {
        final Message mMessgae = getBaseSendMessage(MsgType.AUDIO);
        AudioMsgBody mFileMsgBody = new AudioMsgBody();
        mFileMsgBody.setLocalPath(path);
        mFileMsgBody.setDuration(time);
        mMessgae.setBody(mFileMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        //模拟两秒后发送成功
        updateMsg(mMessgae);
    }

    //视频消息
    private void sendVedioMessage(final LocalMedia media) {
        final Message mMessgae = getBaseSendMessage(MsgType.VIDEO);
        //生成缩略图路径
        String vedioPath = media.getPath();
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(vedioPath);
        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime();
        String imgname = System.currentTimeMillis() + ".jpg";
        String urlpath = Environment.getExternalStorageDirectory() + "/" + imgname;
        File f = new File(urlpath);
        try {
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Logger.d("视频缩略图路径获取失败：" + e.toString());
            e.printStackTrace();
        }
        VideoMsgBody mImageMsgBody = new VideoMsgBody();
        mImageMsgBody.setExtra(urlpath);
        mMessgae.setBody(mImageMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        //模拟两秒后发送成功
        updateMsg(mMessgae);

    }

    //文件消息
    private void sendFileMessage(final String path) {
        final Message mMessgae = getBaseSendMessage(MsgType.FILE);
        FileMsgBody mFileMsgBody = new FileMsgBody();
        mFileMsgBody.setLocalPath(path);
        mFileMsgBody.setDisplayName(FileUtils.getFileName(path));
        mFileMsgBody.setSize(FileUtils.getFileLength(path));
        mMessgae.setBody(mFileMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        //模拟两秒后发送成功
        updateMsg(mMessgae);

    }

    /**
     * 获取发送消息公共数据
     *
     * @param msgType 消息类型
     * @return
     */
    private Message getBaseSendMessage(MsgType msgType) {
        Message mMessgae = new Message();
        mMessgae.setUuid(UUID.randomUUID() + "");
        mMessgae.setSenderId(AppUtils.mSenderId);
        mMessgae.setTargetId(AppUtils.mTargetId);
        mMessgae.setSentTime(System.currentTimeMillis());
        mMessgae.setSentStatus(MsgSendStatus.SENDING);
        mMessgae.setMsgType(msgType);
        return mMessgae;
    }

    /**
     * 获取接收消息公共数据
     *
     * @param msgType 消息类型
     * @return
     */
    private Message getBaseReceiveMessage(MsgType msgType) {
        Message mMessgae = new Message();
        mMessgae.setUuid(UUID.randomUUID() + "");
        mMessgae.setSenderId(AppUtils.mTargetId);
        mMessgae.setTargetId(AppUtils.mSenderId);
        mMessgae.setSentTime(System.currentTimeMillis());
        mMessgae.setSentStatus(MsgSendStatus.SENDING);
        mMessgae.setMsgType(msgType);
        return mMessgae;
    }

    private void updateMsg(final Message mMessgae) {
        mRvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
        //模拟2秒后发送成功
        new Handler().postDelayed(new Runnable() {
            public void run() {
                int position = 0;
                mMessgae.setSentStatus(MsgSendStatus.SENT);
                //更新单个子条目
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    Message mAdapterMessage = mAdapter.getData().get(i);
                    if (mMessgae.getUuid().equals(mAdapterMessage.getUuid())) {
                        position = i;
                    }
                }
                mAdapter.notifyItemChanged(position);
            }
        }, 2000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FILE:
                    String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    Logger.d("获取到的文件路径:" + filePath);
                    sendFileMessage(filePath);
                    break;
                case REQUEST_CODE_IMAGE:
                    // 图片选择结果回调
                    List<LocalMedia> selectListPic = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectListPic) {
                        Logger.d("获取图片路径成功:" + media.getPath());
                        sendImageMessage(media);
                    }
                    break;
                case REQUEST_CODE_VEDIO:
                    // 视频选择结果回调
                    List<LocalMedia> selectListVideo = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectListVideo) {
                        Logger.d("获取视频路径成功:" + media.getPath());
                        sendVedioMessage(media);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.reset();
    }
}
