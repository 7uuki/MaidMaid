package de.fhandshit.maidmaid.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.fhandshit.maidmaid.data.model.Category;
import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.data.model.ProductItem;

@Dao
public interface ProductItemDao {
    @Query("SELECT * FROM product")
    List<ProductItem> getAll();

    @Query("SELECT * FROM product WHERE id IN (:itemIds)")
    List<ProductItem> loadAllByIds(int[] itemIds);

    @Query("SELECT * FROM product WHERE id IN (:categories)")
    List<ProductItem> loadAllByCategory(List<Category> categories);

    @Query("SELECT * FROM product WHERE id IN (:products)")
    List<ProductItem> loadAllByProducts(List<Product> products);



    @Insert
    void insertAll(ProductItem... products);

    @Delete
    void delete(ProductItem product);
}
