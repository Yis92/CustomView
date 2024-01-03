package com.yis.customviewapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Created by liuyi on 2023/12/29.
 */
public class QQStepView extends View {
    private Paint mInnerPaint;
    private Paint mOutPaint;
    private Paint mTextPaint;
    private int mOutColor = Color.RED;
    private int mInnerColor = Color.BLUE;
    private int mBorderWidth;
    private int mStepTextSize;
    private String mStepText;
    private int mStepTextColor = Color.RED;
    private Context mContext;
    private int mStepMax = 10000;
    private int mCurrentStep = 3750;

    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mOutColor = a.getColor(R.styleable.QQStepView_outColor, mOutColor);
        mInnerColor = a.getColor(R.styleable.QQStepView_innerColor, mInnerColor);
        mBorderWidth = (int) a.getDimension(R.styleable.QQStepView_borderWidth, Utils.dp2px(context, 6));
        mStepTextSize = (int) a.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, Utils.sp2px(context, 26));
        mStepText = a.getString(R.styleable.QQStepView_stepText);
        mStepTextColor = a.getColor(R.styleable.QQStepView_stepTextColor, mStepTextColor);
        a.recycle();

        if (!TextUtils.isEmpty(mStepText) && TextUtils.isDigitsOnly(mStepText)) {
            mCurrentStep = Integer.valueOf(mStepText);
        } else {
            mStepText = mCurrentStep + "";
        }

        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setColor(mOutColor);
        mOutPaint.setStrokeWidth(mBorderWidth);
        mOutPaint.setStyle(Paint.Style.STROKE);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);
        /**
         * setStrokeCap意思是设置线帽子 就是一个线段结束后的额外部分
         BUTT    (0),//无线帽 短一点
         ROUND   (1),//圆形线帽 比上面的长一点，因为带了个帽子
         SQUARE  (2);//方形线帽 比上面的长一点，因为带了个帽子
         */

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mStepTextSize);
        mTextPaint.setColor(mStepTextColor);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {//如果是wrap_content 就给一个默认值
            width = Utils.dp2px(100, mContext);
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = Utils.dp2px(100, mContext);
        }

        int min = Math.max(width, height);//如果长宽不是一个正方形，那么就选最小的作为这个图形的尺寸
        setMeasuredDimension(min, min);

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int halfBorder = mBorderWidth / 2;
        //边缘没有显示清楚 RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        //因为画笔有粗度，所以不能把矩形计算的这么大，所以矩形边缘要留出画笔宽度的一半就能显示完整
        RectF rectF = new RectF(halfBorder, halfBorder, getWidth() - halfBorder, getHeight() - halfBorder);

        float angle = (float) mCurrentStep / mStepMax;
        //角度是从135开始 扫过270度 useCenter false 是开头结尾不与中心点相连
        canvas.drawArc(rectF, 135, 270, false, mOutPaint);

        canvas.drawArc(rectF, 135, angle * 270, false, mInnerPaint);

        Rect rect = new Rect();
        mTextPaint.getTextBounds(mStepText, 0, mStepText.length(), rect);
        //文字的起始点就是 容器的一半 减去 文字宽度的一半
        int x = getWidth() / 2 - rect.width() / 2;
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        //计算基线的位置 top表示基线到文字最上面的位置的距离 是个负值 bottom为基线到最下面的距离，为正值
        // 计算中线跟基线的差值
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(mStepText, x, baseLine, mTextPaint);

    }

    public void setStepMax(int stepMax) {
        this.mStepMax = stepMax;
    }

    public void setCurrentStep(int currentStep) {
        this.mCurrentStep = currentStep;
        mStepText = mCurrentStep + "";
        invalidate();

    }


}
