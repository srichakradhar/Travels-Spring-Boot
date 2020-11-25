package com.fresco.tournament.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fresco.tournament.models.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>{
	Optional<Team> findByName(String name);
}