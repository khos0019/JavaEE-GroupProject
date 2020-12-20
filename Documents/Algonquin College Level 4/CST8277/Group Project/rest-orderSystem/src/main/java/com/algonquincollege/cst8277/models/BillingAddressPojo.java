/*****************************************************************c******************o*******v******id********
 * File Name: BillingAddressPojo
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Description: model for the BillingAddress object
 */
@DiscriminatorValue("B")
@Entity(name = "BillingAddress")
@Table(name = "CUST_ADDR")
@AttributeOverride(name = "id", column = @Column(name="ADDR_ID"))
public class BillingAddressPojo extends AddressPojo implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Boolean isAlsoShipping.
     */
    protected boolean isAlsoShipping;

    /**
     * JPA requires each @Entity class have a default constructor
     */
    public BillingAddressPojo() {
    }

    /**
     * Description: Determines if the billing address is also the shipping address.
     * 
     * @return The value for isAlsoShipping.
     */
    public boolean isAlsoShipping() {
        return isAlsoShipping;
    }
    
    /**
     * Description: Sets also shipping address.
     * 
     * @param isAlsoShipping
     */
    public void setAlsoShipping(boolean isAlsoShipping) {
        this.isAlsoShipping = isAlsoShipping;
    }
    
}