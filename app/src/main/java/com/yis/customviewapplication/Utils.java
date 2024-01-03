package com.yis.customviewapplication;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by liuyi on 2023/12/29.
 */
class Utils {
   /**
    * 其实Android原生Api有提供dip和sp转px的方法
    * java中浮点型强制类型转换成int类型是没有四舍五入的，而是直接把小数点后的值直接去掉
    * 那么如果要转化为int类型就需要加上0.5达到四舍五入的效果。
    * @param sp
    * @param context
    * @return
    */

   public static int sp2px(int sp , Context context){
      return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,context.getResources().getDisplayMetrics());
   }

   public static int dp2px(int dp , Context context){
      return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
   }

   /**
    * dp转px
    */
   public static int dp2px(Context context, float dp) {
      float density = context.getResources().getDisplayMetrics().density;
      return (int) (dp * density + 0.5f);
   }

   /**
    * sp转px
    */
   public static int sp2px(Context context, float sp) {
      float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
      return (int) (sp * scaledDensity + 0.5f);
   }

   /**
    * px转dp
    */
   public static int px2dp(Context context, float px) {
      float density = context.getResources().getDisplayMetrics().density;
      return (int) (px / density + 0.5f);
   }

   /**
    * px转sp
    */
   public static int px2sp(Context context, float px) {
      float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
      return (int) (px / scaledDensity + 0.5f);
   }


}
