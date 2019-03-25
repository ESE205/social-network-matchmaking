package com.example.cutetogether;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


//https://stackoverflow.com/questions/20000018/set-viewpager-height-inside-scrollview-in-android

public class WrapContentViewPager extends ViewPager {

    private int mCurrentPagePosition = 0;
    private boolean scrollable = true;

    public WrapContentViewPager(@NonNull Context context) {
        super(context);
    }

    public WrapContentViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            View child = getChildAt(mCurrentPagePosition);
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void reMeasureCurrentPage(int position) {
        mCurrentPagePosition = position;
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        if(!scrollable){
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isScrollable(){
        return scrollable;
    }

    public void setScrollable(boolean scrollable){
        this.scrollable = scrollable;
    }


}
