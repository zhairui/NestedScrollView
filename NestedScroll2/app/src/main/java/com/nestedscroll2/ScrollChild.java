package com.nestedscroll2;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.ScrollView;

/**
 * Created by 翟锐 on 2016/9/27.
 */

public class ScrollChild extends ScrollView implements NestedScrollingChild{

    private NestedScrollingChildHelper childHelper;
    private int [] consumed=new int[2];
    private int [] offsetInWindow=new int[2];
    private int downX;
    private int downY;
    private VelocityTracker mVelocityTracker=null;
    private boolean allowFly=false;
    public ScrollChild(Context context) {
        super(context);
    }

    public ScrollChild(Context context, AttributeSet attrs) {
        super(context, attrs);
        childHelper=new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(mVelocityTracker==null){
            mVelocityTracker=VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                allowFly=false;
                downX= (int) ev.getRawX();
                downY= (int) ev.getRawY();
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL|ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX= (int) ev.getRawX();
                int moveY= (int) ev.getRawY();
                int dx=moveX-downX;
                int dy=-(moveY-downY);
                downY=moveY;
                downX=moveX;
                if(dispatchNestedPreScroll(0,dy,consumed,offsetInWindow)){
                    dy=consumed[1];
                    ScrollChild.this.scrollBy(0,dy);
                    allowFly=true;
                }
                break;
            case MotionEvent.ACTION_UP:
                stopNestedScroll();
                if(allowFly){
                    mVelocityTracker.computeCurrentVelocity(1000);
                    int mScrollFly= (int) mVelocityTracker.getYVelocity();
                    fling(-mScrollFly);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void stopNestedScroll() {
        childHelper.stopNestedScroll();

    }
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }
    @Override
    public boolean startNestedScroll(int axes) {
        return childHelper.startNestedScroll(axes);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        childHelper.setNestedScrollingEnabled(enabled);
    }
}
