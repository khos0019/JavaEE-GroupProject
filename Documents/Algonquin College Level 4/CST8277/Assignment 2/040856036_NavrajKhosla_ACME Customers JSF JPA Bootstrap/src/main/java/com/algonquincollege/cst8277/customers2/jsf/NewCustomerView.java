/*****************************************************************c******************o*******v******id********
 * File: NewCustomerView.java
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

import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;

import com.algonquincollege.cst8277.customers2.model.CustomerPojo;

@Named
@SessionScoped
public class NewCustomerView implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * String firstName.
     */
    protected String firstName;
    
    /**
     * String lastName.
     */
    protected String lastName;
    
    /**
     * String email.
     */
    protected String email;
    
    /**
     * String phoneNumber.
     */
    protected String phoneNumber;

    /**
     * CustomerController employeeController.
     */
    @Inject
    @ManagedProperty("#{customerController}")
    protected CustomerController employeeController;

    /**
     * Description: No-arg constructor.
     */
    public NewCustomerView() {
    }

    /**
     * Description: Gets first name.
     * 
     * @return the value for firstName.
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Description: Sets first name.
     * 
     * @param firstName new value for firstName.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Description: Gets last name.
     * 
     * @return The value for lastName.
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Description: Sets last name.
     * 
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Description: Gets the email.
     * 
     * @return The value for email.
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Description: Sets the email.
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Description: Gets the phonenumber.
     * 
     * @return The value for phoneNumber.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Description: Sets the phonenumber.
     * 
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Description: Gets all the input from the addNewCustomer form and uses the input to create a new customer
     * record.
     */
    public void addCustomer() {
        if (allNotNullOrEmpty(firstName, lastName, email, phoneNumber)) {
            CustomerPojo theNewCustomer = new CustomerPojo();
            theNewCustomer.setFirstName(getFirstName());
            theNewCustomer.setLastName(getLastName());
            theNewCustomer.setEmail(getEmail());
            theNewCustomer.setPhoneNumber(getPhoneNumber());         
         
            // this Managed Bean does not know how to 'do' anything, ask controller
            employeeController.addNewCustomer(theNewCustomer);

            //clean up
            employeeController.toggleAdding();
            setFirstName(null);
            setLastName(null);
            setEmail(null);
            setPhoneNumber(null);
        }
    }
    
    /**
     * Description: Checks if any object is not null or empty.
     * 
     * @param values
     * @return true or false.
     */
    static boolean allNotNullOrEmpty(final Object... values) {
        if (values == null) {
            return false;
        }
        for (final Object val : values) {
            if (val == null) {
                return false;
            }
            if (val instanceof String) {
                String str = (String)val;
                if (str.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}