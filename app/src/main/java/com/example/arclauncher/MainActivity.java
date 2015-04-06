package com.example.arclauncher;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.arclauncher.CustomerView.ArcMenu;
import com.example.arclauncher.CustomerView.ObersverScollerView;


public class MainActivity extends Activity {
    private int currentX;
    private int currentY;
    //这个值是一个超过了screenHeight+arcMenu.height的，只是为了全面隐藏arcMenu
    private int targetY;
    private ArcMenu arcMenu;
    private ObersverScollerView scrollerView;


    private int height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void init() {
        arcMenu = (ArcMenu) findViewById(R.id.arc_menu);
        currentX = (int) arcMenu.getX();
        currentY = (int) arcMenu.getY();
        targetY=currentY-arcMenu.getHeight()+150;
        arcMenu.setOnArcMenuItemClickListener(new ArcMenu.OnArcMenuItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //根据用户选择的item可以采取相应的措施
                Toast.makeText(MainActivity.this, "~:" + position, Toast.LENGTH_SHORT).show();
            }
        });

        final TranslateAnimation disappearAnimation = new TranslateAnimation(currentX,currentX,currentY,targetY);
        disappearAnimation.setFillAfter(true);
        disappearAnimation.setDuration(300);

        final TranslateAnimation appearAnimation = new TranslateAnimation(currentX,currentX,targetY,currentY);
        appearAnimation.setDuration(300);
        appearAnimation.setFillAfter(true);

        scrollerView = (ObersverScollerView) findViewById(R.id.scrollerview);
        //之前的判断getY()一直不起作用原来getY()一直都是等于0的返回的都是相对于父布局的位置
        scrollerView.setOnScrollListener(new ObersverScollerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY) {
                //滚动条正在向上
                if (oldY - y > 0) {
                    if (oldY - y > 5 && !arcMenu.isInArea()) {
                        arcMenu.startAnimation(appearAnimation);
                        arcMenu.changeInArea();
                    }
                }
                //滚动条正在向下
                else {
                    if (!arcMenu.isInArea()) {
                        return;
                    }
                    //先保证是关闭的状态
                    if(arcMenu.isOpen()) {
                        arcMenu.SwitchMenu(300);
                    }
                    arcMenu.startAnimation(disappearAnimation);
                    arcMenu.changeInArea();
                }
            }
        });
    }
}
