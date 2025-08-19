package com.example.leave_management.utils;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDService {
    public String generateUuid(){
        return UUID.randomUUID().toString();
    }
}
