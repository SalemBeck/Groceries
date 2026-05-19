package com.example.groceries;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "GroceriesPrefs";
    private static final String KEY_GROCERY_LIST = "groceryList";
    private static final String KEY_TRASH_LIST = "trashList";

    EditText editText;
    Button addButton;
    Button deleteAllButton;
    Button viewTrashButton;
    ListView listView;
    ArrayList<GroceryItem> groceryList;
    ArrayList<GroceryItem> trashList;
    GroceryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        addButton = findViewById(R.id.addButton);
        deleteAllButton = findViewById(R.id.deleteAllButton);
        viewTrashButton = findViewById(R.id.viewTrashButton);
        listView = findViewById(R.id.listView);

        loadData();

        adapter = new GroceryAdapter(this, groceryList);
        listView.setAdapter(adapter);

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                addItem();
                return true;
            }
            return false;
        });

        addButton.setOnClickListener(v -> addItem());

        deleteAllButton.setOnClickListener(v -> {
            // Move ALL items to trash (checked OR unchecked)
            trashList.addAll(groceryList);
            groceryList.clear();
            adapter.updateList(groceryList);
            saveData();
            Toast.makeText(this, "All items moved to trash", Toast.LENGTH_SHORT).show();
        });

        viewTrashButton.setOnClickListener(v -> {
            ArrayList<String> trashNames = new ArrayList<>();
            for (GroceryItem item : trashList) {
                trashNames.add(item.name);
            }
            Intent intent = new Intent(MainActivity.this, TrashActivity.class);
            intent.putExtra("trash_list", trashNames);
            startActivity(intent);
        });
    }

    private void addItem() {
        String itemName = editText.getText().toString().trim();
        if (!itemName.isEmpty()) {
            groceryList.add(new GroceryItem(itemName));
            sortList();
            adapter.updateList(groceryList);
            editText.setText("");
            saveData();
        } else {
            Toast.makeText(this, "Enter item name", Toast.LENGTH_SHORT).show();
        }
    }

    private void sortList() {
        // Move checked items to bottom
        Collections.sort(groceryList, (a, b) -> {
            if (a.isChecked == b.isChecked) return 0;
            return a.isChecked ? 1 : -1;
        });
    }

    private void saveData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();

        String groceryJson = gson.toJson(groceryList);
        String trashJson = gson.toJson(trashList);

        editor.putString(KEY_GROCERY_LIST, groceryJson);
        editor.putString(KEY_TRASH_LIST, trashJson);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();

        String groceryJson = prefs.getString(KEY_GROCERY_LIST, "");
        String trashJson = prefs.getString(KEY_TRASH_LIST, "");

        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();

        if (!groceryJson.isEmpty()) {
            groceryList = gson.fromJson(groceryJson, type);
        } else {
            groceryList = new ArrayList<>();
        }

        if (!trashJson.isEmpty()) {
            trashList = gson.fromJson(trashJson, type);
        } else {
            trashList = new ArrayList<>();
        }
        if (adapter != null) {
            adapter.updateList(groceryList);
        }
    }

    public void sortAndRefresh() {
        sortList();
        adapter.updateList(groceryList);
        saveData();
    }
}