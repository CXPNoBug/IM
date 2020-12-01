package com.cxp.im.record;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cxp.im.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 压缩对话框
 */
public class CompressDialog extends Dialog {
    @BindView(R.id.progress_horizontal)
    ProgressBar progressHorizontal;
    @BindView(R.id.rl_compressing)
    RelativeLayout rlCompressing;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.ll_compress_failed)
    LinearLayout llCompressFailed;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    public CompressDialog(@NonNull Context context) {
        super(context);
    }

    public CompressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CompressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_diy_record_compress);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initEvent();
    }


    private void initEvent() {
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesOnclick();
                }
            }
        });

    }


    public void showLayout(boolean isFail) {
        if (isFail) {
            rlCompressing.setVisibility(View.GONE);
            llCompressFailed.setVisibility(View.VISIBLE);
        } else {
            rlCompressing.setVisibility(View.VISIBLE);
            llCompressFailed.setVisibility(View.GONE);
        }
    }

    public void setProgress(int progress) {
        tvProgress.setText(progress + "%");
        progressHorizontal.setProgress(progress);
    }

    public void setYesOnclickListener(onYesOnclickListener yesOnclickListener) {
        this.yesOnclickListener = yesOnclickListener;
    }


    public interface onYesOnclickListener {
        public void onYesOnclick();
    }


}
