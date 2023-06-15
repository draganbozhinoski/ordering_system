package com.ulaf.ste.ordering_system.Repository;

import com.ulaf.ste.ordering_system.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Add any custom query methods if required

}
