package com.armin.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
