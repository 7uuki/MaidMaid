package de.fhandshit.maidmaid.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ProductWithCategory {
    @Embedded
    public Product product;

    @Relation(
            parentColumn = "categoryName",
            entityColumn = "name"
    )
    public Category category;
}
