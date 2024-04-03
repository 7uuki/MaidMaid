package de.fhandshit.maidmaid.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.Locale;

import de.fhandshit.maidmaid.data.model.Category;
import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.data.model.ProductWithCategory;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    LiveData<List<ProductWithCategory>> getProducts();

    @Insert
    void insertAll(Product... products);

    List<Product> getProductsOfCategory(Category category);
/*
    @Transaction
    @Query("SELECT * FROM product WHERE id = :id")
    ProductWithCategory findById(UUID id);

    @Query("SELECT products.*, categories.* FROM products INNER JOIN categories ON products.categoryName = categories.categoryName")
    List<ProductWithCategory> getProductsWithCategories();



    @Query("SELECT * FROM product WHERE name LIKE :name LIMIT 1")
    Product findByName(String name);



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

*/
}
