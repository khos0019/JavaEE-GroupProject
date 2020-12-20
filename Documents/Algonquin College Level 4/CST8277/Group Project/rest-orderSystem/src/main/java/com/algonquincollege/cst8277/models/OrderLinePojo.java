/*****************************************************************c******************o*******v******id********
 * File Name: OrderLinePojo
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
*
* Description: model for the OrderLine object
*/
@Entity(name="OrderLine")
@Table(name = "ORDERLINE")
@Access(AccessType.PROPERTY) // NOTE: by using this annotations, any annotation on a field is ignored without warning
public class OrderLinePojo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * OrderLinePk primaryKey.
     */
    protected OrderLinePk primaryKey;
    /**
     * OrderPojo owningOrder.
     */
    protected OrderPojo owningOrder;
    /**
     * Double amount.
     */
    protected Double amount;
    /**
     * ProductPojo product.
     */
    protected ProductPojo product;

    /**
     * JPA requires each @Entity class have a default constructor
     */
    public OrderLinePojo() {
    }

    /**
     * Description: Gets primary key.
     * 
     * @return The value for primaryKey.
     */
    @EmbeddedId
    public OrderLinePk getPk() {
        return primaryKey;
    }
    
    /**
     * Description: Sets city name.
     * 
     * @param city
     */
    public void setPk(OrderLinePk primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    
    /**
     * Description: Gets owning order.
     * 
     * @return The value for owingOrder.
     */
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "OWNING_ORDER_ID")
    @MapsId("owningOrderId")
    public OrderPojo getOwningOrder() {
        return owningOrder;
    }
    
    /**
     * Description: Sets owning order.
     * 
     * @param owningOrder
     */
    public void setOwningOrder(OrderPojo owningOrder) {
        this.owningOrder = owningOrder;
    }

    /**
     * Description: Gets the amount.
     * 
     * @return The value for amount.
     */
    public Double getAmount() {
        return amount;
    }
    /**
     * Description: Sets the amount.
     * 
     * @param amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    /**
     * Description: Gets the product.
     * 
     * @return The value for product.
     */
    @OneToOne
    @JoinColumn(name="PRODUCT_ID")
    public ProductPojo getProduct() {
        return product;
    }
    /**
     * Description: Sets the amount.
     * 
     * @param amount
     */
    public void setProduct(ProductPojo product) {
        this.product = product;
    }

}