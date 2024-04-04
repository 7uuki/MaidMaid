package de.fhandshit.maidmaid.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

import de.fhandshit.maidmaid.data.model.Product;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM products WHERE category = :category")
    LiveData<List<Product>> getProductsFromCategory(String category);

    @Query("SELECT * FROM products WHERE product_id = :uuid")
    LiveData<Product> getProduct(UUID uuid);

    @Query("SELECT * FROM products WHERE name = :name")
    LiveData<Product> findByName(String name);

    @Insert
    void insertAll(Product... products);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

}
