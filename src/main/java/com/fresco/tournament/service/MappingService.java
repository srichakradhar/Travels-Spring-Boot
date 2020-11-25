package com.fresco.tournament.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fresco.tournament.models.Mapping;
import com.fresco.tournament.repo.MappingRepository;

@Service
public class MappingService {
	@Autowired
	MappingRepository mappingRepo;

	public ResponseEntity<JSONArray> viewMappings() {
		JSONArray arr = new JSONArray();
		try {
			for (Mapping mapping : mappingRepo.findAll()) {
				JSONObject json = new JSONObject();
				json.put("Id", mapping.getId());
				json.put("Name", mapping.getName());
				json.put("Category", mapping.getCategory());
				arr.add(json);
			}
			return ResponseEntity.ok().body(arr);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(arr);
		}
	}

	public ResponseEntity<JSONObject> updateMapping(Mapping mapping) {
		JSONObject json  = new JSONObject();
		if(mappingRepo.existsById(mapping.getId())) {
			mapping = mappingRepo.save(mapping);
			json.put("Id", mapping.getId());
			json.put("Name", mapping.getName());
			json.put("Category", mapping.getCategory());
			return ResponseEntity.ok().body(json);
		}
		json.put("message", "Mapping id not found");
		return ResponseEntity.status(404).body(json);
	}

}
