package com.coral.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xss on 2019/1/3.
 * 纵向滑动选择器
 */
public class VerticalRangeSlideSeekBar extends View {
    private static final String TAG = VerticalRangeSlideSeekBar.class.getSimpleName();

    private Paint mLinePaint;

    // 线条颜色
    private int mLineColor = Color.GRAY;
    // 选中区间线条颜色
    private int mRangeLineColor = Color.BLUE;

    // 上下游标图标
    private Drawable mLowDrawable, mHighDrawable;

    // thumbnail图片大小
    private int mImageSize = 54 * 2;

    // 线条宽度（刻度线和横线）
    private int mStrokeWidth = 2;
    // 刻度线个数
    private int mTickLineCount = 5;
    // 刻度线高度
    private int mTickLineHeight = 24;
    // 进度条长度（注意：必须满足条件 mAvgLineWidth > mImageSize）
    private int mAvgLineWidth = 144 + 36;
    // 初始固定值，刻度线的起点和终点x位置
    private int mLineStart, mLineEnd;

    // 左侧游标滑动的位置
    private int mSlideLow = 0;
    // 右侧游标滑动的位置
    private int mSlideHigh = mAvgLineWidth;
    // 记录上次按下的游标位置，决定是点击还是滑动事件
    private float mPreSlide;

    // 手指点击游标可导致滑动的范围
    private int touchRange;
    // 小值游标滑动
    private boolean isLowSliding;
    // 大值游标滑动
    private boolean isHighSliding;

    // 选中的区间index
    private int mSelectedLowIndex, mSelectedHighIndex;

    private OnRangeChangedListener mOnRangeChangedListener;

    // 容错滑动距离
    private static final int EXTRA_SPACE = 30;

    public VerticalRangeSlideSeekBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public VerticalRangeSlideSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public VerticalRangeSlideSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VerticalRangeSlideSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mLinePaint = new Paint();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.base_VerticalRangeSlideSeekBar, defStyleAttr, 0);
        mImageSize = (int) ta.getDimension(R.styleable.base_VerticalRangeSlideSeekBar_base_imageSize, 28);

        mLowDrawable = ta.getDrawable(R.styleable.base_VerticalRangeSlideSeekBar_base_imageSrc);
        mHighDrawable = ta.getDrawable(R.styleable.base_VerticalRangeSlideSeekBar_base_imageSrc);

        mStrokeWidth = (int) ta.getDimension(R.styleable.base_VerticalRangeSlideSeekBar_base_strokeWidth, 2);
        mTickLineHeight = (int) ta.getDimension(R.styleable.base_VerticalRangeSlideSeekBar_base_tickLineHeight, 8);
        mTickLineCount = ta.getInt(R.styleable.base_VerticalRangeSlideSeekBar_base_tickLineCount, 5);
        mAvgLineWidth = (int) ta.getDimension(R.styleable.base_VerticalRangeSlideSeekBar_base_avgLineWidth, 48);
        mLineColor = ta.getColor(R.styleable.base_VerticalRangeSlideSeekBar_base_lineColor, Color.GRAY);
        mRangeLineColor = ta.getColor(R.styleable.base_VerticalRangeSlideSeekBar_base_rangeLineColor, Color.BLUE);
        mSelectedLowIndex = ta.getInt(R.styleable.base_VerticalRangeSlideSeekBar_base_selectedLowIndex, 0);
        mSelectedHighIndex = ta.getInt(R.styleable.base_VerticalRangeSlideSeekBar_base_selectedHighIndex, 1);

        ta.recycle();

        if (mSelectedHighIndex < mSelectedLowIndex || mSelectedHighIndex > mTickLineCount) {
            throw new IllegalArgumentException("selectedHighIndex value must above selectedLowIndex and below tickLineCount");
        }

        if (mLowDrawable == null || mHighDrawable == null) {
            mLowDrawable = getResources().getDrawable(R.drawable.base_icon_range_seek_bar_thumbnail);
            mHighDrawable = getResources().getDrawable(R.drawable.base_icon_range_seek_bar_thumbnail);
        }

        log(TAG,  "mImageSize = " + mImageSize + ", " + mAvgLineWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int lineLength = mAvgLineWidth * (mTickLineCount - 1) + mStrokeWidth / 2;
        mLineStart = mImageSize / 2;
        mLineEnd = lineLength + mImageSize / 2;

        mSlideLow = mSelectedLowIndex * mAvgLineWidth;
        mSlideHigh = mSelectedHighIndex * mAvgLineWidth;

        // 指定手指滑动游标的范围
        touchRange = mImageSize / 2;

        setMeasuredDimension(mImageSize, lineLength + mImageSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        setBackgroundColor(Color.parseColor("#000000"));

        drawLine(canvas);
        drawThumbnail(canvas);
    }

    private void drawLine(Canvas canvas) {
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(mStrokeWidth);

        // 画总直线长
        mLinePaint.setColor(mLineColor);
        canvas.drawLine(mLineStart, mImageSize / 2 - mStrokeWidth / 2, mLineStart, mLineEnd, mLinePaint);

        log(TAG, "draw line: " + mSelectedLowIndex + ", " + mSelectedHighIndex);
        // 画选中的区间线
        mLinePaint.setColor(mRangeLineColor);
        canvas.drawLine(mLineStart, mSlideLow + mImageSize / 2, mLineStart, mSlideHigh + mLineStart, mLinePaint);

        // 画刻度线
        int start = mImageSize / 2;
        mLinePaint.setColor(mLineColor);
        for (int i = 0; i < mTickLineCount; i++) {
            canvas.drawLine(mLineStart - mStrokeWidth / 2, start, mLineStart - mStrokeWidth / 2 - mTickLineHeight, start, mLinePaint);
            start += mAvgLineWidth;
        }
    }

    private void drawThumbnail(Canvas canvas) {
        mLowDrawable.setBounds(0, mSlideLow, mImageSize, mImageSize + mSlideLow);
        mLowDrawable.draw(canvas);

        mHighDrawable.setBounds(0, mSlideHigh, mImageSize, mImageSize + mSlideHigh);
        mHighDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float currentX = event.getX();
        float currentY = event.getY();

        // 判断是否触摸范围是否为游标
        boolean isAtCursor = Math.abs(currentX - touchRange) < mImageSize / 2;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                log(TAG, "down: " + currentY + ", " + mSlideLow + ", " + mSlideHigh + ", isAtCursor = " + isAtCursor);
                boolean lowSlide = Math.abs(currentY - mSlideLow) < mImageSize;
                boolean highSlide = Math.abs(currentY - mSlideHigh) < mImageSize;

                if (isAtCursor && lowSlide) {
                    log(TAG, "low slide");
                    isLowSliding = true;
                } else if (isAtCursor && highSlide) {
                    isHighSliding = true;
                    log(TAG, "high slide");
                }

                // 消除 点击动作 造成的无效移动（区分滑动效果）
                mPreSlide = currentY;

                break;
            case MotionEvent.ACTION_MOVE:
                String msg = isLowSliding ? "low" : "";
                msg = isHighSliding ? "high" : msg;

                boolean isClick = Float.compare(currentY, mPreSlide) == 0;

                log(TAG, "move " + msg + ": " + currentY + ", " + mSlideLow + ", " + mSlideHigh + ", " + mPreSlide + ", " + (isClick));

                if (isLowSliding && !isClick) {
                    if (mSlideHigh - currentY >= mAvgLineWidth - EXTRA_SPACE && currentY >= 0) { // 控制左滑的边界
                        mSlideLow = (int) currentY;
                        postInvalidate();
                    } else {
                        log(TAG, "---low thumb cannot slide---");
                    }
                } else if (isHighSliding && !isClick) {
                    if (currentY - mSlideLow >= mAvgLineWidth && currentY <= mLineEnd) {
                        mSlideHigh = (int) currentY;
                        postInvalidate();
                    } else {
                        log(TAG, "---high thumb cannot slide---");
                    }
                }

                break;
            case MotionEvent.ACTION_UP:

                boolean isScroll = Float.compare(currentY, mPreSlide) != 0;

                if (isScroll) {
                    // 132.0, 0
                    log(TAG, "up" + isLowSliding + ", " + isHighSliding + isAtCursor);

                    // 滑动停止时，确定滑块的刻度
                    if (isLowSliding) {
                        int rangeLow = (mSlideLow - mLineStart) / mAvgLineWidth;
                        int rangeSpan = (mSlideLow - mLineStart) % mAvgLineWidth;

                        log(TAG, "up low: " + currentY + ", " + mSlideLow + ", rangeLow=" + rangeLow + ", rangeSpan=" + rangeSpan);

                        if (rangeSpan <= mAvgLineWidth / 2) {
                            mSelectedLowIndex = rangeLow;
                            mSlideLow = rangeLow * mAvgLineWidth;
                        } else {
                            mSelectedLowIndex = rangeLow + 1;
                            mSlideLow = (rangeLow + 1) * mAvgLineWidth;
                        }

                        postInvalidate();
                    }
                    else if (isHighSliding) {
                        int rangeHigh = (mSlideHigh - mLineStart) / mAvgLineWidth;
                        int rangeHighSpan = (mSlideHigh - mLineStart) % mAvgLineWidth;

                        log(TAG, "up high: " + currentY + ", " + mSlideHigh + ", rangeLow=" + rangeHigh + ", rangeHighSpan=" + rangeHighSpan);

                        if (rangeHighSpan <= mAvgLineWidth / 2) {
                            mSelectedHighIndex = rangeHigh;
                            mSlideHigh = rangeHigh * mAvgLineWidth;
                        } else {
                            mSelectedHighIndex = rangeHigh + 1;
                            mSlideHigh = (rangeHigh + 1) * mAvgLineWidth;
                        }

                        postInvalidate();
                    }
                }

                isLowSliding = false;
                isHighSliding = false;

                if (mOnRangeChangedListener != null) {
                    mOnRangeChangedListener.onRange(mSelectedLowIndex, mSelectedHighIndex - 1);
                }

                break;
        }

        return true;
    }

    /**
     * 设置刻度线的数量 刻度线数量 = range区间数 + 1
     * @param count
     */
    public void setTickCount(int count) {
        this.mTickLineCount = count;
    }

    public void setSelectedRange(int lowIndex, int highIndex) {
        this.mSelectedLowIndex = lowIndex;
        this.mSelectedHighIndex = highIndex;
        requestLayout();
    }

    public int getSelectedLowIndex() {
        return mSelectedLowIndex;
    }

    public int getSelectedHighIndex() {
        return mSelectedHighIndex;
    }

    public int getAvgLineLength() {
        return mAvgLineWidth;
    }

    public void setOnRangeChangedListener(OnRangeChangedListener listener) {
        this.mOnRangeChangedListener = listener;
    }

    public interface OnRangeChangedListener {
        void onRange(int lowIndex, int highIndex);
    }
    
    private void log(String tag, String msg) {
        Log.e(tag, msg);
    }

}
