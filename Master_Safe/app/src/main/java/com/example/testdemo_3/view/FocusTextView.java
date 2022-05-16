package com.example.testdemo_3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


import androidx.annotation.Nullable;

/**
 * 能够获取焦点的自定义TextView
 */
public class FocusTextView extends androidx.appcompat.widget.AppCompatTextView {

    /**
     * 通过在Java代码来创建控件
     * @param context 上下文
     */
    public FocusTextView(Context context) {
        super(context);
    }

    /**
     * 通过在xml代码来创建控件
     * @param context 上下文
     * @param attrs 属性
     */
    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 通过在xml代码（结合Style）来创建控件
     * @param context 上下文
     * @param attrs 属性
     * @param defStyleAttr 样式
     */
    public FocusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 获取焦点的方法
     * @return
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
