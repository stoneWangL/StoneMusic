package com.stone.stonemusic.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.stone.stonemusic.R

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/3 22:30
 * @Description: 悦单界面每个条目的自定义view
 */
class YuedanItemView: RelativeLayout{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    init {
        View.inflate(context, R.layout.item_yuedan, this)
    }
}