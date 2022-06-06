package com.example.testdemo_3.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;
import com.example.testdemo_3.engine.ProcessInfoProvider;

public class LockScreenService extends Service {
    IntentFilter intentFilter;
    InnerReceiver innerReceiver;
    @Override
    public void onCreate() {
        intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        innerReceiver = new InnerReceiver();
        registerReceiver(innerReceiver, intentFilter);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (innerReceiver!=null){
            unregisterReceiver(innerReceiver);
        }
        super.onDestroy();
    }

    private class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //清理进程
            ProcessInfoProvider.killAll(context);
        }
    }
}
