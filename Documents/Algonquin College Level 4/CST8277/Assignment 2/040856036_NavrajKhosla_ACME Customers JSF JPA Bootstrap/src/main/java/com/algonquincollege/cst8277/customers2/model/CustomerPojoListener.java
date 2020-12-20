/*****************************************************************c******************o*******v******id********
 * File: CustomerPojoListener.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * Student Name: Navraj Khosla
 * Student Number: 040856036
 * 
 */
package com.algonquincollege.cst8277.customers2.model;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class CustomerPojoListener {

    /**
     * Description: Sets the date of creation and date of updated to be the current date.
     * 
     * @param cust
     */
    @PrePersist
    public void setCreatedOnDate(CustomerPojo cust) {
        LocalDateTime now = LocalDateTime.now();
        cust.setCreatedDate(now);
        //might as well call setUpdatedDate as well
        cust.setUpdatedDate(now);
    }

    /**
     * Description: Sets the date of updated to be the current date.
     * 
     * @param cust
     */
    @PreUpdate
    public void setUpdatedDate(CustomerPojo cust) {
        cust.setUpdatedDate(LocalDateTime.now());
    }

}