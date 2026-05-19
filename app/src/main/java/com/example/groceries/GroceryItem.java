package com.example.groceries;

import java.io.Serializable;

public class GroceryItem implements Serializable {
    String name;
    boolean isChecked;

    public GroceryItem(String name) {
        this.name = name;
        this.isChecked = false;
    }

    public GroceryItem(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }
}