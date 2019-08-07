package com.stone.stonemusic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.stonemusic.R;
import com.stone.stonemusic.model.PlayListBean;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/7 11:29
 * @Description:
 */
public class GeDanItemView extends RelativeLayout {
    View view;
    private TextView title, description;
    private ImageView bg;

    /**
     * 刷新条目view数据
     */
    public void setData(PlayListBean data) {
        //歌单标题
        title.setText(data.getName());
        //歌单描述
        description.setText(data.getDescription());
        //背景图
        Glide.with(getContext()).load(data.getCoverImgUrl()).into(bg);
    }

    public GeDanItemView(Context context) {
        super(context);
        init();
    }

    public GeDanItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GeDanItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void init() {
        view = View.inflate(getContext(), R.layout.item_yuedan, this);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        bg = view.findViewById(R.id.bg);

    }

}
