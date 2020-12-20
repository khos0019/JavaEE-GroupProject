/*****************************************************************c******************o*******v******id********
 * File Name: ConfigureJacksonObjectMapper
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.algonquincollege.cst8277.utils.HttpErrorAsJSONServlet;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Provider
public class ConfigureJacksonObjectMapper implements ContextResolver<ObjectMapper> {
    
    /**
     * ObjectMapper objectMapper.
     */
    private final ObjectMapper objectMapper;

    /**
     * Description: No-arg Constructor.
     */
    public ConfigureJacksonObjectMapper() {
        this.objectMapper = createObjectMapper();
    }

    /**
     * Description: Gets the context.
     * 
     * @return object mapper.
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }

    /**
     * Description: Configure JDK 8's new DateTime objects to use proper ISO-8601 timeformat
     * 
     * @return mapper.
     */
    protected ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            // lenient parsing of JSON - if a field has a typo, don't fall to pieces
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            ;
        HttpErrorAsJSONServlet.setObjectMapper(mapper);
        return mapper;
    }
}