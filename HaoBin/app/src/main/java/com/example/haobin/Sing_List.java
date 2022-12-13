package com.example.haobin;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;

import java.text.MessageFormat;

public class Sing_List extends Activity implements View.OnClickListener {
    public static Sing_List sing_list = null;
    private ListView lv;
    private Button add,re,ex;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private com.example.haobin.AudioMngHelper audioMngHelper = null;
    private SeekBar seekBar;
    private TextView font;
    private ListView mListView;
    private final String[] singer={"陈奕迅","陈奕迅","Eason","陈奕迅","Eason","陈奕迅","陈奕迅","Eason","陈奕迅","陈奕迅","陈奕迅","Eason","陈奕迅","陈奕迅","Eason","陈奕迅","陈奕迅","陈奕迅","Eason","陈奕迅","陈奕迅","陈奕迅"};
    private final String[] sing=
            {"K歌之王(国)", "完", "心烧", "斯德哥尔摩情人", "明年今日", "是但求其爱", "最佳损友", "最冷一天",
                    "淘汰", "白玫瑰", "苦瓜", "Shall We Talk(国)","葡萄成熟时", "阴天快乐", "阿牛", "于心有愧", "人来人往", "你给我听好", "十年", "单车",
                    "喜欢一个人", "太阳照常升起"};
    private final int[] icons={R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,
            R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,
            R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle,R.drawable.cycle};
    private final int[] song={R.raw.a1,R.raw.a2,R.raw.a3,R.raw.a4,R.raw.a5,R.raw.a6,R.raw.a7,R.raw.a8,
            R.raw.a9,R.raw.a10,R.raw.a11,R.raw.a12,R.raw.a13,R.raw.a14,R.raw.a15,R.raw.a16,R.raw.a17,R.raw.a18,
            R.raw.a19,R.raw.a20,R.raw.a21,R.raw.a22
    };
    private final String[] lcr={"a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10", "a11", "a12", "a13", "a14", "a15", "a16", "a17", "a18","a19","a20","a21","a22"};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_list);
        sing_list=this;
        mListView=findViewById(R.id.lv);
        re=findViewById(R.id.re);
        seekBar=findViewById(R.id.seek);
        font =findViewById(R.id.font);
        add=findViewById(R.id.add);
        ex = findViewById(R.id.center);
        lv= findViewById(R.id.lv);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                audioMngHelper.setVoice100(seekBar.getProgress());
                font.setText(MessageFormat.format("音量{0}", seekBar.getProgress()));
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (MainActivity.instance!=null){MainActivity.instance.finish();}
                Intent intent1 = new Intent(Sing_List.this,MainActivity.class);
                intent1.putExtra("values",song[i]);
                Bundle bundleSimple = new Bundle();
                bundleSimple.putInt("values", song[i]);
                bundleSimple.putString("dir",sing[i]);
                bundleSimple.putString("lrc",lcr[i]);
                intent1.putExtras(bundleSimple);
                startActivity(intent1);
            }
        });

        audioMngHelper =new com.example.haobin.AudioMngHelper();
        audioMngHelper.AudioMngHelper(Sing_List.this);
        seekBar.setProgress(audioMngHelper.get100CurrentVolume());
        add.setOnClickListener(this);
        re.setOnClickListener(this);
        ex.setOnClickListener(this);
        font.setText("音量:"+audioMngHelper.get100CurrentVolume());
        Mylist mylist= new Mylist();
        mListView.setAdapter(mylist);
    }


    @Override
    public void onClick(View view) {
        if (view==re){
            audioMngHelper.subVoice100();
        }else if(view==add) {
            audioMngHelper.addVoice100();
        } else if (view==ex){
            builder = new AlertDialog.Builder(  Sing_List.this);
            alert = builder.setIcon(R.drawable.re)
                    .setTitle("系统提示：")
                    .setMessage("确定退出吗？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Sing_List.this, "你点击了取消按钮~", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Sing_List.this, "你点击了确定按钮~", Toast.LENGTH_SHORT).show();
                            finish();
                            System.exit(0);
                        }
                    }).create();             //创建AlertDialog对象
            alert.show();                    //显示对话框

        }
        seekBar.setProgress(audioMngHelper.get100CurrentVolume());
        font.setText("音量:"+audioMngHelper.get100CurrentVolume());
    }

    class Mylist extends BaseAdapter {
        @Override
        public int getCount() {
            return singer.length;
        }

        @Override
        public Object getItem(int i) {
            return singer[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public  View getView(int po,View con,ViewGroup pa){
            ViewHolder holder=null;
            if (con==null){
                con =View.inflate(Sing_List.this,R.layout.list,null);
                holder= new ViewHolder();
                holder.singer=con.findViewById(R.id.singer);
                holder.sing=con.findViewById(R.id.sing);
                holder.iv=con.findViewById(R.id.iv);
                con.setTag(holder); //将数据封存于con里面
            }else {
                holder= (ViewHolder) con.getTag();
            }
            holder.singer.setText("歌手:"+singer[po]);
            holder.sing.setText("《"+sing[po]+"》");
            holder.iv.setBackgroundResource(icons[po]);
            return con;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Activity:onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Activity:onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("Activity:onRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Activity:onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Activity:onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Activity:onStop");
    }
}
class ViewHolder{
    TextView singer,sing;
    ImageView iv;
}
