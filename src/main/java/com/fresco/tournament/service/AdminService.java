
package com.fresco.tournament.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.fresco.tournament.config.JwtUtil;
import com.fresco.tournament.models.Admin;
import com.fresco.tournament.models.Player;
import com.fresco.tournament.models.Team;
import com.fresco.tournament.repo.AdminRepository;
import com.fresco.tournament.repo.PlayerRepository;
import com.fresco.tournament.repo.TeamRepository;

@Service
public class AdminService {
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	TeamRepository teamRepo;
	@Autowired
	PlayerRepository playerRepo;
	@Autowired
	AdminRepository adminRepo;

	public ResponseEntity<JSONObject> loginAdmin(Admin admin) {
		JSONObject json = new JSONObject();
		try {
			admin.setId(adminRepo.findByName(admin.getName()).get().getId());
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(admin.getName(), admin.getPassword()));
			String token = jwtUtil.generateToken(admin.getName());
			json.put("message", "Login successful");
			json.put("token", token);
			return ResponseEntity.ok().body(json);
		} catch (BadCredentialsException e) {
			json.put("message", "Username and Password is wrong");
			return ResponseEntity.badRequest().body(json);
		}
	}

	public ResponseEntity<JSONObject> deleteTeam(Long teamId) {
		JSONObject json = new JSONObject();
		try {
			teamRepo.deleteById(teamId);
			json.put("message", "Team deleted successfully");
			return ResponseEntity.ok().body(json);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(json);
		}
	}

	public ResponseEntity<JSONObject> deletePlayer(Long playerId) {
		JSONObject json = new JSONObject();
		try {
			playerRepo.deleteById(playerId);
			json.put("message", "Player deleted Successfully");
			return ResponseEntity.ok().body(json);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(json);
		}
	}

	public ResponseEntity<JSONArray> viewTeams() {
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

	public ResponseEntity<JSONObject> updateTeam(Long id, Team team) {
		JSONObject json = new JSONObject();
		try {
			Team t = teamRepo.findById(id).get();
			t.setName(team.getName());
			t.setCountry(team.getCountry());
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

	public ResponseEntity<JSONObject> elevenTeam(Long id) {
		JSONObject json = new JSONObject();
		try {

			JSONArray arr = new JSONArray();
			List<Player> players = teamRepo.findById(id).get().getPlayers();
			List<String> types = players.stream().filter(player -> player.getInEleven() == true).map(Player::getType)
					.collect(Collectors.toList());
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
			if (types.size() == 11 && Collections.frequency(types, "Forwarder") >= 1
					&& Collections.frequency(types, "Defender") >= 1 && Collections.frequency(types, "Mid-Fielder") >= 1
					&& Collections.frequency(types, "Goal Keeper") == 1) {
				json.put("message", "");
			} else
				json.put("message", "Playing eleven does not meet the needed conditions");
			return ResponseEntity.ok().body(json);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(json);
		}
	}

	public ResponseEntity<JSONObject> viewPlayer(Long id) {
		JSONObject j = new JSONObject();
		try {
			Player p = playerRepo.findById(id).get();
			j.put("Id", p.getId());
			j.put("Name", p.getName());
			j.put("Age", p.getAge());
			j.put("Type", p.getType());
			j.put("inEleven", p.getInEleven());
			j.put("NumberOfMatches", p.getNoOfMatches());
			j.put("GoalsScored", p.getGoalsScored());
			j.put("BelongsTo", p.getBelongsTo().getName());
			return ResponseEntity.ok().body(j);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(j);
		}
	}

	public ResponseEntity<JSONObject> updatePlayer(Long id, Player player) {
		JSONObject json = new JSONObject();
		try {
			Player p = playerRepo.findById(id).get();
			p.setName(player.getName());
			p.setAge(player.getAge());
			p.setGoalsScored(player.getGoalsScored());
			p.setNoOfMatches(player.getNoOfMatches());
			p.setType(player.getType());
			p.setInEleven(player.getInEleven());
			playerRepo.save(p);
			json.put("Id", p.getId());
			json.put("Name", p.getName());
			json.put("Age", p.getAge());
			json.put("Belongs to", p.getBelongsTo().getName());
			json.put("Goals scored", p.getGoalsScored());
			json.put("Number of matches", p.getNoOfMatches());
			json.put("Type", p.getType());
			json.put("inEleven", p.getInEleven());
			return ResponseEntity.ok().body(json);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(json);
		}
	}

	public ResponseEntity<JSONObject> registerPlayer(Long id, Player player) {
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

	@Transactional
	public ResponseEntity<JSONObject> deleteAllPlayers(Long id) {
		JSONObject json = new JSONObject();
		try {
			playerRepo.deleteByBelongsTo(teamRepo.findById(id).get());
			json.put("message", "All the players of the team were deleted successfully");
			return ResponseEntity.ok().body(json);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(json);
		}
	}

}
