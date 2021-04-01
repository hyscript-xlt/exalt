package hotel.booking.repository;

import lombok.Data;

import java.util.UUID;

@Data
public class Customer {
    private String id = UUID.randomUUID().toString();
    private String name;
    private String surName;
    private String email;
    private String phone;
}
