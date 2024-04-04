package de.fhandshit.maidmaid.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.fhandshit.maidmaid.data.Converters;
import de.fhandshit.maidmaid.data.dao.ProductDao;
import de.fhandshit.maidmaid.data.dao.ProductItemDao;
import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.data.model.ProductItem;

@Database(entities = {ProductItem.class, Product.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProductDao productDao();
    public abstract ProductItemDao productItemDao();

    private static volatile AppDatabase database;
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (database == null) {
            synchronized (AppDatabase.class) {
                if (database == null)
                    database = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "maid_maid_db").build();
            }
        }
        return database;
    }

    public void execute(Runnable runnable) {
        databaseWriteExecutor.execute(runnable);
    }
}
