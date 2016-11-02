package com.hth.stick.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hth.stick.R;
import com.hth.stick.utils.RadioGroupFragmentManager;
import com.hth.stick.widget.PtrScrollView;
import com.hth.stick.widget.ScrollStickView;

public class MainActivity extends AppCompatActivity implements ScrollStickView.SrollChanged {

    private ScrollStickView scrollStickView;
    private LinearLayout linearLayout;//大小和吸顶布局一样
    private RadioGroup llStick;//吸顶布局
    private int lastScrlll;//ScrollView滑动的距离
    private boolean isFirst;//防止一开始布局吸顶
    private StickFragment[] mFragments = new StickFragment[2];
    private int lasttab1;//记录一个tab 滑动距离
    private int lasttab2;//记录二个tab 滑动距离
    private boolean isStick;//判断当前是否是吸顶状态
    private PtrScrollView ptrScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        scrollStickView.setChanged(this);
        scrollStickView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (isFirst)
                    onSrollChanged(scrollStickView.getScrollY());

            }
        });
        //设置下拉刷新模式
        ptrScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        ptrScrollView.setCallBack(new PtrScrollView.CallBack() {
            @Override
            public void ScrollChanged(int t) {
                if (t > 0) {//用一个相同的吸顶布局放置在PullToRefresh同级放在上拉加载时候 吸顶布局闪动
//                    int scrollY = lastScrlll + t;
//                    if (lastScrlll != 0)
//                        llStick.setTranslationY(scrollY);
                    findViewById(R.id.lldd).setVisibility(View.VISIBLE);
                } else {
//                    llStick.setTranslationY(Math.max(linearLayout.getTop(), lastScrlll));
                    findViewById(R.id.lldd).setVisibility(View.GONE);
                }
            }
        });
    }

    private void initData() {
        for (int i = 0; i < mFragments.length; i++) {
            mFragments[i] = StickFragment.newInstance(i + "");
        }
        final RadioGroupFragmentManager mRGFM = new RadioGroupFragmentManager(
                getSupportFragmentManager(),
                R.id.fl_layout);
        llStick.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.tvIng) {
                    lasttab2 = lastScrlll;
                    mRGFM.switchFragments(mFragments[0]);
                } else {
                    lasttab1 = lastScrlll;
                    mRGFM.switchFragments(mFragments[1]);

                }
            }
        });
        llStick.getChildAt(0).performClick();
    }

    /**
     * 显示滚动记录
     *
     */
    public void showRemenber(String type){
        if(type.equals("0")){//0:代表第一个tab；1：代表第二个tab
            scrollStickView.smoothScrollTo(0, lasttab1);
        }else{
            scrollStickView.smoothScrollTo(0, lasttab2);
        }
    }
    private void initView() {
        ptrScrollView = (PtrScrollView) findViewById(R.id.totop_scrollview);
        scrollStickView = ptrScrollView.getRefreshableView();
        linearLayout = (LinearLayout) findViewById(R.id.ll);
        llStick = (RadioGroup) findViewById(R.id.stick);
    }

    @Override
    public void onSrollChanged(int t) {
        if (isFirst) {
            if (scrollStickView.getScrollY() < linearLayout.getTop()) {
                isStick = false;
                lasttab1 = lastScrlll;
                lasttab2 = lastScrlll;
            } else {
                if (!isStick) {
                    isStick = true;
                    lasttab1 = linearLayout.getTop();
                    lasttab2 = linearLayout.getTop();
                }
            }
            lastScrlll = t;
            llStick.setTranslationY(Math.max(lastScrlll, linearLayout.getTop()));
        } else {
            isFirst = true;
            scrollStickView.smoothScrollTo(0, 0);
        }

    }


}
