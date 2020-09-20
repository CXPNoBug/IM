package com.cxp.im.emoji;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * 文 件 名: EmojiVpAdapter
 * 创 建 人: CXP
 * 创建日期: 2020-09-20 18:18
 * 描    述: 表情ViewPager
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class EmojiVpAdapter extends PagerAdapter {

    private List<View> mViewList;
    public EmojiVpAdapter(List<View> mViewList) {
        this.mViewList = mViewList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return (mViewList.get(position));
    }

    @Override
    public int getCount() {
        if (mViewList == null)
            return 0;
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
