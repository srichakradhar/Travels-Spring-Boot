package com.fresco.tournament.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fresco.tournament.models.Mapping;

@Repository
public interface MappingRepository extends JpaRepository<Mapping, Long>{
}