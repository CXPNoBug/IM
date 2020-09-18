package com.cxp.im.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.cxp.im.R;
import com.cxp.im.bean.AudioMsgBody;
import com.cxp.im.bean.FileMsgBody;
import com.cxp.im.bean.ImageMsgBody;
import com.cxp.im.bean.Message;
import com.cxp.im.bean.MsgBody;
import com.cxp.im.bean.MsgSendStatus;
import com.cxp.im.bean.TextMsgBody;
import com.cxp.im.bean.VideoMsgBody;
import com.cxp.im.utils.AppUtils;
import com.cxp.im.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 * 文 件 名: ChatAdapter
 * 创 建 人: CXP
 * 创建日期: 2020-09-18 14:45
 * 描    述: 聊天适配器
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class ChatAdapter extends BaseMultiItemQuickAdapter<Message, BaseViewHolder> {

    private static final int SEND_TEXT = R.layout.item_text_send;  //发送文本消息布局
    private static final int RECEIVE_TEXT = R.layout.item_text_receive; //接收文本消息布局
    private static final int SEND_IMAGE = R.layout.item_image_send; //发送图片消息布局
    private static final int RECEIVE_IMAGE = R.layout.item_image_receive; //接收图片消息布局
    private static final int SEND_VIDEO = R.layout.item_video_send; //发送视频消息布局
    private static final int RECEIVE_VIDEO = R.layout.item_video_receive; //接收视频消息布局
    private static final int SEND_FILE = R.layout.item_file_send; //发送文件消息布局
    private static final int RECEIVE_FILE = R.layout.item_file_receive; //接收文件消息布局
    private static final int SEND_AUDIO = R.layout.item_audio_send; //发送音频消息布局
    private static final int RECEIVE_AUDIO = R.layout.item_audio_receive; //接收音频消息布局


    public ChatAdapter(List<Message> data) {
        super(data);
        addItemType(Message.TYPE_SEND_TEXT, SEND_TEXT);
        addItemType(Message.TYPE_RECEIVE_TEXT, RECEIVE_TEXT);
        addItemType(Message.TYPE_SEND_IMAGE, SEND_IMAGE);
        addItemType(Message.TYPE_RECEIVE_IMAGE, RECEIVE_IMAGE);
        addItemType(Message.TYPE_SEND_VIDEO, SEND_VIDEO);
        addItemType(Message.TYPE_RECEIVE_VIDEO, RECEIVE_VIDEO);
        addItemType(Message.TYPE_SEND_FILE, SEND_FILE);
        addItemType(Message.TYPE_RECEIVE_FILE, RECEIVE_FILE);
        addItemType(Message.TYPE_SEND_AUDIO, SEND_AUDIO);
        addItemType(Message.TYPE_RECEIVE_AUDIO, RECEIVE_AUDIO);

    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Message message) {

        //设置内容
        setContent(baseViewHolder, message);

        //设置状态
        setStatus(baseViewHolder, message);

        //设置点击
        setOnClick(message);

    }

    /**
     * 设置内容
     */
    private void setContent(BaseViewHolder helper, Message item) {
        switch (item.getMsgType()) {
            case TEXT:
                //文本消息
                TextMsgBody textMsgBody = (TextMsgBody) item.getBody();
                helper.setText(R.id.chat_item_content_text, textMsgBody.getMessage());
                break;
            case IMAGE:
                //图片消息
                ImageMsgBody imageMsgBody = (ImageMsgBody) item.getBody();
                if (TextUtils.isEmpty(imageMsgBody.getThumbUrl())) {
                    GlideUtils.loadChatImage(getContext(), imageMsgBody.getThumbUrl(), helper.getView(R.id.bivPic));
                } else {
                    File file = new File(imageMsgBody.getThumbPath());
                    if (file.exists()) {
                        GlideUtils.loadChatImage(getContext(), imageMsgBody.getThumbPath(), helper.getView(R.id.bivPic));
                    } else {
                        GlideUtils.loadChatImage(getContext(), imageMsgBody.getThumbUrl(), helper.getView(R.id.bivPic));
                    }
                }
                break;
            case VIDEO:
                //视频消息
                VideoMsgBody videoMsgBody = (VideoMsgBody) item.getBody();
                File file = new File(videoMsgBody.getExtra());
                if (file.exists()) {
                    GlideUtils.loadChatImage(getContext(), videoMsgBody.getExtra(), helper.getView(R.id.bivPic));
                } else {
                    GlideUtils.loadChatImage(getContext(), videoMsgBody.getExtra(), helper.getView(R.id.bivPic));
                }
                break;
            case FILE:
                //文件消息
                FileMsgBody fileMsgBody = (FileMsgBody) item.getBody();
                helper.setText(R.id.msg_tv_file_name, fileMsgBody.getDisplayName());
                helper.setText(R.id.msg_tv_file_size, fileMsgBody.getSize() + "B");
                break;
            case AUDIO:
                //语音消息
                AudioMsgBody audioMsgBody = (AudioMsgBody) item.getBody();
                helper.setText(R.id.tvDuration, audioMsgBody.getDuration() + "\"");
                break;

        }
    }

    /**
     * 设置状态
     */
    private void setStatus(BaseViewHolder helper, Message item) {
        MsgBody msgContent = item.getBody();
        if (msgContent instanceof TextMsgBody
                || msgContent instanceof AudioMsgBody
                || msgContent instanceof VideoMsgBody
                || msgContent instanceof FileMsgBody
                || msgContent instanceof ImageMsgBody) {
            //只需要设置自己发送的状态
            boolean isSend = item.getSenderId().equals(AppUtils.mSenderId);
            if (isSend) {
                MsgSendStatus sentStatus = item.getSentStatus();
                if (sentStatus == MsgSendStatus.SENDING) {
                    helper.setVisible(R.id.chat_item_progress, true).setVisible(R.id.chat_item_fail, false);
                } else if (sentStatus == MsgSendStatus.FAILED) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, true);
                } else if (sentStatus == MsgSendStatus.SENT) {
                    helper.setVisible(R.id.chat_item_progress, false).setVisible(R.id.chat_item_fail, false);
                }
            }
        }
    }

    /**
     * 注册点击
     */
    private void setOnClick(Message item) {
        MsgBody msgContent = item.getBody();
        if (msgContent instanceof AudioMsgBody) {
            addChildClickViewIds(R.id.rlAudio);
        }
    }
}
