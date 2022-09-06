package com.example.fire2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference ref;
    List<MemoVO> array=new ArrayList<>();
    MemoAdapter adapter=new MemoAdapter();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        db=FirebaseDatabase.getInstance();

        getSupportActionBar().setTitle(user.getEmail()+"님의 메모장");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton insert=findViewById(R.id.insert);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MemoActivity.this,InsertActivity.class);
                startActivity(intent);
            }
        });
        RecyclerView list=findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        onRestart();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        array.clear();
        ref=db.getReference("memos"+user.getUid());

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MemoVO vo = (MemoVO) snapshot.getValue(MemoVO.class);
                array.add(vo);

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

    //어댑터
    class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder>{


        @NonNull
        @Override
        public MemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=getLayoutInflater().inflate(R.layout.item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MemoAdapter.ViewHolder holder, int position) {
            MemoVO vo=array.get(position);
            holder.content.setText(vo.getContent());
            holder.date.setText(vo.getDate());
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MemoActivity.this,ReadActivity.class);
                    intent.putExtra("vo",vo);
                    System.out.println("111................................."+vo);
                    startActivity(intent);
                }

            });
            holder.item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder box=new AlertDialog.Builder(MemoActivity.this);
                    box.setMessage("삭제하시겠습니까?");
                    box.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ref=db.getReference("memos"+user.getUid()).child(vo.getKey());
                            ref.removeValue();
                            onRestart();
                        }
                    });
                    box.setNegativeButton("no", null);
                    box.show();
                    return false;
                }
            });
        }


        @Override
        public int getItemCount() {

            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView content, date;
            LinearLayout item;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                content=itemView.findViewById(R.id.content);
                date=itemView.findViewById(R.id.date);
                item=itemView.findViewById(R.id.item);
            }
        }
    }
}