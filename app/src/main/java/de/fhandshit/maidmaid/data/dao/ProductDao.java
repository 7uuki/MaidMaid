package de.fhandshit.maidmaid.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.fhandshit.maidmaid.data.model.Product;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM products WHERE category = :category")
    LiveData<List<Product>> getProductsFromCategory(String category);

    @Query("SELECT * FROM products WHERE name LIKE :name LIMIT 1")
    Product findByName(String name);

    @Insert
    void insertAll(Product... products);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Delete
    void delete(Product product);

}
