package de.fhandshit.maidmaid.data.repository;

import java.util.List;

import de.fhandshit.maidmaid.data.dao.CategoryDao;
import de.fhandshit.maidmaid.data.dao.ProductDao;
import de.fhandshit.maidmaid.data.dao.ProductItemDao;
import de.fhandshit.maidmaid.data.model.Category;
import de.fhandshit.maidmaid.data.model.Product;

public class Repo {
    private ProductDao productDao;
    private CategoryDao categoryDao;
    private ProductItemDao productItemDao;

    public Repo(ProductDao productDao, CategoryDao categoryDao, ProductItemDao productItemDao) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
        this.productItemDao = productItemDao;
    }

    public List<Category> getCategories(){
        return categoryDao.getAll();
    }

    public List<Product> getProducts(){
        List<Product> products = productDao.getProductsWithCategories();
    }



}
