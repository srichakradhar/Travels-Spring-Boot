package com.fresco.tournament.controllers;

import java.security.Principal;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fresco.tournament.models.Team;
import com.fresco.tournament.service.AdminService;
import com.fresco.tournament.service.TeamsService;

@RestController
@CrossOrigin
public class TeamsController {
	@Autowired
	TeamsService teamsService;
	@Autowired
	AdminService adminService;

	@PostMapping("/teams/registration")
	public ResponseEntity<JSONObject> registerTeam(@RequestBody Team team) {
		System.out.println(team);
		return teamsService.registerTeam(team);
	}

	@PostMapping("/teams/login")
	public ResponseEntity<JSONObject> login(@RequestBody Team team) {
		return teamsService.loginTeam(team);
	}

	@PutMapping("/teams/update")
	public ResponseEntity<JSONObject> updateTeam(Principal principal, @RequestBody Team team) {
		return teamsService.updateTeam(principal.getName(), team);
	}

	@GetMapping("/teams/view")
	public ResponseEntity<JSONArray> viewTeam() {
		return teamsService.viewTeam();
	}

	@DeleteMapping("/teams/delete")
	public ResponseEntity<JSONObject> deleteTeam(Principal principal) {
		return teamsService.deleteTeam(principal.getName());
	}

	@GetMapping("/teams/eleven")
	public ResponseEntity<JSONObject> elevenTeam(Principal principal) {
		return teamsService.elevenTeam(principal.getName());
	}
}
