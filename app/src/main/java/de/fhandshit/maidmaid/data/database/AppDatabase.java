package de.fhandshit.maidmaid.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.fhandshit.maidmaid.data.dao.ProductDao;
import de.fhandshit.maidmaid.data.model.Category;
import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.data.model.ProductItem;
import de.fhandshit.maidmaid.data.dao.CategoryDao;
import de.fhandshit.maidmaid.data.dao.ProductItemDao;

@Database(entities = {Category.class, ProductItem.class, Product.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProductDao productDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductItemDao productItemDao();
}
