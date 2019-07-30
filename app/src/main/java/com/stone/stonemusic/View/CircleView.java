package com.stone.stonemusic.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.stone.stonemusic.R;

public class CircleView extends View {
    private Paint paint;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setAntiAlias(true);   //设置抗锯齿
        paint.setColor(getResources().getColor(R.color.stoneListTopColor));   //设置画笔颜射
        paint.setStyle(Paint.Style.FILL);   //画笔风格
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*画圆*/
        canvas.drawCircle(this.getWidth()/2,this.getHeight()/2,this.getWidth()/2,paint);
    }
}
