package com.fresco.tournament.service;

import javax.transaction.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fresco.tournament.config.JwtUtil;
import com.fresco.tournament.models.Player;
import com.fresco.tournament.models.Team;
import com.fresco.tournament.repo.PlayerRepository;
import com.fresco.tournament.repo.TeamRepository;

@Service
public class TeamsService {
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	TeamRepository teamRepo;
	@Autowired
	PlayerRepository playerRepo;
	@Autowired
	BCryptPasswordEncoder encoder;

	public ResponseEntity<JSONObject> registerTeam(Team team) {
		JSONObject json = new JSONObject();
		try {
			team.setPassword(encoder.encode(team.getPassword()));
			team = teamRepo.save(team);
			json.put("Id", team.getId());
			json.put("Name", team.getName());
			json.put("Country", team.getCountry());
			json.put("Coach", team.getCoach());
			return ResponseEntity.status(201).body(json);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(json);
		}
	}

	public ResponseEntity<JSONObject> loginTeam(Team team) {
		JSONObject json = new JSONObject();
		try {
			team.setId(teamRepo.findByName(team.getName()).get().getId());
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(team.getName(), team.getPassword()));
			String token = jwtUtil.generateToken(team.getName());
			json.put("message", "Login successful");
			json.put("token", token);
			JSONObject teamJSON = new JSONObject();
			teamJSON.put("name", team.getName());
			teamJSON.put("id", team.getId());
			teamJSON.put("country", team.getCountry());
			teamJSON.put("coach", team.getCoach());
			json.put("team", teamJSON);
			return ResponseEntity.ok().body(json);
		} catch (BadCredentialsException e) {
			json.put("message", "Username and Password is wrong");
			return ResponseEntity.badRequest().body(json);
		}
	}

	public ResponseEntity<JSONArray> viewTeam() {
		JSONArray arr = new JSONArray();
		try {
			for (Team team : teamRepo.findAll()) {
				JSONObject json = new JSONObject();
				json.put("Id", team.getId());
				json.put("Name", team.getName());
				json.put("Country", team.getCountry());
				json.put("Coach", team.getCoach());
				arr.add(json);
			}
			return ResponseEntity.ok().body(arr);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(arr);
		}
	}

	public ResponseEntity<JSONArray> allPlayers(String name) {
		Long id = teamRepo.findByName(name).get().getId();
		JSONArray arr = new JSONArray();
		try {
			for (Player p : teamRepo.findById(id).get().getPlayers()) {
				JSONObject j = new JSONObject();
				j.put("Id", p.getId());
				j.put("Name", p.getName());
				j.put("Age", p.getAge());
				j.put("Type", p.getType());
				j.put("NumberOfMatches", p.getNoOfMatches());
				j.put("GoalsScored", p.getGoalsScored());
				j.put("BelongsTo", p.getBelongsTo().getName());
				arr.add(j);
			}
			return ResponseEntity.ok().body(arr);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(arr);
		}
	}

	public ResponseEntity<JSONObject> registerPlayer(String name, Player player) {
		Long id = teamRepo.findByName(name).get().getId();
		JSONObject json = new JSONObject();
		try {
			player.setBelongsTo(teamRepo.findById(id).get());
			player = playerRepo.save(player);
			json.put("Id", player.getId());
			json.put("Name", player.getName());
			json.put("Age", player.getAge());
			json.put("Belongs to", player.getBelongsTo().getName());
			json.put("Goals scored", player.getGoalsScored());
			json.put("Number of matches", player.getNoOfMatches());
			json.put("Type", player.getType());
			return ResponseEntity.status(201).body(json);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(json);
		}
	}

	public ResponseEntity<JSONObject> updateTeam(String name, Team team) {
		Long id = teamRepo.findByName(name).get().getId();
		JSONObject json = new JSONObject();
		try {
			Team t = teamRepo.findById(id).get();			
			t.setCoach(team.getCoach());
			teamRepo.save(t);
			json.put("Id", t.getId());
			json.put("Name", t.getName());
			json.put("Country", t.getCountry());
			json.put("Coach", t.getCoach());
			return ResponseEntity.ok().body(json);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(json);
		}
	}

	public ResponseEntity<JSONObject> elevenTeam(String name) {
		Long id = teamRepo.findByName(name).get().getId();
		JSONObject json = new JSONObject();
		try {			
			JSONArray arr = new JSONArray();
			List<Player> players = teamRepo.findById(id).get().getPlayers();
			List<String> types = players.stream().filter(player -> player.getInEleven()==true).map(Player::getType).collect(Collectors.toList());
			System.out.println(types);
				for (Player p : players) {
					if (p.getInEleven()) {
						JSONObject j = new JSONObject();
						j.put("Id", p.getId());
						j.put("Name", p.getName());
						j.put("Age", p.getAge());
						j.put("Type", p.getType());
						j.put("NumberOfMatches", p.getNoOfMatches());
						j.put("GoalsScored", p.getGoalsScored());
						j.put("BelongsTo", p.getBelongsTo().getName());
						arr.add(j);
					}
				}
				json.put("team11s", arr);
			if (types.size() == 11 && 
			    Collections.frequency(types, "Forwarder") >= 1 && 
			    Collections.frequency(types, "Defender") >= 1 &&
				Collections.frequency(types, "Mid-Fielder") >= 1 &&
				Collections.frequency(types, "Goal Keeper") == 1) {
				json.put("message", "");
			} else
				json.put("message", "Playing eleven does not meet the needed conditions");
			return ResponseEntity.ok().body(json);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(json);
		}
	}

	public ResponseEntity<JSONObject> deleteTeam(String name) {
		Long teamId = teamRepo.findByName(name).get().getId();
		JSONObject json = new JSONObject();
		try {
			teamRepo.deleteById(teamId);
			json.put("message", "Team deleted successfully");
			return ResponseEntity.ok().body(json);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(json);
		}
	}

	@Transactional
	public ResponseEntity<JSONObject> deleteAllPlayers(String name) {
		Long teamId = teamRepo.findByName(name).get().getId();
		JSONObject json = new JSONObject();
		try {
			playerRepo.deleteByBelongsTo(teamRepo.findById(teamId).get());
			json.put("message", "All the players of the team were deleted successfully");
			return ResponseEntity.ok().body(json);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(json);
		}
	}

}
