package com.github.znacloud;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/11/5.
 */
public class RippleView extends View {
    private final static int RING_COLOR = Color.parseColor("#3F51B5");
    private final static int CIRCLE_COLOR = Color.parseColor("#3F51B5");
    private final static int BD_COLOR = Color.parseColor("#3F51B5");
    private final static int RING_WIDTH = 2;
    private static final int TEXT_COLOR = Color.DKGRAY;
    private static final String TAG = "RippleView";
    private static final int BD_WIDTH = 0;


    private int mWidth = 0;
    private int mHeight = 0;

    private ColorStateList mRingColor = ColorStateList.valueOf(RING_COLOR);
    private float mRingWidth = RING_WIDTH;
    private float mBdWidth = BD_WIDTH;

    private ColorStateList mShadowColor = ColorStateList.valueOf(Color.GRAY);
    private float mShadowRadius = 0f;

    private float mRippleDistance = 0;
    private ColorStateList mCircleColor = ColorStateList.valueOf(CIRCLE_COLOR);
    private ColorStateList mBdColor = ColorStateList.valueOf(BD_COLOR);

    private ColorStateList mTextColor = ColorStateList.valueOf(Color.WHITE);

    private Paint mPaint;
    private float mScale;
    private float mRadius;

    private int mDuration = 0;//0-255
    private int mDuration2 = 0;//0-255

    private Runnable mCallback;
    private TimerTask mTimeTask;
    private Timer mTimer;

    private EmbossMaskFilter mEmbossFilter;
    private BlurMaskFilter mBlurFilter;

    private int mCurShadowColor = Color.GRAY;
    private int mCurRingColor = RING_COLOR;
    private int mCurCircleColor = CIRCLE_COLOR;
    private int mCurBdColor = BD_COLOR;
    private int mCurTextColor = Color.WHITE;
    private String mText;
    private float mTextSize = 0;
    private float mTextWidth = 0;
    private float mTextHeight = 0;

    public RippleView(Context context) {
        super(context);
        init(context);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RippleView, defStyleAttr, 0);

        mRingColor = a.getColorStateList(R.styleable.RippleView_ringColor);
        if (mRingColor == null) {
            mRingColor = ColorStateList.valueOf(RING_COLOR);
        } else {
            mCurRingColor = mRingColor.getColorForState(getDrawableState(), RING_COLOR);
        }

        mShadowColor = a.getColorStateList(R.styleable.RippleView_shadowColor);
        if (mShadowColor == null) {
            mShadowColor = ColorStateList.valueOf(Color.GRAY);
        } else {
            mCurShadowColor = mShadowColor.getColorForState(getDrawableState(), Color.GRAY);
        }

        mBdColor = a.getColorStateList(R.styleable.RippleView_bdColor);
        if (mBdColor == null) {
            mBdColor = ColorStateList.valueOf(BD_COLOR);
        } else {
            mCurBdColor = mBdColor.getColorForState(getDrawableState(), BD_COLOR);
        }

        mCircleColor = a.getColorStateList(R.styleable.RippleView_circleColor);
        if (mCircleColor == null) {
            mCircleColor = ColorStateList.valueOf(CIRCLE_COLOR);
        } else {
            mCurCircleColor = mCircleColor.getColorForState(getDrawableState(), CIRCLE_COLOR);
        }

        mTextColor = a.getColorStateList(R.styleable.RippleView_textColor);
        if (mTextColor == null) {
            mTextColor = ColorStateList.valueOf(Color.WHITE);
        } else {
            mCurTextColor = mTextColor.getColorForState(getDrawableState(), Color.WHITE);
        }

        mRingWidth = a.getDimension(R.styleable.RippleView_ringWidth, RING_WIDTH);
        mBdWidth = a.getDimension(R.styleable.RippleView_bdWidth, BD_WIDTH);
        mShadowRadius = a.getDimension(R.styleable.RippleView_shadowRadius, 0f);
        mRippleDistance = a.getDimension(R.styleable.RippleView_rippleDistance, 0);
        mText = a.getString(R.styleable.RippleView_text);
        mTextSize = a.getDimension(R.styleable.RippleView_textSize, 12 * mScale);

        if (!TextUtils.isEmpty(mText)) {
            mPaint.setTextSize(mTextSize);
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mTextWidth = mPaint.measureText(mText);
            mTextHeight = mTextSize;
        }

        a.recycle();

        if (mShadowRadius > 0) {
            mBlurFilter = new BlurMaskFilter(mShadowRadius, BlurMaskFilter.Blur.SOLID);
        }
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mScale = context.getResources().getDisplayMetrics().density;
        mEmbossFilter = new EmbossMaskFilter(new float[]{1f, 1f, 1f}, 0.4f, 6f, 10f);
        mBlurFilter = new BlurMaskFilter(2 * mScale, BlurMaskFilter.Blur.SOLID);
        mTextSize = 12 * mScale;
        setLayerType(LAYER_TYPE_HARDWARE, mPaint);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint);
        }
        Log.d(TAG, "START===================");
        mDuration = 0;
        mDuration2 = 0;
        mTimer = new Timer();
        mTimeTask = new TimerTask() {

            @Override
            public void run() {
//                    Log.d(TAG,"==>");
                if (mDuration2 <= 159) {
                    mDuration += 6;
                    if (mDuration > 96) {
                        mDuration2 += 6;
                    }
                    if (mDuration > 255) {
                        mDuration = 0;
                    }
                } else {
                    mDuration2 += 6;
                    if (mDuration2 > 255) {
                        mDuration2 = 0;
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
//                    Log.d(TAG, "duration=>" + mDuration);
//                    Log.d(TAG, "duration2=>" + mDuration2);
                post(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }
        };
        mTimer.schedule(mTimeTask, 300, 25);
        //set clickable true, set clickable to false in xml wil not work.
        setClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        if (mRippleDistance == 0) {
            mRippleDistance = Math.min(mWidth, mHeight) * 0.15f;
        }
        mRadius = Math.min(mWidth, mHeight) / 2f - mRippleDistance;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw ripple
        mPaint.setStrokeWidth(mRingWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setMaskFilter(null);
        int color;
        float distance;
        if (mDuration > 0) {

            color = mCurRingColor & ((255 - mDuration) << 24 | 0x00ffffff);
            distance = mRadius + mDuration / 255f * mRippleDistance;
            mPaint.setColor(color);
            canvas.drawCircle(mWidth / 2F, mHeight / 2f, distance, mPaint);
        }
        //draw ripple 2
        if (mDuration2 > 0) {
            int ringColor = mRingColor.getColorForState(getDrawableState(), RING_COLOR);
            color = ringColor & ((255 - mDuration2) << 24 | 0x00ffffff);
            distance = mRadius + mDuration2 / 255f * mRippleDistance;
            mPaint.setColor(color);
            canvas.drawCircle(mWidth / 2F, mHeight / 2f, distance, mPaint);
        }


        //draw shadow ring
        if (mShadowRadius > 0) {
            mPaint.setStrokeWidth(0f);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mCurShadowColor);
            mPaint.setMaskFilter(mBlurFilter);
            canvas.drawCircle(mWidth / 2f, mHeight / 2F, mRadius, mPaint);
        }

        //draw main border
        if (mBdWidth > 0) {
            mPaint.setColor(mCurBdColor);
            mPaint.setStrokeWidth(mBdWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setMaskFilter(null);
            canvas.drawCircle(mWidth / 2f, mHeight / 2F, mRadius - mBdWidth / 2f, mPaint);
        }

        //draw main circle
        if (Color.alpha(mCurCircleColor) != Color.TRANSPARENT) {
            mPaint.setStrokeWidth(0f);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mCurCircleColor);
            mPaint.setMaskFilter(null);
            canvas.drawCircle(mWidth / 2f, mHeight / 2F, mRadius - mBdWidth, mPaint);
        }

        //draw text
        if (!TextUtils.isEmpty(mText)) {
            mPaint.setColor(mCurTextColor);
            mPaint.setMaskFilter(null);
            canvas.drawText(mText, (mWidth - mTextWidth) / 2, (mHeight + mTextHeight) / 2, mPaint);
        }

    }


    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Log.d(TAG, "state changede");
        boolean shouldInvalidate = false;
        if (mCircleColor != null && mCircleColor.isStateful()) {
            mCurCircleColor = mCircleColor.getColorForState(getDrawableState(), CIRCLE_COLOR);
            shouldInvalidate = true;
        }

        if (mBdColor != null && mBdColor.isStateful()) {
            mCurBdColor = mBdColor.getColorForState(getDrawableState(), BD_COLOR);
            shouldInvalidate = true;
        }

        if (mRingColor != null && mRingColor.isStateful()) {
            mCurRingColor = mRingColor.getColorForState(getDrawableState(), RING_COLOR);
            shouldInvalidate = true;
        }

        if (mShadowColor != null && mShadowColor.isStateful()) {
            mCurShadowColor = mShadowColor.getColorForState(getDrawableState(), Color.GRAY);
            shouldInvalidate = true;
        }

        if (mTextColor != null && mTextColor.isStateful()) {
            mCurTextColor = mTextColor.getColorForState(getDrawableState(), Color.WHITE);
            shouldInvalidate = true;
        }

        if (shouldInvalidate) {
            invalidate();
        }


    }


    public ColorStateList getRingColor() {
        return mRingColor;
    }

    public void setRingColor(ColorStateList pRingColor) {
        mRingColor = pRingColor;
        mCurRingColor = mRingColor.getColorForState(getDrawableState(), RING_COLOR);
        invalidate();
    }

    public float getRingWidth() {
        return mRingWidth;
    }

    public void setRingWidth(float pRingWidth) {
        mRingWidth = pRingWidth;
        invalidate();
    }

    public float getBdWidth() {
        return mBdWidth;
    }

    public void setBdWidth(float pBdWidth) {
        mBdWidth = pBdWidth;
        invalidate();
    }

    public ColorStateList getShadowColor() {
        return mShadowColor;
    }

    public void setShadowColor(ColorStateList pShadowColor) {
        mShadowColor = pShadowColor;
        mCurShadowColor = mShadowColor.getColorForState(getDrawableState(), Color.GRAY);
        invalidate();
    }

    public float getShadowRadius() {
        return mShadowRadius;
    }

    public void setShadowRadius(float pShadowRadius) {
        mShadowRadius = pShadowRadius;
        if (mShadowRadius > 0) {
            mBlurFilter = new BlurMaskFilter(mShadowRadius, BlurMaskFilter.Blur.SOLID);
        }
        invalidate();
    }

    public float getRippleDistance() {
        return mRippleDistance;
    }

    public void setRippleDistance(float pRippleDistance) {
        mRippleDistance = pRippleDistance;
        invalidate();
    }

    public ColorStateList getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(ColorStateList pCircleColor) {
        mCircleColor = pCircleColor;
        mCurCircleColor = mCircleColor.getColorForState(getDrawableState(), CIRCLE_COLOR);
        invalidate();
    }

    public ColorStateList getBdColor() {
        return mBdColor;
    }

    public void setBdColor(ColorStateList pBdColor) {
        mBdColor = pBdColor;
        mCurBdColor = mBdColor.getColorForState(getDrawableState(), BD_COLOR);
        invalidate();
    }

    public ColorStateList getTextColor() {
        return mTextColor;
    }

    public void setTextColor(ColorStateList pTextColor) {
        mTextColor = pTextColor;
        mCurTextColor = mTextColor.getColorForState(getDrawableState(), Color.WHITE);
        invalidate();
    }

    public String getText() {
        return mText;
    }

    public void setText(String pText) {
        mText = pText;
        if (!TextUtils.isEmpty(mText)) {
            mPaint.setTextSize(mTextSize);
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mTextWidth = mPaint.measureText(mText);
            mTextHeight = mTextSize;
        }
        invalidate();

    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float pTextSize) {
        mTextSize = pTextSize;
        if (!TextUtils.isEmpty(mText)) {
            mPaint.setTextSize(mTextSize);
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mTextWidth = mPaint.measureText(mText);
            mTextHeight = mTextSize;
        }
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        mTimer.cancel();
        Log.d(TAG, "detachced");
        super.onDetachedFromWindow();
    }
}
