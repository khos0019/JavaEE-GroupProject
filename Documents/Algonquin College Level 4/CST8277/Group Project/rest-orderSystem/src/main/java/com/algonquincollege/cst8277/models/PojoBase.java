/*****************************************************************c******************o*******v******id********
 * File Name: PojoBase
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Abstract class that is base of (class) hierarchy for all c.a.cst8277.models @Entity classes
 */
@MappedSuperclass
@Access(AccessType.PROPERTY) // NOTE: by using this annotations, any annotation on a field is ignored without warning
@EntityListeners(PojoListener.class)
public abstract class PojoBase implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Int id.
     */
    protected int id;
    /**
     * LocalDateTime created.
     */
    protected LocalDateTime created;
    /**
     * LocalDateTime updated.
     */
    protected LocalDateTime updated;
    /**
     * Int version.
     */
    protected int version;

    /**
     * Description: Gets ID.
     * 
     * @return The value for id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    /**
     * Description: Sets ID.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Description: Gets the created date.
     * 
     * @return The value for created.
     */
    @Column(name="CREATED")
    public LocalDateTime getCreatedDate() {
        return created;
    }
    /**
     * Description: Sets the created date.
     * 
     * @param created
     */
    public void setCreatedDate(LocalDateTime created) {
        this.created = created;
    }

    /**
     * Description: Gets the updated date.
     * 
     * @return The value for updated.
     */
    @Column(name="UPDATED")
    public LocalDateTime getUpdatedDate() {
        return updated;
    }
    /**
     * Description: Sets the updated date.
     * 
     * @param updated
     */
    public void setUpdatedDate(LocalDateTime updated) {
        this.updated = updated;
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
     *  It is a good idea for hashCode() to use the @Id property
     *  since it maps to table's PK and that is how Db determine's identity
     *  (same for equals()
     */
    
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
        if (!(obj instanceof PojoBase)) {
            return false;
        }
        PojoBase other = (PojoBase)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}