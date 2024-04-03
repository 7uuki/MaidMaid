package de.fhandshit.maidmaid.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class ProductItem {
    @PrimaryKey(autoGenerate = true)
    private int productId;

    public ProductItem(int productId, LocalDate expiryDate, Product product) {
        this.productId = productId;
        this.expiryDate = expiryDate;
        this.product = product;
        this.parentProductId = product.getId();
    }

    public ProductItem() {
    }

    @Ignore
    private LocalDate expiryDate;
    @Ignore
    private Product product;

    private int parentProductId;

    public int getParentProductId() {
        return parentProductId;
    }

    public void setParentProductId(int parentProductId) {
        this.parentProductId = parentProductId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
