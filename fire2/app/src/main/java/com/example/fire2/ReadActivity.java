package com.example.fire2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference ref;
    MemoVO vo = new MemoVO();
    EditText content;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Intent intent = getIntent();
         vo = (MemoVO) intent.getSerializableExtra("vo");

        System.out.println("...............................1111"+vo.getContent());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        content = findViewById(R.id.content);
        db = FirebaseDatabase.getInstance();
        content.setText(vo.getContent());

        getSupportActionBar().setTitle("메모장 정보"+user.getEmail());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button save=findViewById(R.id.save);
        save.setText("수정");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vo.setContent(content.getText().toString());
                vo.setDate(sdf.format(new Date()));
                AlertDialog.Builder box=new AlertDialog.Builder(ReadActivity.this);
                box.setMessage("수정하시겠습니까?");
                box.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ref=db.getReference("memos"+user.getUid()).child(vo.getKey());
                        ref.setValue(vo);
                        System.out.println("...............................1111"+ref);
                        finish();
                    }
                });
                box.setNegativeButton("no",null);
                box.show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
