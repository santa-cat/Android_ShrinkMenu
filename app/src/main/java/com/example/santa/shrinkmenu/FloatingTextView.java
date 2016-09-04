package com.example.santa.shrinkmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by santa on 16/7/22.
 */
public class FloatingTextView extends TextView {
    private float mDensity;
    private static final int DEFAULT_RECT_BG_LIGHT = 0xFFFDFDFD;
    private static final int DEFAULT_TEXT_BG_LIGHT = 0xFFEFEFEF;

    public FloatingTextView(Context context) {
        this(context, null);
    }

    public FloatingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FloatingTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        mDensity = context.getResources().getDisplayMetrics().density;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setShadow(canvas);
    }

    private void setShadow(Canvas canvas) {

        //内部textview圆角
        float[] outerRbg = new float[] { 8, 8, 8, 8, 8, 8, 8, 8 };
        ShapeDrawable bg = new ShapeDrawable(new RoundRectShape(outerRbg, null, null));
        bg.getPaint().setColor(DEFAULT_TEXT_BG_LIGHT);
        setBackground(bg);

        //阴影
        //TODO:低于5.0的版本
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            // 外部矩形弧度
            float[] outerR = new float[] { 8, 8, 8, 8, 8, 8, 8, 8 };
            // 内部矩形与外部矩形的距离
//            RectF inset = new RectF(1000, 100, 500, 500);
            // 内部矩形弧度
//            float[] innerRadii = new float[] { 20, 20, 20, 20, 20, 20, 20, 20 };

            ShapeDrawable bgRect = new ShapeDrawable(new RoundRectShape(outerR, null, null));
            ViewCompat.setElevation(this, 3 * mDensity);
            bgRect.getPaint().setColor(DEFAULT_RECT_BG_LIGHT);
//            setBackgroundDrawable(bgRect);
            bgRect.draw(canvas);
        }

    }

}
