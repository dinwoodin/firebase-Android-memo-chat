package com.example.fire2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase db;
    EditText content;
    DatabaseReference ref;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<MemoVO> array=new ArrayList<>();
    ChatAdapter adapter=new ChatAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        db= FirebaseDatabase.getInstance();
        content=findViewById(R.id.content);
        Intent intent = getIntent();

        getSupportActionBar().setTitle("채팅 : "+user.getEmail());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView list=findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        ImageView send=findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content.getText().toString().equals("")){
                    Toast.makeText(ChatActivity.this,"내용을 입력하세요",Toast.LENGTH_SHORT).show();
                }else {
                    MemoVO vo=new MemoVO();
                    vo.setContent(content.getText().toString());
                    vo.setDate(sdf.format(new Date()));
                    vo.setEmail(user.getEmail());
                    ref=db.getReference("chats").push();
                    vo.setKey(ref.getKey());
                    ref.setValue(vo);
                    content.setText("");

                }
            }
        });
        ref=db.getReference("chats");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MemoVO vo=(MemoVO) snapshot.getValue(MemoVO.class);
                array.add(vo);
                list.scrollToPosition(array.size()-1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

        @NonNull
        @Override
        public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=getLayoutInflater().inflate(R.layout.item_chat,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
            MemoVO vo=array.get(position);
            holder.content.setText(vo.getContent());
            holder.date.setText(vo.getDate());
            holder.email.setText(vo.getEmail());
            LinearLayout.LayoutParams pcontent=(LinearLayout.LayoutParams)holder.content.getLayoutParams();
            LinearLayout.LayoutParams pemail=(LinearLayout.LayoutParams)holder.email.getLayoutParams();
            LinearLayout.LayoutParams pdate=(LinearLayout.LayoutParams)holder.date.getLayoutParams();

            if(user.getEmail().equals(vo.getEmail())){
                holder.email.setVisibility(View.INVISIBLE);
                pcontent.gravity= Gravity.RIGHT;
                pemail.gravity= Gravity.RIGHT;
                pdate.gravity= Gravity.RIGHT;
            }else {
                holder.email.setVisibility(View.VISIBLE);
                pcontent.gravity= Gravity.LEFT;
                pemail.gravity= Gravity.LEFT;
                pdate.gravity= Gravity.LEFT;

            }

        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView content, date, email;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                content=itemView.findViewById(R.id.content);
                date=itemView.findViewById(R.id.date);
                email=itemView.findViewById(R.id.email);
            }
        }
    }
}