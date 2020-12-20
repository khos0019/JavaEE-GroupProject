/*****************************************************************c******************o*******v******id********
 * File Name: SecurityRole
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import static com.algonquincollege.cst8277.models.SecurityRole.ROLE_BY_NAME_QUERY;


/**
 * Role class used for (JSR-375) Java EE Security authorization/authentication
 */
@Entity(name="SecurityRole")
@Table(name="SECURITY_ROLE")
@NamedQueries(
    @NamedQuery(name=ROLE_BY_NAME_QUERY, query = "select r from SecurityRole r where r.roleName=:param1")
)
public class SecurityRole implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * String ROLE_BY_NAME_QUERY with value roleByName.
     * 
     * Description: Holds the query name.
     */
    public static final String ROLE_BY_NAME_QUERY = "roleByName";

    /**
     * Integer id.
     */
    protected int id;
    /**
     * String roleName.
     */
    protected String roleName;
    /**
     * Set SecurityUser users.
     */
    protected Set<SecurityUser> users;

    /**
     * JPA requires each @Entity class have a default constructor
     */
    public SecurityRole() {
        super();
    }

    /**
     * Description: Gets the id.
     * @return id value
     */
    @Id
    @Column(name="ROLE_ID")
    public int getId() {
        return id;
    }
    
    /**
     * Description: Sets the user id.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Description: Gets the role name.
     * 
     * @return The value for roleName.
     */
    public String getRoleName() {
        return roleName;
    }
    
    /**
     * Description: Sets the role name.
     * @param roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * Description: Gets the users.
     * @return users
     */
    @JsonInclude(Include.NON_NULL)
    @ManyToMany(mappedBy = "roles")
    public Set<SecurityUser> getUsers() {
        return users;
    }
    
    /**
     * Description: Sets the users.
     * @param users
     */
    public void setUsers(Set<SecurityUser> users) {
        this.users = users;
    }
    /**
     * Description: Adds the users.
     * @param user
     */
    public void addUserToRole(SecurityUser user) {
        getUsers().add(user);
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
        SecurityRole other = (SecurityRole)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}