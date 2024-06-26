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

import de.fhandshit.maidmaid.data.model.ProductItem;

@Dao
public interface ProductItemDao {
    @Query("SELECT * FROM product_items")
    LiveData<List<ProductItem>> getAll();

    @Insert
    void insertAll(ProductItem... products);

    @Query("SELECT * FROM product_items WHERE product_id = :productId")
    LiveData<List<ProductItem>> getProductItems(UUID productId);

    @Query("SELECT * FROM product_items WHERE category = :category")
    LiveData<List<ProductItem>> getProductItems(String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductItem product);


    @Update
    void update(ProductItem productItem);

    @Delete
    void delete(ProductItem product);

    @Query("DELETE FROM product_items")
    public void nukeTable();
}
