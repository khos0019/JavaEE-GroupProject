/*****************************************************************c******************o*******v******id********
 * File Name: CredentialResource
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.utils.MyConstants.CREDENTIAL_RESOURCE_NAME;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.security.Principal;

import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.soteria.WrappingCallerPrincipal;

import com.algonquincollege.cst8277.models.SecurityUser;

/**
 * Descript: REST endpoint class to test authorization/authentication
 */
@Path(CREDENTIAL_RESOURCE_NAME)
@Produces(MediaType.APPLICATION_JSON)
public class CredentialResource {

    /**
     * ServletContext servletContext.
     */
    @Inject
    protected ServletContext servletContext;

    /**
     * SecurityContext securityContent.
     */
    @Inject
    protected SecurityContext securityContent;

    /**
     * Description: Gets credentials.
     * 
     * @return repsonse.
     */
    @GET
    public Response getCredentials() {
        servletContext.log("testing credentials ...");
        Response response = null;
        Principal callerPrincipal = securityContent.getCallerPrincipal();
        if (callerPrincipal == null) {
            response = Response.status(UNAUTHORIZED).build();
        }
        else {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)callerPrincipal;
            SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
            response = Response.ok(sUser).build();
        }
        return response;
    }

}