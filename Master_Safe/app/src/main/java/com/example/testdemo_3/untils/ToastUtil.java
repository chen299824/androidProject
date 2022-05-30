package com.example.testdemo_3.untils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    //打印吐司
    public static  void show(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
