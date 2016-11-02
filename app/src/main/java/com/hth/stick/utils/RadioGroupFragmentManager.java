package com.hth.stick.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 
* @ClassName: RadioGroupFragmentManager
* @Description: 用一句话描述该文件做什么
* @author tanlifei 
* @date 2015年2月8日 上午9:29:00
*
 */
public class RadioGroupFragmentManager {

	private Fragment mCurrentPage;
	private FragmentManager manager;
	private int tabContentId;//替换碎片id

	public RadioGroupFragmentManager(FragmentManager manager, int tabContentId) {
		super();
		this.manager = manager;
		this.tabContentId = tabContentId;
	}

	public void switchFragments(Fragment fragment) {
		FragmentTransaction ft = manager.beginTransaction();
		if (mCurrentPage == null) {
			ft.add(tabContentId, fragment).commitAllowingStateLoss();
		} else {
			if (fragment.isAdded()) {
				ft.hide(mCurrentPage).show(fragment).commitAllowingStateLoss();
			} else {
				ft.hide(mCurrentPage).add(tabContentId, fragment).commitAllowingStateLoss();
			}
		}
		mCurrentPage = fragment;
	}

	public Fragment getmCurrentPage() {
		return mCurrentPage;
	}


	

}
