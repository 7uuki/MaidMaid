package de.fhandshit.maidmaid.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "products",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "name",
                childColumns = "categoryName",
                onDelete = ForeignKey.CASCADE))
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public Product(int id,LocalDateTime lastAdd, String productName, Category category) {
        this.id= id;
        this.lastAdd = lastAdd;
        this.productName = productName;
        this.categoryName = category.getName();
        this.category = category;
    }

    public Product(int i, String productName, Category category) {
        this.id= id;
        this.productName = productName;
        this.categoryName = category.getName();
        this.category = category;
    }

    public Product() {
    }
    @Ignore
    private LocalDateTime lastAdd;

    private String productName;

    @ColumnInfo(index = true)
    private String categoryName; // Foreign key

    @Embedded
    private Category category;

    public LocalDateTime getLastAdd() {
        return lastAdd;
    }

    public void setLastAdd(LocalDateTime lastAdd) {
        this.lastAdd = lastAdd;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", lastAdd=" + lastAdd +
                ", productName='" + productName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", category:Name" + category.getName() +
                '}';
    }
}

