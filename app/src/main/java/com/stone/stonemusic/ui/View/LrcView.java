package com.stone.stonemusic.ui.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.view.ViewTreeObserver;

import com.stone.stonemusic.R;
import com.stone.stonemusic.model.LrcContent;
import com.stone.stonemusic.present.interfaceOfPresent.OnLrcSearchClickListener;
import com.stone.stonemusic.utils.DisplayUtil;
import com.stone.stonemusic.utils.MediaUtils;
import com.stone.stonemusic.utils.TimeUtil;

import java.util.List;

import static com.stone.stonemusic.data.LrcStateContants.QUERY_ONLINE;
import static com.stone.stonemusic.data.LrcStateContants.QUERY_ONLINE_FAIL;
import static com.stone.stonemusic.data.LrcStateContants.QUERY_ONLINE_NULL;
import static com.stone.stonemusic.data.LrcStateContants.QUERY_ONLINE_OK;
import static com.stone.stonemusic.data.LrcStateContants.READ_LOC_FAIL;
import static com.stone.stonemusic.data.LrcStateContants.READ_LOC_OK;

public class LrcView extends ScrollView implements
        ViewTreeObserver.OnScrollChangedListener,View.OnTouchListener {
    private static String TAG  = "LrcView";
    private float width;    //歌词视图宽度
    private float height;    //歌词视图高度
    private Paint currentPaint;        //当前画笔对象
    private Paint notCurrentPaint;        //非当前画笔对象
    private Paint tipsPaint;  //提示信息画笔
    private Paint linePaint; //滑动线画笔

    private float lightTextSize;        //高亮文本大小
    private float norTextSize;        //非高亮文本大小
    private float tipsTextSize;    //提示文本大小
    private float textHeight;    //文本高度
    private int index;    //歌词list集合下标

    private int lrcState = -1;
    private LrcTextView lrcTextView;
    private List<LrcContent> lrcLists;

    private int scrollY;
    private boolean canDrawLine = false;
    private int pos = -1; //手指按下后歌词要到的位置


    private boolean hasLrc = false; //是否有歌词，可以触摸并调整歌词


    private int count = 0;  //绘制加载点的次数

    private Context mContext;

    private OnLrcSearchClickListener onLrcSearchClickListener;

    public LrcView(Context context) {
        this(context, null);
        mContext = context;
        this.setOnTouchListener(this);
        init();
    }

    public LrcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
        this.setOnTouchListener(this);
        init();
    }

    public LrcView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        this.setOnTouchListener(this);

        init();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void init() {
        setFocusable(true);    //设置该控件可以有焦点
        this.setWillNotDraw(false); //该View是否可以自己绘制，false：可以自己绘制

        norTextSize = DisplayUtil.sp2px(mContext, 16);
        lightTextSize = DisplayUtil.sp2px(mContext, 18);
        tipsTextSize = DisplayUtil.sp2px(mContext, 20);
        textHeight = norTextSize+DisplayUtil.dip2px(mContext,20);

        //高亮歌词部分
        currentPaint = new Paint();
        currentPaint.setAntiAlias(true);    //设置抗锯齿
        currentPaint.setTextAlign(Paint.Align.CENTER);    //设置文本居中

        //非高亮歌词部分
        notCurrentPaint = new Paint();
        notCurrentPaint.setAntiAlias(true);
        notCurrentPaint.setTextAlign(Paint.Align.CENTER);

        //提示信息画笔
        tipsPaint = new Paint();
        tipsPaint.setAntiAlias(true);
        tipsPaint.setTextAlign(Paint.Align.CENTER);

        //滑动线画笔
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setTextAlign(Paint.Align.CENTER);

        //设置画笔颜色
        currentPaint.setColor(getResources().getColor(R.color.white));
        notCurrentPaint.setColor(getResources().getColor(R.color.lightWhite));
        tipsPaint.setColor(getResources().getColor(R.color.white));
        linePaint.setColor(getResources().getColor(R.color.lightWhite));


        //设置字体
        currentPaint.setTextSize(lightTextSize);
        currentPaint.setTypeface(Typeface.SERIF);

        notCurrentPaint.setTextSize(norTextSize);
        notCurrentPaint.setTypeface(Typeface.DEFAULT);

        tipsPaint.setTextSize(tipsTextSize);
        tipsPaint.setTypeface(Typeface.DEFAULT);

        linePaint.setTextSize(lightTextSize);
        linePaint.setTypeface(Typeface.SERIF);

    }

    public void setOnLrcSearchClickListener(OnLrcSearchClickListener onLrcSearchClickListener) {
        this.onLrcSearchClickListener = onLrcSearchClickListener;
    }

    public List<LrcContent> getLrcLists() {
        return lrcLists;
    }

    public void setLrcLists(List<LrcContent> lrcLists) {
        this.lrcLists = lrcLists;

        //判断是否有歌词
		if(lrcLists==null||lrcLists.size()==0)
		    hasLrc = false;
		else //有歌词
            hasLrc = true;

        //设置index=-1
        this.index = -1;

        LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lrcTextView = new LrcTextView(this.getContext());
        lrcTextView.setLayoutParams(params1);

        this.removeAllViews();
        this.addView(lrcTextView);
    }

    /*返回歌词集合下标*/
    public int getIndex() {
        return index;
    }

    /*设置歌词集合下标*/
    public void setIndex(int index) {
        //歌曲位置发生变化 && 不是手指调整歌词位置的状态（手指调整歌词位置会将pos设置为-1）
        if (this.index != index && pos == -1) {
            this.scrollTo(0, (int) (index * textHeight));
        }

        this.index = index;
    }

    /*设置滑动速速*/
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 4);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.width = w;
        this.height = h;
    }

    /*输入当前时间，获取歌词下标*/
    public int getIndexByLrcTime(int currentTime) {
        if (lrcLists == null) {
            return 0;
        }

        for (int i = 0; i < lrcLists.size(); i++) {
            if (currentTime < lrcLists.get(i).getLrcTime()) {
                return i - 1;
            }
        }
        return lrcLists.size() - 1;
    }

    /*清空LrcView中的LrcLists列表*/
    public void clear() {
        lrcLists = null;
    }

    /*设置Lrc状态*/
    public void setLrcState(int lrcState) {
        this.lrcState = lrcState;
        invalidate();
    }

    public int getLrcState() {
        return lrcState;
    }

    /*TextView 在线查询更新*/
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            invalidate();

//            if (!SlidingPanel.mTracking) {
//                invalidate();
//            }

        }

    };

    class LrcTextView extends android.support.v7.widget.AppCompatTextView {


        public LrcTextView(Context context) {
            this(context, null);
        }

        public LrcTextView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public LrcTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.setWillNotDraw(false);
        }

        //绘制歌词
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.i(TAG, "LrcTextView onDraw");

            if (canvas == null) return;

            int tempY = (int) height / 2;

            switch (lrcState) {
                case READ_LOC_FAIL:
                    tipsPaint.setUnderlineText(true);
                    canvas.drawText("暂无歌词,正在搜索...", width / 2, tempY, tipsPaint);
                    break;
                case QUERY_ONLINE: /*正在联网查找*/
                    tipsPaint.setUnderlineText(false);
                    String drawContentStr = "正在在线匹配歌词";
                    for (int i = 0; i < count; i++) {
                        drawContentStr += ".";
                    }

                    count++;
                    if (count >= 6) count = 0;

                    canvas.drawText(drawContentStr, width / 2, tempY, tipsPaint);

//                    handler.sendEmptyMessageDelayed(1, 1000);
                    break;
                case QUERY_ONLINE_OK:
                case READ_LOC_OK:
                    //绘制歌词
                    for (int i = 0; i < lrcLists.size(); i++, tempY += textHeight) {
                        if (i == index) {
                            canvas.drawText(lrcLists.get(i).getLrcStr(), width / 2, tempY, currentPaint);
                        } else if (i == pos) {
                            canvas.drawText(lrcLists.get(i).getLrcStr(), width / 2, tempY, linePaint);
                        } else {
                            canvas.drawText(lrcLists.get(i).getLrcStr(), width / 2, tempY, notCurrentPaint);
                        }
                    }
                    break;
//                case QUERY_ONLINE_FAIL:
//                    tipsPaint.setUnderlineText(true);
//                    canvas.drawText("搜索失败，请重试", width / 2, tempY, tipsPaint);
//                    break;
                case QUERY_ONLINE_NULL:
                    tipsPaint.setUnderlineText(false);
                    canvas.drawText("网络无匹配歌词", width / 2, tempY, tipsPaint);
//                    handler.sendEmptyMessageDelayed(1, 1000);
                    break;
            }

        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            heightMeasureSpec = (int) (height + textHeight * (lrcLists.size() - 1));
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }


    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (canDrawLine) {
            canvas.drawLine(0, scrollY + height / 2, width, scrollY + height / 2, linePaint);
            canvas.drawText(TimeUtil.mill2mmss(lrcLists.get(pos).getLrcTime()), 42, scrollY + height / 2 - 2, linePaint);
        }
    }



    /*触发onDraw方法*/
    @Override
    public void invalidate() {
        super.invalidate();

        if (null != lrcTextView)
            lrcTextView.invalidate();
    }

    @Override
    public void onScrollChanged() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //没有歌词
        if (!hasLrc) {
            Log.i(TAG, "LrcStateView=>onTouch=>没有歌词");
            return handleTouchLrcFail(event.getAction());
        }

        //界面可以触摸
        switch (lrcState) {
            case READ_LOC_FAIL: /*读取本地歌词失败*/
            case QUERY_ONLINE_FAIL: /*查询网络歌词失败*/
                return handleTouchLrcFail(event.getAction());
            case READ_LOC_OK: /*读取本地歌词成功*/
            case QUERY_ONLINE_OK: /*查询网络歌词成功*/
                return handleTouchLrcOK(event.getAction());

        }

        return true;
    }

    boolean handleTouchLrcOK(int action) {

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                scrollY = this.getScrollY();
                pos = (int) (this.getScrollY() / textHeight); /*当前视图顶点位置/文本高度 = 当前的歌词的下标*/

                canDrawLine = true;
                this.invalidate();

                Log.i(TAG, "LrcStateView=>handleTouchLrcOK=>ACTION_DOWN && MOVE");
                break;
            case MotionEvent.ACTION_UP:
                /*歌词已在手指移动下，位置变化, 改变歌曲位置到手指UP后的位置*/
                if(pos!=-1)
                    MediaUtils.getMediaPlayer().seekTo(lrcLists.get(pos).getLrcTime());
                canDrawLine = false;
                pos =-1; /*手指移动状态-》没有手指移动*/
                this.invalidate(); //触发OnDraw
                Log.i(TAG, "LrcStateView=>handleTouchLrcOK=>ACTION_UP");
                break;
        }
        return true;
    }

    boolean handleTouchLrcFail(int action) {
        switch (action) {
            case MotionEvent.ACTION_UP:
                /*本地歌词获取失败或者网络歌词查询失败，将当前View设置给onLrcSearchClickListener接口*/
                if (onLrcSearchClickListener != null) {
                    onLrcSearchClickListener.onLrcSearchClicked(this);
                }
                this.invalidate(); //触发OnDraw
                Log.i(TAG, "LrcStateView=>handleTouchLrcFail=>ACTION_UP");
                break;
        }
        /*返回的true，消费该事件，继续接受后续事件*/
        return true;
    }


}
