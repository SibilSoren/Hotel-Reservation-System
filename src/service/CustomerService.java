package service;

import model.Customer;

import java.util.*;

public class CustomerService {
    // Singleton instance
    private static final CustomerService instance = new CustomerService();
    private static final Map<String, Customer> customers = new HashMap<String, Customer>();

    // Private constructor prevents instantiation
    private CustomerService() {
    }

    // Static accessor
    public static CustomerService getInstance() {
        return instance;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        Customer customer = new Customer(email, firstName, lastName);
        customers.put(email, customer);
    }

    public Customer getCustomer(String customerEmail) {
        return customers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
