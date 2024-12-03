package com.alten.back.repos;

import com.alten.back.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByAccount_Id(Long id);
    boolean existsByAccountIdAndProductId(Long accountId, Long productId);
}
