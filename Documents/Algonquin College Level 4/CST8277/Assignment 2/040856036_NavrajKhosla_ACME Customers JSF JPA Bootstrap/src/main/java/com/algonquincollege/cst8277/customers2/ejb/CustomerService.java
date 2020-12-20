/*****************************************************************c******************o*******v******id********
 * File: CustomerService.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * Student Name: Navraj Khosla
 * Student Number: 040856036
 * 
 */
package com.algonquincollege.cst8277.customers2.ejb;

import static com.algonquincollege.cst8277.customers2.model.CustomerPojo.ALL_CUSTOMERS_QUERY_NAME;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.algonquincollege.cst8277.customers2.model.CustomerPojo;

@Singleton
public class CustomerService implements Serializable {
    
    /**
     * Long serialVersionUID.
     */
    private static final long SerialVersionUID = 1L;
    
    /**
     * String CUSTOMER_PU.
     * 
     * Description: Holds the persistence-unit name.
     */
    public static final String CUSTOMER_PU = "acmeCustomers-PU";
    
    /**
     * EntityManager em.
     */
    @PersistenceContext(name = CUSTOMER_PU)
    protected EntityManager em;
    
    /**
     * Description: Gets all the customers information.
     * 
     * @return All the information about every customer.
     */
    public List<CustomerPojo> getAllCustomers() {
        TypedQuery<CustomerPojo> allCustomersQuery = em.createNamedQuery(ALL_CUSTOMERS_QUERY_NAME, CustomerPojo.class);
        return allCustomersQuery.getResultList();
    }
    
    /**
     * Description: Creates the new customer record.
     * 
     * @param customer.
     * @return Newly created customer record.
     */
    @Transactional
    public CustomerPojo newCustomer(CustomerPojo customer) {
        em.persist(customer);
        return customer;
    }
    
    /**
     * Description: Finds the customer record using the customer's ID.
     * 
     * @param customerId.
     * @return Information about the specified customer record.
     */
    public CustomerPojo findCustomerById(int customerId) {
        return em.find(CustomerPojo.class, customerId);
    }
    
    /**
     * Description: Updates the information about the specified customer record. 
     * 
     * @param customerWithUpdates.
     * @return Customer record with updated information.
     */
    @Transactional
    public CustomerPojo changeCustomerInformation(CustomerPojo customerWithUpdates) {
        CustomerPojo mergedCustomerPojo = em.merge(customerWithUpdates);
        return mergedCustomerPojo;
    }
    
    /**
     * Description: Deletes the customer record using the customer's ID.
     * 
     * @param customerId
     */
    public void deleteCustomerById(int customerId) {
        CustomerPojo customer = findCustomerById(customerId);
        if (customer != null) {
            em.refresh(customer);
            em.remove(customer);
        }
    }

}
