package de.fhandshit.maidmaid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import de.fhandshit.maidmaid.data.dao.CategoryDao;
import de.fhandshit.maidmaid.data.dao.ProductDao;
import de.fhandshit.maidmaid.data.database.AppDatabase;
import de.fhandshit.maidmaid.data.model.Category;
import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.data.model.ProductWithCategory;
@RunWith(AndroidJUnit4.class)
public class ProductCategoryRelationshipTest {
    private ProductDao productDao;
    private CategoryDao categoryDao;
    private AppDatabase appDatabase;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        productDao = appDatabase.productDao();
        categoryDao = appDatabase.categoryDao();
    }

    @After
    public void tearDown() {
        appDatabase.close();
    }

    @Test
    public void testProductWithCategory() throws InterruptedException {
        // Insert a category
        Category category = new Category("Electronics");
        categoryDao.insertAll(category);

        // Insert a product associated with the inserted category
        Product product = new Product();
        product.setProductName("Smartphone");
        productDao.insertAll(product);

        // Retrieve the product with its associated category
        LiveData<List<ProductWithCategory>> productsWithCategoriesLiveData = productDao.getProducts();
        productsWithCategoriesLiveData.observeForever(new Observer<List<ProductWithCategory>>() {
            @Override
            public void onChanged(List<ProductWithCategory> productWithCategories) {
                assertNotNull(productWithCategories);
                assertFalse(productWithCategories.isEmpty());

                ProductWithCategory productWithCategory = productWithCategories.get(0);
                assertNotNull(productWithCategory);

                Product retrievedProduct = productWithCategory.product;
                assertNotNull(retrievedProduct);
                assertEquals("Smartphone", retrievedProduct.getProductName());

                Category retrievedCategory = productWithCategory.category;
                assertNotNull(retrievedCategory);
                assertEquals("Electronics", retrievedCategory.getName());
            }
        });
    }
}
