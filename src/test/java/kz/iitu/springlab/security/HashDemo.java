package kz.iitu.springlab.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** Run this main twice and compare the outputs for the same password. */
public class HashDemo {
    public static void main(String[] args) {
        var encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("user123"));
        System.out.println(encoder.encode("user123"));
        System.out.println(encoder.encode("admin123"));
    }
}