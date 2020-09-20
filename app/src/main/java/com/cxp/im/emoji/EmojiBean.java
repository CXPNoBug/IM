package com.cxp.im.emoji;

/**
 * 文 件 名: EmojiBean
 * 创 建 人: CXP
 * 创建日期: 2020-09-20 18:15
 * 描    述: 表情的实体类
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class EmojiBean {

    private int id;
    private int unicodeInt;

    public String getEmojiString() {
        return getEmojiStringByUnicode(unicodeInt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnicodeInt() {
        return getEmojiStringByUnicode(unicodeInt);
    }

    public void setUnicodeInt(int unicodeInt) {
        this.unicodeInt = unicodeInt;
    }

    public static String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    @Override
    public String toString() {
        return "EmojiBean{" +
                "id=" + id +
                ", unicodeInt=" + unicodeInt +
                '}';
    }

}
