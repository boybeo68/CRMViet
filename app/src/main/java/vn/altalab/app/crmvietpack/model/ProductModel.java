package vn.altalab.app.crmvietpack.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import vn.altalab.app.crmvietpack.object.Product;

/**
 * Created by Luongnv on 10/25/2016.
 */

public class ProductModel {
    public ProductModel() {
    }

    public void createProduct(Realm realm, Product _product) {
        try {
            RealmResults<Product> realmResults = realm.where(Product.class).equalTo("productId", _product.getProductId()).findAll();
            if (!(realmResults.size() > 0)) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(_product);
                realm.commitTransaction();
            }
        } catch (Exception e) {
            realm.cancelTransaction();
            Log.e("createproduct", e.toString());
        }
    }

    public void editProduct(Realm realm, final Product _product) {
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Product realmProduct = realm.where(Product.class).equalTo("productId", _product.getProductId()).findFirst();
                    if (realmProduct != null) {
                        realmProduct.setWidth(_product.getWidth());
                        realmProduct.setProductCode(_product.getProductCode());
                        realmProduct.setProductCategory(_product.getProductCategory());
                        realmProduct.setProductName(_product.getProductName());
                        realmProduct.setProductCode(_product.getProductCode());
                        realmProduct.setProductCode(_product.getProductCode());
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    public RealmResults<Product> getAllProduct(Realm realm) {
        return realm.where(Product.class).findAll();
    }

    public RealmResults<Product> getProducts(Realm realm) {
        return realm.where(Product.class).findAll();
    }
}
