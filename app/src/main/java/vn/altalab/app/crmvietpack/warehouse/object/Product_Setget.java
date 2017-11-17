package vn.altalab.app.crmvietpack.warehouse.object;

/**
 * Created by mac2 on 3/6/17.
 */

public class Product_Setget {

    String PRODUCT_CODE;
    String PRODUCT_NAME;
    String INVENTORY;
    String WAREHOUSE_NAME;
    String LINK_IMAGE;

    public String getLINK_IMAGE() {
        return LINK_IMAGE;
    }

    public void setLINK_IMAGE(String LINK_IMAGE) {
        this.LINK_IMAGE = LINK_IMAGE;
    }

    public String getPRODUCT_CODE() {
        return PRODUCT_CODE;
    }

    public void setPRODUCT_CODE(String PRODUCT_CODE) {
        this.PRODUCT_CODE = PRODUCT_CODE;
    }

    public String getPRODUCT_NAME() {
        return PRODUCT_NAME;
    }

    public void setPRODUCT_NAME(String PRODUCT_NAME) {
        this.PRODUCT_NAME = PRODUCT_NAME;
    }

    public String getINVENTORY() {
        return INVENTORY;
    }

    public void setINVENTORY(String INVENTORY) {
        this.INVENTORY = INVENTORY;
    }

    public String getWAREHOUSE_NAME() {
        return WAREHOUSE_NAME;
    }

    public void setWAREHOUSE_NAME(String WAREHOUSE_NAME) {
        this.WAREHOUSE_NAME = WAREHOUSE_NAME;
    }
}
