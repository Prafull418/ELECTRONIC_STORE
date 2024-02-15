package com.lcwd.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
