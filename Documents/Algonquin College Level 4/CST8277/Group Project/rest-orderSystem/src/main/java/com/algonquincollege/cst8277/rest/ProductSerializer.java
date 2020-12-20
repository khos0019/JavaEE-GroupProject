/*****************************************************************c******************o*******v******id********
 * File Name: ProductSerializer
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.rest;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.algonquincollege.cst8277.models.ProductPojo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ProductSerializer extends StdSerializer<Set<ProductPojo>> implements Serializable {
    
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * No-arg Constructor.
     */
    public ProductSerializer() {
        this(null);
    }

    /**
     * One-arg Constructor.
     * 
     * @param t
     */
    public ProductSerializer(Class<Set<ProductPojo>> t) {
        super(t);
    }

    /**
     * Description: Create a 'hollow' copy of the original Product entity.
     * 
     * @param originalProducts
     * @param generator
     * @param provider
     */
    @Override
    public void serialize(Set<ProductPojo> originalProducts, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
        
        Set<ProductPojo> hollowProducts = new HashSet<>();
        for (ProductPojo originalProduct : originalProducts) {
            ProductPojo hollowP = new ProductPojo();
            hollowP.setId(originalProduct.getId());
            hollowP.setDescription(originalProduct.getDescription());
            hollowP.setCreatedDate(originalProduct.getCreatedDate());
            hollowP.setUpdatedDate(originalProduct.getUpdatedDate());
            hollowP.setVersion(originalProduct.getVersion());
            hollowP.setSerialNo(originalProduct.getSerialNo());
            hollowP.setStores(null);
            hollowProducts.add(hollowP);
        }
        generator.writeObject(hollowProducts);
    }
}