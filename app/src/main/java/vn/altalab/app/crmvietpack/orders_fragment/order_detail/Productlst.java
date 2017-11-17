package vn.altalab.app.crmvietpack.orders_fragment.order_detail;

import java.io.Serializable;
import java.util.List;

import vn.altalab.app.crmvietpack.object.Product;

/**
 * Created by Tung on 4/4/2017.
 */
//Class để lưu tạm dữ liệu list object khi chuyển dữ liệu giữa 2 màn hình
public class Productlst implements Serializable {
    List<Product> products;

    public Productlst() {

    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
