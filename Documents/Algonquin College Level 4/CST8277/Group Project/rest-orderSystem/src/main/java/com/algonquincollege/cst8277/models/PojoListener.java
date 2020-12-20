/*****************************************************************c******************o*******v******id********
 * File Name: PojoListener
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.models;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Description: Determines the time for when a record is created and updated.
 */
public class PojoListener {
    
    /**
     * Description: Sets the date of creation and date of updated to be the current date.
     * 
     * @param pojo
     */
    @PrePersist
    public void setCreatedOnDate(PojoBase pojo) {
        LocalDateTime now = LocalDateTime.now();
        pojo.setCreatedDate(now);
        //might as well call setUpdatedDate as well
        pojo.setUpdatedDate(now);
    }

    /**
     * Description: Sets the date of updated to be the current date.
     * 
     * @param pojo
     */
    @PreUpdate
    public void setUpdatedDate(PojoBase pojo) {
        pojo.setUpdatedDate(LocalDateTime.now());
    }

}