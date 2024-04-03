package de.fhandshit.maidmaid.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    public Category(@NonNull String name, int color) {
        this.name = name;
        this.color = color;
    }

    @PrimaryKey
    @NonNull
    private String name;

    private int color; //argb

    public Category(@NonNull String categoryName) {
        this.name = categoryName;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
