/*****************************************************************c******************o*******v******id********
 * File: CustomerController.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * Student Name: Navraj Khosla
 * Student Number: 040856036
 * 
 */
package com.algonquincollege.cst8277.customers2.jsf;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.ManagedProperty;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import com.algonquincollege.cst8277.customers2.dao.CustomerDao;
import com.algonquincollege.cst8277.customers2.model.CustomerPojo;

/**
 * Description: Responsible for collection of Customer Pojo's in XHTML (list) <h:dataTable> </br>
 * Delegates all C-R-U-D behaviour to DAO
 */
@Named("customerController")
@SessionScoped
public class CustomerController implements Serializable {
    
    /**
     * Long serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * String UICONSTS_BUNDLE_EXPR.
     * 
     * Description: Holds a uiconsts string.
     */
    public static final String UICONSTS_BUNDLE_EXPR = "#{uiconsts}";
    
    /**
     * String CUSTOMER_MISSING_REFRESH_BUNDLE_MSG.
     * 
     * Description: Holds a refresh string.
     */
    public static final String CUSTOMER_MISSING_REFRESH_BUNDLE_MSG = "refresh";
    
    /**
     * String CUSTOMER_OUTOFDATE_REFRESH_BUNDLE_MSG.
     * 
     * Description: Holds a outOfDate string.
     */
    public static final String CUSTOMER_OUTOFDATE_REFRESH_BUNDLE_MSG = "outOfDate";

    /**
     * FacesContext facesContext.
     */
    @Inject
    protected FacesContext facesContext;

    /**
     * ServletContext sc.
     */
    @Inject
    protected ServletContext sc;

    /**
     * CustomerDao customerDao.
     */
    @Inject
    protected CustomerDao customerDao;

    /**
     * ResourceBundle uiconsts.
     */
    @Inject
    @ManagedProperty(UICONSTS_BUNDLE_EXPR)
    protected ResourceBundle uiconsts;
    
    /**
     * List<CustomerPojo> customers.
     */
    protected List<CustomerPojo> customers;
    
    /**
     * Boolean adding.
     * 
     * Description: Initialized to be false.
     */
    protected boolean adding = false;

    /**
     * Description: Loads all of the customer records.
     */
    public void loadCustomers() {
        logMsg("loadCustomers");
        customers = customerDao.readAllCustomers();
    }

    /**
     * Description: Gets the customer record.
     * 
     * @return customer record.
     */
    public List<CustomerPojo> getCustomers() {
        return this.customers;
    }
    
    /**
     * Description: Sets the customer record.
     * 
     * @param customers
     */
    public void setCustomers(List<CustomerPojo> customers) {
        this.customers = customers;
    }

    /**
     * Description: Determines if we are adding a record.
     * 
     * @return
     */
    public boolean isAdding() {
        return adding;
    }
    
    /**
     * Description: Sets that we are adding a record.
     * 
     * @param adding
     */
    public void setAdding(boolean adding) {
        this.adding = adding;
    }

    /**
     * Description: Toggles the add customer mode which determines whether the addCustomer form is rendered
     */
    public void toggleAdding() {
        this.adding = !this.adding;
    }

    /**
     * Description: Edit a customer record if the 'edit' button is pressed. 
     * 
     * @param cust
     * @return stay on the same page.
     */
    public String editCustomer(CustomerPojo cust) {
        logMsg("editCustomer");
        cust.setEditable(true);
        return null; //current page
    }

    /**
     * Description: Determines if the record exists and allows the record changes to be done.
     * Method is called whenever the 'submit' button is pressed.
     * 
     * @param customerWithEdits
     * @return Stay on the same page.
     */
    public String updateCustomer(CustomerPojo customerWithEdits) {
        logMsg("updateCustomer");
        CustomerPojo customerToBeUpdated = customerDao.readCustomerById(customerWithEdits.getId());
        if (customerToBeUpdated == null) {
            // someone else deleted it
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                uiconsts.getString(CUSTOMER_MISSING_REFRESH_BUNDLE_MSG), null));
        }
        else {
            customerToBeUpdated = customerDao.updateCustomer(customerWithEdits);
            if (customerToBeUpdated == null) {
                //OptimisticLockException 
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    uiconsts.getString(CUSTOMER_OUTOFDATE_REFRESH_BUNDLE_MSG), null));
            }
            else {
                customerToBeUpdated.setEditable(false);
                int idx = customers.indexOf(customerWithEdits);
                customers.remove(idx);
                customers.add(idx, customerToBeUpdated);   
            }
        }
        return null; //current page
    }
    
    /**
     * Description: Cancels the current function. This can either be the editting function or adding a new customer.
     * Method is called whenever the 'cancel' button is pressed.
     * 
     * @param cust
     * @return stay on the same page.
     */
    public String cancelUpdate(CustomerPojo cust) {
        logMsg("cancelUpdate");
        cust.setEditable(false);
        return null; //current page
    }

    /**
     * Description: Deletes the customer record based of the customer's ID. Method is called when the 'delete' button 
     * is pressed.
     * 
     * @param custId
     */
    public void deleteCustomer(int custId) {
        logMsg("deleteCustomer: " + custId);
        CustomerPojo customerToBeRemoved = customerDao.readCustomerById(custId);
        if (customerToBeRemoved != null) {
            customerDao.deleteCustomerById(custId);
            customers.remove(customerToBeRemoved);
        }
    }

    /**
     * Description: Adds a new customer record to database and table.
     * 
     * @param theNewCustomer
     */
    public void addNewCustomer(CustomerPojo theNewCustomer) {
        logMsg("adding new Customer");
        CustomerPojo newCust = customerDao.createCustomer(theNewCustomer);
        customers.add(newCust);
    }

    /**
     * Description: Reloads the table to make sure it is filled with all the records. Method is called whenever 
     * 'refresh' is pressed.
     * 
     * @return
     */
    public String refreshCustomerForm() {
        logMsg("refreshCustomerForm");
        Iterator<FacesMessage> facesMessageIterator = facesContext.getMessages();
        while (facesMessageIterator.hasNext()) {
            facesMessageIterator.remove();
        }
        return "index.xhtml?faces-redirect=true";
    }

    /**
     * Description: Prints and handles all the messages depending on which method is being called.
     * 
     * @param msg
     */
    protected void logMsg(String msg) {
        sc.log(msg);
    }
}