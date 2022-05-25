package com.example.testdemo_3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testdemo_3.constant.ConstantValue;
import com.example.testdemo_3.untils.SharedPreferencesUtil;
import com.example.testdemo_3.untils.ToastUtil;

import com.example.testdemo_3.untils.StreamUtil;
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

    /**
     * 更新新版本的状态码
     */
    private static final int UPDATE_VERSION = 100;
    /**
     * 进入主界面的状态码
     */
    private static final int ENTER_HOME = 101;
    /**
     * url出错的状态码
     */
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;
    private TextView tv_version_name;
    //本地版本号（成员变量）
    private int mLocalVersionCode;
    private String mVersionDes;
    private String mDownloadUrl;
    private RelativeLayout rl_root;
    private static final String tag = "SplashActivity";

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case UPDATE_VERSION:
                    //弹出对坏框，提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入主界面，activity跳转过程
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtil.show(getApplicationContext(),"url异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(getApplicationContext(),"地区异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(getApplicationContext(),"json解析异常");
                    enterHome();
                    break;
                default:break;
            }
        }
    };

    /**
     * 弹出对话框，提示用户更新
     */
    private void showUpdateDialog() {
        //对话框，是依赖于activity存在的
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置左上角图标
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("版本更新");
        //设置描述内容
        builder.setMessage(mVersionDes);
        //设置按钮，立即更新
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //下载apk，apk链接地址
                downloadApk();
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 取消对话框，进入主界面
                enterHome();
            }
        });// 消极按钮，“否”
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // 按下回退后，进入主界面，然后隐藏对话框
                enterHome();
                dialog.dismiss();
            }
        });// 回退按钮
        builder.show();
    }

    /**
     * apk的下载
     */
    private void downloadApk() {
        //apk下载地址，放置apk所在的路径

        //1.判断SD卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //2.获取sd路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator+"mobilesafe.apk";
            //3.发送请求，获取apk，并放置到指定的路径
            HttpUtils httpUtils = new HttpUtils();
            //4.发送请求，传递参数
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功
                    File file = responseInfo.result;
                    //提示用户安装
                    installApk(file);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //下载失败
                }
                //刚刚开始下载
                @Override
                public void onStart() {
                    super.onStart();
                }
                //下载过程中的方法（下载apk总大小，当前下载位置，是否正在下载）
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                }
            });
        }
    }

    /**
     * 安装APK
     */
    private void installApk(File file) {
        // 系统应用界面，源码，安装apk入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        // 文件作为数据源
        // intent.setData(Uri.fromFile(file));
        // 设置安装的类型
        // intent.setType("application/vnd.android.package-archive");
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        //startActivity(intent);
        startActivityForResult(intent,0);
    }

    /**
     * 开启一个activity后，返回结果调用的方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 进入应用程序主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        //开启一个新的界面后，将导航界面关闭
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //去掉当前activity的有title,仅限于当前页面
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        //初始化UI
        initUI();
        //获取数据
        initData();
        //初始化冻坏
        initAnimation();
    }

    /**
     * 初始化动画方法
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        rl_root.startAnimation(alphaAnimation);
    }

    /**
     * 获取数据方法
     */
    private void initData()  {
        //2.获取版本名称
       tv_version_name.setText("版本名称"+getVersionName());
       //获取版本的更新，用本地版本与服务器版本进行比较
        //2.获取本地版本号
        mLocalVersionCode = getVersionCode();
        //3.获取服务器版本（客户端请求，服务端响应，json）
        //http:www.xxx.com/updata.json?key=value  返回请求成功，流的方式将数据读取下来
        if (SharedPreferencesUtil.getBoolean(this, ConstantValue.OPEN_UPDATE,false)){
            checkVersion();
        }
        else {
            // 这里不调用enterHome()，是因为直接调用会很快进入主界面（HomeActivity)，跳过闪屏页面（SplashActivity）
            // 也不选择Thread.sleep(4000)后再enterHome()，是因为在主线程阻塞4秒风险太大，若到达7秒则会ANR
            // 所以选择发送消息的形式来实现在没有选择“自动更新”的情况下直接进入主界面，即时发送消息后，延时4秒钟，否则会太快进入主界面
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,4000);
        }
    }

    /**
     * 检查版本号方法
     */
    private void checkVersion() {
        new Thread(){
            public void run(){
                //发送请求数据，参数则为请求json的参数地址
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    //1.封装url地址
                    URL url = new URL("http://10.0.2.2:8080/update.json");
                    //2.开启一个连接
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        //3.设置常见请求参数

                    //请求超时
                    connection.setConnectTimeout(2000);
                    //读取超时
                    connection.setReadTimeout(2000);
                    //请求方式，默认为get
                    //connection.setRequestMethod("POST");

                    //4.获取请求成功响应码
                    if(connection.getResponseCode() == 200){
                        //5.以流的方式，将数据获取下来
                        InputStream is = connection.getInputStream();
                        //6.将流装换成字符串（工具类）
                        String json = StreamUtil.streamToString(is);
                        Log.i(tag,json);  //用日志进行测试
                        //7.解析json
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");
                        String versionCode = jsonObject.getString("versionCode");
                        mVersionDes = jsonObject.getString("versionDes");
                        mDownloadUrl = jsonObject.getString("downloadUrl");

                        //8.比对版本号（提示用户 更新）
                        if(mLocalVersionCode < Integer.parseInt(versionCode)){
                            //提示用户更新，弹出对话框（UI）消息机制
                            msg.what = UPDATE_VERSION;
                        }else{
                            //进入主界面
                            msg.what = ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                }  catch (IOException e) {
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                }finally {

                  long endTime = System.currentTimeMillis();
                  //splash页面转到主页面的时间调整为4s
                  if (endTime-startTime < 4000){
                      try {
                          Thread.sleep(4000-(endTime-startTime));
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  }
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 获取本地版本号方法
     */
    private int getVersionCode() {
        //1.包管理对象packageManager
        PackageManager pm = getPackageManager();
        //2.从包管理对象中，获取指定包名的基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(),0);
            //3.获取版本名称
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称方法
     */
    private String getVersionName()  {
        //1.包管理对象packageManager
        PackageManager pm = getPackageManager();
        //2.从包管理对象中，获取指定包名的基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(),0);
            //3.获取版本名称
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化UI方法
     */
    private void initUI() {
        tv_version_name = findViewById(R.id.tv_version_name);
        rl_root = findViewById(R.id.rl_root);
    }
}