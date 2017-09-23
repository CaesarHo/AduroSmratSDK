package com.adurosmart.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapRegionDecoder;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adurosmart.activitys.MainActivity;
import com.adurosmart.sdk.R;

/**
 * Created by best on 2017/1/6.
 */

public class PopwUtil extends PopupWindow {
    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private RelativeLayout full_screen_rel;
    private View mMenuView;

    public PopwUtil(final MainActivity context, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop, null);
        btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_photo);
        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        full_screen_rel = (RelativeLayout)mMenuView.findViewById(R.id.full_screen_rel);
        full_screen_rel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                dismiss();
            }
        });
        //取消按钮
        btn_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        btn_pick_photo.setOnClickListener(itemsOnClick);
        btn_take_photo.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(context,"ACTION_DOWN",Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(context,"ACTION_UP",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

    }
}
