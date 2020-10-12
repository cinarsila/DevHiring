package com.trendyol.testcasews.model;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "boutique_id")
    private String boutiqueId;

    @Column(name = "name")
    private String name;

    @Column(name = "merchant_id")
    private String merchantId;


    public  Product(){}

    public Product(Long contentId, String brand_name, String boutiqueId, String name, String merchantId) {

        this.contentId = contentId;
        this.brandName = brand_name;
        this.boutiqueId = boutiqueId;
        this.name = name;
        this.merchantId = merchantId;
    }
    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBoutiqueId() {
        return boutiqueId;
    }

    public void setBoutiqueId(String boutiqueId) {
        this.boutiqueId = boutiqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
