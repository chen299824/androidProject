package com.example.testdemo_3.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.testdemo_3.R;
import com.example.testdemo_3.constant.ConstantValue;
import com.example.testdemo_3.service.AddressService;
import com.example.testdemo_3.service.BlackNumberService;
import com.example.testdemo_3.service.WatchDogService;
import com.example.testdemo_3.untils.ServiceUtil;
import com.example.testdemo_3.untils.SpUtil;
import com.example.testdemo_3.view.SettingClickView;
import com.example.testdemo_3.view.SettingItemView;

public class SettingActivity extends Activity {

    private String[] mToastStyleDes;
    private int mToastStyle;
    private SettingClickView scv_toast_style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //版本更新开关
        initUpdate();
        //电话归属地的显示设置
        initAddress();
        //设置归属地显示风格
        initToastStyle();
        //归属地提示框的位置
        initLocation();
        //拦截黑名单短信电话
        initBlacknumber();
        //初始化程序锁
        initAppLock();
    }

    /**
     * 初始化程序锁方法
     */
    private void initAppLock() {
        final SettingItemView siv_app_lock = (SettingItemView) findViewById(R.id.siv_app_lock);
        boolean isRunning = ServiceUtil.isRunning(this, "indi.cc.mobilesafe.service.WatchDogService");
        siv_app_lock.setCheck(isRunning);

        siv_app_lock.setOnClickListener((View v)-> {

                boolean isCheck = siv_app_lock.isCheck();
                siv_app_lock.setCheck(!isCheck);
                if (!isCheck) {
                    //开启服务
                    startService(new Intent(getApplicationContext(), WatchDogService.class));
                } else {
                    //关闭服务
                    stopService(new Intent(getApplicationContext(), WatchDogService.class));
                }

        });
    }


    /**
     * 拦截黑名单短信电话
     */
    private void initBlacknumber() {
        final SettingItemView siv_blacknumber = (SettingItemView) findViewById(R.id.siv_blacknumber);
        boolean isRunning = ServiceUtil.isRunning(this, "com/example/testdemo_3/service/BlackNumberService");
        siv_blacknumber.setCheck(isRunning);

        siv_blacknumber.setOnClickListener((View v)-> {

                boolean isCheck = siv_blacknumber.isCheck();
                siv_blacknumber.setCheck(!isCheck);
                if (!isCheck) {
                    //开启服务
                    startService(new Intent(getApplicationContext(), BlackNumberService.class));
                } else {
                    //关闭服务
                    stopService(new Intent(getApplicationContext(), BlackNumberService.class));
                }
        });
    }

    /**
     * 双击居中view所在屏幕位置的处理方法
     */
    private void initLocation() {

    }


    /**
     * 设置归属地显示风格
     */
    private void initToastStyle() {

    }


    /**
     * 创建选中显示样式的对话框
     */
    protected void showToastStyleDialog() {

    }


    /**
     * 是否显示电话号码归属地的方法
     */
    private void initAddress() {
        final SettingItemView siv_address = (SettingItemView) findViewById(R.id.siv_address);

        //对服务是否开的状态做显示


        //点击过程中,状态(是否开启电话号码归属地)的切换过程
        siv_address.setOnClickListener((View v)-> {

                //返回点击前的选中状态
                boolean isCheck = siv_address.isCheck();
                siv_address.setCheck(!isCheck);
                if (!isCheck) {
                    //开启服务,管理吐司
                    startService(new Intent(getApplicationContext(), AddressService.class));
                } else {
                    //关闭服务,不需要显示吐司
                    stopService(new Intent(getApplicationContext(), AddressService.class));
                }

        });

    }


    /**
     * 版本更新开关
     */
    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        //获取已有的开关状态,用作显示
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        //是否选中,根据上一次存储的结果去做决定
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener((View v)-> {

                //如果之前是选中的,点击过后,变成未选中
                //如果之前是未选中的,点击过后,变成选中

                //获取之前的选中状态
                boolean isCheck = siv_update.isCheck();
                //将原有状态取反,等同上诉的两部操作
                siv_update.setCheck(!isCheck);
                //将取反后的状态存储到相应sp中
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !isCheck);

        });
    }
}