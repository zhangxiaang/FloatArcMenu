package com.example.arclauncher.CustomerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.example.arclauncher.R;

/**
 * Created by  张翔 on 2015/4/6.
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener{
    private static final int LEFT_TOP = 0;
    private static final int RIGHT_TOP = 1;
    private static final int RIGHT_BOTTOM = 2;
    private static final int LEFT_BOTTOM = 3;

    private View mainButton;
    //默认位置在右下角
    private Position current_position = Position.LEFT_BOTTOM;
    private int mRadius;//是卫星发射的辐射半径

    //默认菜单是出于关闭ide状态
    private Status current_status = Status.CLOSE;
    //为了配合判断当前是否在可见区域内
    private boolean InArea = true;//默认可见
    private OnArcMenuItemClickListener onArcMenuItemClickListener;

    public void changeInArea() {
        InArea = !InArea;
    }

    public boolean isInArea() {
        return InArea == true;
    }
    @Override
    public void onClick(View v) {
        rotateMainButn(v, 0f, 360f, 300);

        //开关
        SwitchMenu(300);

    }

    /**
     * 用户地自定义长度
     * @param duration
     */
    public void SwitchMenu(int duration) {
        //开关的时候item是要添加平移 和 旋转动画
        int counts = getChildCount();
        for (int i = 0; i < counts - 1; i++) {
            final View ChildView = getChildAt(i + 1);
            ChildView.setVisibility(View.VISIBLE);
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (counts - 2) * i));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (counts - 2) * i));

            int xflag = 1;
            int yflag = 1;

            if (current_position == Position.LEFT_TOP
                    || current_position == Position.LEFT_BOTTOM) {
                xflag = -1;
            }

            if (current_position == Position.LEFT_TOP
                    || current_position == Position.RIGHT_TOP) {
                yflag = -1;
            }
            //根据用户的位置对item运动的方式进行一个汇总
            AnimationSet animSet = new AnimationSet(true);
            Animation trananim = null;
            if (current_status == Status.CLOSE) {
                trananim = new TranslateAnimation(xflag * cl, 0, yflag * ct, 0);
                ChildView.setClickable(true);
                ChildView.setFocusable(true);

            } else {
                trananim = new TranslateAnimation(0, xflag * cl, 0, yflag * ct);
                ChildView.setClickable(false);
                ChildView.setFocusable(false);
            }
            trananim.setFillAfter(true);
            trananim.setDuration(duration);
            trananim.setStartOffset((i * 100) / counts);
            trananim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (current_status == Status.CLOSE) {
                        ChildView.setVisibility(View.GONE);
                    }
                }
            });
            // 旋转动画
            RotateAnimation rotateAnim = new RotateAnimation(0, 720,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setDuration(duration);
            rotateAnim.setFillAfter(true);

            animSet.addAnimation(rotateAnim);
            animSet.addAnimation(trananim);
            ChildView.startAnimation(animSet);

            final int pos = i + 1;
            ChildView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onArcMenuItemClickListener != null)
                        onArcMenuItemClickListener.onItemClick(ChildView, pos);

                    menuItemAnim(pos - 1);
                    changeStatus();

                }
            });
        }
        // 切换菜单状态
        changeStatus();
    }

    private void changeStatus()
    {
        current_status = (current_status == Status.CLOSE ? Status.OPEN
                : Status.CLOSE);
    }

    private void menuItemAnim(int pos) {
        for (int i = 0; i < getChildCount() - 1; i++)
        {

            View childView = getChildAt(i + 1);
            if (i == pos)
            {
                childView.startAnimation(scaleBigAnim(300));
            } else
            {

                childView.startAnimation(scaleSmallAnim(300));
            }

            childView.setClickable(false);
            childView.setFocusable(false);

        }
    }

    private Animation scaleSmallAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);
        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;
    }

    private Animation scaleBigAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);

        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;
    }
    
    //给主按钮设置旋转动画
    private void rotateMainButn(View v, float start, float end, int duration) {

        RotateAnimation anim = new RotateAnimation(start, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);//以自己为中心点
        anim.setDuration(duration);
        anim.setFillAfter(true);
        v.startAnimation(anim);

    }

    /**
     * 菜单的位置的枚举类
     */
    private enum Position{
        LEFT_TOP,
        RIGHT_TOP,
        LEFT_BOTTOM,
        RIGHT_BOTTOM
    }


    /**
     * 菜单的两种状态
     */
    private enum Status{
        OPEN,
        CLOSE
    }
    //在菜单的中心的位置是一个Button,也就是菜单的主按钮
    //链式初始化的方式比较好
    public ArcMenu(Context context) {
        super(context);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化工作放在这里，
        //类似这种给视图对象设置大小（是尺寸对象）一般都会用到这个方法的
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        //获取自定义属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, 0, 0);
        //a用完必须要回收
        int position = a.getInt(R.styleable.ArcMenu_position, RIGHT_BOTTOM);//获取位置值,默认值为2
        switch (position) {
            case LEFT_TOP:
                current_position = Position.LEFT_TOP;
                break;
            case RIGHT_TOP:
                current_position = Position.RIGHT_TOP;
                break;
            case RIGHT_BOTTOM:
                current_position = Position.RIGHT_BOTTOM;
                break;
            case LEFT_BOTTOM:
                current_position = Position.LEFT_BOTTOM;
                break;
        }
        mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));

        a.recycle();
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //第一步就是循环测量每一个subView的大小
        int counts = getChildCount();
        for (int i = 0; i < counts; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //指的是布局文件发生改变的时候,也就是需要创建subview的时候
        if (changed) {
            LocateCurrentButton();//定位centerButton

            int counts = getChildCount();
            for (int i = 0; i < counts-1; i++) {
                //因为getChildAt(0);是centerButton ,所以只能从1开始取
                View child = getChildAt(i + 1);
                child.setVisibility(View.GONE);

                //接着就是要计算child的 left top,这个方式默认是的时在left top那里的
                int childLeft = (int) (mRadius * Math.sin(Math.PI / 2 / (counts - 2) * i));
                int childTop = (int) (mRadius * Math.cos(Math.PI / 2 / (counts - 2) * i));
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                if (current_position == Position.LEFT_BOTTOM || current_position == Position.RIGHT_BOTTOM) {
                    childTop = getMeasuredHeight() - childHeight - childTop;
                }
                if (current_position == Position.RIGHT_BOTTOM || current_position == Position.RIGHT_TOP) {
                    childLeft = getMeasuredWidth() - childWidth - childLeft;
                }
                child.layout(childLeft, childTop, childLeft+childWidth, childTop+childHeight);
            }
        }
    }
    //负责的任务是给centerbutton定位
    private void LocateCurrentButton() {
        mainButton = getChildAt(0);
        mainButton.setOnClickListener(this);//给centerbutton设置click事件
        //default value
        int left = 0;
        int top = 0;

        int width = mainButton.getMeasuredWidth();
        int height = mainButton.getMeasuredHeight();
        //根据current_position来定位,并且设置一下margin
        switch (current_position) {
            case LEFT_TOP:

                left = 0;
                top = 0;
                break;
            case RIGHT_TOP:
                left = getMeasuredWidth() - width;
                top = 0;
                break;
            case RIGHT_BOTTOM:
                left = getMeasuredWidth() - width;
                top = getMeasuredHeight() - height;
                break;
            case LEFT_BOTTOM:
                left = 0;
                //getMeasuredHeight是当前view的高度
                top = getMeasuredHeight() - height;
                //
                break;
        }
        mainButton.layout(left, top, left + width, top + height);
    }

    //给user的回调函数
    public void setOnArcMenuItemClickListener(OnArcMenuItemClickListener onArcMenuItemClickListener) {
        this.onArcMenuItemClickListener = onArcMenuItemClickListener;
    }
    public interface OnArcMenuItemClickListener{
        void onItemClick(View view, int position);//这个view是接收click的对象,position点击的位置
    }

    public boolean isOpen() {
        return current_status == Status.OPEN;
    }
}
