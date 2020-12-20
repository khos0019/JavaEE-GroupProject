/*****************************************************************c******************o*******v******id********
 * File Name: AddressPojo
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
*
* Description: model for the Address object
*/
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
  @JsonSubTypes({
    @Type(value = BillingAddressPojo.class, name = "B"),
    @Type(value = ShippingAddressPojo.class, name = "S")
})
@Entity(name="Address")
@Table(name = "CUST_ADDR")
@AttributeOverride(name="id", column=@Column(name="ADDR_ID"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ADDR_TYPE", columnDefinition = "VARCHAR", length = 1)
@MappedSuperclass
public abstract class AddressPojo extends PojoBase implements Serializable {

    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * String street.
     */
    protected String street;
    /**
     * String city.
     */
    protected String city;
    /**
     * String country.
     */
    protected String country;
    /**
     * String postal.
     */
    protected String postal;
    /**
     * String state.
     */
    protected String state;
    /**
     * String addrType.
     */
    protected String addrType;

    /**
     * JPA requires each @Entity class have a default constructor
     */
    public AddressPojo() {
        super();
    }

    /**
     * Description: Gets city name.
     * 
     * @return The value for city.
     */
    public String getCity() {
        return city;
    }
    /**
     * Description: Sets city name.
     * 
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Description: Gets country name.
     * 
     * @return The value for country.
     */
    public String getCountry() {
        return country;
    }
    /**
     * Description: Sets country name.
     * 
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Description: Gets the postal code.
     * 
     * @return The value for postal.
     */
    public String getPostal() {
        return postal;
    }
    /**
     * Description: Sets the postal code.
     * 
     * @param postal
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     * Description: Gets state name.
     * 
     * @return The value for state.
     */
    public String getState() {
        return state;
    }
    /**
     * Description: Sets state name.
     * 
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Description: Gets street name.
     * 
     * @return The value for street.
     */
    public String getStreet() {
        return street;
    }
    /**
     * Description: Sets street name.
     * 
     * @param street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Description: Gets address.
     * 
     * @return The value for addrType.
     */
    @Column(name = "ADDR_TYPE")
    public String getAddrType() {
        return addrType;
    }

    /**
     * Description: Sets address.
     * 
     * @param addrType
     */
    public void setAddrType(String addrType) {
        this.addrType = addrType;
    }
    
}