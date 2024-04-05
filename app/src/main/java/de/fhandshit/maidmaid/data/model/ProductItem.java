package de.fhandshit.maidmaid.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.Objects;

@Entity(tableName = "product_items")
public class ProductItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_item_id")
    private int productItemId;
    private LocalDate expiryDate;
    @Embedded
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductItem)) return false;
        ProductItem that = (ProductItem) o;
        return productItemId == that.productItemId && Objects.equals(expiryDate, that.expiryDate) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productItemId, expiryDate, product);
    }

    @Override
    public String toString() {
        return "ProductItem{" +
                "productItemId=" + productItemId +
                ", expiryDate=" + expiryDate +
                ", product=" + product +
                '}';
    }

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
