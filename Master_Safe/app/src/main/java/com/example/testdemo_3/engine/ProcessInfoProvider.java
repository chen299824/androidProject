package com.cyz.mobilesafe_engine;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;

import com.cyz.db.domain.ProcessInfo;
import com.cyz.mobilesafe_master.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessInfoProvider {
    //获取进程总数的方法
    public static int getProcessCount(Context ctx){
        //1、获取activityManager
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        //2、获取正在运行的进程的集合
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        //3、返回集合的总数
        return runningAppProcesses.size();
    }

    /**
     *
     * @param ctx
     * @return 返回可用的内存数 bytes
     */
    public static long getAvailSpace(Context ctx){
        //1、获取activityManager
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        //2、构建存储可用内存对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //3、给memoryInfo对象赋值
        am.getMemoryInfo(memoryInfo);
        //4、获取memoryInfo中相应的可用内存大小
        return memoryInfo.availMem;
    }

    /**
     *
     * @param ctx
     * @return 返回总共的内存数 bytes
     */
    public static long getTotalSpace(Context ctx){
        //版本丢弃了
        /*//1、获取activityManager
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        //2、构建存储可用内存对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //3、给memoryInfo对象赋值
        am.getMemoryInfo(memoryInfo);
        //4、获取memoryInfo中相应的可用内存大小
        return memoryInfo.totalMem;*/

        //内存大小写入文件中， 读取proc。meminfo文件， 读取第一行，获取数字字符，转换为btyes返回
        FileReader fileReader=null;
        BufferedReader bufferedReader=null;
        try {
            fileReader = new FileReader("/proc/meminfo");
            bufferedReader = new BufferedReader(fileReader);
            String linOne = bufferedReader.readLine();
            //
            char[] charArray = linOne.toCharArray();
            //循环
            StringBuffer stringBuffer = new StringBuffer();
            for (char c: charArray) {
                if (c>='0' && c<='9'){
                    stringBuffer.append(c);
                }
            }
            return Long.parseLong(stringBuffer.toString())*1024;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
                try {
                    if (fileReader!=null && bufferedReader!=null) {
                        fileReader.close();
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return 0;
    }

    /**
     *
     * @param ctx 上下文环境
     * @return 当前手机正在运行的进程的相关信息
     */
    public static List<ProcessInfo> getProcessInfo(Context ctx){
        //获取进程相关信息
        List<ProcessInfo> processInfoList = new ArrayList<>();
        //1、activityManager管理者对象和PackageManager管理者对象
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = ctx.getPackageManager();
        //2、获取正在运行的进程的集合
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

        //3、循环遍历上述集合， 获取进程相关信息（名称， 包名， 图标， 使用内存大小， 是否为系统进程）
        for (ActivityManager.RunningAppProcessInfo info: runningAppProcesses)
        {
            ProcessInfo processInfo = new ProcessInfo();
            //4、获取进程的名称==应用名
            processInfo.packageName = info.processName;
            //5、获取进程占用的内存大小(传递一个进程对应的pid数组)
            Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{info.pid});
            //6、返回数组中索引位置为0的对象，为当前进程的内存信息的对象
            Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
            //7、获取已使用的大小
            processInfo.memSize = memoryInfo.getTotalPrivateDirty()*1024;
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(processInfo.packageName, 0);
                //8、获取应用的名称
                processInfo.name = applicationInfo.loadLabel(pm).toString();
                //9、获取应用的图标
                processInfo.icon = applicationInfo.loadIcon(pm);
                //10、判断是否为系统进程
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM){
                    processInfo.isSystem = true;
                }else {
                    processInfo.isSystem = false;
                }
            } catch (PackageManager.NameNotFoundException e) {
                //
                processInfo.name = info.processName;
                processInfo.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher);
                processInfo.isSystem = true;
                e.printStackTrace();
            }

            processInfoList.add(processInfo);
        }
        return  processInfoList;
    }

    /**
     * 杀死进程
     * @param ctx
     * @param processInfo
     */
    public static void killProcess(Context ctx, ProcessInfo processInfo) {
        //获取ActivityManager
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        //杀死进程
        am.killBackgroundProcesses(processInfo.packageName);
    }


    public static void killAll(Context ctx) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses){
            /*if (info.processName.equals(ctx.getPackageName())){
                continue;
            }*/
            am.killBackgroundProcesses(info.processName);
        }
    }
}
