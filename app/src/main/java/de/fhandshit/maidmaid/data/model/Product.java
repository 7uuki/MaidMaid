package de.fhandshit.maidmaid.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId) && Objects.equals(name, product.name) && Objects.equals(category, product.category) && Objects.equals(lastAdd, product.lastAdd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, category, lastAdd);
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

