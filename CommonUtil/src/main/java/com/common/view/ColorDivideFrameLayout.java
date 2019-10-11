package com.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.common.util.R;

/**
 * 容器可以设置背景颜色，并设置颜色高度比重
 */
public class ColorDivideFrameLayout extends FrameLayout {

    private int colorArrayResourceId; // 颜色数组资源Id
    private int fractionArrayResourceId; // 比重数组资源Id
    private int[] colorRes; // 颜色数组
    private int[] fractions; // 比重数组
    private Paint mPaint;
    private float[] colorHeight; // 每部分颜色所占高度
    private float totalFraction; // 总比重，用来计算每部分颜色所占高度

    public ColorDivideFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public ColorDivideFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorDivideFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorDivideFrameLayout);
        colorArrayResourceId = array.getResourceId(R.styleable.ColorDivideFrameLayout_color_attr, -1);
        fractionArrayResourceId = array.getResourceId(R.styleable.ColorDivideFrameLayout_fraction_attr, -1);
        array.recycle();

        init();
    }

    private void init() {
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        colorRes = getResources().getIntArray(colorArrayResourceId);
        fractions = getResources().getIntArray(fractionArrayResourceId);
        for (int fraction : fractions) {
            totalFraction += fraction;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        colorHeight = new float[fractions.length];
        for (int i = 0; i < fractions.length; i++) {
            colorHeight[i] = (fractions[i] / totalFraction) * h;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        float drawHeightStart = 0;
        for (int i = 0; i < colorHeight.length; i++) {
            mPaint.setColor(colorRes[i % colorRes.length]);
            canvas.drawRect(0, drawHeightStart, width, drawHeightStart + colorHeight[i], mPaint);
            drawHeightStart += colorHeight[i];
        }
    }
}
