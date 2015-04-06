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
    //���ֵ��һ��������screenHeight+arcMenu.height�ģ�ֻ��Ϊ��ȫ������arcMenu
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
                //�����û�ѡ���item���Բ�ȡ��Ӧ�Ĵ�ʩ
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
        //֮ǰ���ж�getY()һֱ��������ԭ��getY()һֱ���ǵ���0�ķ��صĶ�������ڸ����ֵ�λ��
        scrollerView.setOnScrollListener(new ObersverScollerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY) {
                //��������������
                if (oldY - y > 0) {
                    if (oldY - y > 5 && !arcMenu.isInArea()) {
                        arcMenu.startAnimation(appearAnimation);
                        arcMenu.changeInArea();
                    }
                }
                //��������������
                else {
                    if (!arcMenu.isInArea()) {
                        return;
                    }
                    //�ȱ�֤�ǹرյ�״̬
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
