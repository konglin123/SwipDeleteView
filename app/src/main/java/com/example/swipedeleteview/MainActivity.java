package com.example.swipedeleteview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRv;
    private List<String> list=new ArrayList<>();
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRv=findViewById(R.id.rv);
        initData();
        initView();
        initListener();
    }

    private void initListener() {
        itemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {


                switch (view.getId()){
                    case R.id.content:
                        Toast.makeText(MainActivity.this,list.get(position),Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.delete:
                        SwipDeleteView swipDeleteView= (SwipDeleteView) view.getParent();
                        swipDeleteView.closeDelete();
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    private void initView() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(R.layout.item_main, list);
        mRv.setAdapter(itemAdapter);

    }

    private void initData() {
        for (int i = 0; i <100; i++) {
            list.add("content"+i);
        }
    }
}
