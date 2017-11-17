package vn.altalab.app.crmvietpack.hanghoa.object;

import java.io.Serializable;

/**
 * Created by boybe on 03/23/2017.
 */

public class Goods implements Serializable {
    private Integer productId;
    private String productDescription;
    private String status;
    private String productName;
    private String productManufactory;
    private String productPrice;
    private String productCode;
    private String productUnit;
    private Double tax;
    private Double discount;
    private String warrantyTime;
    private Double productCogs;
    private String productOrigin;
    private String serial;
    private Double outOfStock;
    private Double width;
    private Double height;
    private Double length;
    private String productCategory;
    private String ImageUrl;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Goods() {
    }



    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
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

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductManufactory() {
        return productManufactory;
    }

    public void setProductManufactory(String productManufactory) {
        this.productManufactory = productManufactory;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return getProductName();
    }
}
