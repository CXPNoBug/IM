package com.cxp.im.fingerprint;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cxp.im.R;

/**
 * 文 件 名: FingerprintDialog
 * 创 建 人: CXP
 * 创建日期: 2020-09-27 10:57
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class FingerprintDialog extends DialogFragment {

    private static FingerprintDialog mDialog;
    private OnDialogActionListener actionListener;
    private TextView tvTip, tvCancel, tvUsepwd;
    private ImageView ivFingerprint;

    public static FingerprintDialog newInstance() {
        if (mDialog == null) {
            synchronized (FingerprintDialog.class) {
                if (mDialog == null) {
                    mDialog = new FingerprintDialog();
                }
            }
        }
        return mDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        View view = inflater.inflate(R.layout.layout_fingerprint_dialog, container);
        ivFingerprint = view.findViewById(R.id.ivFingerprint);
        tvTip = view.findViewById(R.id.tvTip);
        tvUsepwd = view.findViewById(R.id.tvUsepwd);
        tvUsepwd.setOnClickListener(v -> {
            if (actionListener != null)
                actionListener.onUsepwd();
            dismiss();
        });
        tvCancel = view.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(v -> {
            if (actionListener != null)
                actionListener.onCancle();
            dismiss();
        });
        return view;
    }


    public FingerprintDialog setActionListener(OnDialogActionListener actionListener) {
        this.actionListener = actionListener;
        return mDialog;
    }

    /**
     * 根据指纹验证的结果更新tip的文字内容和文字颜色
     *
     * @param tip
     * @param colorId
     */
    public void setTip(String tip, @ColorRes int colorId) {
        tvTip.setText(tip);
        tvTip.setTextColor(getResources().getColor(colorId));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (actionListener != null)
            actionListener.onDismiss();
    }

    public interface OnDialogActionListener {
        void onUsepwd();

        void onCancle();

        void onDismiss();
    }
}
