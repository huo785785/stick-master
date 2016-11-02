package com.hth.stick.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.OverscrollHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;


/**
 * Created by hth
 *
 * 使用该类继承PullToRefreshBase，并使用泛型 ScrollStickView
 * 来达srollview 的下拉刷新和上拉刷新和吸顶三种效果
 *
 *
 */
public class PtrScrollView extends PullToRefreshBase<ScrollStickView> {

    public PtrScrollView(Context context) {
        super(context);
    }

    public PtrScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public PtrScrollView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }


    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected ScrollStickView createRefreshableView(Context context, AttributeSet attrs) {
        ScrollStickView scrollView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            scrollView = new ScrollStickView(context, attrs);
        } else {
            scrollView = new ScrollStickView(context, attrs);
        }

        scrollView.setId(com.handmark.pulltorefresh.library.R.id.scrollview);
        return scrollView;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        View scrollViewChild = getRefreshableView().getChildAt(0);
        if (null != scrollViewChild) {
            return getRefreshableView().getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }
        return false;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return getRefreshableView().getScrollY() == 0;
    }

    @TargetApi(9)
    final class InternalScrollViewSDK9 extends ScrollView {

        public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PtrScrollView.this, deltaX, scrollX, deltaY, scrollY,
                    getScrollRange(), isTouchEvent);

            return returnValue;
        }

        /**
         * Taken from the AOSP ScrollView source
         */
        private int getScrollRange() {
            int scrollRange = 0;
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
            }
            return scrollRange;
        }
    }

    @Override
    public void setOnScrollChangeListener(View.OnScrollChangeListener l) {
        super.setOnScrollChangeListener(l);
        System.out.println(l+"fsdfsf");
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(callBack!=null)
            callBack.ScrollChanged(t);
    }

    private CallBack callBack;
   public interface CallBack{
        void ScrollChanged(int t);
    }
    public void setCallBack(CallBack callBack){
        this.callBack=callBack;
    }
}
