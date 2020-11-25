package com.fresco.tournament.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fresco.tournament.models.Player;
import com.fresco.tournament.models.Team;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
	public void deleteByBelongsTo(Team belongsTo);
}