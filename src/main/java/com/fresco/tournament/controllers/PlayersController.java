package com.fresco.tournament.controllers;

import java.security.Principal;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fresco.tournament.models.Player;
import com.fresco.tournament.service.AdminService;
import com.fresco.tournament.service.TeamsService;

@RestController
@CrossOrigin
public class PlayersController {
	@Autowired
	AdminService adminService;
	@Autowired
	TeamsService teamsService;

	@PostMapping("/players/register")
	public ResponseEntity<JSONObject> registerPlayer(Principal principal, @RequestBody Player player) {
		return teamsService.registerPlayer(principal.getName(), player);
	}

	@PutMapping("/players/update/{id}")
	public ResponseEntity<JSONObject> updatePlayer(@PathVariable Long id, @RequestBody Player player) {
		System.out.println(player);
		return adminService.updatePlayer(id, player);
	}

	@GetMapping("/players/view")
	public ResponseEntity<JSONArray> viewPlayer(Principal principal) {
		System.out.println(principal);
		return teamsService.allPlayers(principal.getName());//getName
	}

	@DeleteMapping("/players/delete/{id}")
	public ResponseEntity<JSONObject> deletePlayer(@PathVariable Long id) {
		return adminService.deletePlayer(id);
	}

	@DeleteMapping("/players/deleteAll")
	public ResponseEntity<JSONObject> deleteAll(Principal principal) {
		return teamsService.deleteAllPlayers(principal.getName());
	}

}
