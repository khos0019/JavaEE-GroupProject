/*****************************************************************c******************o*******v******id********
 * File Name: SecurityUser
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.models;

import static com.algonquincollege.cst8277.models.SecurityUser.SECURITY_USER_BY_NAME_QUERY;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.algonquincollege.cst8277.rest.SecurityRoleSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.algonquincollege.cst8277.models.SecurityUser.USER_FOR_OWNING_CUST_QUERY;

/**
 * User class used for (JSR-375) Java EE Security authorization/authentication
 */
@Entity(name="SecurityUser")
@Table(name="SECURITY_USER")
@NamedQueries({
@NamedQuery(name=USER_FOR_OWNING_CUST_QUERY, query = "select u from SecurityUser u where u.customer= :param1"),
@NamedQuery(name=SECURITY_USER_BY_NAME_QUERY, query="Select s from SecurityUser s Where s.username = ?1")
})
public class SecurityUser implements Serializable, Principal {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * String USER_FOR_OWNING_CUST_QUERY.
     * 
     * Description: Holds the query name for USER_FOR_OWNING_CUST_QUERY.
     */
    public static final String USER_FOR_OWNING_CUST_QUERY =
        "userForOwningCust";
    
    /**
     * String SECURITY_USER_BY_NAME_QUERY.
     * 
     * Description: Holds the query name for SECURITY_USER_BY_NAME_QUERY.
     */
    public static final String SECURITY_USER_BY_NAME_QUERY =
        "userByName";

    /**
     * Integer id.
     */
    protected int id;
    /**
     * String username.
     */
    protected String username;
    /**
     * String pwhash.
     */
    protected String pwHash;
    /**
     * Set SecurityRole roles.
     */
    protected Set<SecurityRole> roles = new HashSet<>();
    /**
     * CustomerPojo cust.
     */
    protected CustomerPojo cust;
    

    /**
     * JPA requires each @Entity class have a default constructor
     */
    public SecurityUser() {
        super();
    }

    @Id
    @Column(name="USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * Description: Gets the id.
     * 
     * @return The value for id.
     */
    public int getId() {
        return id;
    }
    /**
     * Description: Sets the id.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Description: Gets the username.
     * 
     * @return The value for username.
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Description: Sets the username.
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Description: Gets the pwHash.
     * @return The value for pwHash.
     */
    @JsonIgnore
    public String getPwHash() {
        return pwHash;
    }
    /**
     * Description: Sets the pwHash.
     * @param pwHash
     */
    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }
    
    /**
     * Description: Gets the roles.
     * @return The value for roles.
     */
    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(using = SecurityRoleSerializer.class)
    @ManyToMany
    @JoinTable(
        name="SECURITY_USER_SECURITY_ROLE",
        joinColumns=@JoinColumn(name="USER_ID", referencedColumnName="USER_ID"),
        inverseJoinColumns=@JoinColumn(name="ROLE_ID", referencedColumnName="ROLE_ID")
        )
    public Set<SecurityRole> getRoles() {
        return roles;
    }
    /**
     * Description: Sets the for roles.
     * @param roles
     */
    public void setRoles(Set<SecurityRole> roles) {
        this.roles = roles;
    }

    
    /**
     * Description: Gets the customer.
     * @return value for customer
     */
    @OneToOne
    @JoinColumn(name = "CUST_ID")
    public CustomerPojo getCustomer() {
        return cust;
    }
    
    /**
     * Description: Sets the Customer.
     * @param cust
     */
    public void setCustomer(CustomerPojo cust) {
        this.cust = cust;
    }

    /**
     * Description: Gets the Username.
     * 
     * @return The value for Username.
     */
    //Principal
    @JsonIgnore
    @Override
    public String getName() {
        return getUsername();
    }

    /**
     * Description: Accociates an integer and id value for each object.
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
     * Description: Checks if the objects and id's are equal or not.
     * 
     * @return True or false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SecurityUser other = (SecurityUser)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

}