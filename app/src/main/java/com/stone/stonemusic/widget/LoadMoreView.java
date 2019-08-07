package com.stone.stonemusic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.stone.stonemusic.R;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/7 11:42
 * @Description:
 */
public class LoadMoreView extends RelativeLayout {


    public LoadMoreView(Context context) {
        super(context);
        init();
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void init() {
        View.inflate(getContext(), R.layout.item_loadmore, this);
    }

}