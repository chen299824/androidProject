package com.cyz.mobilesafe_master.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

import com.cyz.mobilesafe_master.R;
import com.cyz.mobilesafe_master.service.LockScreenService;
import com.cyz.mobilesafe_master.utils.ServiceUtil;
import com.cyz.mobilesafe_master.utils.SpUtil;

public class ProcessSettingActivity extends Activity {
    CheckBox cb_show_system, cb_lock_clear;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_setting);

        initSystemShow();
        initLockScreenClear();
    }

    private void initLockScreenClear() {

        cb_lock_clear = (CheckBox) findViewById(R.id.cb_lock_clear);

        boolean isRunning = ServiceUtil.isRunning(this, "com.cyz.mobilesafe_master.service.LockScreenService");
        if (isRunning){
            cb_lock_clear.setText("锁屏清理已开启");
        }else {
            cb_lock_clear.setText("锁屏清理已关闭");
        }
        cb_lock_clear.setChecked(isRunning);

        cb_lock_clear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    cb_lock_clear.setText("锁屏清理已开启");
                    //开启服务
                    startService(new Intent(getApplicationContext(), LockScreenService.class));
                }else {
                    cb_lock_clear.setText("锁屏清理已关闭");
                    stopService(new Intent(getApplicationContext(), LockScreenService.class));
                }
            }
        });
    }

    private void initSystemShow() {
        cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);

        boolean showSystem = SpUtil.getBoolean(this, "show_system", false);
        cb_show_system.setChecked(showSystem);
        if (showSystem){
            cb_show_system.setText("显示系统进程");
        }else {
            cb_show_system.setText("隐藏系统进程");
        }
        cb_show_system.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    cb_show_system.setText("显示系统进程");
                }else {
                    cb_show_system.setText("隐藏系统进程");
                }
                SpUtil.putBoolean(ProcessSettingActivity.this, "show_system", isChecked);
            }
        });
    }
}
