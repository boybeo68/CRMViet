package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Luongnv on 10/25/2016.
 */

public class ProductCategory extends RealmObject implements Serializable {
    @Index
    @PrimaryKey
    private Integer productCategoryId;
    private Category category;
    private Product product;

    private RealmList<Category> categoryRealmList = new RealmList<>();
    private RealmList<Product> productsRealmList = new RealmList<>();

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductCategoryId(Integer productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public RealmList<Category> getCategoryRealmList() {
        return categoryRealmList;
    }

    public void setCategoryRealmList(RealmList<Category> categoryRealmList) {
        this.categoryRealmList = categoryRealmList;
    }

    public RealmList<Product> getProductsRealmList() {
        return productsRealmList;
    }

    public void setProductsRealmList(RealmList<Product> productsRealmList) {
        this.productsRealmList = productsRealmList;
    }
}
