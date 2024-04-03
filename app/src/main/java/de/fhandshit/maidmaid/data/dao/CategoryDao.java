package de.fhandshit.maidmaid.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.fhandshit.maidmaid.data.model.Category;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories")
    List<Category> getAll();

    @Query("SELECT * FROM categories WHERE name IN (:categoryName)")
    Category loadByName(String categoryName);

    @Insert
    void insertAll(Category... categories);

    @Delete
    void delete(Category category);

}
