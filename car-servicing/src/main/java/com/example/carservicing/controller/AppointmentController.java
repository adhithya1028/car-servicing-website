package com.example.carservicing.controller;

import com.example.carservicing.model.Appointment;
import com.example.carservicing.model.CartItem;
import com.example.carservicing.repository.AppointmentRepository;
import com.example.carservicing.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        // Save the appointment first
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Fetch cart items from the database
        List<CartItem> cartItems = cartItemRepository.findAll();

        // Link cart items to the appointment
        for (CartItem cartItem : cartItems) {
            cartItem.setAppointment(savedAppointment);
        }

        // Save the cart items
        cartItemRepository.saveAll(cartItems);

        return ResponseEntity.ok(savedAppointment);
    }
}