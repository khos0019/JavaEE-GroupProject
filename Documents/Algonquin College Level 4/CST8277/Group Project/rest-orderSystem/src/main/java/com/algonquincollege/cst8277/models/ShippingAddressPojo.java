/*****************************************************************c******************o*******v******id********
 * File Name: ShippingAddressPojo
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

@DiscriminatorValue("S")
@Entity(name = "ShippingAddress")
@Table(name = "CUST_ADDR")
@AttributeOverride(name = "id", column = @Column(name="ADDR_ID"))
/**
*
* Description: model for the ShippingAddress object
*/
public class ShippingAddressPojo extends AddressPojo implements Serializable  {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * JPA requires each @Entity class have a default constructor
     */
    public ShippingAddressPojo() {
    }

}