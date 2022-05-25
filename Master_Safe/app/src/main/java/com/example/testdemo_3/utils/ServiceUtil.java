package com.cyz.mobilesafe_master.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ServiceUtil {

    /**
     * 判断服务是否运行
     * @param serviceName 服务名
     * @param context 上下文环境
     * @return 运行结果
     */
    public static boolean isRunning(Context context,String serviceName){
        // 1.获取activityManager管理者对象，可以去获取当前手机正在运行的所有服务
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 2.获取手机中正在运行的服务（多少个服务）
        List<ActivityManager.RunningServiceInfo> runningServices = mActivityManager.getRunningServices(100);
        // 3.遍历获取的所有的服务集合，拿到每一个服务的类名称和传递进来的类名称作比对，如果一致说明服务正在运行
        for (ActivityManager.RunningServiceInfo runningService : runningServices) {
            if (runningService.service.getClassName().equals(serviceName)){
                return true;
            }
        }
        return false;
    }
}
