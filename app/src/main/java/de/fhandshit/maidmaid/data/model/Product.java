package de.fhandshit.maidmaid.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    @NonNull
    public UUID productId = UUID.randomUUID();

    public Product(String name, String category, LocalDateTime lastAdd) {
        this.name = name;
        this.category = category;
        this.lastAdd = lastAdd;
    }

    private String name;
    private String category;

    @ColumnInfo(name = "last_add")
    private LocalDateTime lastAdd;

    public LocalDateTime getLastAdd() {
        return lastAdd;
    }

    public void updateLastAdd() {
        this.lastAdd = LocalDateTime.now();
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

