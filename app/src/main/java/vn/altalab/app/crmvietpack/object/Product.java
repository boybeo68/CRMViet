package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by HieuDT on 10/24/2016.
 */

public class Product extends RealmObject implements Serializable {
    @Ignore
    private Boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }



    private Integer productId;
    private String productCode;
    private String productName;
    private String productUnit;
    private String description;
    private Double priceIn;
    private Double oldPriceIn;
    private Double priceOut;
    private Double oldPriceOut;
    private Double tax;
    private Integer status;
    private Integer regUser;
    private Integer updUser;
    private Date regDate;
    private Date updDate;
    private String warrantyTime;
    private Double productCogs;
    private String productOrigin;
    private String productManufactory;
    private Double outOfStock;
    private Double width;
    private Double height;
    private Double length;
    private String productCategory;
    private String price;
    private int quantity = 1;
    private Double discount;
    private String INVENTORY="";
    private String PRODUCT_IMAGE;
    private String url_image;

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getPRODUCT_IMAGE() {
        return PRODUCT_IMAGE;
    }

    public void setPRODUCT_IMAGE(String PRODUCT_IMAGE) {
        this.PRODUCT_IMAGE = PRODUCT_IMAGE;
    }

    public String getINVENTORY() {
        return INVENTORY;
    }

    public void setINVENTORY(String INVENTORY) {
        this.INVENTORY = INVENTORY;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPriceIn() {
        return priceIn;
    }

    public void setPriceIn(Double priceIn) {
        this.priceIn = priceIn;
    }

    public Double getOldPriceIn() {
        return oldPriceIn;
    }

    public void setOldPriceIn(Double oldPriceIn) {
        this.oldPriceIn = oldPriceIn;
    }

    public Double getPriceOut() {
        return priceOut;
    }

    public void setPriceOut(Double priceOut) {
        this.priceOut = priceOut;
    }

    public Double getOldPriceOut() {
        return oldPriceOut;
    }

    public void setOldPriceOut(Double oldPriceOut) {
        this.oldPriceOut = oldPriceOut;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRegUser() {
        return regUser;
    }

    public void setRegUser(Integer regUser) {
        this.regUser = regUser;
    }

    public Integer getUpdUser() {
        return updUser;
    }

    public void setUpdUser(Integer updUser) {
        this.updUser = updUser;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public void setUpdDate(Date updDate) {
        this.updDate = updDate;
    }

    public String getWarrantyTime() {
        return warrantyTime;
    }

    public void setWarrantyTime(String warrantyTime) {
        this.warrantyTime = warrantyTime;
    }

    public Double getProductCogs() {
        return productCogs;
    }

    public void setProductCogs(Double productCogs) {
        this.productCogs = productCogs;
    }

    public String getProductOrigin() {
        return productOrigin;
    }

    public void setProductOrigin(String productOrigin) {
        this.productOrigin = productOrigin;
    }

    public String getProductManufactory() {
        return productManufactory;
    }

    public void setProductManufactory(String productManufactory) {
        this.productManufactory = productManufactory;
    }

    public Double getOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(Double outOfStock) {
        this.outOfStock = outOfStock;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
