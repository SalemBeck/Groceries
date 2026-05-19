package com.example.groceries;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TrashActivity extends AppCompatActivity {

    ListView listView;
    Button emptyTrashButton;
    ArrayList<String> trashNames;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        listView = findViewById(R.id.trashListView);
        emptyTrashButton = findViewById(R.id.emptyTrashButton);

        trashNames = (ArrayList<String>) getIntent().getSerializableExtra("trash_list");
        if (trashNames == null) {
            trashNames = new ArrayList<>();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trashNames);
        listView.setAdapter(adapter);

        emptyTrashButton.setOnClickListener(v -> {
            trashNames.clear();
            adapter.notifyDataSetChanged();
        });
    }
}