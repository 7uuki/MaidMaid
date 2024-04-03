package de.fhandshit.maidmaid.data.repository;

import android.graphics.Color;
import android.os.Build;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import de.fhandshit.maidmaid.data.dao.CategoryDao;
import de.fhandshit.maidmaid.data.dao.ProductDao;
import de.fhandshit.maidmaid.data.dao.ProductItemDao;
import de.fhandshit.maidmaid.data.model.Category;
import de.fhandshit.maidmaid.data.model.Product;
import de.fhandshit.maidmaid.data.model.ProductItem;
import de.fhandshit.maidmaid.data.model.ProductWithCategory;

public class DummyRepo extends Repo{



    private List<Category> categoires;
    private List<Product> products;
    private List<ProductItem> productItems;

    public List<Category> getCategoires() {
        return categoires;
    }

    public void setCategoires(List<Category> categoires) {
        this.categoires = categoires;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<ProductItem> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductItem> productItems) {
        this.productItems = productItems;
    }

    int lastItemID = 0;

    public DummyRepo() {
        super(null,null,null);

        categoires = new ArrayList<>();
        products = new ArrayList<>();
        productItems = new ArrayList<>();

        Category putzmittel = new Category("Putzmittel");
        putzmittel.setColor(Color.rgb(0,150,123));
        categoires.add(putzmittel);

        Product lappen = createPersonAndAdd(putzmittel,"Lappen",0);
        createPersonItemsAndAdd(lappen,10);

        Product toilettenReiniger = createPersonAndAdd(putzmittel,"ToilettenReiniger",1);
        createPersonItemsAndAdd(toilettenReiniger,3);

        Product fensterputzmittel = createPersonAndAdd(putzmittel,"Fensterputzmittel",2);
        createPersonItemsAndAdd(fensterputzmittel,4);



        Category nahrungsmittel = new Category("Nahrungsmittel");
        nahrungsmittel.setColor(Color.rgb(180,150,123));
        categoires.add(nahrungsmittel);

        Product bohnen = createPersonAndAdd(putzmittel,"Bohnen",3);
        createPersonItemsAndAdd(bohnen,50);

        Product nudeln = createPersonAndAdd(putzmittel,"Nudeln",4);
        createPersonItemsAndAdd(nudeln,3);

        setProductDao(getProductDao());
        setProductItemDao(getProductItemDao());
        setCategoryDao(getCategoryDao());


    }

    private Product createPersonAndAdd(Category category, String name, int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Product person = new Product(id,LocalDateTime.now(),name,category);
            products.add(person);
            return person;

        }
        return null;
    }

    private Random random = new Random();
    private ProductItem createPersonItemsAndAdd(Product product, int count){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            for (int i = 0; i < count ; i++) {
                ProductItem item = new ProductItem(lastItemID, LocalDate.now().plusDays(random.nextInt(10)),product);
                productItems.add(item);
                lastItemID++;
            }
        }
        return null;
    }

    private ProductDao createProductDao(){
        return new ProductDao(){

            @Override
            public LiveData<List<ProductWithCategory>> getProducts() {
                return null;
            }

            @Override
            public void insertAll(Product... inputproducts) {
                products.addAll(Arrays.asList(inputproducts));
            }

            @Override
            public List<Product> getProductsOfCategory(Category category) {
                List<Product> out = new ArrayList<>();
                for(Product product:products){
                    if(product.getCategory().equals(category)){
                        out.add(product);
                    }
                }
                return out;
            }
        };
    }
    private ProductItemDao createProductItemDao(){
        return new ProductItemDao(){

            @Override
            public List<ProductItem> getAll() {
                return productItems;
            }

            @Override
            public List<ProductItem> loadAllByIds(int[] itemIds) {
                //nothing
                return null;
            }

            @Override
            public List<ProductItem> loadAllByProduct(Product product) {
                List<ProductItem> out = new ArrayList<>();
                for(ProductItem productItem:productItems){
                    if(product.getId()==productItem.getParentProductId()){
                        out.add(productItem);
                    }
                }

                return out;
            }

            @Override
            public void insertAll(ProductItem... inputproducts) {
                productItems.addAll(Arrays.asList(inputproducts));

            }

            @Override
            public void delete(ProductItem product) {
                //Do nothing
            }
        };
    }

    private CategoryDao createCategoryDao(){
        return new CategoryDao() {
            @Override
            public List<Category> getAll() {
                return categoires;
            }

            @Override
            public Category loadByName(String categoryName) {
               for(Category category:categoires){
                   if(category.getName().equals(categoryName)){
                       return category;
                   }
               }
               return null;
            }

            @Override
            public void insertAll(Category... inputcategories) {
                getCategories().addAll(Arrays.asList(inputcategories));

            }

            @Override
            public void delete(Category category) {
                getCategories().remove(category);
            }
        };
    }



}
