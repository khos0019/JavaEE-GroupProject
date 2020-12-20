/*****************************************************************c******************o*******v******id********
 * File: CustomerPojo.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * Student Name: Navraj Khosla
 * Student Number: 040856036
 * 
 */
package com.algonquincollege.cst8277.customers2.model;

import static com.algonquincollege.cst8277.customers2.model.CustomerPojo.ALL_CUSTOMERS_QUERY_NAME;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
* Description: model for the Customer object
*/
@Entity(name = "customers")
@EntityListeners(CustomerPojoListener.class)
@Table(name = "customer")
@Access(AccessType.PROPERTY)
@NamedQueries({
    @NamedQuery(name=ALL_CUSTOMERS_QUERY_NAME, query = "SELECT c FROM customers c"),
    })

public class CustomerPojo implements Serializable {
    
    /**
     * Long serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * String ALL_CUSTOMERS_QUERY_NAME
     * 
     * Description: Holds the query name.
     */
    public static final String ALL_CUSTOMERS_QUERY_NAME =
        "allCustomers";
    
    /**
     * Int id.
     */
    protected int id;
    
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
     * Int version.
     */
    protected int version;
    
    /**
     * LocalDateTime created.
     */
    protected LocalDateTime created;
    
    /**
     * LocalDateTime updated.
     */
    protected LocalDateTime updated;
    
    /**
     * Boolean editable.
     */
    protected boolean editable;

    /**
     * Description: Gets ID.
     * 
     * @return Value of id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    
    /**
     * Description: Sets ID.
     * 
     * @param id new value for id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Description: Gets first name.
     * 
     * @return the value for firstName.
     */
    @Column (name = "FNAME")
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
    @Column (name = "LNAME")
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
    @Column (name = "EMAIL")
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
    @Column (name = "PHONENUMBER")
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
     * Description: Gets the version.
     * 
     * @return The value for version.
     */
    @Version
    public int getVersion() {
        return version;
    }
    
    /**
     * Description: Sets the version.
     * 
     * @param version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Description: Gets the date of creation.
     * 
     * @return The value for created.
     */
    @Column (name = "CREATED")
    public LocalDateTime getCreatedDate() {
        return created;
    }
    
    /**
     * Description: Sets the date of creation.
     * 
     * @param created
     */
    public void setCreatedDate(LocalDateTime created) {
        this.created = created;
    }

    /**
     * Description: Gets the date of updated.
     * 
     * @return The value for updated.
     */
    @Column (name = "UPDATED")
    public LocalDateTime getUpdatedDate() {
        return updated;
    }
    
    /**
     * Description: Sets the date of updated.
     * 
     * @param updated
     */
    public void setUpdatedDate(LocalDateTime updated) {
        this.updated = updated;
    }
    
    /**
     * Description: Switches between editable and not for specific textboxes.
     */
    public void toggleEditable() {
        this.editable = !this.editable;
    }
    
    /**
     * Description: Checks if record is editable.
     * 
     * @return The value of editable.
     */
    @Transient 
    public boolean isEditable() {
        return editable;
    }
    
    /**
     * Description: Sets record as editable or not.
     * 
     * @param editable
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Description: Accociates an integer value for each object.
     * 
     * @return The value for result.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
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
        if (!(obj instanceof CustomerPojo)) {
            return false;
        }
        CustomerPojo other = (CustomerPojo) obj;
        if (id != other.id) {
            return false;
        }
        return true;
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
        if (version != -1) {
            builder
                .append("version=")
                .append(version)
                .append(", ");
        }
        if (created != null) {
            builder
                .append("created=")
                .append(created)
                .append(", ");
        }
        if (updated != null) {
            builder
                .append("updated=")
                .append(updated)
                .append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

}