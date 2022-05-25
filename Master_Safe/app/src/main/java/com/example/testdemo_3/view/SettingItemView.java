package com.example.testdemo_3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.testdemo_3.R;

public class SettingItemView extends RelativeLayout {
    /**
     * 自定义的命名空间
     */
    private static final String NAMESPACE = "http://AndroidWork\\Master_Safe\\app\\src\\main\\java\\com\\example\\testdemo_3e";
    /*
    CheckBox
     */
    private CheckBox cb_box;
    /*
    描述文本
     */
    private TextView tv_des;
    /**
     * 自定义命名空间：标题
     */
    private String mDestitle;

    /**
     * 自定义命名空间：关闭按钮后文本
     */
    private String mDesoff;

    /**
     * 自定义命名空间：开启按钮后文本
     */
    private String mDeson;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 1.将xml转换为view，即将设置界面的一个条目转换成view对象，并添加到了当前的类中
        View.inflate(context, R.layout.setting_item_view,this);
        // 2.获取自定义组合控件中的每个控件的实例
        TextView tv_title = findViewById(R.id.tv_title);
        tv_des = findViewById(R.id.tv_des);
        cb_box = findViewById(R.id.cb_box);
        // 3.获取自定义以及原生属性，从AttributeSet attrs参数中获取
        initAttr(attrs);
        // 4.为控件赋值
        tv_title.setText(mDestitle);
    }

    /**
     * 判断是否被选中
     * @return
     */
    public boolean isCheck(){
        //有CheckBox的选中结果，决定当前条目是否开启
        return cb_box.isChecked();
    }

    /**
     * 设置选中的状态
     * @param isCheck
     */
    public void setCheck(boolean isCheck){
        // 当前条目在选择的过程中，cb_box的选中状态也在跟随(isCheck)变化
        cb_box.setChecked(isCheck);
        if (isCheck){
            // 开启
            tv_des.setText(mDeson);
        }else {
            // 关闭
            tv_des.setText(mDesoff);
        }
    }
    /**
     * 3.初始化属性
     * @param attrs 维护好的属性集合
     */
    private void initAttr(AttributeSet attrs) {
        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");
    }
}
