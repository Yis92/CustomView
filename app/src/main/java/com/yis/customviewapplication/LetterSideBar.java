package com.yis.customviewapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by liuyi on 2024/1/3.
 */
public class LetterSideBar extends View {
    private Context mContext;
    private String[] letters = {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y",
            "Z", "#"
    };
    private Paint mDefaultPaint, mSelectPaint;
    private int defaultColor = Color.BLACK, selectedColor = Color.BLUE;
    private int currentTouchPosition;


    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LetterSideBar);
        defaultColor = a.getColor(R.styleable.LetterSideBar_defaultColor, defaultColor);
        selectedColor = a.getColor(R.styleable.LetterSideBar_selectedColor, selectedColor);
        a.recycle();

        mDefaultPaint = new Paint();
        mDefaultPaint.setColor(defaultColor);
        mDefaultPaint.setTextSize(Utils.sp2px(mContext, 15));
        mDefaultPaint.setAntiAlias(true);

        mSelectPaint = new Paint();
        mSelectPaint.setColor(selectedColor);
        mSelectPaint.setTextSize(Utils.sp2px(mContext, 15));
        mSelectPaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int letterWidth = (int) mDefaultPaint.measureText(letters[0]);
        int width = getPaddingLeft() + getPaddingRight() + letterWidth;
        int height = MeasureSpec.getSize(heightMeasureSpec);//宽度取match_parent
        // at_most 模式  如果不处理的话就默认和match_parent的效果一样
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / letters.length;
        for (int i = 0; i < letters.length; i++) {
            //x 为宽度的一半减去文字宽度的一半
            float letterWidth = mDefaultPaint.measureText(letters[i]);
            float x = getWidth() / 2 - letterWidth / 2;
            Paint.FontMetricsInt fontMetricsInt = mDefaultPaint.getFontMetricsInt();
            int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
            //中心位置+ dy为基线位置
            float baseline = itemHeight * i + itemHeight / 2 + getPaddingTop() + dy;
            if (currentTouchPosition == i) {
                canvas.drawText(letters[i], x, baseline, mSelectPaint);
            } else {
                canvas.drawText(letters[i], x, baseline, mDefaultPaint);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //计算触摸的字母的位置
                float moveY = event.getY();
                float itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / letters.length;
//                int touchPosition = (int) ((moveY - getPaddingTop()) / itemHeight);
                int touchPosition = (int) (moveY / itemHeight);

                if (touchPosition < 0) {
                    touchPosition = 0;
                }
                if (touchPosition > letters.length - 1) {
                    touchPosition = letters.length - 1;
                }

                if (currentTouchPosition == touchPosition) {
                    return true;
                }

                currentTouchPosition = touchPosition;
                invalidate();
        }
        return true;
    }
}


