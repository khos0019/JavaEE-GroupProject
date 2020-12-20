/*****************************************************************c******************o*******v******id********
 * File Name: OrderLinePk
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JPA helper class: Composite Primary Key class for OrderLine - two columns
 * ORDERLINE_NO identifies which orderLine within an Order (i.e. line 1, line 2, line 3)
 * OWNING_ORDER_ID identifies which Order this orderLine belongs to
*/

@Embeddable
@Access(AccessType.PROPERTY) // NOTE: by using this annotations, any annotation on a field is ignored without warning
public class OrderLinePk implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Int owningOrderId.
     */
    protected int owningOrderId;
    
    /**
     * Int orderLineNo.
     */
    protected int orderLineNo;
    
    /**
     * Description: Gets the owning order ID.
     * 
     * @return The value for owningOrderId.
     */
    @Column(name = "OWNING_ORDER_ID")
    public int getOwningOrderId() {
        return owningOrderId;
    }
    
    /**
     * Description: Sets the owning order ID.
     * 
     * @param owningOrderId
     */
    public void setOwningOrderId(int owningOrderId) {
        this.owningOrderId = owningOrderId;
    }

    /**
     * Description: Gets the order line number.
     * 
     * @return The value for orderLineNo.
     */
    @Column(name = "ORDERLINE_NO")
    public int getOrderLineNo() {
        return orderLineNo;
    }
    
    /**
     * Description: Sets the order line number.
     * 
     * @param orderLineNo
     */
    public void setOrderLineNo(int orderLineNo) {
        this.orderLineNo = orderLineNo;
    }
   
    /**
     * Description: Accociates a hash code for each object.
     * 
     * @return The objects hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(orderLineNo, owningOrderId);
    }
    
    /**
     * Description: Checks if the objects are equal or not.
     * 
     * @return True or false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OrderLinePk)) {
            return false;
        }
        OrderLinePk other = (OrderLinePk) obj;
        return orderLineNo == other.orderLineNo && owningOrderId == other.owningOrderId;
    }

}