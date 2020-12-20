/*****************************************************************c******************o*******v******id********
 * File Name: BuildSecurityRolesAndUsers
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.ejb;

import static com.algonquincollege.cst8277.utils.MyConstants.ADMIN_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_ADMIN_USER;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_KEY_SIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_PROPERTY_ALGORITHM;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_PROPERTY_ITERATIONS;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_SALT_SIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_ALGORITHM;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_ITERATIONS;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_KEYSIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_SALTSIZE;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

import com.algonquincollege.cst8277.models.SecurityRole;
import com.algonquincollege.cst8277.models.SecurityUser;
import com.algonquincollege.cst8277.security.CustomIdentityStoreJPAHelper;

/**
 * This Stateless Session bean is 'special' because it is also a Singleton and
 * it runs at startup.
 *
 * How do we 'bootstrap' the security system? This EJB checks to see if the default ADMIN user
 * has already been created. If not, it then builds the ADMIN role, the default ADMIN user with
 * ADMIN role of ADMIN and the USER role ... and stores all of them in the database.
 *
 */
@Startup
@Singleton
public class BuildSecurityRolesAndUsers {

    /**
     * CustomerIdentityStoreJPAHelper jpaHelper.
     */
    @Inject
    protected CustomIdentityStoreJPAHelper jpaHelper;

    /**
     * Pbkdf2PasswordHash pbAndjPasswordHash.
     */
    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;

    /**
     * Description: A method that will be used to build a deafult admin user and initialize things.
     */
    @PostConstruct
    public void init() {
        // build default admin user (if needed)
        SecurityUser defaultAdminUser = jpaHelper.findUserByName(DEFAULT_ADMIN_USER);
        if (defaultAdminUser == null) {
            defaultAdminUser = new SecurityUser();
            defaultAdminUser.setUsername(DEFAULT_ADMIN_USER);
            Map<String, String> pbAndjProperties = new HashMap<>();
            pbAndjProperties.put(PROPERTY_ALGORITHM, DEFAULT_PROPERTY_ALGORITHM);
            pbAndjProperties.put(PROPERTY_ITERATIONS, DEFAULT_PROPERTY_ITERATIONS);
            pbAndjProperties.put(PROPERTY_SALTSIZE, DEFAULT_SALT_SIZE);
            pbAndjProperties.put(PROPERTY_KEYSIZE, DEFAULT_KEY_SIZE);
            pbAndjPasswordHash.initialize(pbAndjProperties);
            String pwHash = pbAndjPasswordHash.generate(DEFAULT_ADMIN_USER_PASSWORD.toCharArray());
            defaultAdminUser.setPwHash(pwHash);

            SecurityRole theAdminRole = new SecurityRole();
            theAdminRole.setRoleName(ADMIN_ROLE);
            Set<SecurityRole> roles = defaultAdminUser.getRoles();
            if (roles == null) {
                roles = new HashSet<>();
            }
            roles.add(theAdminRole);
            defaultAdminUser.setRoles(roles);
            jpaHelper.saveSecurityUser(defaultAdminUser);
            
            // if building Admin User/Role,might as well also build USER_ROLE
            SecurityRole theUserRole = new SecurityRole();
            theUserRole.setRoleName(USER_ROLE);
            jpaHelper.saveSecurityRole(theUserRole);
        }
    }
}