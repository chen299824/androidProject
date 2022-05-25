package com.cyz.mobilesafe_master.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {

    private static SharedPreferences sp;

    /**
     * 1.写入(boolean)
     * @param ctx 上下文
     * @param key 键
     * @param value 值
     */
    public static void putBoolean(Context ctx,String key,boolean value){
        if (sp == null){
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 2.读取(boolean)
     * @param ctx 上下文
     * @param key 键
     * @param defValue (默认)值
     * @return 默认值或者相应结果
     */
    public static boolean getBoolean(Context ctx,String key,boolean defValue){
        if (sp == null){
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defValue);
    }
}
