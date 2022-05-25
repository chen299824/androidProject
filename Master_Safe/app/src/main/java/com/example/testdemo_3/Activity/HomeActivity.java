package com.cyz.mobilesafe_master.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.cyz.mobilesafe_master.R;

public class HomeActivity extends Activity {
    GridView gv_home;
    String[] mTitleStr;
    int[] mDrawableIds;
    TextView tv_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //初始化
        initUI();
        //初始化数据的方法
        initData();
    }

    private void initData() {
        //准备数据（数字9组，图片9张）
        mTitleStr = new String[]{
                "手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"
        };
        mDrawableIds = new int[]{
                R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
                R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings
        };

        //九宫格控件设置数据适配器
        gv_home.setAdapter(new MyAdapter());
        //注册九宫格单个条目点击事件
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:

                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), ProcessManagerActivity.class));
                        break;
                    case 8:
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;
                    default:
                }
            }
        });
    }

    private void initUI() {
        gv_home  =(GridView)findViewById(R.id.gv_home);

    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            //条目的总数 文字的组数 == 图片的组数
            return mTitleStr.length;
        }

        @Override
        public Object getItem(int i) {
            return mTitleStr[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            TextView tv_title = (TextView) view1.findViewById(R.id.tv_title);
            ImageView iv_icon = (ImageView) view1.findViewById(R.id.iv_icon);
            System.out.println(tv_title);
            tv_title.setText(mTitleStr[i]);
            iv_icon.setBackgroundResource(mDrawableIds[i]);
            return view1;
        }
    }
}
