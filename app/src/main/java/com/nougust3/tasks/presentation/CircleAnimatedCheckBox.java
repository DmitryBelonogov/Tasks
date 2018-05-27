package com.nougust3.tasks.presentation;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.nougust3.tasks.R;

public class CircleAnimatedCheckBox extends android.support.v7.widget.AppCompatCheckBox {


    private int centerY;
    private int centerX;
    private int outerRadius;
    private int pressedRingRadius;

    private Paint circlePaint;
    private Paint backgroundPaint;

    private float animationProgress = 0f;

    private int pressedRingWidth = 4;
    private int baseRingColor = Color.WHITE;
    private int baseBackColor = 0x66000000;
    private int selectedRingColor = 0xFF33b5e5;
    private int selectedBackColor = 0xcc33b5e5;
    private ObjectAnimator animator;
    private boolean isShow;
    private Drawable d;
    private int checkSize = 18;

    public CircleAnimatedCheckBox(Context context) {
        super(context);
        init(context);
    }

    public CircleAnimatedCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleAnimatedCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if(super.isPressed() != pressed) {
            if (pressed) {
                showSelected();
            } else {
                hideSelected();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (animationProgress >= 0f && animationProgress <= 1f) {
            int toColorSelected = 0;
            int fromColorSelected = 0;
            int toColorBack = 0;
            int fromColorBack = 0;

            if (!isChecked()) {
                if (isShow) {
                    fromColorSelected = baseRingColor;
                    toColorSelected = selectedRingColor;

                    fromColorBack = baseBackColor;
                    toColorBack = baseBackColor;

                } else {
                    fromColorSelected = baseRingColor;
                    toColorSelected = baseRingColor;

                    fromColorBack = selectedBackColor;
                    toColorBack = baseBackColor;
                }
            } else {
                if (isShow) {
                    fromColorSelected = selectedRingColor;
                    toColorSelected = baseRingColor;

                    fromColorBack = selectedBackColor;
                    toColorBack = selectedBackColor;
                } else {
                    fromColorSelected = selectedRingColor;
                    toColorSelected = selectedRingColor;

                    fromColorBack = baseBackColor;
                    toColorBack = selectedBackColor;
                }
            }
            int ringColor = blendColors(toColorSelected, fromColorSelected, animationProgress, false);
            int backColor = blendColors(toColorBack, fromColorBack, animationProgress, true);
            circlePaint.setColor(ringColor);
            backgroundPaint.setColor(backColor);

        }

        canvas.drawCircle(centerX, centerY, pressedRingRadius + animationProgress * pressedRingWidth, backgroundPaint);
        canvas.drawCircle(centerX, centerY, pressedRingRadius + animationProgress * pressedRingWidth, circlePaint);

        int padding = getWidth() - 12;

        d.setBounds(padding, padding, getWidth() - padding, getHeight() - padding);
        if (isChecked()) {
            d.draw(canvas);
        }
    }

    public static int blendColors(int color1, int color2, float amount, boolean inverse) {
        final byte ALPHA_CHANNEL = 24;
        final byte RED_CHANNEL = 16;
        final byte GREEN_CHANNEL = 8;
        final byte BLUE_CHANNEL = 0;

        if (inverse) {
            amount = 1.0f - amount;
        }
        final float inverseAmount = 1.0f - amount;

        int a = ((int) (((float) (color1 >> ALPHA_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> ALPHA_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int r = ((int) (((float) (color1 >> RED_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> RED_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int g = ((int) (((float) (color1 >> GREEN_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> GREEN_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int b = ((int) (((float) (color1 & 0xff) * amount) +
                ((float) (color2 & 0xff) * inverseAmount))) & 0xff;

        return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        outerRadius = Math.min(w, h) / 2;
        pressedRingRadius = outerRadius - pressedRingWidth - pressedRingWidth / 2;
    }

    private void hideSelected() {
        isShow = false;
        animator.setDuration(200);

        animator.setFloatValues(1f, 0f);
        animator.start();
    }

    private void showSelected() {
        isShow = true;
        animator.setDuration(100);
        animator.setFloatValues(animationProgress, 1f);
        animator.start();
    }

    @SuppressLint("ObjectAnimatorBinding")
    private void init(Context context) {
        setBackground(null);
        d = ContextCompat.getDrawable(context, R.drawable.ic_check_grey600_18dp);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);

        circlePaint.setStrokeWidth(pressedRingWidth);
        animator = ObjectAnimator.ofFloat(this, "animationProgress", 0f, 1f);
    }

}