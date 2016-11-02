package com.handmark.pulltorefresh.library.leftdelete;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;


/**
 * 带左滑删除和下拉刷新的ListView
 * @author Administrator
 *
 */
public class LeftDeleteListView extends ListView {

    private static final String TAG = "ListViewCompat";

    private LeftDeleteView mFocusedItemView;
    
    private int mLastX = 0;
    private int mLastY = 0;
    
    private boolean isDelete;
    
    public LeftDeleteListView(Context context) {
        super(context);
    }

    public LeftDeleteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LeftDeleteListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void shrinkListItem(int position) {
        View item = getChildAt(position);

        if (item != null) {
            try {
                ((LeftDeleteView) item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

	@SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent event) {
    	int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) { //按下
        	int position = pointToPosition(x, y);
            if (position != INVALID_POSITION) {
            	if (getItemAtPosition(position) instanceof LeftDeleteBean) {
            		LeftDeleteBean data = (LeftDeleteBean) getItemAtPosition(position);
                	if (data != null) {
                		 mFocusedItemView = data.slideView;
    				}
                    Log.e(TAG, "FocusedItemView=" + mFocusedItemView);
				}/*else if (getItemAtPosition(position) instanceof CourseNoteListBean) {
            		CourseNoteListBean data = (CourseNoteListBean) getItemAtPosition(position);
                	if (data != null) {
                		 //mFocusedItemView = data.slideView;
                		 mFocusedItemView = data.getSlideView();
    				}
//					mFocusedItemView = (LeftDeleteView) getChildAt(position);
                    Log.e(TAG, "FocusedItemView=" + mFocusedItemView);
				}*/
            }
            if (mFocusedItemView != null) {
                mFocusedItemView.onRequireTouchEvent(event);
            }
            mLastX = x;
            mLastY = y;
            return true;
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){ //移动
			 int deltaX = x - mLastX;
	    	 int deltaY = y - mLastY;
	    	 if (mFocusedItemView != null) {
	    		 mFocusedItemView.onRequireTouchEvent(event);
	         }
	         if (Math.abs(deltaX)>=5 && Math.abs(deltaY)<=5) {
	        	 isDelete = true;
	        	 return true;
			 }else{
				 if (LeftDeleteView.deleteOpen) {
					 isDelete = true;
					 return true;
				 }else{
					 isDelete = false;
					 return super.onTouchEvent(event);
				 }

			 }
		}else if(event.getAction() == MotionEvent.ACTION_UP){ //拾起
	    	if (mFocusedItemView != null) {
		        mFocusedItemView.onRequireTouchEvent(event);
		    }
	    	if (isDelete) {
	    		return true;
			}else{
				return super.onTouchEvent(event);
			}
		}else{
			return super.onTouchEvent(event);
		}
    }

}
