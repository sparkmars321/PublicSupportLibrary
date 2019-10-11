package com.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.common.util.R;

public class AutoRotateTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {

    private float mTextSize;
    private int mColor = 0xffffff;
    private int paintColor;
    private int mGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    private int curIndex;
    private boolean[] hasSetColor = {false, false};
    private Context mContext;
    private Rotate3dAnimation mInUp;
    private Rotate3dAnimation mOutUp;

    private Rotate3dAnimation mInDown;
    private Rotate3dAnimation mOutDown;

    public AutoRotateTextView(Context context) {
        this(context, null);
    }

    public AutoRotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoRotateTextView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.AutoRotateTextView_text_size) {
                mTextSize = a.getDimensionPixelSize(attr, 28);
            } else if (attr == R.styleable.AutoRotateTextView_text_color) {
                paintColor = mColor = a.getColor(attr, 0x000000);
            } else if (attr == R.styleable.AutoRotateTextView_gravity) {
                mGravity = a.getInt(attr, -1);
            }
        }
        a.recycle();
        mContext = context;
        init();
    }

    private void init() {
        setFactory(this);
        mInUp = createAnim(-90, 0, true, true);
        mOutUp = createAnim(0, 90, false, true);
        mInDown = createAnim(90, 0, true, false);
        mOutDown = createAnim(0, -90, false, false);
        setInAnimation(mInUp);
        setOutAnimation(mOutUp);
    }

    private Rotate3dAnimation createAnim(float start, float end, boolean turnIn, boolean turnUp) {
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, turnIn, turnUp);
        rotation.setDuration(800);
        rotation.setFillAfter(false);
        rotation.setInterpolator(new AccelerateInterpolator());
        return rotation;
    }

    @Override
    public View makeView() {
        TextView t = new MarqueeTextView(mContext);
        t.getPaint().setTextSize(mTextSize);
        t.setGravity(mGravity);
        t.setTextColor(paintColor);
        return t;
    }

    public boolean hasSetColor() {
        return hasSetColor[indexOfChild(getNextView())];
    }

    public void resetColor() {
        paintColor = mColor;
        int index = indexOfChild(getNextView());
        removeViewAt(index);
        addView(makeView(), index);
        hasSetColor[index] = false;
    }

    public void setTextColor(int color) {
        paintColor = color;
        curIndex = indexOfChild(getNextView());
        hasSetColor[curIndex] = true;
        removeViewAt(curIndex);
        addView(makeView(), curIndex);
    }

    public void previous() {
        if (getInAnimation() != mInDown) {
            setInAnimation(mInDown);
        }
        if (getOutAnimation() != mOutDown) {
            setOutAnimation(mOutDown);
        }
    }

    public void next() {
        if (getInAnimation() != mInUp) {
            setInAnimation(mInUp);
        }
        if (getOutAnimation() != mOutUp) {
            setOutAnimation(mOutUp);
        }
    }

    class Rotate3dAnimation extends Animation {
        private final float mFromDegrees;
        private final float mToDegrees;
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;

        public Rotate3dAnimation(float fromDegrees, float toDegrees, boolean turnIn, boolean turnUp) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight() / 2;
            mCenterX = getWidth() / 2;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float fromDegrees = mFromDegrees;
            float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1 : -1;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime), 0.0f);
            }
            camera.rotateX(degrees);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}
