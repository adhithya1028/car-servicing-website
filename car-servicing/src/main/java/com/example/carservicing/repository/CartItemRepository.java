// filepath: src/main/java/com/example/carservicing/repository/CartItemRepository.java
package com.example.carservicing.repository;

import com.example.carservicing.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(value = "SELECT table_schema, table_name FROM information_schema.tables WHERE table_name = 'cart_item'", nativeQuery = true)
    List<Object[]> findTableSchema();
    
    List<CartItem> findByAppointmentId(Long appointmentId);
}
