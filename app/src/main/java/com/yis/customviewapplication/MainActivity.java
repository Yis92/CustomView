package com.yis.customviewapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QQStepView stepView = (QQStepView) findViewById(R.id.step_view);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);

        stepView.setStepMax(20000);

        //属性动画
        //500毫秒内从0～递增到8900
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(0, 8900);
        //AccelerateInterpolator 刚开始慢，后来变快
        //DecelerateInterpolator 刚开始快，后来变慢
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                stepView.setCurrentStep(value);
            }
        });
        valueAnimator.start();

        progressBar.setMaxProgress(100);
        ValueAnimator valueAnimator2 = ObjectAnimator.ofInt(0, 80);
        //AccelerateInterpolator 刚开始慢，后来变快
        //DecelerateInterpolator 刚开始快，后来变慢
        valueAnimator2.setInterpolator(new DecelerateInterpolator());
        valueAnimator2.setDuration(1000);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                progressBar.setProgress(value);
            }
        });
        valueAnimator2.start();

    }
}