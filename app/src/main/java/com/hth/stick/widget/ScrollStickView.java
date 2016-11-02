package com.hth.stick.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 吸顶 控件
 * Created by hth on 2016/11/1.
 */

public class ScrollStickView extends ScrollView{
    public ScrollStickView(Context context) {
        this(context,null);
    }

    public ScrollStickView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollStickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(changed!=null)
            changed.onSrollChanged(t);
    }

    private SrollChanged changed;
    public interface SrollChanged{
        void onSrollChanged(int t);
    }

    public void setChanged(SrollChanged changed) {
        this.changed = changed;
    }
}
