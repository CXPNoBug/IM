package com.cxp.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cxp.im.R;
import com.cxp.im.listener.OnItemClickListener;
import com.cxp.im.utils.AppUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文 件 名: SmsGroupSendListAdapter
 * 创 建 人: CXP
 * 创建日期: 2020-09-24 18:35
 * 描    述: 短信群发
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SmsGroupSendListAdapter extends RecyclerView.Adapter<SmsGroupSendListAdapter.SmsGroupSendViewHolder> {

    private Context mContext;
    private List<Map<String, Object>> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public SmsGroupSendListAdapter(Context context, List<Map<String, Object>> datas, OnItemClickListener onItemClickListener) {
        mContext = context;
        mDatas = datas;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SmsGroupSendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SmsGroupSendViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_sms_group_send_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SmsGroupSendViewHolder holder, int position) {

        Map<String, Object> map= mDatas.get(position);
        if (map!=null) {
            //收件人不等于空
            if (AppUtils.notIsEmpty(map.get("people"))) {
                holder.mItemSmsGroupSendListPeople.setText(map.get("people").toString());
            }
            //信息内容不等于空
            if (AppUtils.notIsEmpty(map.get("content"))) {
                holder.mItemSmsGroupSendListContent.setText(map.get("content").toString());
            }
            //发送时间不等于空
            if (AppUtils.notIsEmpty(map.get("time"))) {
                holder.mItemSmsGroupSendListTime.setText(map.get("time").toString());
            }
        }

        holder.mItemSmsGroupSendListRoot.setOnClickListener(view -> mOnItemClickListener.itemClickListener(view,position));
    }


    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    static class SmsGroupSendViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_sms_group_send_list_people)
        TextView mItemSmsGroupSendListPeople;
        @BindView(R.id.item_sms_group_send_list_content)
        TextView mItemSmsGroupSendListContent;
        @BindView(R.id.item_sms_group_send_list_time)
        TextView mItemSmsGroupSendListTime;
        @BindView(R.id.item_sms_group_send_list_root)
        LinearLayout mItemSmsGroupSendListRoot;

        public SmsGroupSendViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
