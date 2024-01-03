package com.yis.customviewapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by liuyi on 2024/1/2.
 */
public class ProgressBar extends View {
    private int progressTextColor = Color.RED;
    private float circleWidth;
    private int innerCircleColor = Color.RED;
    private int outterCircleColor = Color.BLUE;
    private int progress = 0;
    private int maxProgress = 100;
    private int progressTextSize;
    private Paint innerPaint;
    private Paint outterPaint;
    private Paint textPaint;
    private Context mContext;


    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        innerCircleColor = a.getColor(R.styleable.ProgressBar_innerCircleColor, innerCircleColor);
        outterCircleColor = a.getColor(R.styleable.ProgressBar_outterCircleColor, outterCircleColor);
        circleWidth = a.getDimension(R.styleable.ProgressBar_circleWidth, Utils.dp2px(context, 12));
        progress = a.getInteger(R.styleable.ProgressBar_progressText, progress);
        progressTextSize = a.getDimensionPixelSize(R.styleable.ProgressBar_progressTextSize, Utils.sp2px(context, 24));
        progressTextColor = a.getColor(R.styleable.ProgressBar_progressTextColor, progressTextColor);
        initPaint();
        a.recycle();
    }

    private void initPaint() {
        innerPaint = new Paint();
        innerPaint.setStrokeWidth(circleWidth);
        innerPaint.setStyle(Paint.Style.STROKE);
        innerPaint.setColor(innerCircleColor);
        innerPaint.setStrokeCap(Paint.Cap.ROUND);
        innerPaint.setAntiAlias(true);

        outterPaint = new Paint();
        outterPaint.setStrokeWidth(circleWidth);
        outterPaint.setStyle(Paint.Style.STROKE);
        outterPaint.setColor(outterCircleColor);
        outterPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(progressTextColor);
        textPaint.setTextSize(progressTextSize);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            width = Utils.dp2px(mContext, 200f);
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = Utils.dp2px(mContext, 200f);
        }

        int min = Math.max(width, height);

        setMeasuredDimension(min, min);

    }

    /**
     * 画一个椭圆：
     * 第一个是一个矩形，与椭圆外边相切
     * 第二个是开始的角度，以右边水平位置为0度，单位是角度，不是弧度
     * 第三个是扫过的角度，同样是角度单位
     * 第四个是是否从椭圆中心引出两条线将弧与中心连接，这时在画笔设置为STROKE才能看出区别
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画一个圆：前两个坐标分别是圆心坐标，第三个是半径，第四个画笔
        int width = getWidth();
        int height = getHeight();
        canvas.drawCircle(width / 2, height / 2, width / 2 - circleWidth / 2, outterPaint);
        RectF rectF = new RectF();
        if (maxProgress == 0) return;
        float percent = progress / maxProgress;
        canvas.drawArc(rectF, 0, percent * 360, false, innerPaint);
        String text = progress + "%";
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int x = width / 2 - bounds.width() / 2;
        Paint.FontMetricsInt metricsInt = textPaint.getFontMetricsInt();

        int dy = (metricsInt.bottom - metricsInt.top) / 2 - metricsInt.bottom;
        int baseline = getHeight() / 2 + dy;
        canvas.drawText(text, x, baseline, textPaint);

    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
