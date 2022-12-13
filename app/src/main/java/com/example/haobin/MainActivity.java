package com.example.haobin;



import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;

import android.os.Bundle;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lauzy.freedom.library.Lrc;
import com.lauzy.freedom.library.LrcHelper;
import com.lauzy.freedom.library.LrcView;


import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {
    public static MainActivity instance = null;
    private int songing;
    private String str;
    private TextView t1, t2;
    private boolean cycle = false;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private Button btn_play, Go, home;
    private Button btn_pause;
    private Button btn_stop;
    private Button btn_cycle;
    private static SeekBar sb;
    private static TextView tv_progress, tv_total;
    private ObjectAnimator animator;
    private MusicService.MusicControl musicControl;
    private static LrcView mLrcView;
    private String lrc;
    MyServiceConn conn;
    Intent intent;


    private boolean isUnbind = false;//记录服务是否被解绑
    boolean g = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        System.out.println("MainActivity:onCreate");
        init();
        Bundle bundle = this.getIntent().getExtras();
        songing = bundle.getInt("values");
        str = bundle.getString("dir");
//        lrc = bundle.getString("lrc");
        t1.setText(str);
        mLrcView=findViewById(R.id.lrc);


        List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(MainActivity.this, "a5.lrc");
        mLrcView.setLrcData(lrcs);
        mLrcView.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
            @Override
            public void onPlay(long time, String content) {
                musicControl.seekTo((int) time);
            }
        });



//        File file = new File(getFilesDir(), "aaa.lrc");

    }



    private void init() {
        tv_progress = findViewById(R.id.tv_progress);
        tv_total = findViewById(R.id.tv_total);
        sb = findViewById(R.id.sb);
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_cycle = (Button) findViewById(R.id.btn_cycle);
        Go = findViewById(R.id.list);
        home = findViewById(R.id.home);
        t1 = findViewById(R.id.tv_music_title);
        t2 = findViewById(R.id.tv_type);
        btn_play.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_cycle.setOnClickListener(this);
        btn_cycle.setEnabled(false);
        Go.setOnClickListener(this);
        home.setOnClickListener(this);

        intent = new Intent(this, MusicService.class);//创建意图对象
        conn = new MyServiceConn();                       //创建服务连接对象
        bindService(intent, conn, BIND_AUTO_CREATE);  //绑定服务
//        unbind(false);
        //为滑动条添加事件监听
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean
                    fromUser) {                          //滑动条进度改变时，会调用此方法
                if (progress == seekBar.getMax()) { //当滑动条滑到末端时，结束动画
                    animator.pause();                   //停止播放动画
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//滑动条开始滑动时调用
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { //滑动条停止滑动时调用
                //根据拖动的进度改变音乐播放进度
                int progress = seekBar.getProgress();//获取seekBar的进度
                musicControl.seekTo(progress);         //改变播放进度
            }
        });

        ImageView iv_music = findViewById(R.id.iv_music);
        animator = ObjectAnimator.ofFloat(iv_music, "rotation", 0f, 360.0f);
        animator.setDuration(10000);  //动画旋转一周的时间为10秒
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);  //-1表示设置动画无限循环
    }

    public static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = msg.getData(); //获取从子线程发送过来的音乐播放进度
            int duration = bundle.getInt("duration");                  //歌曲的总时长
            int currentPostition = bundle.getInt("currentPosition");//歌曲当前进度
            sb.setMax(duration);                //设置SeekBar的最大值为歌曲总时长
            sb.setProgress(currentPostition);//设置SeekBar当前的进度位置
            mLrcView.updateTime(currentPostition);
            //歌曲的总时长
            int minute = duration / 1000 / 60;
            int second = duration / 1000 % 60;
            String strMinute = null;
            String strSecond = null;
            if (minute < 10) {              //如果歌曲的时间中的分钟小于10
                strMinute = "0" + minute; //在分钟的前面加一个0
            } else {
                strMinute = minute + "";
            }
            if (second < 10) {             //如果歌曲的时间中的秒钟小于10
                strSecond = "0" + second;//在秒钟前面加一个0
            } else {
                strSecond = second + "";
            }
            tv_total.setText(strMinute + ":" + strSecond);
            //歌曲当前播放时长
            minute = currentPostition / 1000 / 60;
            second = currentPostition / 1000 % 60;
            if (minute < 10) {             //如果歌曲的时间中的分钟小于10
                strMinute = "0" + minute;//在分钟的前面加一个0
            } else {
                strMinute = minute + "";
            }
            if (second < 10) {               //如果歌曲的时间中的秒钟小于10
                strSecond = "0" + second;  //在秒钟前面加一个0
            } else {
                strSecond = second + "";
            }
            tv_progress.setText(strMinute + ":" + strSecond);
            return false;
        }
    });

    class MyServiceConn implements ServiceConnection { //用于实现连接服务
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
            Log.i("MainActivity", "服务绑定成功，内存地址为：");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("MainActivity", "服务解绑成功");
        }
    }

    private void unbind(boolean isUnbind) {
        if (!isUnbind) {                  //判断服务是否被解绑
            musicControl.pausePlay();//暂停播放音乐
            unbindService(conn);      //解绑服务
            stopService(intent);      //停止服务
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:                //播放按钮点击事件
                musicControl.play(songing);           //播放音乐
                if (animator.isStarted()) {
                    animator.resume();
                } else {
                    animator.start();
                }

                btn_play.setEnabled(false);
                btn_play.setBackground(getResources().getDrawable(R.drawable.play_end));

                btn_pause.setEnabled(true);
                btn_pause.setBackground(getResources().getDrawable(R.drawable.pause));

                btn_cycle.setEnabled(true);
                btn_stop.setEnabled(true);
                btn_stop.setBackground(getResources().getDrawable(R.drawable.ex));
                break;
            case R.id.btn_pause:               //暂停按钮点击事件
                musicControl.pausePlay();     //暂停播放音乐
                animator.pause();              //暂停播放动画
                btn_play.setEnabled(true);
                btn_play.setBackground(getResources().getDrawable(R.drawable.play));
                btn_pause.setEnabled(false);
                btn_pause.setBackground(getResources().getDrawable(R.drawable.pause_end));
                break;
            case R.id.btn_cycle:     //循环播放事件
                cycle = !cycle;
                if (cycle) {
                    btn_cycle.setBackground(getResources().getDrawable(R.drawable.cycle_star));
                    Toast.makeText(MainActivity.this,"开启循环播放！",Toast.LENGTH_SHORT).show();

                } else {
                    btn_cycle.setBackground(getResources().getDrawable(R.drawable.cycle_end));
                    Toast.makeText(MainActivity.this,"关闭循环播放！",Toast.LENGTH_SHORT).show();

                }
                musicControl.setLooping(cycle); //循环播放音乐
                break;
            case R.id.btn_stop:                //退出按钮点击事件

                btn_stop.setBackground(getResources().getDrawable(R.drawable.ex_end));
                musicControl.pausePlay();
                animator.pause();
                builder = new AlertDialog.Builder(MainActivity.this);
                alert = builder.setIcon(R.drawable.re)
                        .setTitle("系统提示：")
                        .setMessage("确定退出吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                musicControl.play(songing);
                                animator.resume();
                                btn_stop.setBackground(getResources().getDrawable(R.drawable.ex));
                                Toast.makeText(MainActivity.this, "你点击了取消按钮~", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "你点击了确定按钮~", Toast.LENGTH_SHORT).show();
                                if (Sing_List.sing_list!=null){Sing_List.sing_list.finish();}
                                unbind(isUnbind);               //解绑服务绑定
                                isUnbind = true;                //完成解绑服务
                                btn_stop.setEnabled(false);
                                animator.pause();
                                cycle = false;
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);
                            }
                        }).create();             //创建AlertDialog对象
                alert.show();                    //显示对话框
                break;
            case R.id.list:
                Intent in = new Intent(MainActivity.this, Sing_List.class);
                startActivity(in);
                break;
            case R.id.home:
                finish();
                Intent out = new Intent(MainActivity.this, Login.class);
                startActivity(out);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind(isUnbind); //解绑服务
        System.out.println("MainActivity:onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("MainActivity:onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("MainActivity:onRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("MainActivity:onResume");
        Bundle bundl = this.getIntent().getExtras();
        songing = bundl.getInt("values");
        str = bundl.getString("dir");
        System.out.println("MainActivity:" + str + songing);
        t1.setText(str);
        System.out.println("MainActivity:hello");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("MainActivity:onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("MainActivity:onStop");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}