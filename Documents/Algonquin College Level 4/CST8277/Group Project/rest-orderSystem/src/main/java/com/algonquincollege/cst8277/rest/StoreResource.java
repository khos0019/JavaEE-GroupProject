/*****************************************************************c******************o*******v******id********
 * File Name: StoreResource
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.STORE_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.ADMIN_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;

import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.algonquincollege.cst8277.ejb.CustomerService;
import com.algonquincollege.cst8277.models.ProductPojo;
import com.algonquincollege.cst8277.models.StorePojo;

/**
 * This class exposes the store endpoints
 */
@Path(STORE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StoreResource {

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
     * Fetches all stores
     * @return reponse.
     */
    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getStores() {
        servletContext.log("retrieving all stores ...");
        List<StorePojo> stores = customerServiceBean.getAllStores();
        Response response = Response.ok(stores).build();
        return response;
    }

    /**
     * Fetches store by it's id
     * @param id
     * @return response.
     */
    @GET
    @Path(RESOURCE_PATH_ID_PATH)
    @RolesAllowed({USER_ROLE, ADMIN_ROLE})
    public Response getStoreById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific store " + id);
        StorePojo theStore = customerServiceBean.getStoreById(id);
        Response response = Response.ok(theStore).build();
        return response;
    }

    /**
     * Adds a store
     * @param newStore
     * @return response.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(ADMIN_ROLE)
    @Transactional
    public Response addStore(StorePojo newStore) {
      servletContext.log("try to add a new store");
      Response response = null;
      StorePojo store = customerServiceBean.persistStore(newStore);
      response = Response.ok(store).build();
      return response;
    }

    /**
     * Deletes a store
     * @param id
     * @return no content or status.
     */
    @DELETE
    @Path(RESOURCE_PATH_ID_PATH)
    @RolesAllowed(ADMIN_ROLE)
    @Transactional
    public Response deleteStore(@PathParam("id") int id) {
        boolean deleted = customerServiceBean.deleteStore(id);
        if(deleted)
        return Response.noContent().build();
        else
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    /**
     * Adds a product to store
     * @param id
     * @param newProduct
     * @return response.
     */
    @PUT
    @Path(RESOURCE_PATH_ID_PATH+"/product")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ADMIN_ROLE})
    @Transactional
    public Response addProductToStore(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, ProductPojo newProduct) {
      Response response = null;
      StorePojo updatedStore = customerServiceBean.setProductToStore(id, newProduct);
      response = Response.ok(updatedStore).build();
      return response;
    }

    /**
     * Fetches all products of a store
     * @param id
     * @return response.
     */
    @GET
    @Path(RESOURCE_PATH_ID_PATH+"/product")
    @RolesAllowed({USER_ROLE, ADMIN_ROLE})
    public Response getAllProductsOfStore(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve all products of a store " + id);
       Set<ProductPojo> products  = customerServiceBean.getProductsByStoreId(id);
        Response response = Response.ok(products).build();
        return response;
    }
 
}