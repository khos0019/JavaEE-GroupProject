/*****************************************************************c******************o*******v******id********
 * File Name: SecurityRoleSerializer
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

import com.algonquincollege.cst8277.models.SecurityRole;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class SecurityRoleSerializer extends StdSerializer<Set<SecurityRole>> implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * No-arg Constructor.
     */
    public SecurityRoleSerializer() {
        this(null);
    }

    /**
     * One-arg Constructor.
     * 
     * @param t
     */
    public SecurityRoleSerializer(Class<Set<SecurityRole>> t) {
        super(t);
    }

    /**
     * Description: Create a 'hollow' copy of the original Security Roles entity.
     * 
     * @param originalRoles
     * @param generator
     * @param provider
     */
    @Override
    public void serialize(Set<SecurityRole> originalRoles, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
        
        Set<SecurityRole> hollowRoles = new HashSet<>();
        for (SecurityRole originalRole : originalRoles) {
            SecurityRole hollowP = new SecurityRole();
            hollowP.setId(originalRole.getId());
            hollowP.setRoleName(originalRole.getRoleName());
            hollowP.setUsers(null);
            hollowRoles.add(hollowP);
        }
        generator.writeObject(hollowRoles);
    }
}