package com.milkmachine.itemswipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        initSwipe();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getDumbData());

        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    private void initSwipe() {
    }

    private ArrayList<String> getDumbData() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Banana");
        list.add("Orange");
        list.add("Apple");
        list.add("Lemon");
        list.add("Pineapple");

        return list;
    }
}
