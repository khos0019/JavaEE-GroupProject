/*****************************************************************c******************o*******v******id********
 * File Name: CusstomerIdentityStoreJPAHelper
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.security;

import static com.algonquincollege.cst8277.models.SecurityUser.SECURITY_USER_BY_NAME_QUERY;

import static java.util.Collections.emptySet;

import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.algonquincollege.cst8277.models.SecurityRole;
import com.algonquincollege.cst8277.models.SecurityUser;

/*
 * Stateless Session bean should also be a Singleton
 */
@Singleton
public class CustomIdentityStoreJPAHelper {

    /**
     * String CUSTOMER_PU.
     * 
     * Description: Holds the customer persistance unit name.
     */
    public static final String CUSTOMER_PU = "20f-groupProject-PU";

    /**
     * EntityManager em.
     */
    @PersistenceContext(name = CUSTOMER_PU)
    protected EntityManager em;

    /**
     * Description: Find the user by username.
     * 
     * @param username
     * @return user.
     */
    public SecurityUser findUserByName(String username) {
        SecurityUser user = null;
        try {
            
            user = em.createNamedQuery(SECURITY_USER_BY_NAME_QUERY, SecurityUser.class).setParameter(1, username).getSingleResult();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Description: Find role names for user using username.
     * 
     * @param username
     * @return roleNames.
     */
    public Set<String> findRoleNamesForUser(String username) {
        Set<String> roleNames = emptySet();
        SecurityUser securityUser = findUserByName(username);
        if (securityUser != null) {
            roleNames = securityUser.getRoles().stream().map(s -> s.getRoleName()).collect(Collectors.toSet());
        }
        return roleNames;
    }

    /**
     * Description: Save security user.
     * 
     * @param user
     */
    @Transactional
    public void saveSecurityUser(SecurityUser user) {
        em.persist(user);
    }

    /**
     * Description: Save security role.
     * 
     * @param role
     */
    @Transactional
    public void saveSecurityRole(SecurityRole role) {
        em.persist(role);
    }
}