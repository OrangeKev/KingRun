package com.king.run.intface;

import com.king.run.view.ObservableScrollView;

/**
 * 作者：shuizi_wade on 2017/9/20 15:15
 * 邮箱：674618016@qq.com
 */
public interface ScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}
