package com.fresco.tournament.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fresco.tournament.models.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>{
	Optional<Admin> findByName(String name);
}