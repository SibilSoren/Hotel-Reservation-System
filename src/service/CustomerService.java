package service;

import model.Customer;

import java.util.*;
import java.util.regex.Pattern;

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
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        Customer customer = new Customer(email, firstName, lastName);
        customers.put(email, customer);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^(.+)@(.+)$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    public Customer getCustomer(String customerEmail) {
        return customers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
