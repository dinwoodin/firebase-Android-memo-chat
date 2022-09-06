package com.example.fire2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("로그인");

        email=findViewById(R.id.email);
        email.setText("123@123.123");
        password=findViewById(R.id.password);
        password.setText("123123123");

        //파이어베이스 연동
        mAuth=FirebaseAuth.getInstance();

        //이메일 등록
        Button register=findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail=email.getText().toString();
                String strPass=password.getText().toString();
                AlertDialog.Builder box=new AlertDialog.Builder(MainActivity.this);
                mAuth.createUserWithEmailAndPassword(strEmail,strPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //접속성공
                            box.setMessage("가입완료");
                            box.setPositiveButton("닫기",null);
                            box.show();
                        }else {
                            //접속실패
                            box.setTitle("접속실패");
                            box.setMessage("네트워크 확인 필요");
                            box.setPositiveButton("닫기",null);
                            box.show();
                        }
                    }
                });
            }
        });
        //로그인
        Button login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //입력된 변수값을 받음
                String strEmail=email.getText().toString();
                String strPass=password.getText().toString();
                AlertDialog.Builder box=new AlertDialog.Builder(MainActivity.this);
                mAuth.signInWithEmailAndPassword(strEmail,strPass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //접속성공
                                    box.setMessage("로그인 완료");
                                    box.setPositiveButton("메모", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(MainActivity.this,MemoActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    box.setNegativeButton("채팅", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(MainActivity.this,ChatActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    box.show();

                                }else {
                                    //접속실패
                                    box.setTitle("로그인 실패");
                                    box.setMessage("아이디 비밀번호 확인");
                                    box.setPositiveButton("닫기", null);
                                    box.show();
                                }
                            }
                        });
            }
        });

    }
}