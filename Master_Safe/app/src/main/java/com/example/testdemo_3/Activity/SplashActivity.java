package com.cyz.mobilesafe_master.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.GnssAntennaInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyz.mobilesafe_master.R;
import com.cyz.mobilesafe_master.utils.StreamUtil;
import com.cyz.mobilesafe_master.utils.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    protected static final String tag="SplashActivity";

    /**
     * 更新新版本的状态码
     */
    private static final int UPDATE_VERSION = 100;
    /**
     * 进入应用程序主界面的状态码
     */
    private static final int ENTER_HOME = 101;
    /**
     * URL出错的状态码
     */
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;

    private TextView tv_version_name;
    private int mLocalVersionCode;
    private String mVersionDes;
    private String mDownloadUrl;
    private RelativeLayout rl_root;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what)
            {
                case UPDATE_VERSION:
                    //弹出对话框，提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入应用程序主界面
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtil.show(SplashActivity.this, "url异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(getApplicationContext(), "读取异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(SplashActivity.this, "json异常");
                    enterHome();
                    break;
            }

        };
    };

    /**
     * 弹出对话框，提示用户更新
     */
    private void showUpdateDialog() {
        //对话框，是依赖activity存在的
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置左上角图标
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("版本更新");
        //设置描述内容
        builder.setMessage(mVersionDes);

        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //下载apk, apk链接地址，downloadUrl
                downloadApk();
            }
        });

        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //取消对话框，进入主界面
                enterHome();
            }
        });
        builder.show();
    }

    private void downloadApk() {
        //apk下载链接地址，放置apk的所在路径

        //1、判断sd卡是否可用，是否挂在上
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //2、获取sd卡路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"mobilesafe74.apk";
            //3、发送请求，获取apk，并且放置到指定路径
            HttpUtils httpUtils = new HttpUtils();
            //4、发送请求，传递参数（下载地址，下载应用放置的位置）
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功
                    Log.i(tag, "下载成功");
                    File file = responseInfo.result;
                    installApk(file);

                }
                @Override
                public void onFailure(HttpException e, String s) {
                    //下载失败
                    Log.i(tag, "下载失败");

                }
                //刚刚开始下载的方法
                @Override
                public void onStart() {
                    Log.i(tag, "刚刚开始下载");

                    super.onStart();
                }
                //下载过程中的方法（下载apk总大小，当前下载进度，是否正在下载）
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    Log.i(tag, "下载中……");
                    Log.i(tag, "total" + total);
                    Log.i(tag, "current" + current);
                    super.onLoading(total, current, isUploading);
                }
            });

        }
    }

    /**
     * 安装对应apk
     * @param file 安装文件
     */
    private void installApk(File file) {
        //系统应用界面，源码，安装apk入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        /*//文件作为数据源
        intent.setData(Uri.fromFile(file));
        //设置安装类型
        intent.setType("application/vnd.anndroid.package-archive");*/
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.anndroid.package-archive");
        startActivity(intent);
    }

    /**
     * 进入应用程序主界面
     */
    private void enterHome() {
        //
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //在开启一个新的界面后，将导航界面关闭（导航界面只可见一次）
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_spalsh);


        //初始化UI
        initUI();
        //初始化数据
        initData();
        //初始化动画
        initAnimation();
    }

    /**
     * 添加淡入动画效果
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        rl_root.startAnimation(alphaAnimation);
    }

    /**
     * 获取数据的方法
     */
    private void initData() {
        //1，应用版本名称
        tv_version_name.setText("版本名称：" + getVersionName());
        //检测（本地版本号和服务器版本号对比）是否有更新，如有更新，提示用户下载
        //2，获取本地版本号
        mLocalVersionCode = getVersionCode();
        //3，获取服务器版本号（客户端发送请求，服务端给响应，（json，xml））
        //http://www.oxxx.com/update74.json?key=value 返回200请求成功， 流的方式将数据读取下来
        //json中的内容包含
        /*
        *   1、 更新的版本名称
        *   2、新版本的描述信息
        *   3、服务器版本号
        *   4、新版本apk的下载地址
        * */

        checkVersion();

    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        new Thread(){
            @Override
            public void run() {
                //发送请求获取数据，参数则为请求json的链接地址
                //http://10.100.1.31:8080/update74.json 测试阶段不合适，可能随时更换ip地址
                //仅限于模拟器访问电脑tomcat

                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    //1、封装url地址
                   URL url = new URL("http://10.0.2.2:8080/update74.json");
                   //2、开启一个链接
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    //3、设置常见的请求参数（请求头）

                    //请求超时
                    connection.setConnectTimeout(2000);
                    //读取超时
                    connection.setReadTimeout(2000);

                    //POST请求需要写，现在默认是get请求可以不写
                    //connection.setRequestMethod("POST");

                    //4、获取请求响应码
                    if (connection.getResponseCode() == 200){
                        //5、以流的方式，数据获取下来
                        InputStream is = connection.getInputStream();
                        //6、将流转换为字符串（工具类封装）
                        String json = StreamUtil.streamToString(is);
                        Log.i(tag, json);
                        //7、json解析
                        JSONObject jsonObject = new JSONObject(json);


                        jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");

                        Log.i(tag,jsonObject.getString("versionName"));
                        Log.i(tag,jsonObject.getString("versionDes"));
                        Log.i(tag,jsonObject.getString("versionCode"));
                        Log.i(tag,jsonObject.getString("downloadUrl"));

                        //8、比对版本号（服务器版本号>本地版本号）提示用户更新
                        if (mLocalVersionCode<Integer.parseInt(jsonObject.getString("versionCode"))){
                            //提示用户更新（弹出对话框）
                            msg.what = UPDATE_VERSION;
                        }else {
                            //进入应用程序的主界面
                            msg.what = ENTER_HOME;
                        }

                    }
                } catch (MalformedURLException e) {
                    msg.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = IO_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = JSON_ERROR;
                    e.printStackTrace();
                }finally {
                    //指定睡眠时间，请求网络的时长超过4秒不做处理
                    //请求网络的时间小于4秒，强制让其睡眠满4秒
                    long endTime = System.currentTimeMillis();
                    if (endTime - startTime<4000) {
                        try {
                            Thread.sleep(4000-(endTime-startTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }

            }
        }.start();
    }

    /**
     * 返回版本号
     * @return 非0代表成功
     */
    private int getVersionCode() {
        //1,包管理者对象PackageManager
        PackageManager pm = getPackageManager();
        //2，从包的管理者对象中，获取指定包名的基本信息（版本名称，版本号），传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //3，获取版本名称
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称：清单文件中
     * @return 应用版本名称 返回null代表异常
     */
    private String getVersionName() {
        //1,包管理者对象PackageManager
        PackageManager pm = getPackageManager();
        //2，从包的管理者对象中，获取指定包名的基本信息（版本名称，版本号），传0代表获取基本信息
        try {
           PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
           //3，获取版本名称
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 初始化UI方法
     */
    private void initUI() {
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
    }


}
