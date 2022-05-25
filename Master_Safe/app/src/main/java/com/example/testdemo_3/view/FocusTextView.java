package com.cyz.mobilesafe_master.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class FocusTextView extends TextView {
    //使用在通过java代码创建控件
    public FocusTextView(Context context) {
        super(context);
    }

    //由系统调用（带属性+上下文环境构造方法）
    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //有系统调用（带属性+上下文环境构造方法+布局文件中定义样式文件构造方法）
    public FocusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
