package vn.altalab.app.crmvietpack.object;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Luongnv on 10/25/2016.
 */

public class Products extends RealmObject implements Serializable {
    @Index
    @PrimaryKey
    private Integer productId;

    private String productCode;
    private String productName;
    private String productUnit;
    private String description;
    private String productImage;
    private String urlImage;
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
    private String serial;
    private Double outOfStock;
    private Double width;
    private Double height;
    private Double length;
    private String productCategory;
    private Double realPrice;
    private Double totalAmount;
    private Double realPriceInput;
    private Double productPriceConvertInput;
    private Double productAmountConvertInput;
    private String priceInString;
    private String priceOutString;
    private Boolean selected = false;
    private Double changePriceRate;
    private Double amountAfterTax;
    private Double totalPrice;
    private Double amountBeforeTax;
    private Double amountVat;
    private Double quantity;
    private Double sumQuantity;
    private Double discount;
    private Double discountAmount;
    private Double discountAmountInput;
    private Double amountBeforeTaxInput;
    private Double amountVatInput;
    private Double totalAmountInput;
    private Integer productQuoteId;
    private Integer ProductOrderInputId;
    private Integer ProductOrderOutputId;
    private Integer productForecastId;
    private String productQuantity;
    private int categoryId;
    private Double productAmountConvert;
    private Double productPriceConvert;
    private double oldQuantity;
    private int oldWarehouseId;

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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
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

    public Double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(Double realPrice) {
        this.realPrice = realPrice;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getRealPriceInput() {
        return realPriceInput;
    }

    public void setRealPriceInput(Double realPriceInput) {
        this.realPriceInput = realPriceInput;
    }

    public Double getProductPriceConvertInput() {
        return productPriceConvertInput;
    }

    public void setProductPriceConvertInput(Double productPriceConvertInput) {
        this.productPriceConvertInput = productPriceConvertInput;
    }

    public Double getProductAmountConvertInput() {
        return productAmountConvertInput;
    }

    public void setProductAmountConvertInput(Double productAmountConvertInput) {
        this.productAmountConvertInput = productAmountConvertInput;
    }

    public String getPriceInString() {
        return priceInString;
    }

    public void setPriceInString(String priceInString) {
        this.priceInString = priceInString;
    }

    public String getPriceOutString() {
        return priceOutString;
    }

    public void setPriceOutString(String priceOutString) {
        this.priceOutString = priceOutString;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Double getChangePriceRate() {
        return changePriceRate;
    }

    public void setChangePriceRate(Double changePriceRate) {
        this.changePriceRate = changePriceRate;
    }

    public Double getAmountAfterTax() {
        return amountAfterTax;
    }

    public void setAmountAfterTax(Double amountAfterTax) {
        this.amountAfterTax = amountAfterTax;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getAmountBeforeTax() {
        return amountBeforeTax;
    }

    public void setAmountBeforeTax(Double amountBeforeTax) {
        this.amountBeforeTax = amountBeforeTax;
    }

    public Double getAmountVat() {
        return amountVat;
    }

    public void setAmountVat(Double amountVat) {
        this.amountVat = amountVat;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getSumQuantity() {
        return sumQuantity;
    }

    public void setSumQuantity(Double sumQuantity) {
        this.sumQuantity = sumQuantity;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getDiscountAmountInput() {
        return discountAmountInput;
    }

    public void setDiscountAmountInput(Double discountAmountInput) {
        this.discountAmountInput = discountAmountInput;
    }

    public Double getAmountBeforeTaxInput() {
        return amountBeforeTaxInput;
    }

    public void setAmountBeforeTaxInput(Double amountBeforeTaxInput) {
        this.amountBeforeTaxInput = amountBeforeTaxInput;
    }

    public Double getAmountVatInput() {
        return amountVatInput;
    }

    public void setAmountVatInput(Double amountVatInput) {
        this.amountVatInput = amountVatInput;
    }

    public Double getTotalAmountInput() {
        return totalAmountInput;
    }

    public void setTotalAmountInput(Double totalAmountInput) {
        this.totalAmountInput = totalAmountInput;
    }

    public Integer getProductQuoteId() {
        return productQuoteId;
    }

    public void setProductQuoteId(Integer productQuoteId) {
        this.productQuoteId = productQuoteId;
    }

    public Integer getProductOrderInputId() {
        return ProductOrderInputId;
    }

    public void setProductOrderInputId(Integer productOrderInputId) {
        ProductOrderInputId = productOrderInputId;
    }

    public Integer getProductOrderOutputId() {
        return ProductOrderOutputId;
    }

    public void setProductOrderOutputId(Integer productOrderOutputId) {
        ProductOrderOutputId = productOrderOutputId;
    }

    public Integer getProductForecastId() {
        return productForecastId;
    }

    public void setProductForecastId(Integer productForecastId) {
        this.productForecastId = productForecastId;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Double getProductAmountConvert() {
        return productAmountConvert;
    }

    public void setProductAmountConvert(Double productAmountConvert) {
        this.productAmountConvert = productAmountConvert;
    }

    public Double getProductPriceConvert() {
        return productPriceConvert;
    }

    public void setProductPriceConvert(Double productPriceConvert) {
        this.productPriceConvert = productPriceConvert;
    }

    public double getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(double oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    public int getOldWarehouseId() {
        return oldWarehouseId;
    }

    public void setOldWarehouseId(int oldWarehouseId) {
        this.oldWarehouseId = oldWarehouseId;
    }
}
