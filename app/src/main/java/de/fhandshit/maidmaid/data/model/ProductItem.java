package de.fhandshit.maidmaid.data.model;

import java.time.LocalDate;
import java.util.UUID;

public class ProductItem {
    private UUID id;
    private LocalDate expiryDate;
    private Product product;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
