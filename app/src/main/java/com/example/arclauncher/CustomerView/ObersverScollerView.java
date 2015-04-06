package com.example.arclauncher.CustomerView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by ’≈œË on 2015/4/6.
 */
public class ObersverScollerView extends ScrollView {
    private OnScrollChangedListener onScrollChangedListener;
    public ObersverScollerView(Context context) {
        super(context);
    }
    public ObersverScollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ObersverScollerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public interface OnScrollChangedListener {
        public void onScrollChanged(int x, int y, int oldX, int oldY);
    }
    public void setOnScrollListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(x, y, oldX, oldY);
        }
    }
}
