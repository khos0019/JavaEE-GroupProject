/*****************************************************************c******************o*******v******id********
 * File Name: ProductPojo
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
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
*
* Description: model for the Product object
*/
@Entity(name = "Product")
@Table(name = "PRODUCT")
@AttributeOverride(name="id", column=@Column(name="PRODUCT_ID"))
public class ProductPojo extends PojoBase implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * String description.
     */
    protected String description;
    /**
     * String serialNo.
     */
    protected String serialNo;
    /**
     * Set StorePojo stores object.
     */
    protected Set<StorePojo> stores = new HashSet<>();

    /**
     * JPA requires each @Entity class have a default constructor
     */
    public ProductPojo() {
    }
    
    /**
     * Description: Gets the description.
     * @return the value for firstName
     */
    public String getDescription() {
        return description;
    }
    /**
     * Description: Sets the description.
     * @param description new value for description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Description: Gets the serial number.
     * 
     * @return The value for serialNo.
     */
    @Column(name="SERIALNUMBER")
    public String getSerialNo() {
        return serialNo;
    }
    /**
     * Description: Sets the serial number.
     * 
     * @param serialNo
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    /**
     * Description: Gets the stores name.
     * 
     * @return The value for stores.
     */
    @JsonInclude(Include.NON_NULL)
    @ManyToMany(mappedBy = "products")
    public Set<StorePojo> getStores() {
        return stores;
    }
    /**
     * Description: Sets the stores name.
     * 
     * @param stores
     */
    public void setStores(Set<StorePojo> stores) {
        this.stores = stores;
    }

}