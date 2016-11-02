package com.handmark.pulltorefresh.library.upanddown;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class UpAndDownOnScrollListener implements OnScrollListener {

	private boolean scrollFlag = false;// 标记是否滑动
	private int lastVisibleItemPosition = 0;// 标记上次滑动位置
	private boolean isUpScroll;
	
	public UpAndDownOnScrollListener(UPandDownScrollListener updownListener) {
		this.updownListener = updownListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		// 当不滚动时
		case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
			scrollFlag = false;
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
			scrollFlag = true;
			break;
		case OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
			scrollFlag = false;
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (scrollFlag) {
			if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
				if (!isUpScroll) {
					isUpScroll = true;
					if (updownListener != null) {
						updownListener.upScroll(view, firstVisibleItem,
								visibleItemCount, totalItemCount);
					}
				}
			} else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
				if (isUpScroll) {
					isUpScroll = false;
					if (updownListener != null) {
						updownListener.downScroll(view, firstVisibleItem,
								visibleItemCount, totalItemCount);
					}
				}
			} else {
				return;
			}
			lastVisibleItemPosition = firstVisibleItem;
		}
	}

	private UPandDownScrollListener updownListener;

	/**
	 * 上滑下滑监听器设置
	 * 
	 * @param updownListener
	 */
	public void setUPandDownScrollListener(
			UPandDownScrollListener updownListener) {
		this.updownListener = updownListener;
	}

	/**
	 * 上滑下滑监听接口
	 * 
	 * @author ZHANGZEYAN
	 * 
	 */
	public interface UPandDownScrollListener {
		void upScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount);

		void downScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount);
	}

}
