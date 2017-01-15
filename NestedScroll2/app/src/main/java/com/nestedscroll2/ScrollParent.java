package com.nestedscroll2;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.OverScroller;

/**
 * Created by 翟锐 on 2016/9/26.
 */

public class ScrollParent extends LinearLayout implements NestedScrollingParent{
    private static final String TAG = "ScrollParent";
    private int topviewHeight;
    private View topView;
    private OverScroller mScroller;

    public ScrollParent(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller=new OverScroller(context);
    }

    public ScrollParent(Context context) {
        super(context);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        topviewHeight=topView.getMeasuredHeight();
        Log.i(TAG,"topheight="+topviewHeight);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topView=findViewById(R.id.topview);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll");
        return true;

    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        Log.e(TAG, "onNestedScrollAccepted");
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        Log.e(TAG, "onStopNestedScroll");
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, "onNestedScroll");
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll");
        super.onNestedPreScroll(target, dx, dy, consumed);
        boolean hiddenTop=dy>0 && getScrollY() <topviewHeight;
        boolean showTop=dy<0 && getScrollY()>=0 && !ViewCompat.canScrollVertically(target,-1);

        if(hiddenTop||showTop){
            scrollBy(0,dy);
            consumed[1]=dy; //消耗y轴滑动事件
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, "onNestedFling");
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {

        Log.e(TAG, "onNestedPreFling");
        if(getScrollY()>=topviewHeight) return false;
        fling((int)velocityY);
        return true;
    }

    @Override
    public int getNestedScrollAxes() {
        Log.e(TAG, "getNestedScrollAxes");
        return 0;
    }

    public void fling(int velocityY){
        mScroller.fling(0,getScrollY(),0,velocityY,0,0,0,topviewHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {

        if(y<0){
            y=0;
        }
        if(y>topviewHeight){
            y=topviewHeight;
        }
        if(y!=getScrollY()){
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(0,mScroller.getCurrY());
            invalidate();
        }
    }
}
