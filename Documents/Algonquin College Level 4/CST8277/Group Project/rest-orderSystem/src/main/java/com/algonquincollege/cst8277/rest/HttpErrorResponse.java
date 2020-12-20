/*****************************************************************c******************o*******v******id********
 * File Name: HttpErrorResponse
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HttpErrorResponse implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /**
     * Int statusCode.
     */
    private final int statusCode;
    
    /**
     * String reasonPhrase.
     */
    private final String reasonPhrase;

    /**
     * Description: 2-arg Constructor.
     * 
     * @param code
     * @param reasonPhrase
     */
    public HttpErrorResponse(int code, String reasonPhrase) {
        this.statusCode = code;
        this.reasonPhrase = reasonPhrase;
    }
    
    /**
     * Description: Gets status code.
     * 
     * @return status code.
     */
    @JsonProperty("status-code")
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Description: Gets reason phrase.
     * 
     * @return reason phrase.
     */
    @JsonProperty("reason-phrase")
    public String getReasonPhrase() {
        return reasonPhrase;
    }

}