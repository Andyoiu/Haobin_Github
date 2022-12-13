package com.example.haobin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Register extends Activity {
    // 调用Activity
    RadioGroup radgroup ;
    public MySqliteHelper mySqliteHelper;
    CheckBox one,two,three,four;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //关联activity_register.xml
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 关联用户名、密码、确认密码、邮箱和注册、返回登录按钮
        mySqliteHelper = new MySqliteHelper(this);
        EditText userName = (EditText) this.findViewById(R.id.UserNameEdit);
        EditText passWord = (EditText) this.findViewById(R.id.PassWordEdit);
        EditText passWordAgain = (EditText) this.findViewById(R.id.PassWordAgainEdit);
        EditText email = (EditText) this.findViewById(R.id.EmailEdit);
        Button signUpButton = (Button) this.findViewById(R.id.SignUpButton);
        Button backLoginButton = (Button) this.findViewById(R.id.BackLoginButton);
        radgroup = (RadioGroup) findViewById(R.id.radioGroup);
        one = (CheckBox) findViewById(R.id.one);
        two = (CheckBox) findViewById(R.id.two);
        three = (CheckBox) findViewById(R.id.three);
        four= (CheckBox) findViewById(R.id.four);
        RadioButton ra = (RadioButton) radgroup.getChildAt(0);
        RadioButton rd = (RadioButton) radgroup.getChildAt(1);

        // 立即注册按钮监听器
        signUpButton.setOnClickListener(
                v -> {
                    String strUserName = userName.getText().toString().trim();
                    String strPassWord = passWord.getText().toString().trim();
                    String strPassWordAgain = passWordAgain.getText().toString().trim();
                    String strPhoneNumber = email.getText().toString().trim();
                    String mix="";


                    //注册格式粗检
                    if (strUserName.length() > 10) {
                        Toast.makeText(Register.this, "用户名长度必须小于10！", Toast.LENGTH_SHORT).show();
                    } else if (strUserName.length() < 4) {
                        Toast.makeText(Register.this, "用户名长度必须大于4！", Toast.LENGTH_SHORT).show();
                    } else if (strPassWord.length() > 16) {
                        Toast.makeText(Register.this, "密码长度必须小于16！", Toast.LENGTH_SHORT).show();
                    } else if (strPassWord.length() < 6) {
                        Toast.makeText(Register.this, "密码长度必须大于6！", Toast.LENGTH_SHORT).show();
                    } else if (!strPassWord.equals(strPassWordAgain)) {
                        Toast.makeText(Register.this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
                    } else if (!strPhoneNumber.contains("@")) {
                        Toast.makeText(Register.this, "邮箱格式不正确！", Toast.LENGTH_SHORT).show();
                    } else if(!ra.isChecked() && !rd.isChecked()){
                        Toast.makeText(getApplicationContext(), "请选择性别:", Toast.LENGTH_LONG).show();
                    } else {
                        db = mySqliteHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("Username",strUserName);
                        values.put("Password",strPassWord);
                        values.put("Mailbox",strPhoneNumber);
                        if (ra.isChecked()){values.put("Sex","男");}else {values.put("Sex","女");}
                        if (one.isChecked()) mix=mix+one.getText().toString()+"、";
                        if (two.isChecked()) mix=mix+two.getText().toString()+"、";
                        if (three.isChecked()) mix=mix+three.getText().toString()+"、";
                        if (four.isChecked()) mix=mix+four.getText().toString();
                        values.put("Recommend",mix);
//数据库执行插入命令
                        db.insert("Users", null, values);
                        db.close();

                        Toast.makeText(Register.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        // 跳转到登录界面
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                        this.finish();
                    }
                });

        // 返回登录按钮监听器
        backLoginButton.setOnClickListener(
                v -> {
                    // 跳转到登录界面
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    finish();
                }
        );
    }
}



