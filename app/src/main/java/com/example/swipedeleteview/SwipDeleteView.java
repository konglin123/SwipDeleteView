package com.example.swipedeleteview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SwipDeleteView extends FrameLayout {

    private View contentView;
    private View deleteView;
    private int contentWidth;
    private int deleteWidth;
    private int viewHeight;
    private float startX;
    private float startY;
    private float downX;
    private float downY;
    private Scroller scroller;
    private OnStateChangeListener onStateChangeListener;

    public SwipDeleteView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller=new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        contentView=getChildAt(0);
        deleteView=getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth=contentView.getMeasuredWidth();
        deleteWidth=deleteView.getMeasuredWidth();
        viewHeight=getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        deleteView.layout(contentWidth,0,contentWidth+deleteWidth,viewHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=startX = event.getX();
                downY=startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                float distanceX=endX-startX;

                int scrollToX= (int) (getScrollX()-distanceX);
                if(scrollToX<0){
                    scrollToX=0;
                }else if(scrollToX>deleteWidth){
                    scrollToX=deleteWidth;
                }
                scrollTo(scrollToX,getScrollY());
                startX=event.getX();
                startY=event.getY();

                float DX=Math.abs(endX-downX);
                float DY=Math.abs(endY-downY);
                if(DX>DY&&DX>5){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
            case MotionEvent.ACTION_UP:
                int totalScrollX=getScrollX();
                if(totalScrollX<deleteWidth/2){
                    //关闭delete
                    closeDelete();
                }else{
                    //打开delete
                    openDelete();
                }
                break;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept=false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX=ev.getX();
                onStateChangeListener.onDown(this);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = ev.getX();
                float DX=Math.abs(endX-startX);
                if(DX>8){
                    intercept=true;
                }
                break;
        }
        return intercept;
    }

    private void openDelete() {
        int dx=deleteWidth-getScrollX();
        scroller.startScroll(getScrollX(),getScrollY(),dx,getScrollY());
        invalidate();
        onStateChangeListener.onOpen(this);
    }

    public void closeDelete() {
        //dx等于目标值减去getscrollX
        int dx=0-getScrollX();
        scroller.startScroll(getScrollX(),getScrollY(),dx,getScrollY());
        invalidate();
        onStateChangeListener.onClose(this);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }

    //监听swipDeleteView打开，关闭和按下
    public interface OnStateChangeListener{
        void onOpen(SwipDeleteView swipDeleteView);
        void onClose(SwipDeleteView swipDeleteView);
        void onDown(SwipDeleteView swipDeleteView);
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }


}
