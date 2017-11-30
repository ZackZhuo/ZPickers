package com.zhz.pickers.util;

import java.util.Map;

/**
 * Created by Zack_zhuo on 2017/8/29.
 */

public interface ItemTouchListener<T> {

    void onItemClick(Map<String, T> map, int position);
    void onItemClick(T date, int position);
}
