package com.cyz.mobilesafe_master.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.cyz.mobilesafe_master.R;
import com.cyz.mobilesafe_master.View.SettingItemView;

public class SettingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
    }

    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果之前是选中的，点击过后，变成选中;反之……

                //获取之前的选中状态
                boolean isCheck = siv_update.isCheck();
                siv_update.setCheck(!isCheck);
            }
        });
    }

}
