package com.cxp.im.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cxp.im.R;
import com.cxp.im.widget.RecordButton;

/**
 * 文 件 名: ChatUiHelper
 * 创 建 人: CXP
 * 创建日期: 2020-09-18 10:03
 * 描    述: 聊天UI工具类
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class ChatUiHelper {

    private static final String SHARE_PREFERENCE_NAME = "ChatUiHelper";
    private static final String SHARE_PREFERENCE_TAG = "soft_input_height";

    private static final String NAVIGATION = "navigationBarBackground";

    private Activity mActivity;
    private LinearLayout mContentLayout;//整体界面布局
    private RelativeLayout mBottomLayout;//底部布局
    private LinearLayout mEmojiLayout;//表情布局
    private LinearLayout mAddLayout;//添加布局
    private Button mSendBtn;//发送按钮
    private View mAddButton;//加号按钮
    private Button mAudioButton;//录音按钮
    private ImageView mAudioIv;//录音图片

    private EditText mEditText;
    private InputMethodManager mInputManager;
    private SharedPreferences mSp;
    private ImageView mIvEmoji;

    public ChatUiHelper() {
    }

    public static ChatUiHelper with(Activity activity) {
        ChatUiHelper helper = new ChatUiHelper();
        helper.mActivity = activity;
        helper.mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        helper.mSp = activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return helper;
    }

    /**
     * 绑定整体界面布局
     */
    public ChatUiHelper bindContentLayout(LinearLayout bottomLayout) {
        mContentLayout = bottomLayout;
        return this;
    }

    /**
     * 绑定输入框
     */
    @SuppressLint("ClickableViewAccessibility")
    public ChatUiHelper bindEditText(EditText editText) {
        mEditText = editText;
        mEditText.requestFocus();
        mEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && mBottomLayout.isShown()) {
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideBottomLayout(true);//隐藏表情布局，显示软件盘
                    mIvEmoji.setImageResource(R.mipmap.ic_emoji);
                    //软件盘显示后，释放内容高度
                    mEditText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            unlockContentHeightDelayed();
                        }
                    }, 200L);
                }
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEditText.getText().toString().trim().length() > 0) {
                    mSendBtn.setVisibility(View.VISIBLE);
                    mAddButton.setVisibility(View.GONE);
                } else {
                    mSendBtn.setVisibility(View.GONE);
                    mAddButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return this;
    }

    /**
     * 绑定底部布局
     */
    public ChatUiHelper bindBottomLayout(RelativeLayout bottomLayout) {
        mBottomLayout = bottomLayout;
        return this;
    }

    /**
     * 绑定表情布局
     */
    public ChatUiHelper bindEmojiLayout(LinearLayout emojiLayout) {
        mEmojiLayout = emojiLayout;
        return this;
    }

    /**
     * 绑定添加布局
     */
    public ChatUiHelper bindAddLayout(LinearLayout addLayout) {
        mAddLayout = addLayout;
        return this;
    }

    /**
     * 绑定发送按钮
     */
    public ChatUiHelper bindToSendButton(Button sendbtn) {
        mSendBtn = sendbtn;
        return this;
    }

    /**
     * 绑定语音按钮点击事件
     */
    public ChatUiHelper bindAudioBtn(RecordButton audioBtn) {
        mAudioButton = audioBtn;
        return this;
    }

    /**
     * 绑定语音图片点击事件
     */
    public ChatUiHelper bindAudioIv(ImageView audioIv) {
        mAudioIv = audioIv;
        audioIv.setOnClickListener(view -> {
            //如果录音按钮显示
            if (mAudioButton.isShown()) {
                hideAudioButton();
                mEditText.requestFocus();
                showSoftInput();
            } else {
                mEditText.clearFocus();
                showAudioButton();
                hideEmotionLayout();
                hideMoreLayout();
            }
        });
        return this;
    }

    //绑定表情按钮点击事件
    public ChatUiHelper bindToEmojiButton(ImageView emojiBtn) {
        mIvEmoji = emojiBtn;
        return this;
    }

    /**
     * 显示录音按钮
     */
    private void showAudioButton() {
        mAudioButton.setVisibility(View.VISIBLE);
        mEditText.setVisibility(View.GONE);
        mAudioIv.setImageResource(R.mipmap.ic_keyboard);
        if (mBottomLayout.isShown()) {
            hideBottomLayout(false);
        } else {
            hideSoftInput();
        }
    }

    /**
     * 隐藏录音按钮
     */
    private void hideAudioButton() {
        mAudioButton.setVisibility(View.GONE);
        mEditText.setVisibility(View.VISIBLE);
        mAudioIv.setImageResource(R.mipmap.ic_audio);
    }

    /**
     * 显示底部布局
     */
    private void showBottomLayout() {
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = mSp.getInt(SHARE_PREFERENCE_TAG, dip2Px(270));
        }
        hideSoftInput();
        mBottomLayout.getLayoutParams().height = softInputHeight;
        mBottomLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏底部布局
     *
     * @param showSoftInput 是否显示软件盘
     */
    public void hideBottomLayout(boolean showSoftInput) {
        if (mBottomLayout.isShown()) {
            mBottomLayout.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }

    /**
     * 显示表情布局
     */
    private void showEmotionLayout() {
        mEmojiLayout.setVisibility(View.VISIBLE);
        mIvEmoji.setImageResource(R.mipmap.ic_keyboard);
    }

    /**
     * 隐藏表情布局
     */
    private void hideEmotionLayout() {
        mEmojiLayout.setVisibility(View.GONE);
        mIvEmoji.setImageResource(R.mipmap.ic_emoji);
    }

    /**
     * 显示更多布局
     */
    private void showMoreLayout() {
        mAddLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏更多布局
     */
    private void hideMoreLayout() {
        mAddLayout.setVisibility(View.GONE);
    }


    /**
     * 显示软键盘
     */
    public void showSoftInput() {
        mEditText.requestFocus();
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                mInputManager.showSoftInput(mEditText, 0);
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 底部虚拟按键栏的高度
     */
    public int getNavigationHeight() {
        if (mActivity == null) {
            return 0;
        }
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        int height = 0;
        if (resourceId > 0) {
            //获取NavigationBar的高度
            height = resources.getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * 是否存在虚拟按键栏
     * 该方法需要在View完全被绘制出来之后调用，否则判断不了
     * 在比如 onWindowFocusChanged（）方法中可以得到正确的结果
     */
    private boolean isNavigationBarExist() {
        ViewGroup vp = (ViewGroup) mActivity.getWindow().getDecorView();
        if (vp != null) {
            for (int i = 0; i < vp.getChildCount(); i++) {
                vp.getChildAt(i).getContext().getPackageName();
                if (vp.getChildAt(i).getId() != View.NO_ID &&
                        NAVIGATION.equals(mActivity.getResources().getResourceEntryName(vp.getChildAt(i).getId()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取键盘高度
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();

        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        //是否存在虚拟按键栏
        if (isNavigationBarExist()) {
            softInputHeight = softInputHeight - getNavigationHeight();
        }
        //存一份到本地
        if (softInputHeight > 0) {
            mSp.edit().putInt(SHARE_PREFERENCE_TAG, softInputHeight).apply();
        }
        return softInputHeight;
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentHeight() {
        //将weight置0，然后将height设置为当前的height，在父控件（LinearLayout）的高度变化时它的高度也不再会变化。
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentLayout.getLayoutParams();
        params.height = mContentLayout.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    private void unlockContentHeightDelayed() {
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentLayout.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }


    /**
     * dp转px
     */
    public int dip2Px(int dip) {
        float density = mActivity.getApplicationContext().getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }
}
