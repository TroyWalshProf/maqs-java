/*
 * Copyright 2020 (C) Magenic, All rights Reserved
 */

package com.magenic.jmaqs.webservices.jdk11.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.xml.bind.annotation.XmlAttachmentRef;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

@JacksonXmlRootElement(namespace = "http://schemas.datacontract.org/2004/07/AutomationTestSite.Models", localName = "Product")
public class Product {

    @JsonProperty("Id")
    private int id;

    @JsonProperty("Name")

    private String name;

    @JsonProperty("Category")
    private String category;

    @JsonProperty("Price")
    private double price;

    public Product(int id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public Product() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString() {
        return String.format("%s:%d\n", "Id", this.getId()) + String
            .format("%s:%s\n", "Name", this.getName()) + String
            .format("%s:%s\n", "Category", this.getCategory()) + String
            .format("%s:%s\n", "Price", this.getPrice());
    }
}
