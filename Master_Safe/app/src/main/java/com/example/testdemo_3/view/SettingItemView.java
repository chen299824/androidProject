package com.cyz.mobilesafe_master.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyz.mobilesafe_master.R;

public class SettingItemView extends RelativeLayout {
    CheckBox cb_box;
    TextView tv_des;
    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml---->view将设置界面的一个条目转换为view对象,直接添加到了当前settignitemview对应的view中
        View view = View.inflate(context, R.layout.setting_item_view, null);
        this.addView(view);

        //自定义组合控件中的标题描述
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);


    }

    /**
     *
     * @return 返回当前settingitemview是否选中状态 true表示开启， false表示关闭
     */
    public boolean isCheck(){
        //由checkBox的选中结果，决定当前条目是否开启
        return cb_box.isChecked();
    }

    /**
     *
     * @param isCheck 是否作为开启的变量，有点击过程去做传递
     */
    public void setCheck(boolean isCheck){
        //当前条目在选择过程中， cb_box选中状态也随着改变
        cb_box.setChecked(isCheck);
        if (isCheck){
            tv_des.setText("自动更新已开启");
        }else {
            tv_des.setText("自动更新已关闭");
        }
    }
}
