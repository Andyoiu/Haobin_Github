package com.example.haobin;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Login extends Activity {
    MySqliteHelper mySqliteHelper;
    SQLiteDatabase db;

    // 调用Actvity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 关联activity.xml
        setContentView(R.layout.activity_login);
        // 关联用户名、密码和登录、注册按钮
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);//初始化控件
        String[] city = getResources().getStringArray(R.array.spinnername);//建立数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city);//建立Adapter并且绑定数据源
        //第一个参数表示在哪个Activity上显示，第二个参数是系统下拉框的样式，第三个参数是数组。
        spinner.setAdapter(adapter);//绑定Adapter到控件
        mySqliteHelper = new MySqliteHelper(this);
        Button loginButton = (Button) this.findViewById(R.id.LoginButton);
        Button signUpButton = (Button) this.findViewById(R.id.SignUpButton);
        db = mySqliteHelper.getReadableDatabase();
        // 登录按钮监听器
        loginButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queryAllPersonData();
//                        // 获取用户名和密码
//                        String strUserName = userName.getText().toString().trim();
//                        String strPassWord = passWord.getText().toString().trim();
//                        // 判断如果用户名为"123456"密码为"123456"则登录成功
//                        if (strUserName.equals("123456") && strPassWord.equals("123456")) {
//                            Toast.makeText(Login.this, "登录成功！", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(Login.this, Sing_List.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            Toast.makeText(Login.this, "请输入正确的用户名或密码！", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
        );
//         注册按钮监听器
        signUpButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转到注册界面
                        Intent intent = new Intent(Login.this, Register.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    /**
     * 查询全部数据
     */

    public void queryAllPersonData() {

        EditText userName = (EditText) this.findViewById(R.id.UserNameEdit);
        EditText passWord = (EditText) this.findViewById(R.id.PassWordEdit);
        String strUserName = userName.getText().toString().trim();
        String strPassWord = passWord.getText().toString().trim();

        //查询全部数据
        Cursor cursor = db.query("Users", new String[]{"Password"}, "Username=?", new String[]{strUserName}, null, null, null, "0,1");
        // 游标移动进行校验
        if (cursor.moveToNext()) {
            // 从数据库获取密码进行校验
            @SuppressLint("Range") String dbPassword = cursor.getString(cursor.getColumnIndex("Password"));
            // 关闭游标
            Toast.makeText(Login.this, "密码或者账号！！！", Toast.LENGTH_SHORT).show();
            cursor.close();
            if (strPassWord.equals(dbPassword)) {
                Toast.makeText(Login.this, "登录成功！", Toast.LENGTH_SHORT).show();
                // 校验成功则跳转到歌单
                Intent intent = new Intent(this, Sing_List.class);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(Login.this, "请输入正确的用户名或密码！", Toast.LENGTH_SHORT).show();
            cursor.close();
        }
    }
}
