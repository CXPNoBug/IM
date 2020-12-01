package com.cxp.im.other;

import java.io.Serializable;
import java.util.Map;

/**
 * 文 件 名: SerializableMap
 * 创 建 人: CXP
 * 创建日期: 2017-09-22 17:12
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class SerializableMap implements Serializable {

    private Map<String,Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
