/*****************************************************************c******************o*******v******id********
 * File Name: CustomerResource
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.utils.MyConstants.ADMIN_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_ADDRESS_RESOURCE_PATH;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.soteria.WrappingCallerPrincipal;

import com.algonquincollege.cst8277.ejb.CustomerService;
import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.SecurityUser;

/**
 * This class exposes the customer endpoints
 */
@Path(CUSTOMER_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {
    @EJB
    protected CustomerService customerServiceBean;
    @Inject
    protected ServletContext servletContext;
    @Inject
    protected SecurityContext sc;

    /**
     * Endpoint to get all customers. Ex: /customer
     * 
     * @return response.
     */
    @GET
    @RolesAllowed(ADMIN_ROLE)
    public Response getCustomers() {
        servletContext.log("retrieving all customers ...");
        List<CustomerPojo> custs = customerServiceBean.getAllCustomers();
        Response response = Response.ok(custs).build();
        return response;
    }

    /**
     * Gets customer by customer id
     * This method also makes sure that either admin or same customer can access its detail.
     * 
     * @return response.
     */
    @GET
    @Path(RESOURCE_PATH_ID_PATH)
    @RolesAllowed({USER_ROLE, ADMIN_ROLE})
    public Response getCustomerById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific customer " + id);
        Response response = null;
        CustomerPojo cust = null;
        if (sc.isCallerInRole(ADMIN_ROLE)) {
            cust = customerServiceBean.getCustomerById(id);
            response = Response.status(cust == null ? NOT_FOUND : OK).entity(cust).build();
        }
        else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
            cust = sUser.getCustomer();
            if (cust != null && cust.getId() == id) {
                response = Response.status(OK).entity(cust).build();
            }
            else {
                throw new ForbiddenException();
            }
        }
        else {
            response = Response.status(BAD_REQUEST).build();
        }
        return response;
    }

    /**
     * Add a new customer.
     * 
     * @param newCustomer
     * @return response.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(ADMIN_ROLE)
    @Transactional
    public Response addCustomer(CustomerPojo newCustomer) {
        servletContext.log("try to add a new customer");
        Response response = null;
        CustomerPojo newCustomerWithIdTimestamps = customerServiceBean.persistCustomer(newCustomer);
        // build a SecurityUser linked to the new customer
        customerServiceBean.buildUserForNewCustomer(newCustomerWithIdTimestamps);
        response = Response.ok(newCustomerWithIdTimestamps).build();
        return response;
    }

    /**
     * Add address for a customer
     * 
     * @param id
     * @param newAddress
     * @return response.
     */
    @PUT
    @Path(CUSTOMER_ADDRESS_RESOURCE_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ADMIN_ROLE})
    @Transactional
    public Response addAddressForCustomer(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id,
        AddressPojo newAddress) {
        servletContext.log("try to add address for a customer");
        Response response = null;
        CustomerPojo updatedCustomer = customerServiceBean.setAddressFor(id, newAddress);
        response = Response.ok(updatedCustomer).build();
        return response;
    }

    /**
     * Deletes a customer
     * 
     * @param id
     * @return no content or status.
     */
    @DELETE
    @Path(RESOURCE_PATH_ID_PATH)
    @RolesAllowed(ADMIN_ROLE)
    @Transactional
    public Response deleteCustomer(@PathParam("id") int id) {
        servletContext.log("try to delete a customer and corresponding security user");
        boolean deleted = customerServiceBean.deleteCustomer(id);
        if (deleted)
            return Response.noContent().build();
        else
            return Response.status(NOT_FOUND).build();
    }

    /**
     * Adds order for a customer
     * 
     * @param id
     * @param newOrder
     * @return response.
     */
    @PUT
    @Path(RESOURCE_PATH_ID_PATH + "/order")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Transactional
    public Response addOrderForCustomer(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, OrderPojo newOrder) {
        servletContext.log("try to add a new customer");
        Response response = null;
        CustomerPojo updatedCustomer = customerServiceBean.setOrderFor(id, newOrder);
        response = Response.ok(updatedCustomer).build();
        return response;
    }

    /**
     * Fetches billing address of a customer
     * @param id
     * @return response.
     */
    @GET
    @Path(RESOURCE_PATH_ID_PATH + "/billingaddress")
    @RolesAllowed({USER_ROLE, ADMIN_ROLE})
    public Response getCustomerBillingAddressById(@PathParam("id") int id) {
        servletContext.log("try to fetch customer's billing address");
        Response response = null;
        AddressPojo address = null;
        if (sc.isCallerInRole(ADMIN_ROLE)) {
            address = customerServiceBean.getBillingAddressByCustomerId(id);
            response = Response.status(address == null ? NOT_FOUND : OK).entity(address).build();
        }
        else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
            CustomerPojo cust = sUser.getCustomer();
            if (cust != null && cust.getId() == id) {
                address = customerServiceBean.getBillingAddressByCustomerId(id);
                response = Response.status(OK).entity(address).build();
            }
            else {
                throw new ForbiddenException();
            }
        }
        else {
            response = Response.status(BAD_REQUEST).build();
        }
        return response;
    }

    /**
     * Fetches shipping address of a customer
     * @param id
     * @return response.
     */
    @GET
    @Path(RESOURCE_PATH_ID_PATH + "/shippingaddress")
    @RolesAllowed({USER_ROLE, ADMIN_ROLE})
    public Response getCustomerShippingAddressById(@PathParam("id") int id) {
        servletContext.log("try to fetch customer's shipping address");
        Response response = null;
        AddressPojo address = null;
        if (sc.isCallerInRole(ADMIN_ROLE)) {
            address = customerServiceBean.getShippingAddressByCustomerId(id);
            response = Response.status(address == null ? NOT_FOUND : OK).entity(address).build();
        }
        else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
            CustomerPojo cust = sUser.getCustomer();
            if (cust != null && cust.getId() == id) {
                address = customerServiceBean.getShippingAddressByCustomerId(id);
                response = Response.status(OK).entity(address).build();
            }
            else {
                throw new ForbiddenException();
            }
        }
        else {
            response = Response.status(BAD_REQUEST).build();
        }
        return response;
    }

    /**
     * Fetches all orders of a customer
     * @param id
     * @return response.
     */
    @GET
    @Path(RESOURCE_PATH_ID_PATH + "/order")
    @RolesAllowed({USER_ROLE, ADMIN_ROLE})
    public Response getCustomerOrdersByCustomerId(@PathParam("id") int id) {
        servletContext.log("try to fetch customer's orders");
        Response response = null;
        List<OrderPojo> orders = null;
        if (sc.isCallerInRole(ADMIN_ROLE)) {
            orders = customerServiceBean.getOrdersByCustomerId(id);
            response = Response.status(orders == null ? NOT_FOUND : OK).entity(orders).build();
        }
        else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
            CustomerPojo cust = sUser.getCustomer();
            if (cust != null && cust.getId() == id) {
                orders = customerServiceBean.getOrdersByCustomerId(id);
                response = Response.status(OK).entity(orders).build();
            }
            else {
                throw new ForbiddenException();
            }
        }
        else {
            response = Response.status(BAD_REQUEST).build();
        }
        return response;
    }

    /**
     * Fetches a specific order of a customer
     * @param orderId
     * @return response.
     */
    @GET
    @Path("/order/{orderId}")
    @RolesAllowed({USER_ROLE, ADMIN_ROLE})
    public Response getCustomerOrdersByOrderId(@PathParam("orderId") int orderId) {
        servletContext.log("try to fetch specific order for a customer");
        Response response = null;
        OrderPojo order = null;
        order = customerServiceBean.getOrderByOrderId(orderId);
        if (sc.isCallerInRole(ADMIN_ROLE)) {
            response = Response.status(order == null ? NOT_FOUND : OK).entity(order).build();
        }
        else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
            CustomerPojo cust = sUser.getCustomer();
            if (cust != null && order != null && order.getOwningCustomer() != null &&
                cust.getId() == order.getOwningCustomer().getId()) {
                response = Response.status(OK).entity(order).build();
            }
            else {
                throw new ForbiddenException();
            }
        }
        else {
            response = Response.status(BAD_REQUEST).build();
        }
        return response;
    }
    
    
    
}