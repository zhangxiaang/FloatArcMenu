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
 * Created by  ���� on 2015/4/6.
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener{
    private static final int LEFT_TOP = 0;
    private static final int RIGHT_TOP = 1;
    private static final int RIGHT_BOTTOM = 2;
    private static final int LEFT_BOTTOM = 3;

    private View mainButton;
    //Ĭ��λ�������½�
    private Position current_position = Position.LEFT_BOTTOM;
    private int mRadius;//�����Ƿ���ķ���뾶

    //Ĭ�ϲ˵��ǳ��ڹر�ide״̬
    private Status current_status = Status.CLOSE;
    //Ϊ������жϵ�ǰ�Ƿ��ڿɼ�������
    private boolean InArea = true;//Ĭ�Ͽɼ�
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

        //����
        SwitchMenu(300);

    }

    /**
     * �û����Զ��峤��
     * @param duration
     */
    public void SwitchMenu(int duration) {
        //���ص�ʱ��item��Ҫ���ƽ�� �� ��ת����
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
            //�����û���λ�ö�item�˶��ķ�ʽ����һ������
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
            // ��ת����
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
        // �л��˵�״̬
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
    
    //������ť������ת����
    private void rotateMainButn(View v, float start, float end, int duration) {

        RotateAnimation anim = new RotateAnimation(start, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);//���Լ�Ϊ���ĵ�
        anim.setDuration(duration);
        anim.setFillAfter(true);
        v.startAnimation(anim);

    }

    /**
     * �˵���λ�õ�ö����
     */
    private enum Position{
        LEFT_TOP,
        RIGHT_TOP,
        LEFT_BOTTOM,
        RIGHT_BOTTOM
    }


    /**
     * �˵�������״̬
     */
    private enum Status{
        OPEN,
        CLOSE
    }
    //�ڲ˵������ĵ�λ����һ��Button,Ҳ���ǲ˵�������ť
    //��ʽ��ʼ���ķ�ʽ�ȽϺ�
    public ArcMenu(Context context) {
        super(context);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        //��ʼ�������������
        //�������ָ���ͼ�������ô�С���ǳߴ����һ�㶼���õ����������
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        //��ȡ�Զ�������
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, 0, 0);
        //a�������Ҫ����
        int position = a.getInt(R.styleable.ArcMenu_position, RIGHT_BOTTOM);//��ȡλ��ֵ,Ĭ��ֵΪ2
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
        //��һ������ѭ������ÿһ��subView�Ĵ�С
        int counts = getChildCount();
        for (int i = 0; i < counts; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //ָ���ǲ����ļ������ı��ʱ��,Ҳ������Ҫ����subview��ʱ��
        if (changed) {
            LocateCurrentButton();//��λcenterButton

            int counts = getChildCount();
            for (int i = 0; i < counts-1; i++) {
                //��ΪgetChildAt(0);��centerButton ,����ֻ�ܴ�1��ʼȡ
                View child = getChildAt(i + 1);
                child.setVisibility(View.GONE);

                //���ž���Ҫ����child�� left top,�����ʽĬ���ǵ�ʱ��left top�����
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
    //����������Ǹ�centerbutton��λ
    private void LocateCurrentButton() {
        mainButton = getChildAt(0);
        mainButton.setOnClickListener(this);//��centerbutton����click�¼�
        //default value
        int left = 0;
        int top = 0;

        int width = mainButton.getMeasuredWidth();
        int height = mainButton.getMeasuredHeight();
        //����current_position����λ,��������һ��margin
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
                //getMeasuredHeight�ǵ�ǰview�ĸ߶�
                top = getMeasuredHeight() - height;
                //
                break;
        }
        mainButton.layout(left, top, left + width, top + height);
    }

    //��user�Ļص�����
    public void setOnArcMenuItemClickListener(OnArcMenuItemClickListener onArcMenuItemClickListener) {
        this.onArcMenuItemClickListener = onArcMenuItemClickListener;
    }
    public interface OnArcMenuItemClickListener{
        void onItemClick(View view, int position);//���view�ǽ���click�Ķ���,position�����λ��
    }

    public boolean isOpen() {
        return current_status == Status.OPEN;
    }
}
