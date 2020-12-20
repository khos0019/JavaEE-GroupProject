/*****************************************************************c******************o*******v******id********
 * File name: CustomerPojo
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.List;
import static com.algonquincollege.cst8277.models.CustomerPojo.ALL_CUSTOMERS_QUERY_NAME;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.CascadeType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
*
* Description: model for the Customer object
* The CustomerPojo class demonstrates several JPA features:
* <ul>
* <li>OneToOne relationship
* <li>OneToMany relationship
* </ul>
*/

@Entity(name = "Customer")
@Table(name = "CUSTOMER")
@AttributeOverride(name="id", column=@Column(name="CUST_ID"))
@NamedQuery(name=ALL_CUSTOMERS_QUERY_NAME, query="Select c from Customer c")
public class CustomerPojo extends PojoBase implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * String ALL_CUSTOMER_QUERY_NAME.
     * 
     * Description: Holds the query name.
     */
    public static final String ALL_CUSTOMERS_QUERY_NAME = "allCustomers";

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
     * ShippingAddressPojo shippingAddress.
     */
    protected AddressPojo shippingAddress;
    /**
     * BillingAddressPojo billingAddress.
     */
    protected AddressPojo billingAddress;
    /**
     * List OrderPojo orders.
     */
    protected List<OrderPojo> orders;
	
    /**
     * JPA requires each @Entity class have a default constructor
     */
	public CustomerPojo() {
	}
	
    /**
     * Description: Gets first name.
     * @return the value for firstName
     */
    @Column(name = "FNAME")
    public String getFirstName() {
        return firstName;
    }
    /**
     * Description: Sets first name.
     * @param firstName new value for firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Description: Gets last name.
     * @return the value for lastName
     */
    @Column(name = "LNAME")
    public String getLastName() {
        return lastName;
    }
    /**
     * Description: Sets last name.
     * @param lastName new value for lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Description: Gets the email.
     * @return the value for email
     */
    public String getEmail() {
        return email;
    }
    /**
     * Description: Sets the email.
     * @param email new value for email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Description: Gets the phone number.
     * @return the value for phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * Description: Sets the phone number.
     * @param phonenumber new value for phonenumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * dont use CascadeType.All (skipping CascadeType.REMOVE): what if two customers
     * live at the same address and 1 leaves the house but the other does not?
     * @return
     */
    
    /**
     * Description: Gets the shipping address.
     * 
     * @return The value for shippingAddress.
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "SHIPPING_ADDR")
    public AddressPojo getShippingAddress() {
        return shippingAddress;
    }
    /**
     * Description: Sets the shipping address.
     * 
     * @param shippingAddress
     */
    public void setShippingAddress(AddressPojo shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * Description: Gets the billing address.
     * 
     * @return The value for billingAddress.
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "BILLING_ADDR")
    public AddressPojo getBillingAddress() {
        return billingAddress;
    }
    /**
     * Description: Sets the billing address.
     * 
     * @param billingAddress
     */
    public void setBillingAddress(AddressPojo billingAddress) {
        this.billingAddress = billingAddress;
    }
    /**
     * Description: Gets the orders.
     * 
     * @return The value for orders.
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "owningCustomer", cascade = CascadeType.PERSIST, orphanRemoval = true)
    public List<OrderPojo> getOrders() {
        return orders;
    }

    /**
     * Description: Sets the orders.
     * 
     * @param orders
     */
    public void setOrders(List<OrderPojo> orders) {
        this.orders = orders;
    }

    /**
     * Description: Formats the information using the string builder.
     * 
     * @return The value for builder.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
            .append("Customer [id=")
            .append(id)
            .append(", ");
        if (firstName != null) {
            builder
                .append("firstName=")
                .append(firstName)
                .append(", ");
        }
        if (lastName != null) {
            builder
                .append("lastName=")
                .append(lastName)
                .append(", ");
        }
        if (phoneNumber != null) {
            builder
                .append("phoneNumber=")
                .append(phoneNumber)
                .append(", ");
        }
        if (email != null) {
            builder
                .append("email=")
                .append(email)
                .append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

}