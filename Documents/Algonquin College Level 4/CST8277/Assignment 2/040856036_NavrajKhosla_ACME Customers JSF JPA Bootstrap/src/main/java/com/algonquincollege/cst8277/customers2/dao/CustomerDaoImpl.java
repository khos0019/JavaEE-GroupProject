/*****************************************************************c******************o*******v******id********
 * File: CustomerDaoImpl.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * Student Name: Navraj Khosla
 * Student Number: 040856036
 * 
 */
package com.algonquincollege.cst8277.customers2.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import com.algonquincollege.cst8277.customers2.ejb.CustomerService;
import com.algonquincollege.cst8277.customers2.model.CustomerPojo;

/**
* Description: Implements the C-R-U-D API for the database
*/
@Named
@ApplicationScoped
public class CustomerDaoImpl implements CustomerDao, Serializable {
    /** explicitly set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * CustomerService customerService.
     */
    @EJB
    protected CustomerService customerService;
    
    /**
     * ServletContext sc.
     */
    protected ServletContext sc;

    /**
     * Description: Constructor with one arguement.
     * 
     * @param sc
     */
    @Inject
    public CustomerDaoImpl(ServletContext sc) {
        super();
        this.sc = sc;
    }
    
    /**
     * Description: Prints out and handles all the messages depending on which method was triggered.
     * 
     * @param msg
     */
    protected void logMsg(String msg) {
        sc.log(msg);
    }
    
    /**
     * Description: Reads all the customers in the table. 
     * 
     * @return Customer record(s).
     */
    @Override
    public List<CustomerPojo> readAllCustomers() {
        logMsg("reading all customers");
        return customerService.getAllCustomers();
    }
    
    /**
     * Description: Creates a customer record.
     * 
     * @param customer.
     * @return Newly created customer record. 
     */
    @Override
    public CustomerPojo createCustomer(CustomerPojo customer) {
        logMsg("creating an customer");
        return customerService.newCustomer(customer);
    }

    /**
     * Description: Using the ID of a customer record, it will gather all the information about that customer.
     * 
     * @param customerId.
     * @return Information about the specified customer record.
     */
    @Override
    public CustomerPojo readCustomerById(int customerId) {
        logMsg("read a specific customer");
        return customerService.findCustomerById(customerId);
    }

    /**
     * Description: Updates the information about the specified customer record. 
     * 
     * @param customerWithUpdates.
     * @return Customer record with updated information.
     */
    @Override
    public CustomerPojo updateCustomer(CustomerPojo customerWithUpdates) {
        logMsg("updating a specific customer");
        return customerService.changeCustomerInformation(customerWithUpdates);
    }

    /**
     * Description: Using the ID of a customer record, it will delete the customer record from the table and database.
     * 
     * @param customerId.
     */
    @Override
    public void deleteCustomerById(int customerId) {
        logMsg("deleting a specific customer");
        customerService.deleteCustomerById(customerId);
    }

}