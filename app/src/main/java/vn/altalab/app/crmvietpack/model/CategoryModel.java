package vn.altalab.app.crmvietpack.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import vn.altalab.app.crmvietpack.object.Category;

/**
 * Created by Luongnv on 10/25/2016.
 */

public class CategoryModel {
    public CategoryModel() {

    }

    public void createCategory(Realm realm, Category _category) {
        try {
            RealmResults<Category> categoryRealmResults = realm.where(Category.class).equalTo("categoryId", _category.getCategoryId()).findAll();
            if (!(categoryRealmResults.size() > 0)) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(_category);
                realm.commitTransaction();
            }

        } catch (Exception e) {
            Log.e("createCategory", e.toString());
        }
    }

    public RealmResults<Category> getAllCategories(Realm realm) {
        return realm.where(Category.class).findAll();
    }
}
