package com.example.fire2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText content;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    FirebaseDatabase db;
    DatabaseReference ref;//db를 어디에 저장할지 지정
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        //파이어베이스
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        user=mAuth.getCurrentUser();
        String strEmail=mAuth.getCurrentUser().getEmail();

        getSupportActionBar().setTitle(user.getEmail() + "님의 메모작성");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        content=findViewById(R.id.content);

        Button save =findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemoVO vo=new MemoVO();
                vo.setContent(content.getText().toString());
                if(vo.getContent().equals("")){
                    Toast.makeText(InsertActivity.this,"내용을 입력하세요",Toast.LENGTH_SHORT).show();
                }else {
                    vo.setEmail(user.getEmail());
                    vo.setDate(sdf.format(new Date()));
                    System.out.println(vo.toString());
                    ref=db.getReference("memos"+user.getUid()).push(); //db에 momos를 생성하고 키값을 받아옴
                    vo.setKey(ref.getKey());
                    ref.setValue(vo);
                    Toast.makeText(InsertActivity.this,"저장 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
