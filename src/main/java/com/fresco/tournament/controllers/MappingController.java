package com.fresco.tournament.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fresco.tournament.models.Mapping;
import com.fresco.tournament.service.MappingService;

@RestController
@CrossOrigin
public class MappingController {
	@Autowired
	MappingService mappingService;

	@GetMapping("/mapping/view")
	public ResponseEntity<JSONArray> viewMappings() {
		return mappingService.viewMappings();
	}

	@PutMapping("/mapping/update")
	public ResponseEntity<JSONObject> updateMapping(@RequestBody Mapping mapping) {
		System.out.println(mapping);
		return mappingService.updateMapping(mapping);
	}
}
