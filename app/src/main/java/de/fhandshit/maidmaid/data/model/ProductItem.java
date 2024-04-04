package de.fhandshit.maidmaid.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "product_items")
public class ProductItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_item_id")
    private int productItemId;
    private LocalDate expiryDate;
    @Embedded
    private Product product;

    public ProductItem(LocalDate expiryDate, Product product) {
        this.expiryDate = expiryDate;
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getProductItemId() {
        return productItemId;
    }

    public void setProductItemId(int productItemId) {
        this.productItemId = productItemId;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

}
