/*****************************************************************c******************o*******v******id********
 * File: CustomerDao.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * Student Name: Navraj Khosla
 * Student Number: 040856036
 * 
 */
package com.algonquincollege.cst8277.customers2.dao;

import java.util.List;

import com.algonquincollege.cst8277.customers2.model.CustomerPojo;

/**
 * Description: API for the database C-R-U-D operations
 */
public interface CustomerDao {

    /**
     * Description: Creates the customer record.
     * 
     * @param customer
     */
    public CustomerPojo createCustomer(CustomerPojo customer);
    
    /**
     * Description: Reads the customer based of their ID.
     * 
     * @param customerId
     */
    public CustomerPojo readCustomerById(int customerId);
    
    /**
     * Description: Reads all the customers in the list.
     */
    public List<CustomerPojo> readAllCustomers();
    
    /**
     * Description: Updates a specific record with new informaiton.
     * 
     * @param customer
     */
    public CustomerPojo updateCustomer(CustomerPojo customer);   
    
    /**
     * Description: Deletes a specific record from the table and database using the customer's ID.
     * 
     * @param customerId
     */
    public void deleteCustomerById(int customerId);

}