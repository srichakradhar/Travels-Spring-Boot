package com.fresco.tournament.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fresco.tournament.models.Admin;
import com.fresco.tournament.models.Player;
import com.fresco.tournament.models.Team;
import com.fresco.tournament.service.AdminService;

@RestController
@CrossOrigin
public class AdminController {
	@Autowired
	AdminService adminService;

	@PostMapping("/login")
	public ResponseEntity<JSONObject> login(@RequestBody Admin admin) {
		return adminService.loginAdmin(admin);
	}

	@DeleteMapping("/admin/teams/delete/{id}")
	public ResponseEntity<JSONObject> deleteTeam(@PathVariable Long id) {
		return adminService.deleteTeam(id);
	}

	@DeleteMapping("/admin/players/delete/{id}")
	public ResponseEntity<JSONObject> deletePlayer(@PathVariable Long id) {
		return adminService.deletePlayer(id);
	}

	@GetMapping("/admin/teams/view")
	public ResponseEntity<JSONArray> viewTeams() {
		return adminService.viewTeams();
	}

	@PutMapping("/admin/teams/update/{id}")
	public ResponseEntity<JSONObject> updateTeam(@PathVariable Long id, @RequestBody Team team) {
		return adminService.updateTeam(id, team);
	}

	@GetMapping("/admin/teams/eleven/{id}")
	public ResponseEntity<JSONObject> elevenTeam(@PathVariable Long id) {
		return adminService.elevenTeam(id);
	}

	@GetMapping("/admin/players/view/{id}")
	public ResponseEntity<JSONObject> viewPlayer(@PathVariable Long id) {
		return adminService.viewPlayer(id);
	}

	@PutMapping("/admin/players/update/{id}")
	public ResponseEntity<JSONObject> updatePlayer(@PathVariable Long id, @RequestBody Player player) {
		System.out.println(player);
		return adminService.updatePlayer(id, player);
	}

	@PostMapping("/admin/players/register/{id}")
	public ResponseEntity<JSONObject> registerPlayer(@PathVariable Long id, @RequestBody Player player) {
		System.out.println(player);
		return adminService.registerPlayer(id, player);
	}

	@DeleteMapping("/admin/players/deleteAll/{id}")
	public ResponseEntity<JSONObject> deleteAll(@PathVariable Long id) {
		return adminService.deleteAllPlayers(id);
	}
}
