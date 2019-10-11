package com.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.common.util.R;

public class OvalTextView extends TextView {

    private TextPaint textPaint;
    private Paint backGroundPaint;
    private int leftAndRightPadding;
    private int topAndBottomPadding;
    private int background_color = 0xe63528;
    private int mTextColor;
    private int mTextSize;
    private int cornerRadius;

    public OvalTextView(Context context) {
        this(context, null);
    }

    public OvalTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OvalTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OvalTextView);
        leftAndRightPadding = typedArray.getDimensionPixelSize(R.styleable.OvalTextView_oval_leftAndRightPadding, 0);
        topAndBottomPadding = typedArray.getDimensionPixelSize(R.styleable.OvalTextView_oval_topAndBottomPadding, 0);
        background_color = typedArray.getColor(R.styleable.OvalTextView_oval_background_color, background_color);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.OvalTextView_oval_text_size, 12);
        mTextColor = typedArray.getColor(R.styleable.OvalTextView_oval_text_color, 0x000000);
        typedArray.recycle();
        init();
    }

    private void init() {
        textPaint = getPaint();
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setDither(true);
        textPaint.setAntiAlias(true);
        backGroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        backGroundPaint.setStyle(Paint.Style.FILL);
        backGroundPaint.setColor(background_color);
    }

    public void setLeftAndRightPadding(int leftAndRightPadding) {
        this.leftAndRightPadding = leftAndRightPadding;
    }

    public void setTopAndBottomPadding(int topAndBottomPadding) {
        this.topAndBottomPadding = topAndBottomPadding;
    }

    public void setBackground_color(int backgroundColor) {
        this.background_color = backgroundColor;
        backGroundPaint.setColor(background_color);
        invalidate();
    }

    public void setOvalTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        textPaint.setTextSize(mTextSize);
        invalidate();
    }

    public void setOvalTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        textPaint.setColor(mTextColor);
        invalidate();
    }

    public void setText(String text) {
        super.setText(text);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        String text = getText().toString();
        if (text.length() > 0) {
            int widthSize;
            int heightSize;
            Rect rect = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), rect);
            if (text.length() == 1) {
                int baseWidth = rect.width() > rect.height() ? rect.width() : rect.height();
                int padding = leftAndRightPadding > topAndBottomPadding ? leftAndRightPadding : topAndBottomPadding;
                widthSize = heightSize = baseWidth + padding * 2;
            } else {
                widthSize = rect.width() + leftAndRightPadding * 2;
                heightSize = rect.height() + topAndBottomPadding * 2;
            }
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String text = getText().toString();
        Rect textRect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        int width = getWidth();
        int height = getHeight();
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float bottomLineY = height / 2 - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
        if (text.length() == 1) {
            canvas.drawCircle(width / 2, height / 2, width / 2, backGroundPaint);
            canvas.drawText(text, width / 2 - textRect.centerX(), bottomLineY, textPaint);
        } else if (text.length() > 1) {
            RectF rectF = new RectF(0, 0, width, height);
            cornerRadius = height / 2;
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, backGroundPaint);
            canvas.drawText(text, width / 2 - textRect.centerX(), bottomLineY, textPaint);
        } else {
            super.onDraw(canvas);
        }
    }
}
