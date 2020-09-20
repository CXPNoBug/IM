package com.cxp.im.emoji;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.cxp.im.R;

import java.util.List;

/**
 * 文 件 名: EmojiAdapter
 * 创 建 人: CXP
 * 创建日期: 2020-09-20 18:17
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class EmojiAdapter extends BaseQuickAdapter<EmojiBean, BaseViewHolder> {


    public EmojiAdapter(List<EmojiBean> data, int index, int pageSize) {
        super(R.layout.item_emoji, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmojiBean item) {
        //判断是否为最后一个item
        if (item.getId() == 0) {
            helper.setBackgroundResource(R.id.et_emoji, R.mipmap.rc_icon_emoji_delete);
        } else {
            helper.setText(R.id.et_emoji, item.getUnicodeInt());
        }


    }


}

