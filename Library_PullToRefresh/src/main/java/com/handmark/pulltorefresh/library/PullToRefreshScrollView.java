/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.stickyview.StickyScrollView;

public class PullToRefreshScrollView extends PullToRefreshBase<ScrollView> {


    private View mActionBar;
    private Drawable mBgDrawable;
    private int fadingHeight = 800;
    private int oldY;
    private int fadingOffset;
    public static final int ALPHA_START = 0;
    public static final int ALPHA_END = 255;

    public PullToRefreshScrollView(Context context) {
        super(context);
    }

    public PullToRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshScrollView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
        ScrollView scrollView;
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            scrollView = new InternalScrollViewSDK9(context, attrs);
        } else {
            scrollView = new ScrollView(context, attrs);
        }

        scrollView.setId(R.id.scrollview);
        return scrollView;
    }

    @Override
    protected ScrollView createStickScrollView(Context context, AttributeSet attrs) {
        ScrollView scrollView;
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            scrollView = new InternalStickyScrollViewSDK9(context, attrs);
        } else {
            scrollView = new ScrollView(context, attrs);
        }

        scrollView.setId(R.id.scrollview);
        return scrollView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        View scrollViewChild = mRefreshableView.getChildAt(0);
        if (null != scrollViewChild) {
            return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }
        return false;
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
            OverscrollHelper.overScrollBy(PullToRefreshScrollView.this, deltaX, scrollX, deltaY, scrollY,
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


        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);

            if (t <= -1) {
                updateActionBarAlpha(ALPHA_START);
            } else if (t <= fadingHeight && t >= 0) {
                updateActionBarAlpha(t * (ALPHA_END - ALPHA_START) / fadingHeight + ALPHA_START);
            } else {
                updateActionBarAlpha(ALPHA_END);
            }

        }


//		@Override
//		public boolean onTouchEvent(MotionEvent ev) {
//
//			switch (ev.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					oldY = (int) ev.getY();
//					break;
//				case MotionEvent.ACTION_MOVE:
//					int scrollY = getScrollY();
//
//					int y = (int) ev.getY();
//					int deltaY = y - oldY;
//
//					int willScrollY = scrollY - deltaY;
//
//					if (willScrollY > fadingHeight) {
//						willScrollY = fadingHeight;
//					}
//
//					if (willScrollY < 0) {
//						willScrollY = 0;
//					}
//
//					//scrollTo(0, willScrollY);
//					if (willScrollY == 0){
//						updateActionBarAlpha(0);
//					}else{
//						updateActionBarAlpha(willScrollY*(ALPHA_END-ALPHA_START)/fadingHeight+ALPHA_START);
//					}
//					oldY = y;
//
//					break;
//				case MotionEvent.ACTION_UP:
//					break;
//			}
//
//			return super.onTouchEvent(ev);
//		}

    }

    @TargetApi(9)
    final class InternalStickyScrollViewSDK9 extends StickyScrollView {

        public InternalStickyScrollViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshScrollView.this, deltaX, scrollX, deltaY, scrollY,
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

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);

            if (t <= -1) {
                updateActionBarAlpha(ALPHA_START);
            } else if (t <= fadingHeight && t >= 0) {
                updateActionBarAlpha(t * (ALPHA_END - ALPHA_START) / fadingHeight + ALPHA_START);
            } else {
                updateActionBarAlpha(ALPHA_END);
            }


        }

//		@Override
//		public boolean onTouchEvent(MotionEvent ev) {
//
//			switch (ev.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					oldY = (int) ev.getY();
//					break;
//				case MotionEvent.ACTION_MOVE:
//					int scrollY = getScrollY();
//
//					int y = (int) ev.getY();
//					int deltaY = y - oldY;
//
//					int willScrollY = scrollY - deltaY;
//
//					if (willScrollY > fadingHeight) {
//						willScrollY = fadingHeight;
//					}
//
//					if (willScrollY < 0) {
//						willScrollY = 0;
//					}
//
//					//scrollTo(0, willScrollY);
//					if (willScrollY == 0){
//						updateActionBarAlpha(0);
//					}else{
//						updateActionBarAlpha(willScrollY*(ALPHA_END-ALPHA_START)/fadingHeight+ALPHA_START);
//					}
//					oldY = y;
//
//					break;
//				case MotionEvent.ACTION_UP:
//					break;
//			}
//
//			return super.onTouchEvent(ev);
//		}
    }


    public void setActionBarBgDrawable(Drawable bgDrawable) throws Exception {
        if (mActionBar == null) {
            throw new Exception("Please try to binding the actionBar before set it's background.");
        }

        mBgDrawable = bgDrawable;
        mBgDrawable.setAlpha(ALPHA_START);
        mActionBar.setBackgroundDrawable(mBgDrawable);
        mActionBar.setBackgroundDrawable(getResources().getDrawable(
                getResources().getIdentifier("new_top_bg", "drawable", getmContext().getPackageName())));

    }

    public void setActionBarAlpha(int alpha) throws Exception {
        if (mActionBar == null || mBgDrawable == null) {
            throw new Exception("acitonBar is not binding or bgDrawable is not set.");
        }
        if (alpha < 10){
            mActionBar.setBackgroundDrawable(getResources().getDrawable(
                    getResources().getIdentifier("new_top_bg", "drawable", getmContext().getPackageName())));
        }else {
            mBgDrawable.setAlpha(alpha);
            mActionBar.setBackgroundDrawable(mBgDrawable);
        }
    }

    void updateActionBarAlpha(int alpha) {
        try {
            setActionBarAlpha(alpha);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public View getActionBar() {
        return mActionBar;
    }

    public void setFadingOffset(int height) {
        fadingOffset = height;
    }

    public void bindingActionBar(View actionBar) {
        mActionBar = actionBar;
    }
}
