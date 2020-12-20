/*****************************************************************c******************o*******v******id********
 * File Name: CustomerService
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277.ejb;

import static com.algonquincollege.cst8277.models.SecurityRole.ROLE_BY_NAME_QUERY;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_KEY_SIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_PROPERTY_ALGORITHM;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_PROPERTY_ITERATIONS;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_SALT_SIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PREFIX;
import static com.algonquincollege.cst8277.utils.MyConstants.PARAM1;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_ALGORITHM;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_ITERATIONS;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_KEYSIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_SALTSIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;
import static com.algonquincollege.cst8277.models.CustomerPojo.ALL_CUSTOMERS_QUERY_NAME;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
//import javax.transaction.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;

import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.ProductPojo;
import com.algonquincollege.cst8277.models.SecurityRole;
import com.algonquincollege.cst8277.models.SecurityUser;
import com.algonquincollege.cst8277.models.ShippingAddressPojo;
import com.algonquincollege.cst8277.models.StorePojo;

/**
 * Stateless Singleton Session Bean - CustomerService
 */
@Singleton
@Stateless
public class CustomerService implements Serializable {
    
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    
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
     * Pbkdf2PasswordHash pbAndjPasswordHash.
     */
    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;

    /**
     * Description: Gets all the customers.
     *
     * @return customer information.
     */
    public List<CustomerPojo> getAllCustomers() {
        em.flush();
        return em.createNamedQuery(ALL_CUSTOMERS_QUERY_NAME, CustomerPojo.class).getResultList();
    }

    /**
     * Description: Gets customer by ID.
     * 
     * @param custPK
     * @return customer information based of the ID.
     */
    public CustomerPojo getCustomerById(int custPK) {
        return em.find(CustomerPojo.class, custPK);
    }
    
    /**
     * Description: Creates a new customer.
     * 
     * @param newCustomer
     * @return new customer information.
     */
    @Transactional
    public CustomerPojo persistCustomer(CustomerPojo newCustomer) {
        em.persist(newCustomer);
        em.flush();
        return newCustomer;
    }
    
    /**
     * Description: Builds a user for the new customer created.
     * 
     * @param newCustomerWithIdTimestamps
     */
    @Transactional
    public void buildUserForNewCustomer(CustomerPojo newCustomerWithIdTimestamps) {
        SecurityUser userForNewCustomer = new SecurityUser();
        userForNewCustomer.setUsername(DEFAULT_USER_PREFIX + "" + newCustomerWithIdTimestamps.getId());
        Map<String, String> pbAndjProperties = new HashMap<>();
        pbAndjProperties.put(PROPERTY_ALGORITHM, DEFAULT_PROPERTY_ALGORITHM);
        pbAndjProperties.put(PROPERTY_ITERATIONS, DEFAULT_PROPERTY_ITERATIONS);
        pbAndjProperties.put(PROPERTY_SALTSIZE, DEFAULT_SALT_SIZE);
        pbAndjProperties.put(PROPERTY_KEYSIZE, DEFAULT_KEY_SIZE);
        pbAndjPasswordHash.initialize(pbAndjProperties);
        String pwHash = pbAndjPasswordHash.generate(DEFAULT_USER_PASSWORD.toCharArray());
        userForNewCustomer.setPwHash(pwHash);
        userForNewCustomer.setCustomer(newCustomerWithIdTimestamps);
        SecurityRole userRole = em.createNamedQuery(ROLE_BY_NAME_QUERY,
            SecurityRole.class).setParameter(PARAM1, USER_ROLE).getSingleResult();
        userForNewCustomer.getRoles().add(userRole);
        userRole.getUsers().add(userForNewCustomer);
        em.persist(userForNewCustomer);
        em.flush();
    }

    /**
     * Description: Sets the address for a specific customer.
     * 
     * @param custId
     * @param newAddress
     * @return address information.
     */
    @Transactional
    public CustomerPojo setAddressFor(int custId, AddressPojo newAddress) {
        CustomerPojo updatedCustomer = em.find(CustomerPojo.class, custId);
        if (newAddress instanceof ShippingAddressPojo) {
            updatedCustomer.setShippingAddress(newAddress);
        }
        else {
            updatedCustomer.setBillingAddress(newAddress);
        }
        em.merge(updatedCustomer);
        return updatedCustomer;
    }

    /**
     * Description: Gets all products.
     * 
     * @return products.
     */
    public List<ProductPojo> getAllProducts() {
        //example of using JPA Criteria query instead of JPQL
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProductPojo> q = cb.createQuery(ProductPojo.class);
            Root<ProductPojo> c = q.from(ProductPojo.class);
            q.select(c);
            TypedQuery<ProductPojo> q2 = em.createQuery(q);
            List<ProductPojo> allProducts = q2.getResultList();
            return allProducts;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets products using ID.
     * 
     * @param prodId
     * @return specific product.
     */
    public ProductPojo getProductById(int prodId) {
        return em.find(ProductPojo.class, prodId);
    }

    /**
     * Description: Gets all stores.
     * 
     * @return stores information.
     */
    public List<StorePojo> getAllStores() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<StorePojo> q = cb.createQuery(StorePojo.class);
            Root<StorePojo> c = q.from(StorePojo.class);
            q.select(c);
            TypedQuery<StorePojo> q2 = em.createQuery(q);
            List<StorePojo> allStores = q2.getResultList();
            return allStores;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Description: Gets all orders.
     * 
     * @return orders information.
     */
    public List<OrderPojo> getAllOrders() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<OrderPojo> q = cb.createQuery(OrderPojo.class);
            Root<OrderPojo> c = q.from(OrderPojo.class);
            q.select(c);
            TypedQuery<OrderPojo> q2 = em.createQuery(q);
            List<OrderPojo> allOrders = q2.getResultList();
            return allOrders;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Descriptin: Gets store using the ID.
     * 
     * @param id
     * @return specific store.
     */
    public StorePojo getStoreById(int id) {
        return em.find(StorePojo.class, id);
    }

    /**
     * Description: Gets products by using store ID.
     * 
     * @param storeId
     * @return specific product(s).
     */
    public Set<ProductPojo> getProductsByStoreId(int storeId) {
        return em.find(StorePojo.class, storeId).getProducts();
    }
    
    /**
     * Description: Gets order by ID.
     * 
     * @param id
     * @return specific order.
     */
    public OrderPojo getOrderById(int id) {
        OrderPojo order = em.find(OrderPojo.class, id);
        return order;
    }

    /**
     * Description: Gets billing address using customer ID.
     * 
     * @param custPK
     * @return Billing Address information.
     */
    public AddressPojo getBillingAddressByCustomerId(int custPK) {
        return getCustomerById(custPK).getBillingAddress();
    }
    
    /**
     * Description: Gets shipping address by customer ID.
     * 
     * @param custPK
     * @return Shipping Address information.
     */
    public AddressPojo getShippingAddressByCustomerId(int custPK) {
        return getCustomerById(custPK).getShippingAddress();
    }

    /**
     * Description: Gets orders by using customer ID.
     * 
     * @param custPK
     * @return specific order(s).
     */
    public List<OrderPojo> getOrdersByCustomerId(int custPK) {
        return getCustomerById(custPK).getOrders();
    }
    
    /**
     * Description: Gets order by using the order ID.
     * 
     * @param id
     * @return specific order.
     */
    public OrderPojo getOrderByOrderId(int id) {
        return em.find(OrderPojo.class, id);
    }
    
    /**
     * Description: Creates a new product.
     * 
     * @param newProduct
     * @return new product
     */
    @Transactional
    public ProductPojo persistProduct(ProductPojo newProduct) {
        em.persist(newProduct);
        return newProduct;
    }

    /**
     * Description: Creates a new store.
     * 
     * @param newStore
     * @return new store.
     */
    @Transactional
    public StorePojo persistStore(StorePojo newStore) {
        em.persist(newStore);
        return newStore;
    }
   
    /**
     * Description: Deletes a customer using ID.
     * 
     * @param custPK
     * @return true or false.
     */
    @Transactional
    public boolean deleteCustomer(int custPK) {
        CustomerPojo cust = getCustomerById(custPK);
        if(cust!=null) {
            SecurityUser user = getSecurityUserByCustomer(cust);
            if(user!=null) {
                em.remove(user);
            }
            em.remove(cust);
            return true;
        }
        return false;
    }

    /**
     * Description: Deletes store using ID.
     * 
     * @param id
     * @return true or false.
     */
    @Transactional
    public boolean deleteStore(int id) {
        StorePojo store = getStoreById(id);
        if(store!=null) {
            em.remove(store);
            return true;
        }
        return false;
    }

    /**
     * Description: Deletes product using ID.
     * 
     * @param id
     * @return true or false.
     */
    @Transactional
    public boolean deleteProduct(int id) {
        ProductPojo product = getProductById(id);
        if(product!=null) {
            em.remove(product);
            return true;
        }
        return false;
    } // Agile Desing PAtterns

    /**
     * Description: Gets security user by customer.
     * 
     * @param cust
     * @return user.
     */
    @Transactional
    public SecurityUser getSecurityUserByCustomer(CustomerPojo cust) {
        SecurityUser user = em.createNamedQuery(SecurityUser.USER_FOR_OWNING_CUST_QUERY, SecurityUser.class).setParameter(PARAM1, cust).getSingleResult();
        return user;
    }

    /**
     * Description: Sets the order for a specific customer using customer ID.
     * 
     * @param custId
     * @param newOrder
     * @return updated customer.
     */
    @Transactional
    public CustomerPojo setOrderFor(int custId, OrderPojo newOrder) {
        CustomerPojo updatedCustomer = em.find(CustomerPojo.class, custId);
        updatedCustomer.getOrders().add(newOrder);
        em.merge(updatedCustomer);
        em.flush();
        return updatedCustomer;
    }

    /**
     * Description: Sets product to store.
     * 
     * @param storeId
     * @param newProduct
     * @return updated store.
     */
    @Transactional
    public StorePojo setProductToStore(int storeId, ProductPojo newProduct) {
        StorePojo updatedStore = em.find(StorePojo.class, storeId);
        updatedStore.getProducts().add(newProduct);
        em.merge(updatedStore);
        em.flush();
        return updatedStore;
    }
}