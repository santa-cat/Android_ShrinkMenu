package com.example.santa.shrinkmenu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by santa on 16/7/22.
 */
public class MenuItem extends RelativeLayout {
    private FloatingTextView mTextView;
    private FloatingActionButton mButton;
    private int mFabColor = 0xff87dafa;
    private static final int ID_BTN1 = 1;
    private OnClickOnCircleListener mListener;

    public MenuItem(Context context) {
        this(context, null);
    }

    public MenuItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MenuItem(final Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        float density = context.getResources().getDisplayMetrics().density;

        LayoutParams layoutParams = new LayoutParams((int) (density*40), (int) (density*40));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.setMargins((int)(density*6), (int)(density*2), (int)(density*6), (int)(density*8));
        mButton = new FloatingActionButton(context);
        mButton.setLayoutParams(layoutParams);
        mButton.setId(View.generateViewId());
        mButton.setBackgroundTintList(ColorStateList.valueOf(mFabColor));
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mListener) {
                    mListener.onClick();
                }
//                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
            }
        });
        mButton.setPadding((int)(density*6), (int)(density*6), (int)(density*6), (int)(density*6));
        addView(mButton);

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.LEFT_OF, mButton.getId());
        layoutParams.addRule(CENTER_VERTICAL);
        mTextView = new FloatingTextView(context);
        mTextView.setLayoutParams(layoutParams);
        mTextView.setText("haliluya  yayayayyayayay");
        addView(mTextView);


    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setFabColor(int color) {
        mButton.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void setFabDrawable(Drawable drawable) {
        mButton.setImageDrawable(drawable);
    }


    public int getCircleOffsetX() {
        return mButton.getMeasuredWidth()/2 + ((MarginLayoutParams)mButton.getLayoutParams()).rightMargin;
    }

    public int getCircleOffsetY() {
        return mButton.getMeasuredHeight()/2 + ((MarginLayoutParams)mButton.getLayoutParams()).bottomMargin;
    }

    //给用户设置点击菜单的action
    public void setOnClickOnCircleListener(OnClickOnCircleListener listener) {
        mListener = listener;
    }

    public interface OnClickOnCircleListener{
        void onClick();
    }

}
