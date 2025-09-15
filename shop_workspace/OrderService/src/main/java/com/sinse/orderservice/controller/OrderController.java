package com.sinse.orderservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderController {
    @GetMapping("/orders")
    public ResponseEntity<?> orders() {
        return ResponseEntity.ok(Map.of("result","주문 목록입니다"));
    }
}
