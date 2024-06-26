package com.armin.customer;

import com.armin.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class CustomerJDBCDataAccessServiceTestContainer extends AbstractTestContainer {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp(){
        underTest=new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );

    }

    @Test
    void selectAllCustomers(){

        //Given
        Customer customer=new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID(),
                20,
                Gender.MALE
        );
        underTest.insertCustomer(customer);

        //When
        List<Customer> actual = underTest.selectAllCustomers();

        //Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomersById(){

        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomersById(){
        //Given
        int id = 0;

        //When
        var actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer(){
    }

    @Test
    void existsPersonWithEmail(){

        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE
        );
        underTest.insertCustomer(customer);

        //When
        boolean actual = underTest.existPersonWithEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExists(){

        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        //When
        boolean actual = underTest.existPersonWithEmail(email);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsPersonWithId(){

        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        var actual = underTest.existCustomerById(id);

        //Then
        assertThat(actual).isTrue();

    }

    @Test
    void existsPersonWithIdWillReturnFalseWhenIdNotPresent(){

        //Given
        int id = -1;

        //When
        var actual = underTest.existCustomerById(id);

        //Then
        assertThat(actual).isFalse();

    }

    @Test
    void deleteCustomerById(){

        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        underTest.deleteCustomerById(id);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName(){

        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newName="foo";

        //When
        Customer update= new Customer();
        update.setId(id);
        update.setName(newName);
        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());

        });
    }

    @Test
    void updateCustomerEmail(){

        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newEmail="hello@gmail.com";

        //When
        Customer update= new Customer();
        update.setId(id);
        update.setEmail(newEmail);
        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());

        });
    }
    @Test
    void updateCustomerAge(){

        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newAge=3;

        //When
        Customer update= new Customer();
        update.setId(id);
        update.setAge(newAge);
        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge);
            assertThat(c.getGender()).isEqualTo(Gender.MALE);
        });
    }


    @Test
    void willUpdateAllPropertiesCustomer(){

        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newEmail="hello@world.com";


        //When
        Customer update= new Customer();
        update.setId(id);
        update.setName("foo");
        update.setAge(3);
        update.setEmail(newEmail);
        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(updated -> {
            assertThat(updated.getId()).isEqualTo(id);
            assertThat(updated.getGender()).isEqualTo(Gender.MALE);
            assertThat(updated.getName()).isEqualTo("foo");
            assertThat(updated.getEmail()).isEqualTo(newEmail);
            assertThat(updated.getAge()).isEqualTo(3);
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate(){

        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Customer update= new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());

        });
    }



}
