package com.handmark.pulltorefresh.library.leftdelete;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.R;


/**
 * 带左滑删除的布局
 * @author Administrator
 *
 */
public class LeftDeleteView extends LinearLayout {

    private static final String TAG = "LeftDeleteView";

    private Context mContext;
    private LinearLayout mViewContent;
    private Scroller mScroller;
    private OnSlideListener mOnSlideListener;

    private int mHolderWidth = 100;

    private int mLastX = 0;
    private int mLastY = 0;
    private static final int TAN = 2;
  //  private int type =0;
    
//    /**
//     * 监听删除消息按钮弹出
//     */
//    public static boolean deleteMessageOn =  false; 
//    
//    /**
//     * 监听删除知识库缓存按钮弹出
//     */
//    public static boolean deleteDocOn =  false; 
//    
//    /**
//     * 监听删除笔记按钮弹出
//     */
//    public static boolean deleteNotePad =  false; 
    
    /**
     * 监听是否弹出删除
     */
    public static boolean deleteOpen = false;
    
    private int layout,width;

    public interface OnSlideListener {
        public static final int SLIDE_STATUS_OFF = 0;
        public static final int SLIDE_STATUS_START_SCROLL = 1;
        public static final int SLIDE_STATUS_ON = 2;

        /**
         * @param view current SlideView
         * @param status SLIDE_STATUS_ON or SLIDE_STATUS_OFF
         */
        public void onSlide(View view, int status);
    }

    public LeftDeleteView(Context context,int layout,int width) {
        super(context);
        this.layout = layout;
        this.width = width;
        initView();
    }

    public LeftDeleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mContext = getContext();
        mScroller = new Scroller(mContext);

        setOrientation(LinearLayout.HORIZONTAL);
//        if (type ==0 ) { //消息列表删除
//        	 View.inflate(mContext, R.layout.message_delete_view, this);
//        	 mHolderWidth = 100;
//		}else if(type ==1){ //离线缓存知识库删除
//			 View.inflate(mContext, R.layout.carch_doc_delete_view, this);
//			 mHolderWidth = 70;
//		}else if(type ==2){ //笔记删除
//			 View.inflate(mContext, R.layout.notelist_delete_view, this);
//        	 mHolderWidth = 80;
//		}
        
        View.inflate(mContext, layout , this);
   	 	mHolderWidth = width;
   	 
        mViewContent = (LinearLayout) findViewById(R.id.view_content);
        mHolderWidth = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources()
                        .getDisplayMetrics()));
    }

    public void setButtonText(CharSequence text) {
        ((TextView)findViewById(R.id.delete)).setText(text);
    }

    public void setContentView(View view) {
        mViewContent.addView(view);
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        mOnSlideListener = onSlideListener;
    }

    public void shrink() {
        if (getScrollX() != 0) {
            this.smoothScrollTo(0, 0);
        }
    }

    public void onRequireTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();
        Log.d(TAG, "x=" + x + "  y=" + y);

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            if (mOnSlideListener != null) {
                mOnSlideListener.onSlide(this,
                        OnSlideListener.SLIDE_STATUS_START_SCROLL);
            }
            break;
        }
        case MotionEvent.ACTION_MOVE: {
            int deltaX = x - mLastX;
            int deltaY = y - mLastY;
            if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                break;
            }

            int newScrollX = scrollX - deltaX;
            if (deltaX != 0) {
                if (newScrollX < 0) {
                    newScrollX = 0;
                } else if (newScrollX > mHolderWidth) {
                    newScrollX = mHolderWidth;
                }
                this.scrollTo(newScrollX, 0);
            }
            break;
        }
        case MotionEvent.ACTION_UP: {
            int newScrollX = 0;
            if (scrollX - mHolderWidth * 0.75 > 0) {
                newScrollX = mHolderWidth;
            }
            this.smoothScrollTo(newScrollX, 0);
            if (mOnSlideListener != null) {
            	if (newScrollX == 0) {
//            		if (type == 0)
//            			deleteMessageOn = false;
//					else if(type == 1)
//						deleteDocOn = false;
//            		
//					else if(type == 2)
//						deleteNotePad = false;
            		//MainActivity.mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            		deleteOpen = false;
				}else{
//					if (type == 0)
//            			deleteMessageOn = true;
//					else if(type == 1)
//						deleteDocOn = true;
//					
//					else if(type == 2)
//						deleteNotePad = true;
					//MainActivity.mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
					deleteOpen = true;
				}
                mOnSlideListener.onSlide(this,
                        newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF
                                : OnSlideListener.SLIDE_STATUS_ON);
            }
            break;
        }
        default:
            break;
        }

        mLastX = x;
        mLastY = y;
    }

    private void smoothScrollTo(int destX, int destY) {
        // 缓慢滚动到指定位置
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev) { 
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);  
    }
}
