package com.yis.customviewapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyi on 2024/1/8.
 */
public class TagLayout extends ViewGroup {
    private List<ArrayList<View>> listView = new ArrayList<>();

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = getPaddingTop() + getPaddingBottom();
        int maxHeight = 0;

        int realWidth = width - getPaddingLeft() - getPaddingBottom();

        int count = getChildCount();
        listView.clear();
        ArrayList<View> listLine = new ArrayList<>();
        listView.add(listLine);
        int lineWidth = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            //这段话执行之后就可以 获取子view的宽高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            Log.e("qqq", "onMeasure childWidth->" + childWidth + " childHeight->" + childHeight);

            if (lineWidth + childWidth > realWidth) {
                //需要换行
                height += maxHeight;//这是 加上之前的那一行的最高的高度
                Log.e("qqq", "onMeasure2 height->" + height);

                lineWidth = childWidth;
                maxHeight = childHeight;
                Log.e("qqq", "onMeasure2 maxHeight ->" + maxHeight);

                listLine = new ArrayList();
                listView.add(listLine);

            } else {
                //同一行 宽度叠加
                lineWidth += childWidth;
                maxHeight = Math.max(maxHeight, childHeight);
            }

            Log.e("qqq", "onMeasure maxHeight->" + maxHeight);
            listLine.add(child);
        }
        //如果是最后一行，那么需要加上最后一行的高度（或者只有一行）
        height += maxHeight;
        Log.e("qqq", "height-> " + height);
        //根据子view计算和指定自己的高度
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        int left = 0, top = getPaddingTop(), right = 0, bottom = 0;

        for (int i = 0; i < listView.size(); i++) {

            ArrayList<View> lineViews = listView.get(i);

            int maxHeight = 0;


            for (int j = 0; j < lineViews.size(); j++) {

                View child = lineViews.get(j);

                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                left += params.leftMargin;//

                int childTop = top + params.topMargin;

                right = left + childWidth;

                bottom = childTop + childHeight;

                Log.e("qqq", "left->" + left + "   right->" + right + "  top->" + top + "  bottom->" + bottom);
                child.layout(left, childTop, right, bottom);

                left += childWidth + params.rightMargin;

                maxHeight = Math.max(maxHeight, params.topMargin + childHeight + params.bottomMargin);
            }
            //换行之后
            // 左边初始化
            left = getPaddingLeft();
            //top 是上一行的最大高度
            top += maxHeight;

        }
    }
}
