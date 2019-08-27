package com.stone.stonemusic.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.stone.stonemusic.R;
import com.stone.stonemusic.model.LrcContent;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stoneWang
 * @CreateDate: 2019/8/26 16:37
 * @Description: 自定义歌词View
 */
public class LyricView extends View {
    private static final String TAG = "LyricView";
    private Paint paint; //画笔
    private int viewW = 0; //当前view宽度
    private int viewH = 0; //当前view高度
    float bigSize = 0f; //大号字体
    float smallSize = 0f; //小号字体
    int white = 0; //大白
    int halfWhite = 0; //半白
    int centerLineNum = 10; //居中行号
    int curLineNum = 0; //当前行号
    int lineHeight = 0; //行高
    List<LrcContent> lrcContentList = new ArrayList<>(); //歌词list
    int duration = 0; //当前播放歌曲总时长
    int progress = 0; //当前播放进度


    public List<LrcContent> getLrcContentList() {
        return lrcContentList;
    }

    public void setLrcContentList(List<LrcContent> lrcContentList) {
        this.lrcContentList.clear(); //先清空
        this.lrcContentList = lrcContentList;
//        refreshThisView(); //刷新
    }


    public LyricView(Context context) {
        super(context);
        init();
    }

    public LyricView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);    //设置抗锯齿
        paint.setTextAlign(Paint.Align.CENTER); //设置文本居中,在x方向确定位置是通过中间位置确定坐标

        bigSize = getResources().getDimension(R.dimen.bigSize);
        smallSize = getResources().getDimension(R.dimen.smallSize);
        white = getResources().getColor(R.color.white);
        halfWhite = getResources().getColor(R.color.halfWhite);
        lineHeight = getResources().getDimensionPixelOffset(R.dimen.lineHeight);

//        //模拟歌词初始化
//        for(int i = 0; i < 30; i++) {
//            LrcContent lrcContent = new LrcContent();
//            lrcContent.setLrcTime(2000*i);
//            lrcContent.setLrcStr("正在播放第" + i + "行歌词");
//            lrcContentList.add(lrcContent);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null ==lrcContentList || lrcContentList.size() == 0) {
            //歌词没有加载
            drawSingleLine(canvas);
        } else {
            //歌词已经加载
            drawMuchLine(canvas);
        }

    }

    /**
     * 绘制多行居中文本
     * @param canvas
     */
    private void drawMuchLine(Canvas canvas) {
//        //防止lrcContentList为空
//        if (null == lrcContentList) return;
//        //防止下标越界
//        if (curLineNum < 0
//                || curLineNum > (lrcContentList.size() - 1)
//                || centerLineNum > (lrcContentList.size() - 1) ) return;

        try{
            //求居中行偏移y
            int lineTime = 0; //行可用时间：

            //最后一行居中：
            if (centerLineNum == lrcContentList.size() - 1) {
                //行可用时间 = duration - 最后一行开始时间
                lineTime = duration - lrcContentList.get(centerLineNum).getLrcTime();
            } else {
                //其他行居中：
                //行可用时间 = 下一行开始时间 - 居中行开始时间
                int centerS = lrcContentList.get(curLineNum+1).getLrcTime(); //下一行开始时间
                int nextS = lrcContentList.get(curLineNum).getLrcTime(); //居中行开始时间
                lineTime = centerS - nextS;
            }

            //偏移时间 = progress - 居中行开始时间
            int offsetTime = progress - lrcContentList.get(centerLineNum).getLrcTime();

            //偏移百分比 = 偏移时间/行可用时间
            float offsetPercent = offsetTime / (float) lineTime;

            //偏移y = 偏移百分比 * 行高
            int offsetY = (int) (offsetPercent * lineHeight);



            String centerText = lrcContentList.get(centerLineNum).getLrcStr(); //居中行文本
            //居中行文本的宽度和高度
            Rect bounds = new Rect();
            paint.getTextBounds(centerText, 0, centerText.length(), bounds);
            int textH = bounds.height(); //居中行的高度
            //居中行y
            int centerY = viewH/2 + textH/2 - offsetY;

            for(curLineNum= 0; curLineNum < lrcContentList.size(); curLineNum++) {
                if (curLineNum == centerLineNum) {
                    //绘制居中行
                    paint.setTextSize(bigSize);
                    paint.setColor(white);
                } else {
                    //其他行
                    paint.setTextSize(smallSize);
                    paint.setColor(halfWhite);
                }
                int currentX = viewW/2; //当前行x(中心)
                int currentY = centerY + (curLineNum - centerLineNum) * lineHeight; //当前行y(中下)

                //超出边界
                if (currentY < 0) continue; //处理超出上边界歌词
                if (currentY > viewH + textH) break; //处理超出下边界歌词

                String curText = lrcContentList.get(curLineNum).getLrcStr();
                if (canvas != null)
                    canvas.drawText(curText, (float) currentX, (float) currentY, paint);
            }
        } catch (IndexOutOfBoundsException e1) {
//            Log.e(TAG, "歌词drawMuchLine(Canvas canvas) -> 捕获下标越界异常，IndexOutOfBoundsException,但是啥都不做");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘制单行居中文本
     * @param canvas
     */
    private void drawSingleLine(Canvas canvas) {
        //初始化paint颜色和大小
        paint.setTextSize(bigSize);
        paint.setColor(white);
        String text = "点击屏幕加载歌词";
        //求文本的宽度和高度
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int textW = bounds.width(); //文本的宽度
        int textH = bounds.height(); //文本的高度

        //text的左下角坐标
        int x = viewW/2 - textW/2;
        int y = viewH/2 + textH/2;

        //绘制内容
        if (canvas != null)
            canvas.drawText(text, (float)viewW/2, (float)y, paint);
    }

    /**
     * 布局之后执行
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewW = w;
        viewH = h;
    }

    /**
     * LyricView.updateProgress()就行了
     * 传递当前的播放进度 实现歌词播放
     * @param progress
     */
    public void updateProgress(int progress) {
//        //防止lrcContentList为空
//        if (null == lrcContentList) return;
//        //防止下标越界
//        if (curLineNum > lrcContentList.size() - 1 || centerLineNum > lrcContentList.size() - 1) return;


        try{
            this.progress = progress;
            //获取居中行行号
            //先判断是否最后一行居中
            if (lrcContentList.size() > 0 && progress >= lrcContentList.get(lrcContentList.size()-1).getLrcTime()) {
                centerLineNum = lrcContentList.size() - 1;
            } else {
                //其他行居中 循环遍历集合
                for (curLineNum= 0; curLineNum < lrcContentList.size() - 1; curLineNum++) {
                    //progress >= 当前开始时间 && progress <= 下一行开始时间
                    int curStartTime = lrcContentList.get(curLineNum).getLrcTime();
                    int nextStartTime = lrcContentList.get(curLineNum+1).getLrcTime();
                    if (progress >= curStartTime && progress <= nextStartTime) {
                        centerLineNum = curLineNum;
                        break;
                    }
                }
            }

            //找到居中行之后绘制当前多行歌词
//        invalidate(); //只会触发执行onDraw方法，只会改变绘制里面的内容,条目的绘制
            refreshThisView();
        } catch (IndexOutOfBoundsException e1) {
            Log.e(TAG, "歌词updateProgress(int progress) -> 捕获下标越界异常，IndexOutOfBoundsException,但是啥都不做");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 设置当前播放歌曲总时长
     * @param duration
     */
    public void setSongDuration(int duration) {
        this.duration = duration;
    }

    Handler thisHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1 : //刷新
                    invalidate();//只会触发执行onDraw方法，只会改变绘制里面的内容,条目的绘制
                case 2 :
                    requestLayout(); //view的布局参数改变之后刷新，比如view的宽度和高度都修改了，只能通过requestLayout()方法刷新
                default:
                    break;
            }

        }

    };

    /**
     * 在handler中调用invalidate()方法
     */
    public void refreshThisView() {
        thisHandler.sendEmptyMessage(1);
    }

    public void refreshThisViewAll() {
        thisHandler.sendEmptyMessage(2);
    }
}
