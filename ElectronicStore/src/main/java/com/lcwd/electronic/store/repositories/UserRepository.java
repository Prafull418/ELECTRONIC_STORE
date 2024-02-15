package com.lcwd.electronic.store.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.entities.User;

public interface UserRepository extends JpaRepository<User, String>{

	//at runtime spring boot provides implementation of this interface
	
	//at runtime jpa hibernate provide implementation
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAndPassword(String email, String password);
	List<User> findByNameContaining(String keyword);
	
	
}
