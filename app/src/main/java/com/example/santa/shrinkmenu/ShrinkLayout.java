package com.example.santa.shrinkmenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;

/**
 * Created by santa on 16/7/22.
 */
public class ShrinkLayout extends ViewGroup {
    private Context mContext;
    private ArrayList<MenuItem> mMenuList;
    private FloatingActionButton mMainButton;
    private View mContent;
    private int mMainFabColor = 0xff87dafa;
    private MarginLayoutParams mLayoutParams;
    private final static int DURATION_SUBMENU = 200;
    private final static int DURATION_MAINMENU = 100;
    private FloatEvaluator mEvaluator = new FloatEvaluator();
    private Interpolator mInterpolator = new AccelerateInterpolator();
    private AnimatorSet mAnimatorSetStart;
    private AnimatorSet mAnimatorSetCancle;
    private final static int NUMMAX_ITEM = 10;
    private final static int STATE_START = 0;
    private final static int STATE_CANCLE = 1;
    private final static int STATE_IDEL = 2;
    private int mStateMainFab = STATE_IDEL;
    private AnimatorSet mAnimatorMBStart;
    private AnimatorSet mAnimatorMBCancle;
    private final static int MAXNUM_SUBMENU = 9;

    public final static String MENU_TEXT = "text";
    public final static String MENU_DRAWABLE = "drawable";
    public final static String MENU_ACTION = "action";

    //touch
    private boolean isTouchOnMenu = false;
    private int mLastPosition;
    private int mActivePointerId;
    private float mMainFabOffsetX;

    public ShrinkLayout(Context context) {
        this(context, null);
    }

    public ShrinkLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShrinkLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ShrinkLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        float density = context.getResources().getDisplayMetrics().density;

        LayoutParams layoutParams = new LayoutParams((int) (density*50), (int) (density*50));
        layoutParams.setMargins((int)(density*16), (int)(density*2), (int)(density*16), (int)(density*16));
        mMainButton = new FloatingActionButton(context);
        mMainButton.setLayoutParams(layoutParams);
        mMainButton.setBackgroundTintList(ColorStateList.valueOf(mMainFabColor));
        mMainButton.setPadding((int)(density*10), (int)(density*10), (int)(density*10), (int)(density*10));
        mMainButton.setImageDrawable(getResources().getDrawable(R.mipmap.setting));

        mMenuList = new ArrayList<>();

        mLayoutParams = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 1) {
            throw new IllegalStateException("more than 1 content view can not be supported");
        }
        mContent = getChildAt(0);
        addView(mMainButton);
        super.onFinishInflate();
    }

    private float getValue(float percent, int start, int end) {
        return mEvaluator.evaluate(mInterpolator.getInterpolation(percent), start, end);
    }

    private void setMainClickListener() {
        mMainButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStateMainFab == STATE_IDEL) {
                    startAnimotor();
                }
            }
        });
    }


    private void startAnimotor() {
        stopAllAnimotor();
        Log.d("DEBUG", "mStateMainFab = "+mStateMainFab);
        if (mStateMainFab == STATE_IDEL) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(mAnimatorMBStart, mAnimatorSetStart);
            animatorSet.start();
        } else if (mStateMainFab == STATE_START){
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(mAnimatorSetCancle, mAnimatorMBCancle);
            animatorSet.start();
        } else if (mStateMainFab == STATE_CANCLE){
            mAnimatorSetStart.start();
        }
    }

    private void stopAllAnimotor() {
        mAnimatorMBStart.cancel();
        mAnimatorMBCancle.cancel();
        mAnimatorSetStart.cancel();
        mAnimatorSetCancle.cancel();
    }

    private void initAnimotion() {
        if (mAnimatorSetStart != null && mAnimatorSetCancle != null) {
            return;
        }
        initStartAnimotion();
        initCancleAnimotion();
//        calculateOffsetXOfMainFab();
        initMainFabStartAnim();
        initMainFabCancleAnim();
        hideSubFab();
    }

    private void hideSubFab() {
        for (int i = 0 ; i<mMenuList.size(); i++) {
            mMenuList.get(i).setVisibility(INVISIBLE);
        }
    }

    private void showSubFab() {
        for (int i = 0 ; i<mMenuList.size(); i++) {
            mMenuList.get(i).setVisibility(VISIBLE);
        }
    }

    private void initStartAnimotion() {
        List<Animator> animators = new ArrayList<>();
        int width = getWidth();
        int height = getHeight();
        for (int i = 0 ; i<mMenuList.size(); i++) {
            MenuItem item = mMenuList.get(i);
            Log.d("DEBUG", "value = "+(i*1.0f)/mMenuList.size());
            Log.d("DEBUG", "value = "+getValue(((i+1)*1.0f)/NUMMAX_ITEM, getWidth()/2 , getWidth()));
            ValueAnimator translationX = ObjectAnimator.ofFloat(item, "translationX", 0, getValue(((i+1)*1.0f)/NUMMAX_ITEM, 0 , width/3));
            translationX.setInterpolator(new AccelerateInterpolator());
            ValueAnimator translationY = ObjectAnimator.ofFloat(item, "translationY", 0, -item.getHeight()*(i+1));
            translationY.setInterpolator(new LinearInterpolator());
            item.setPivotX(item.getWidth() - item.getCircleOffsetX()/2);
            item.setPivotY(item.getHeight()/2);
            ValueAnimator rotation = ObjectAnimator.ofFloat(item, "rotation", 0, getValue(((i+1)*1.0f)/NUMMAX_ITEM, 0 , 30));
            rotation.setInterpolator(new AccelerateInterpolator());
            ValueAnimator scaleX = ObjectAnimator.ofFloat(item, "scaleX", 0, 1);
            scaleX.setInterpolator(new DecelerateInterpolator());
            scaleX.setDuration(2);
            ValueAnimator scaleY = ObjectAnimator.ofFloat(item, "scaleY", 0, 1);
            scaleY.setInterpolator(new DecelerateInterpolator());
            scaleY.setDuration(2);


            animators.add(translationX);
            animators.add(translationY);
            animators.add(rotation);
            animators.add(scaleX);
            animators.add(scaleY);
        }
        mAnimatorSetStart = new AnimatorSet();
        mAnimatorSetStart.playTogether(animators);
        mAnimatorSetStart.setDuration(DURATION_SUBMENU);
        mAnimatorSetStart.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mStateMainFab = STATE_START;
                showSubFab();
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initCancleAnimotion() {
        List<Animator> animators = new ArrayList<>();
        int width = getWidth();
        int height = getHeight();
        for (int i = 0 ; i<mMenuList.size(); i++) {
            MenuItem item = mMenuList.get(i);
            ValueAnimator translationX = ObjectAnimator.ofFloat(item, "translationX", getValue(((i+1)*1.0f)/NUMMAX_ITEM, 0 , width/3), 0);
            translationX.setInterpolator(new DecelerateInterpolator());
            ValueAnimator translationY = ObjectAnimator.ofFloat(item, "translationY",  -item.getHeight()*(i+1), 0);
            translationY.setInterpolator(new LinearInterpolator());
            item.setPivotX(item.getWidth() - item.getCircleOffsetX());
            item.setPivotY(item.getHeight()/2);
            ValueAnimator rotation = ObjectAnimator.ofFloat(item, "rotation", getValue(((i+1)*1.0f)/NUMMAX_ITEM, 0 , 30), 0);
            rotation.setInterpolator(new DecelerateInterpolator());
            ValueAnimator scaleX = ObjectAnimator.ofFloat(item, "scaleX", 1, 0);
            scaleX.setInterpolator(new AccelerateInterpolator());
            ValueAnimator scaleY = ObjectAnimator.ofFloat(item, "scaleY", 1, 0);
            scaleY.setInterpolator(new AccelerateInterpolator());


            animators.add(translationX);
            animators.add(translationY);
            animators.add(rotation);
            animators.add(scaleX);
            animators.add(scaleY);

        }
        mAnimatorSetCancle = new AnimatorSet();
        mAnimatorSetCancle.playTogether(animators);
        mAnimatorSetCancle.setDuration(DURATION_SUBMENU);
        mAnimatorSetCancle.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mStateMainFab = STATE_CANCLE;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mStateMainFab = STATE_IDEL;
                hideSubFab();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private void initMainFabStartAnim() {
        mAnimatorMBStart = new AnimatorSet();
        ValueAnimator translationX = ObjectAnimator.ofFloat(mMainButton, "translationX", 0, -getOffsetXOfMainFab());
        ValueAnimator scaleX = ObjectAnimator.ofFloat(mMainButton, "scaleX", 1f, 0.8f);
        ValueAnimator scaleY = ObjectAnimator.ofFloat(mMainButton, "scaleY", 1f, 0.8f);
        mAnimatorMBStart.playTogether(translationX, scaleX, scaleY);
        mAnimatorMBStart.setDuration(DURATION_MAINMENU);
        mAnimatorMBStart.setInterpolator(new DecelerateInterpolator());
    }

    private void initMainFabCancleAnim() {
        mAnimatorMBCancle = new AnimatorSet();
        ValueAnimator translationX = ObjectAnimator.ofFloat(mMainButton, "translationX", -getOffsetXOfMainFab(), 0);
        ValueAnimator scaleX = ObjectAnimator.ofFloat(mMainButton, "scaleX", 0.8f, 1f);
        ValueAnimator scaleY = ObjectAnimator.ofFloat(mMainButton, "scaleY", 0.8f, 1f);
        mAnimatorMBCancle.playTogether(translationX, scaleX, scaleY);
        mAnimatorMBCancle.setDuration(DURATION_MAINMENU);
        mAnimatorMBCancle.setInterpolator(new DecelerateInterpolator());
    }

    public void addMenuItem(ArrayList<HashMap<String, Object>> list) {
        if (list.size() > MAXNUM_SUBMENU) {
            throw new IllegalStateException("more than 9 submenu can not be supported");
        }

        for (int i = 0 ; i<list.size(); i++) {
            MenuItem menuItem = new MenuItem(mContext);
            menuItem.setText(list.get(i).get(MENU_TEXT).toString());
            menuItem.setFabDrawable((Drawable) list.get(i).get(MENU_DRAWABLE));
            menuItem.setOnClickOnCircleListener((MenuItem.OnClickOnCircleListener) list.get(i).get(MENU_ACTION));
            addView(menuItem, mLayoutParams);
            mMenuList.add(menuItem);
        }
        setMainClickListener();
    }

    private void calculateOffsetXOfMainFab() {
        mMainFabOffsetX = getValue(((mMenuList.size())*1.0f)/NUMMAX_ITEM, 0 , getWidth()/3);
    }

    private float getOffsetXOfMainFab() {
        return mMainFabOffsetX;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (null != mMainButton) {
            measureChildWithMargins(mMainButton, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        if (null != mContent) {
            measureChildWithMargins(mContent, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        for (int i = 0 ; i<mMenuList.size(); i++) {
            measureChildWithMargins(mMenuList.get(i), widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingRight = getPaddingRight();
        int paddingleft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        if (null == mMainButton) {
            throw new IllegalStateException("mMainButton can not be null");
        }
        LayoutParams mlp = (LayoutParams) mMainButton.getLayoutParams();
        int right = getRight() - paddingRight - mlp.rightMargin;
        int left = right - mMainButton.getMeasuredWidth();
//        int left = getWidth()*2/3 - mMainButton.getWidth()/2;
        int bottom = getHeight() - paddingBottom - mlp.bottomMargin;
//        int right = left + mMainButton.getMeasuredWidth();
        int top = bottom - mMainButton.getMeasuredHeight();
        mMainButton.layout(left, top, right, bottom);

        if(null != mContent) {
            MarginLayoutParams mlpC = (MarginLayoutParams) mContent.getLayoutParams();
            int leftC = paddingleft + mlpC.leftMargin;
            int topC = paddingTop + mlpC.topMargin;
            int rightC = leftC + mContent.getMeasuredWidth();
            int bottomC = topC + mContent.getMeasuredHeight();
            mContent.layout(leftC, topC, rightC, bottomC);
        }


        calculateOffsetXOfMainFab();
        for(int i = 0 ; i<mMenuList.size(); i++) {
            MenuItem menuItem = mMenuList.get(i);
            MarginLayoutParams mlpItem = (MarginLayoutParams) menuItem.getLayoutParams();
            int rightItem = (mMainButton.getRight() + mMainButton.getLeft())/2 + menuItem.getCircleOffsetX() - (int)getOffsetXOfMainFab();
//            int rightItem = mMainButton.getRight()+mlpItem.rightMargin - (mMainButton.getWidth()/2 - menuItem.getCircleWidth()/2);
            int bottomItem = (mMainButton.getBottom() + mMainButton.getTop())/2 + menuItem.getCircleOffsetY();
            int leftItem = rightItem - menuItem.getMeasuredWidth();
            int topItem = bottomItem - menuItem.getMeasuredHeight();
            menuItem.layout(leftItem, topItem, rightItem, bottomItem);
        }
        initAnimotion();

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if ((ev.getAction() == MotionEvent.ACTION_MOVE || ev.getAction() == MotionEvent.ACTION_UP) && mStateMainFab == STATE_START) {
            startAnimotor();
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @SuppressWarnings({"unused"})
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

}
