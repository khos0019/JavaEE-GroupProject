/*****************************************************************c******************o*******v******id********
 * File Name: OrderSystemTestSuite
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * (Modified) @author Navraj Khosla, Bakul Prajapati, Yash Modi
 * Student Number: 040856036, 040949907, 040949822
 * Creation Date: 12-02-2020
 */
package com.algonquincollege.cst8277;

import static com.algonquincollege.cst8277.utils.MyConstants.APPLICATION_API_VERSION;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.STORE_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.PRODUCT_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_ADMIN_USER;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PREFIX;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.BillingAddressPojo;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.ProductPojo;
import com.algonquincollege.cst8277.models.ShippingAddressPojo;
import com.algonquincollege.cst8277.models.StorePojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class OrderSystemTestSuite {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);
    static final String APPLICATION_CONTEXT_ROOT = "rest-orderSystem";
    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;
    // test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

    /**
     * Method to be called at startup of the suite. This will clear all the customers from the DB.
     */
    private static void clearAllCustomers() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget = client.target(uri);
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Iterator<CustomerPojo> iter = customersList.iterator();
        while (iter.hasNext()) {
            CustomerPojo customer = iter.next();
            webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME + "/" + customer.getId()).request()
                .delete();
        }
    }

    /**
     * Method to be called at startup of the suite. This will clear all the products from the DB.
     */
    private static void clearAllProducts() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget = client.target(uri);
        Response response = webTarget.register(adminAuth).path(STORE_RESOURCE_NAME).request().get();
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>() {});
        Iterator<StorePojo> iter = stores.iterator();
        while (iter.hasNext()) {
            StorePojo store = iter.next();
            webTarget.register(adminAuth).path(STORE_RESOURCE_NAME + "/" + store.getId()).request().delete();
        }
    }

    /**
     * Method to be called at startup of the suite. This will clear all the stores from the DB.
     */
    private static void clearAllStores() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget = client.target(uri);
        Response response = webTarget.register(adminAuth).path(PRODUCT_RESOURCE_NAME).request().get();
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>() {});
        Iterator<ProductPojo> iter = products.iterator();
        while (iter.hasNext()) {
            ProductPojo product = iter.next();
            webTarget.register(adminAuth).path(PRODUCT_RESOURCE_NAME + "/" + product.getId()).request()
                .delete();
        }
    }

    /**
     * Method to be called at startup of the suite. This will clear all the DB except the two default security users.
     */
    private static void clearDB() {
        clearAllCustomers();
        clearAllProducts();
        clearAllStores();
    }
    
    /**
     * Clears the database.
     */
    @AfterAll
    public static void afterCompletion() {
        clearDB();
    }
    
    /**
     * Before everything starts, this method does a one time set up.
     * 
     * @throws Exception
     */
    @BeforeAll
    public static void oneTimeSetUp() throws Exception {
        logger.debug("oneTimeSetUp");
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        uri = UriBuilder.fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION).scheme(HTTP_SCHEMA)
            .host(HOST).port(PORT).build();
        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX, DEFAULT_USER_PASSWORD);
        clearDB();
    }

    /**
     * WebTarget webTarget.
     */
    protected WebTarget webTarget;

    /**
     * Before each test, do a set up.
     */
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }

    /**
     * Method to test that no customer has been added.
     * 
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @Test
    public void test01_adminrole_getCustomers() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        assertThat(customersList.size(), is(0));
    }

    /**
     * Method to test that user role should not be able to access get customers api
     */
    @Test
    public void test02_userrole_getCustomers() {
        Response response = webTarget
            // .register(userAuth)
            .register(userAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(403));
    }

    /**
     * Method to be used as a setup for Billing Address.
     * 
     * @param billingAddress
     */
    private static void setDummyBillingAddress(BillingAddressPojo billingAddress) {
        billingAddress.setAddrType("B");
        billingAddress.setAlsoShipping(false);
        billingAddress.setCity("Ottawa");
        billingAddress.setCountry("Canada");
        billingAddress.setPostal("K2G 1V8");
        billingAddress.setState("Ottawa");
        billingAddress.setStreet("1388 Woodroffe Avenue");
    }

    /**
     * Method to be used as a setup for Shipping Address.
     * 
     * @param shippingAddress
     */
    private static void setDummyShippingAddress(ShippingAddressPojo shippingAddress) {
        shippingAddress.setAddrType("S");
        shippingAddress.setCity("Ottawa");
        shippingAddress.setCountry("Canada");
        shippingAddress.setPostal("K2G 1V8");
        shippingAddress.setState("Ottawa");
        shippingAddress.setStreet("1386 Woodroffe Avenue");
    }

    /**
     * Method to test add customer api
     */
    @Test
    public void test03_add_customer() {
        CustomerPojo customer = new CustomerPojo();
        customer.setEmail("bakul.prajapati@rediffmail.com");
        customer.setFirstName("Bakul");
        customer.setLastName("Prajapati");
        customer.setPhoneNumber("+1.613.555.1212");
        BillingAddressPojo billingAddress = new BillingAddressPojo();
        ShippingAddressPojo shippingAddress = new ShippingAddressPojo();
        setDummyBillingAddress(billingAddress);
        setDummyShippingAddress(shippingAddress);
        OrderPojo order = new OrderPojo();
        order.setId(1);
        order.setDescription("GroceryOrder");
        order.setOwningCustomer(customer);
        List<OrderPojo> orders = new ArrayList<OrderPojo>();
        orders.add(order);
        customer.setBillingAddress(billingAddress);
        customer.setShippingAddress(shippingAddress);
        customer.setOrders(orders);
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON).post(Entity.json(customer));
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Method to test getCustomers api with data
     */
    @Test
    public void test04_adminRole_getCustomers_WithData() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        assertThat(customersList, hasSize(1));
        assertThat(customersList.get(0).getFirstName(), is("Bakul"));
        assertThat(customersList.get(0).getLastName(), is("Prajapati"));
        assertThat(customersList.get(0).getEmail(), is("bakul.prajapati@rediffmail.com"));
        assertThat(customersList.get(0).getPhoneNumber(), is("+1.613.555.1212"));
        assertThat(customersList.get(0).getOrders().size(), is(1));
    }

    /**
     * Method to test the request format of add customer api
     */
    @Test
    public void test05_RequestFormatForAddCustomer() {
        CustomerPojo customer = new CustomerPojo();
        customer.setEmail("bakul.prajapati@rediffmail.com");
        customer.setFirstName("Bakul");
        customer.setLastName("Prajapati");
        customer.setPhoneNumber("+1.613.555.1212");
        BillingAddressPojo billingAddress = new BillingAddressPojo();
        ShippingAddressPojo shippingAddress = new ShippingAddressPojo();
        setDummyBillingAddress(billingAddress);
        setDummyShippingAddress(shippingAddress);
        OrderPojo order = new OrderPojo();
        order.setId(1);
        order.setDescription("GroceryOrder");
        order.setOwningCustomer(customer);
        List<OrderPojo> orders = new ArrayList<OrderPojo>();
        orders.add(order);
        customer.setBillingAddress(billingAddress);
        customer.setShippingAddress(shippingAddress);
        customer.setOrders(orders);
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME)
            .request(MediaType.APPLICATION_XML).post(Entity.json(customer));
        assertThat(response.getStatus(), is(406));
    }

    /**
     * Method to test customer/{id} - admin role
     */
    @Test
    public void test06_adminRole_getCustomerById() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        response = webTarget.register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId()).request().get();
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Method to test customer/{id}/billingAddress
     */
    @Test
    public void test07_adminRole_getBillingAddress() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        response = webTarget.register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId() + "/billingaddress").request()
            .get();
        assertThat(response.getStatus(), is(200));
        AddressPojo billingAddress = response.readEntity(new GenericType<AddressPojo>() {});
        assertThat(billingAddress.getCountry(), is("Canada"));
        assertThat(billingAddress.getAddrType(), is("B"));
    }

    /**
     * Method to test customer/{id}/shippingAddress
     */
    @Test
    public void test08_adminRole_getShippingAddress() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        response = webTarget.register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId() + "/shippingaddress").request()
            .get();
        assertThat(response.getStatus(), is(200));
        AddressPojo shippingAddress = response.readEntity(new GenericType<AddressPojo>() {});
        assertThat(shippingAddress.getCountry(), is("Canada"));
        assertThat(shippingAddress.getAddrType(), is("S"));
    }

    /**
     * Method to test customer/{id} api - user role
     */
    @Test
    public void test09_userRole_getCustomerById() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget2 = client.target(uri);
        Response response2 = webTarget2.register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId()).request().get();
        assertThat(response2.getStatus(), is(403));
    }

    /**
     * Method to test customer/{id} api with same customer
     */
    @Test
    public void test10_sameCustomer_getUserById() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget2 = client.target(uri);
        HttpAuthenticationFeature userAuth = HttpAuthenticationFeature
            .basic(DEFAULT_USER_PREFIX + customersList.get(0).getId(), DEFAULT_USER_PASSWORD);
        Response response2 = webTarget2.register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId()).request().get();
        assertThat(response2.getStatus(), is(200));
    }

    /**
     * Method to test POST customer/{id} - user role
     */
    @Test
    public void test11_userRole_addCustomer() {
        CustomerPojo customer = new CustomerPojo();
        customer.setEmail("bakul.prajapati@rediffmail.com");
        customer.setFirstName("Bakul");
        customer.setLastName("Prajapati");
        customer.setPhoneNumber("+1.613.555.1212");
        BillingAddressPojo billingAddress = new BillingAddressPojo();
        ShippingAddressPojo shippingAddress = new ShippingAddressPojo();
        setDummyBillingAddress(billingAddress);
        setDummyShippingAddress(shippingAddress);
        OrderPojo order = new OrderPojo();
        order.setId(1);
        order.setDescription("GroceryOrder");
        order.setOwningCustomer(customer);
        List<OrderPojo> orders = new ArrayList<OrderPojo>();
        orders.add(order);
        customer.setBillingAddress(billingAddress);
        customer.setShippingAddress(shippingAddress);
        customer.setOrders(orders);
        Response response = webTarget.register(userAuth).path(CUSTOMER_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON).post(Entity.json(customer));
        assertThat(response.getStatus(), is(403));
    }

    /**
     * Method to test customer/{id}/billingAddress - user role
     */
    @Test
    public void test12_userRole_getBillingAddress() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget2 = client.target(uri);
        HttpAuthenticationFeature userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX,
            DEFAULT_USER_PASSWORD);
        response = webTarget2.register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId() + "/billingaddress").request()
            .get();
        assertThat(response.getStatus(), is(403));
    }

    /**
     * Method to test customer/{id}/shippingAddress - user role
     */
    @Test
    public void test13_get_shipping_address_user_role() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget2 = client.target(uri);
        HttpAuthenticationFeature userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX,
            DEFAULT_USER_PASSWORD);
        response = webTarget2.register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId() + "/shippingaddress").request()
            .get();
        assertThat(response.getStatus(), is(403));
    }

    /**
     * Method to test customer/{id}/billingAddress
     */
    @Test
    public void test14_sameCustomer_getBillingAddress() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget2 = client.target(uri);
        HttpAuthenticationFeature userAuth = HttpAuthenticationFeature
            .basic(DEFAULT_USER_PREFIX + customersList.get(0).getId(), DEFAULT_USER_PASSWORD);
        response = webTarget2.register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId() + "/billingaddress").request()
            .get();
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Method to test customer/{id}/billingAddress
     */
    @Test
    public void test15_sameCustomer_getShippingAddress() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget2 = client.target(uri);
        HttpAuthenticationFeature userAuth = HttpAuthenticationFeature
            .basic(DEFAULT_USER_PREFIX + customersList.get(0).getId(), DEFAULT_USER_PASSWORD);
        response = webTarget2.register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId() + "/shippingaddress").request()
            .get();
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Method to test customer/{id}/order - admin role
     * To get all the orders for a customer
     */
    @Test
    public void test16_adminRole_getOrders() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget2 = client.target(uri);
        response = webTarget2.register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId() + "/order").request().get();
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Method to test customer/{id}/order - user role
     */
    @Test
    public void test17_get_orders_user_role() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget2 = client.target(uri);
        HttpAuthenticationFeature userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX,
            DEFAULT_USER_PASSWORD);
        response = webTarget2.register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId() + "/order").request().get();
        assertThat(response.getStatus(), is(403));
    }

    /**
     * Method to test customer/{id}/order - same customer
     */
    @Test
    public void test18_sameCustomer_getOrders() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget2 = client.target(uri);
        HttpAuthenticationFeature userAuth = HttpAuthenticationFeature
            .basic(DEFAULT_USER_PREFIX + customersList.get(0).getId(), DEFAULT_USER_PASSWORD);
        response = webTarget2.register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId() + "/order").request().get();
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Method to test if a user with admin role can get order by ID.
     */
    @Test
    public void test19_adminRole_getOrderById() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget2 = client.target(uri);
        response = webTarget2.register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/order/" + customersList.get(0).getOrders().get(0).getId())
            .request().get();
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Method to test a if user with user role can get order by ID.
     */
    @Test
    public void test20_userRole_getOrderById() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        HttpAuthenticationFeature userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX,
            DEFAULT_USER_PASSWORD);
        WebTarget webTarget2 = client.target(uri);
        response = webTarget2.register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/order/" + customersList.get(0).getOrders().get(0).getId())
            .request().get();
        assertThat(response.getStatus(), is(403));
    }
    
    /**
     * Method to test if a user with user role can delete customer.
     */
    @Test
    public void test21_userRole_deleteCustomer() {
        Response response = webTarget.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        HttpAuthenticationFeature userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX,
            DEFAULT_USER_PASSWORD);
        WebTarget webTarget2 = client.target(uri);
        response = webTarget2.register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId()).request().delete();
        assertThat(response.getStatus(), is(403));
    }

    /**
     * Method to test if a user with admin role can delete customer.
     */
    @Test
    public void test22_adminRole_deleteCustomer() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        WebTarget webTarget2 = client.target(uri);
        Response response = webTarget2.register(adminAuth).path(CUSTOMER_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customersList = response.readEntity(new GenericType<List<CustomerPojo>>() {});
        response = webTarget2.register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + "/" + customersList.get(0).getId()).request().delete();
        assertThat(response.getStatus(), is(204));
    }
    
    /**
     * Method to test request format while adding product.
     */
    @Test
    public void test23_checkRequestFormatWhileAddingProduct() {
        ProductPojo product = new ProductPojo();
        product.setSerialNo("S1");
        product.setDescription("Almonds");
        Response response = webTarget.register(adminAuth).path(PRODUCT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_XML).post(Entity.json(product));
        assertThat(response.getStatus(), is(406));
    }

    /**
     * Method to test if a user with admin role can get stores.
     */
    @Test
    public void test24_adminRole_getStores() {
        Response response = webTarget.register(adminAuth).path(STORE_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>() {});
        assertThat(stores, is(empty()));
    }

    /**
     * Method to test if a user with admin role can get products.
     */
    @Test
    public void test25_adminRole_getProducts() {
        Response response = webTarget.register(adminAuth).path(PRODUCT_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>() {});
        assertThat(products, is(empty()));
    }

    /**
     * Method to test if a user with user role can get stores.
     */
    @Test
    public void test26_userRole_getStores() {
        Response response = webTarget.register(userAuth).path(STORE_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Method to test if a user with user role can get products.
     */
    @Test
    public void test27_userRole_getProducts() {
        Response response = webTarget.register(userAuth).path(PRODUCT_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Method to test if a user with admin role can add store.
     */
    @Test
    public void test28_adminRole_addStore() {
        StorePojo store = new StorePojo();
        store.setStoreName("BigStore");
        Response response = webTarget.register(adminAuth).path(STORE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON).post(Entity.json(store));
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Method to test if a user with admin role can add product.
     */
    @Test
    public void test29_adminRole_addProduct() {
        ProductPojo product = new ProductPojo();
        product.setSerialNo("P1");
        product.setDescription("Almonds");
        Response response = webTarget.register(adminAuth).path(PRODUCT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON).post(Entity.json(product));
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Method to test if a user with admin role can get stores with data.
     */
    @Test
    public void test30_adminRole_getStores_withData() {
        Response response = webTarget.register(adminAuth).path(STORE_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>() {});
        assertThat(stores, hasSize(1));
        assertThat(stores.get(0).getStoreName(), is("BigStore"));
    }

    /**
     * Method to test if a user with admin role can get products with data.
     */
    @Test
    public void test31_adminRole_getProducts_withData() {
        Response response = webTarget.register(adminAuth).path(PRODUCT_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>() {});
        assertThat(products, hasSize(1));
        assertThat(products.get(0).getDescription(), is("Almonds"));
        assertThat(products.get(0).getSerialNo(), is("P1"));
    }

    /**
     * Method to test if a user with user role can add store.
     */
    @Test
    public void test32_userRole_addStore() {
        StorePojo store = new StorePojo();
        store.setStoreName("BigStore");
        Response response = webTarget.register(userAuth).path(STORE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON).post(Entity.json(store));
        assertThat(response.getStatus(), is(403));
    }

    /**
     * Method to test if a user with user role can add product.
     */
    @Test
    public void test33_userRole_addProduct() {
        ProductPojo product = new ProductPojo();
        product.setSerialNo("P1");
        product.setDescription("Almonds");
        Response response = webTarget.register(userAuth).path(PRODUCT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON).post(Entity.json(product));
        assertThat(response.getStatus(), is(403));
    }

    /**
     * Method to test request format while adding store.
     */
    @Test
    public void test34_checkRequestFormatWhileAddingStore() {
        StorePojo store = new StorePojo();
        store.setStoreName("BigStore");
        Response response = webTarget.register(adminAuth).path(STORE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_XML).post(Entity.json(store));
        assertThat(response.getStatus(), is(406));
    }

    /**
     * Method to test if a user with admin role can add product to store.
     */
    @Test
    public void test35_adminRole_addProductToStore() {
        Response response = webTarget.register(adminAuth).path(PRODUCT_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>() {});
        response = webTarget.register(adminAuth).path(STORE_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>() {});
        response = webTarget.register(adminAuth)
            .path(STORE_RESOURCE_NAME + "/" + stores.get(0).getId() + "/product")
            .request(MediaType.APPLICATION_JSON).put(Entity.json(products.get(0)));
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Method to test if a user with admin role can get product from store.
     */
    @Test
    public void test36_adminRole_getProductsViaStore() {
        Response response = webTarget.register(adminAuth).path(STORE_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>() {});
        response = webTarget.register(adminAuth).path(STORE_RESOURCE_NAME + "/" + stores.get(0).getId())
            .request().get();
        assertThat(response.getStatus(), is(200));
        StorePojo store = response.readEntity(new GenericType<StorePojo>() {});
        assertThat(store.getProducts().size(), is(1));
    }

    /**
     * To test of a product can be accessed via its id
     */
    @Test
    public void test37_adminRole_getProductById() {
        Response response = webTarget.register(adminAuth).path(PRODUCT_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>() {});
        response = webTarget.register(adminAuth).path(PRODUCT_RESOURCE_NAME + "/" + products.get(0).getId())
            .request().get();
        assertThat(response.getStatus(), is(200));
        ProductPojo product = response.readEntity(new GenericType<ProductPojo>() {});
        assertThat(product.getDescription(), is("Almonds"));
        assertThat(product.getSerialNo(), is("P1"));
    }

    /**
     * Method to test if a user with user role can get product using store ID.
     */
    @Test
    public void test38_userRole_getProductsByStoreId() {
        Response response = webTarget.register(userAuth).path(STORE_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>() {});
        response = webTarget.register(userAuth).path(STORE_RESOURCE_NAME+"/"+stores.get(0).getId()+"/product").request().get();
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>() {});
        assertThat(products.size(), is(1));
    }
    
    /**
     * Method to test if a user with admin role can delete product.
     */
    @Test
    public void test39_adminRole_deleteProduct() {
        Response response = webTarget.register(adminAuth).path(PRODUCT_RESOURCE_NAME).request().get();
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>() {});
        response = webTarget.register(adminAuth).path(PRODUCT_RESOURCE_NAME + "/" + products.get(0).getId())
            .request().delete();
        assertThat(response.getStatus(), is(204));
    }

    /**
     * Method to test if a user with admin role can delete store.
     */
    @Test
    public void test40_adminRole_deleteStore() {
        Response response = webTarget.register(adminAuth).path(STORE_RESOURCE_NAME).request().get();
        assertThat(response.getStatus(), is(200));
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>() {});
        response = webTarget.register(adminAuth).path(STORE_RESOURCE_NAME + "/" + stores.get(0).getId())
            .request().delete();
        assertThat(response.getStatus(), is(204));
    }

}