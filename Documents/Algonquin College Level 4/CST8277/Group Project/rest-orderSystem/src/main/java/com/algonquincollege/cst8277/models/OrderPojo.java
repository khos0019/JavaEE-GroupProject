/*****************************************************************c******************o*******v******id********
 * File Name: OrderPojo
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

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
*
* Description: model for the Order object
*/
@Entity(name = "Order")
@Table(name = "ORDER_TBL")
@AttributeOverride(name="id", column=@Column(name="ORDER_ID"))
public class OrderPojo extends PojoBase implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * String description.
     */
    protected String description;
    /**
     * List OrderLinePojo orderlines.
     */
    protected List<OrderLinePojo> orderlines;
    /**
     * CustomerPojo owningCustomer.
     */
    protected CustomerPojo owningCustomer;
    
    /**
     * JPA requires each @Entity class have a default constructor
     */
	public OrderPojo() {
	}
	/**
     * Description: Gets description.
     * 
     * @return The value for description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Description: Sets description.
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Description: Gets order lines.
     * 
     * @return The value for orderLines.
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "owningOrder")
	public List<OrderLinePojo> getOrderlines() {
		return this.orderlines;
	}
    /**
     * Description: Sets order lines.
     * 
     * @param orderlines
     */
	public void setOrderlines(List<OrderLinePojo> orderlines) {
		this.orderlines = orderlines;
	}
	/**
     * Description: Adds order line.
     * 
     * @return The value for orderline.
     */
	public OrderLinePojo addOrderline(OrderLinePojo orderline) {
		getOrderlines().add(orderline);
		orderline.setOwningOrder(this);
		return orderline;
	}
	/**
     * Description: Removes order line.
     * 
     * @return The value for orderline.
     */
	public OrderLinePojo removeOrderline(OrderLinePojo orderline) {
		getOrderlines().remove(orderline);
        orderline.setOwningOrder(null);
		return orderline;
	}

	/**
     * Description: Gets owning customer.
     * 
     * @return The value for owningCustomer.
     */
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "OWNING_CUST_ID")
	public CustomerPojo getOwningCustomer() {
		return this.owningCustomer;
	}
	/**
     * Description: Sets owning customer.
     * 
     * @param owner
     */
	public void setOwningCustomer(CustomerPojo owner) {
		this.owningCustomer = owner;
	}

}