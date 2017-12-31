package com.zhgtrade.deal.util;

import com.zhgtrade.deal.model.FentrustData;

import java.util.Comparator;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-05-11 10:53
 */
public class FentrustComparator {

    public final static Comparator<FentrustData> prizeComparatorASC = (o1, o2) -> {
        boolean flag = o1.getFid() == o2.getFid() && o1.getFid() != 0;
        if (flag) {
            return 0;
        }
        int ret = o1.getFprize().compareTo(o2.getFprize());
        if (ret == 0) {
            return o1.getFid().compareTo(o2.getFid());
        } else {
            return ret;
        }
    };

    public final static Comparator<FentrustData> prizeComparatorDESC = (o1, o2) -> {
        boolean flag = o1.getFid().intValue() == o2.getFid().intValue() && o1.getFid().intValue() != 0;
        if (flag) {
            return 0;
        }
        int ret = o2.getFprize().compareTo(o1.getFprize());
        if (ret == 0) {
            return o1.getFid().compareTo(o2.getFid());
        } else {
            return ret;
        }
    };

}
