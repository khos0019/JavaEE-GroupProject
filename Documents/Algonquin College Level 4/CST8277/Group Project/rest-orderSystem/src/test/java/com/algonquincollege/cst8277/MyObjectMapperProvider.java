/*****************************************************************c******************o*******v******id********
 * File Name: MyObjectMapperProvider
 * Course materials (20W) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Provider
public class MyObjectMapperProvider implements ContextResolver<ObjectMapper> {
    
    /**
     * ObjectMapper defaultObjectMapper.
     */
    final ObjectMapper defaultObjectMapper;
    
    /**
     * No-arg Constructor.
     */
    public MyObjectMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
    }
    
    /**
     * Description: Gets context.
     * 
     * @param type
     * @return mapper
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
            return defaultObjectMapper;
        }
    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}