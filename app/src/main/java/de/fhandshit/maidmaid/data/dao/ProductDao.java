package de.fhandshit.maidmaid.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Embedded;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Relation;
import androidx.room.Transaction;

import java.util.List;
import java.util.UUID;

import de.fhandshit.maidmaid.data.model.Category;
import de.fhandshit.maidmaid.data.model.Product;

@Dao
public interface ProductDao {

    @Transaction
    @Query("SELECT * FROM product WHERE id = :id")
    ProductWithCategory findById(UUID id);

    @Query("SELECT products.*, categories.* FROM products INNER JOIN categories ON products.categoryName = categories.categoryName")
    List<ProductWithCategory> getProductsWithCategories();



    @Query("SELECT * FROM product WHERE name LIKE :name LIMIT 1")
    Product findByName(String name);

    @Insert
    void insertAll(Product... products);

    @Delete
    void delete(Product product);

    class ProductWithCategory{
        @Embedded
        public Product product;

        @Relation(
                parentColumn = "",
                entityColumn = "categoryName")
                public Category category;
    }


}
