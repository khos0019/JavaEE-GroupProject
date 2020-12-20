/*****************************************************************c******************o*******v******id********
 * File Name: ProductResource
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.utils.MyConstants.PRODUCT_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.algonquincollege.cst8277.ejb.CustomerService;
import com.algonquincollege.cst8277.models.ProductPojo;
import static com.algonquincollege.cst8277.utils.MyConstants.ADMIN_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;

/**
 * This class exposes the product endpoints
 */
@Path(PRODUCT_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    /**
     * CustomerService customerServiceBean.
     */
    @EJB
    protected CustomerService customerServiceBean;

    /**
     * ServletContext servletContext.
     */
    @Inject
    protected ServletContext servletContext;

    /**
     * Fetches all products
     * @return response.
     */
    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getProducts() {
        servletContext.log("retrieving all products ...");
        List<ProductPojo> custs = customerServiceBean.getAllProducts();
        Response response = Response.ok(custs).build();
        return response;
    }

    /**
     * Fetches a product by it's id
     * @param id
     * @return response.
     */
    @GET
    @Path(RESOURCE_PATH_ID_PATH)
    @RolesAllowed({USER_ROLE, ADMIN_ROLE})
    public Response getProductById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific product " + id);
        ProductPojo theProduct = customerServiceBean.getProductById(id);
        Response response = Response.ok(theProduct).build();
        return response;
    }
    
    /**
     * Adds a new product
     * @param newProduct
     * @return response.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(ADMIN_ROLE)
    @Transactional
    public Response addProduct(ProductPojo newProduct) {
      Response response = null;
      ProductPojo newProductPojo = customerServiceBean.persistProduct(newProduct);
      response = Response.ok(newProductPojo).build();
      return response;
    }

    /**
     * Deletes a product
     * @param id
     * @return no content or status.
     */
    @DELETE
    @Path(RESOURCE_PATH_ID_PATH)
    @RolesAllowed(ADMIN_ROLE)
    @Transactional
    public Response deleteProduct(@PathParam("id") int id) {
        boolean deleted = customerServiceBean.deleteProduct(id);
        if(deleted)
        return Response.noContent().build();
        else
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    
}