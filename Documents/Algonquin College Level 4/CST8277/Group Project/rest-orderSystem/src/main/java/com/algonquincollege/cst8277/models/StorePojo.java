/*****************************************************************c******************o*******v******id********
 * File Name: StorePojo
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.algonquincollege.cst8277.rest.ProductSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
*
* Description: model for the Store object
*/
@Entity(name = "Store")
@Table(name = "STORES")
@AttributeOverride(name="id", column=@Column(name="STORE_ID"))
public class StorePojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * String storeName.
     */
    protected String storeName;
    /**
     * Set ProductPojo products.
     */
    protected Set<ProductPojo> products = new HashSet<>();

    /**
     * JPA requires each @Entity class have a default constructor
     */
    public StorePojo() {
    }

    /**
     * Description: Gets the store name.
     * 
     * @return The value for storeName.
     */
    public String getStoreName() {
        return storeName;
    }
    
    /**
     * Description: Sets the store name.
     * 
     * @param storeName
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    @JsonSerialize(using = ProductSerializer.class)
    @ManyToMany
    @JoinTable(
        name="STORES_PRODUCTS",
        joinColumns=@JoinColumn(name="STORE_ID", referencedColumnName="STORE_ID"),
        inverseJoinColumns=@JoinColumn(name="PRODUCT_ID", referencedColumnName="PRODUCT_ID")
        )
    
    /**
     * Description: Gets products.
     * 
     * @return The value for products.
     */
    public Set<ProductPojo> getProducts() {
        return products;
    }
    /**
     * Description: Sets products.
     * 
     * @param products
     */
    public void setProducts(Set<ProductPojo> products) {
        this.products = products;
    }

}
