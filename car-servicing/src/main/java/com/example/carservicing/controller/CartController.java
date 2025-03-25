// filepath: src/main/java/com/example/carservicing/controller/CartController.java
package com.example.carservicing.controller;

import com.example.carservicing.model.CartItem;
import com.example.carservicing.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping
    public ResponseEntity<List<CartItem>> getAllCartItems() {
        try {
            logger.info("Fetching all cart items");
            List<CartItem> items = cartItemRepository.findAll();
            logger.info("Found {} items in cart", items.size());
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            logger.error("Error fetching cart items: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<CartItem> addCartItem(@RequestBody CartItem cartItem) {
        try {
            logger.info("Received cart item: {}", cartItem);
            if (cartItem.getName() == null || cartItem.getPrice() == null) {
                logger.error("Invalid cart item data: {}", cartItem);
                return ResponseEntity.badRequest().build();
            }
            CartItem savedItem = cartItemRepository.save(cartItem);
            logger.info("Successfully saved item to database: {}", savedItem);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            logger.error("Error saving cart item: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        try {
            logger.info("Clearing cart");
            cartItemRepository.deleteAll();
            logger.info("Cart cleared successfully");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error clearing cart: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/test-db")
    public ResponseEntity<String> testDatabaseConnection() {
        try {
            List<Object[]> schema = cartItemRepository.findTableSchema();
            logger.info("Database schema query result: {}", schema);
            if (schema.isEmpty()) {
                logger.warn("Table 'cart_item' not found in database");
                return ResponseEntity.ok("Table 'cart_item' not found in database");
            }
            logger.info("Found table 'cart_item' in schema: {}", schema.get(0)[0]);
            return ResponseEntity.ok("Database connection successful. Table 'cart_item' found in schema: " + schema.get(0)[0]);
        } catch (Exception e) {
            logger.error("Database connection error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Database connection error: " + e.getMessage());
        }
    }
}
