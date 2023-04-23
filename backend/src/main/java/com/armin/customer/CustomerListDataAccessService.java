package com.armin.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {

    // db
    private static final List<Customer> customers = null;

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);

    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c ->c.getEmail()
                        .equals(email));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.stream()
                .filter(c->c.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public boolean existCustomerById(Integer id) {
        return customers.stream()
                .anyMatch(c ->c.getId().equals(id));
    }

    @Override
    public void updateCustomer(Customer update) {
        customers.add(update);
    }
}
