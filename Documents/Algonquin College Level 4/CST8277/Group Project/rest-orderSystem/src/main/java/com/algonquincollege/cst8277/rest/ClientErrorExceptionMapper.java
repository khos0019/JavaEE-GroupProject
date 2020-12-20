/*****************************************************************c******************o*******v******id********
 * File Name: ClientErrorExceptionMapper
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.rest;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ClientErrorExceptionMapper implements ExceptionMapper<ClientErrorException> {
    
    /**
     * Description: Creates a response based of client request error.
     */
    @Override
    public Response toResponse(ClientErrorException exception) {
      Response response = exception.getResponse();
      Response.StatusType statusType = response.getStatusInfo();
      int statusCode = statusType.getStatusCode();
      String reasonPhrase = statusType.getReasonPhrase();
      HttpErrorResponse entity = new HttpErrorResponse(statusCode, reasonPhrase);
      return Response.status(statusCode).entity(entity).build();
    }
}