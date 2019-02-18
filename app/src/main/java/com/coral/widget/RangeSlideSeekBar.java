package com.coral.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xss on 2019/1/3.
 * 横向滑动选择器
 */
public class RangeSlideSeekBar extends View {
    private static final String TAG = RangeSlideSeekBar.class.getSimpleName();

    private String[] texts = {"第1天", "第2天", "第3天", "第4天", "第5天"};

    private Paint mLinePaint;

    private Paint mBitmapPaint;

    private OnRangeChangedListener mOnRangeChangedListener;

    public RangeSlideSeekBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public RangeSlideSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RangeSlideSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RangeSlideSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mLinePaint = new Paint();

        mBitmapPaint = new Paint();

        if (mBitmapLow == null) {
            mBitmapLow = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        }
        if (mBitmapHigh == null) {
            mBitmapHigh = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        }

        /**游标图片的真实高度 之后通过缩放比例可以把图片设置成想要的大小*/
        int mBitmapHeight = mBitmapLow.getHeight();
        int mBitmapWidth = mBitmapLow.getWidth();
        // 设置想要的大小
        int newWidth = mImageSize;
        int newHeight = mImageSize;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / mBitmapWidth;
        float scaleHeight = ((float) newHeight) / mBitmapHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        /**缩放图片*/
        mBitmapLow = Bitmap.createBitmap(mBitmapLow, 0, 0, mBitmapWidth, mBitmapHeight, matrix, true);
        mBitmapHigh = Bitmap.createBitmap(mBitmapHigh, 0, 0, mBitmapWidth, mBitmapHeight, matrix, true);
        /**重新获取游标图片的宽高*/
        mImageSize = mBitmapLow.getWidth();

        Log.e(TAG,  "mImageSize = " + mImageSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int lineLength = mAvgLineWidth * texts.length + mStrokeWidth;
        mLineStart = mImageSize / 2;
        mLineEnd = lineLength + mImageSize / 2;

        mSlideLowX = 0;
        mSlideHighX = lineLength;

        mSelectedLowIndex = 0;
        mSelectedHighIndex = lineLength / mAvgLineWidth;

        touchRange = mImageSize / 2;

        setMeasuredDimension(lineLength + mImageSize, mImageSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setBackgroundColor(Color.parseColor("#000000"));

        drawLine(canvas);
        drawThumbnail(canvas);
    }

    // 线条宽度（刻度线和横线）
    private int mStrokeWidth = 2;
    // 刻度线高度
    private int mTickLineHeight = 24;
    // 进度条长度
    private int mAvgLineWidth = 144 + 36;
    // 初始固定值，刻度线的起点和终点x位置
    private int mLineStart, mLineEnd;

    private boolean isHorizontal;

    private void drawLine(Canvas canvas) {
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(mStrokeWidth);

        int tickCount = texts.length;
        // 画总直线长
        mLinePaint.setColor(Color.parseColor("#E4E7ED"));
        canvas.drawLine(mLineStart, mImageSize / 2, getMeasuredWidth() - mImageSize / 2, mImageSize / 2, mLinePaint);

        Log.e(TAG, "draw line: " + mSelectedLowIndex + ", " + mSelectedHighIndex);
        // 画选中的区间线
        mLinePaint.setColor(Color.parseColor("#1989FA"));
        canvas.drawLine(mSlideLowX + mLineStart, mImageSize / 2, mSlideHighX + mLineStart, mImageSize / 2, mLinePaint);

        // 画刻度线
        int startX = mImageSize / 2;
        mLinePaint.setColor(Color.parseColor("#E4E7ED"));
        for (int i = 0; i < tickCount + 1; i++) {
            canvas.drawLine(startX, mImageSize / 2 - mTickLineHeight, startX, mImageSize / 2, mLinePaint);
            startX += mAvgLineWidth;
        }
    }

    private Bitmap mBitmapLow;
    private Bitmap mBitmapHigh;
    // thumbnail图片大小
    private int mImageSize = 54 * 2;

    private void drawThumbnail(Canvas canvas) {
        Log.e(TAG, "drawThumb: " + mSlideLowX + ", " + mSlideHighX + ", " + mLineStart + ", " + mLineEnd);
        canvas.drawBitmap(mBitmapLow, mSlideLowX, 0,  mBitmapPaint);
        canvas.drawBitmap(mBitmapLow, mSlideHighX, 0,  mBitmapPaint);
    }

    // 左侧游标滑动的位置
    private int mSlideLowX = 0;
    // 右侧游标滑动的位置
    private int mSlideHighX = mAvgLineWidth;

    private float mPreX;

    private int touchRange;

    private boolean isLowSliding;
    private boolean isHighSliding;

    private int mSelectedLowIndex, mSelectedHighIndex;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float currentX = event.getX();
        float currentY = event.getY();
        boolean isAtCursor = Math.abs(currentY - touchRange) < mImageSize / 2;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                Log.e(TAG, "down: " + currentX + ", " + mSlideLowX + ", " + mSlideHighX + ", isAtCursor = " + isAtCursor);
                boolean lowSlide = Math.abs(currentX - mSlideLowX) < mImageSize;
                boolean highSlide = Math.abs(currentX - mSlideHighX) < mImageSize;
                if (isAtCursor && lowSlide) {  //  && currentX >= mLineStart
                    Log.e(TAG, "low slide");
                    isLowSliding = true;
                } else if (isAtCursor && highSlide) { // && currentX <= mLineEnd - mImageSize / 2
                    isHighSliding = true;
                    Log.e(TAG, "high slide");
                }

                // 消除 点击动作 造成的无效移动（区分滑动效果）
                mPreX = currentX;

                break;
            case MotionEvent.ACTION_MOVE:
                String msg = isLowSliding ? "low" : "";
                msg = isHighSliding ? "high" : msg;

                boolean isClick = Float.compare(currentX, mPreX) == 0;

                Log.e(TAG, "move " + msg + ": " + currentX + ", " + mSlideLowX + ", " + mSlideHighX + ", " + mPreX + ", " + (isClick));

                if (isLowSliding && !isClick) {
                    if (mSlideHighX - currentX >= mAvgLineWidth && currentX >= 0) { // 第二个条件控制左滑的边界 mLineStart - mImageSize / 2   && mSlideLowX >= mLineStart
                        mSlideLowX = (int) currentX;
                        postInvalidate();
                    } else {
                        Log.e(TAG, "---low thumb cannot slide---");
                    }
                } else if (isHighSliding && !isClick) {
                    if (currentX - mSlideLowX >= mAvgLineWidth && currentX <= mLineEnd - mImageSize / 2) {
                        mSlideHighX = (int) currentX;
                        postInvalidate();
                    } else {
                        Log.e(TAG, "---high thumb cannot slide---");
                    }
                }

                break;
            case MotionEvent.ACTION_UP:

                boolean isScroll = Float.compare(currentX, mPreX) != 0;
                if (isScroll) {
                    // 132.0, 0
                    Log.e(TAG, "up" + isLowSliding + ", " + isHighSliding);

                    // 滑动停止时，确定滑块的刻度
                    if (isLowSliding) {
                        int rangeLow = (mSlideLowX - mLineStart) / mAvgLineWidth;
                        int rangeSpan = (mSlideLowX - mLineStart) % mAvgLineWidth;

                        Log.e(TAG, "up low: " + currentX + ", " + mSlideLowX + ", rangeLow=" + rangeLow + ", rangeSpan=" + rangeSpan);

                        if (rangeSpan <= mAvgLineWidth / 2) {
                            mSelectedLowIndex = rangeLow;
                            mSlideLowX = rangeLow * mAvgLineWidth;
                        } else {
                            mSelectedLowIndex = rangeLow + 1;
                            mSlideLowX = (rangeLow + 1) * mAvgLineWidth;
                        }

                        postInvalidate();
                    }
                    else if (isHighSliding) {
                        int rangeHigh = (mSlideHighX - mLineStart) / mAvgLineWidth;
                        int rangeHighSpan = (mSlideHighX - mLineStart) % mAvgLineWidth;

                        Log.e(TAG, "up high: " + currentX + ", " + mSlideHighX + ", rangeLow=" + rangeHigh + ", rangeHighSpan=" + rangeHighSpan);

                        if (rangeHighSpan <= mAvgLineWidth / 2) {
                            mSelectedHighIndex = rangeHigh;
                            mSlideHighX = rangeHigh * mAvgLineWidth;
                        } else {
                            mSelectedHighIndex = rangeHigh + 1;
                            mSlideHighX = (rangeHigh + 1) * mAvgLineWidth;
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

    private void updateRange(int lowIndex, int highIndex) {

    }

    public interface OnRangeChangedListener {
        void onRange(int lowIndex, int highIndex);
    }
}
