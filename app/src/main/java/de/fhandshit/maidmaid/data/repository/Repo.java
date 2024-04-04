package de.fhandshit.maidmaid.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.fhandshit.maidmaid.data.dao.ProductDao;
import de.fhandshit.maidmaid.data.dao.ProductItemDao;
import de.fhandshit.maidmaid.data.database.AppDatabase;
import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.data.model.ProductItem;

public class Repo {
    private AppDatabase database;
    private ProductDao productDao;
    private ProductItemDao productItemDao;

    public Repo(AppDatabase appDatabase) {
        database = appDatabase;
        productDao = database.productDao();
        productItemDao = database.productItemDao();
    }


    public LiveData<List<String>> getCategories(){
        return Transformations.map(productDao.getAll(), products ->
                products.stream().map(Product::getCategory).distinct().collect(Collectors.toList()));
    }


    public LiveData<List<Product>> getProducts(){
        return productDao.getAll();
    }

    public LiveData<List<ProductItem>> getProductItems(){
        return productItemDao.getAll();
    }


    public void insertProduct(Product product){
        database.execute(() -> productDao.insert(product));
    }

    public void insertProductItem(ProductItem productItem){
        database.execute(() -> productItemDao.insert(productItem));
    }

    public LiveData<List<ProductItem>> getProductItems(Product product){
        return productItemDao.getProductItems(product.getProductId());
    }

    public LiveData<Product> getProduct(UUID uuid){
        return productDao.getProduct(uuid);
    }

    public LiveData<Product> findByName(String name){
        return productDao.findByName(name);
    }

    public void updateProduct(Product product){
        database.execute(() -> productDao.update(product));
    }

    public void updateProductItem(ProductItem productItem){
        database.execute(() -> productItemDao.update(productItem));
    }
}
