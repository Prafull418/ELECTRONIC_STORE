package com.lcwd.electronic.store;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.repositories.RoleRepository;

/*@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {*/

@SpringBootApplication
@EnableWebMvc // swagger needs this web mvc that why we added here
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;
	
	@Value("${admin.role.id}")
	private String role_admin_id;
	

	@Value("${normal.role.id}")
	private String role_normal_id;
	
	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			
			Role role_admin = Role.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
			Role role_normal = Role.builder().roleId(role_normal_id).roleName("ROLE_NORMAL").build();
			roleRepository.saveAll(Arrays.asList(role_admin,role_normal));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * @Autowired // i have created bean of this interface in securityconfig class
	 * private PasswordEncoder passwordEncoder;
	 * 
	 * 
	 * 
	 * @Override public void run(String... args) throws Exception {
	 * System.out.println(passwordEncoder.encode("abc123")); }
	 */

}
