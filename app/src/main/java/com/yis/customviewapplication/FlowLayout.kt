package com.yis.customviewapplication

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by liuyi on 2024/1/8.
 */
class FlowLayout : ViewGroup {

    private val listView: ArrayList<ArrayList<View>> = ArrayList()
    private var listLine: ArrayList<View> = ArrayList()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getMode(widthMeasureSpec)
        var height = paddingTop + paddingBottom
        var maxHeight = 0

        val count = childCount
        listView.clear()
        listLine.clear()

        var lineWidth = 0
        for (i in 0..count - 1) {
            val child = getChildAt(i)
            //这段话执行之后就可以 获取子view的宽高
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            if (lineWidth + child.measuredWidth > width) {
                //需要换行
                listView.add(listLine)
                height += maxHeight //这是 加上之前的那一行的最高的高度

                lineWidth = child.measuredWidth
                maxHeight = child.measuredHeight
                listLine = ArrayList()

                if (i == count - 1) {//如果最后一个是单独一行，就要加上这一行的高度
                    height += maxHeight
                }

            } else {
                //同一行 宽度叠加
                lineWidth += child.measuredWidth
                maxHeight = Math.max(maxHeight, child.measuredHeight)
            }
            listLine.add(child)


        }

        //根据子view计算和指定自己的高度
        setMeasuredDimension(width, height)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var top = 0
        var bottom = 0
        var left = 0
        var right = 0


    }


}