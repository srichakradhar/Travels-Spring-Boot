package com.fresco.tournament;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@TestMethodOrder(Alphanumeric.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
class CompletedTests {
	@Autowired
	MockMvc mockMvc;
	static String adminToken, teamToken;
	static Map<String, List<JSONObject>> randData;

	String generateString(boolean flag) {
		Random random = new Random();
		String candidateChars = flag ? "1234567890" : "ABCDEFGHIJKLMNOPQRST1234567890";
		StringBuilder randStr = new StringBuilder();
		while (randStr.length() < (flag ? 2 : 10))
			randStr.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
		return randStr.toString();
	}

	void generateRandomData() {
		randData = new HashMap<String, List<JSONObject>>();
		randData.put("teams", new ArrayList<JSONObject>());
		for (int i = 0; i < 10; i++) {
			JSONObject json = new JSONObject();
			json.put("name", generateString(false));
			json.put("password", generateString(false));
			json.put("country", generateString(false));
			json.put("coach", generateString(false));
			json.put("players", new JSONArray());
			for (int j = 0; j < new Random().nextInt(6) + 5; j++) {
				JSONObject obj = new JSONObject();
				obj.put("name", generateString(false));
				obj.put("age", generateString(true));
				obj.put("noOfMatches", generateString(true));
				obj.put("goalsScored", generateString(true));
				obj.put("type", generateString(false));
				((JSONArray) json.get("players")).add(obj);
			}
			randData.get("teams").add(json);
		}
	}

	@Test
	void test01_loginAdmin() {
		generateRandomData();
		try {
			JSONObject json = new JSONObject();
			json.put("name", "Admin");
			json.put("password", "Fresco@333");
			MvcResult result = mockMvc
					.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json.toJSONString()))
					.andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Login successful")).andReturn();
			json = (JSONObject) new JSONParser().parse(result.getResponse().getContentAsString());
			adminToken = json.get("token").toString();
			assert (!adminToken.contentEquals(""));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test02_loginAdminInvalidCredentials() {
		try {
			JSONObject json = new JSONObject();
			json.put("name", "Admin");
			json.put("password", "admin");
			mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json.toJSONString()))
					.andExpect(jsonPath("$.message").value("Username and Password is wrong"))
					.andExpect(jsonPath("$.token").doesNotExist()).andExpect(status().is4xxClientError());
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test03_loginTeam() {
		try {
			JSONObject json = new JSONObject();
			json.put("name", "Fast Footers");
			json.put("password", "User1@333");
			MvcResult result = mockMvc
					.perform(post("/teams/login").contentType(MediaType.APPLICATION_JSON).content(json.toJSONString()))
					.andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Login successful")).andReturn();
			json = (JSONObject) new JSONParser().parse(result.getResponse().getContentAsString());
			teamToken = json.get("token").toString();
			assert (!teamToken.contentEquals(""));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test04_loginTeamInvalidCredentials() {
		try {
			JSONObject json = new JSONObject();
			json.put("name", "Fast Footers");
			json.put("password", "User1@444");
			mockMvc.perform(post("/teams/login").contentType(MediaType.APPLICATION_JSON).content(json.toJSONString()))
					.andExpect(jsonPath("$.message").value("Username and Password is wrong"))
					.andExpect(jsonPath("$.token").doesNotExist()).andExpect(status().is4xxClientError());
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test05_registerTeam() {
		try {
			for (int i = 0; i < 7; i++) {
				JSONObject json = new JSONObject();
				json.put("name", randData.get("teams").get(i).get("name").toString());
				json.put("password", randData.get("teams").get(i).get("password").toString());
				json.put("country", randData.get("teams").get(i).get("country").toString());
				json.put("coach", randData.get("teams").get(i).get("coach").toString());
				mockMvc.perform(post("/teams/registration").contentType(MediaType.APPLICATION_JSON)
						.content(json.toJSONString())).andExpect(jsonPath("$.Id").value(i + 3))
						.andExpect(status().isCreated())
						.andExpect(jsonPath("$.Country").value(randData.get("teams").get(i).get("country").toString()))
						.andExpect(jsonPath("$.Coach").value(randData.get("teams").get(i).get("coach").toString()))
						.andExpect(jsonPath("$.Name").value(randData.get("teams").get(i).get("name").toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test06_deleteTeam() {
		try {
			mockMvc.perform(delete("/admin/teams/delete/3")).andExpect(status().is4xxClientError())
					.andExpect(jsonPath("$.error").value("Please authenticate"));
			mockMvc.perform(delete("/admin/teams/delete/3").header("JWT", adminToken)).andExpect(status().isOk())
					.andExpect(jsonPath("$.message").value("Team deleted successfully"));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test07_viewTeams() {
		try {
			MvcResult result = mockMvc.perform(get("/admin/teams/view").header("JWT", adminToken))
					.andExpect(status().isOk()).andReturn();
			JSONArray arr = (JSONArray) new JSONParser().parse(result.getResponse().getContentAsString());
			for (int i = 2; i < arr.size(); i++) {
				assertEquals(((JSONObject) arr.get(i)).get("Id").toString(), String.valueOf(i + 2));
				assertEquals(((JSONObject) arr.get(i)).get("Name"), randData.get("teams").get(i - 1).get("name"));
				assertEquals(((JSONObject) arr.get(i)).get("Country"), randData.get("teams").get(i - 1).get("country"));
				assertEquals(((JSONObject) arr.get(i)).get("Coach"), randData.get("teams").get(i - 1).get("coach"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test08_registerPlayers() {
		try {
			for (int i = 1; i < 7; i++) {
				for (Object player : (JSONArray) randData.get("teams").get(i).get("players")) {
					JSONObject json = new JSONObject();
					json.put("name", ((JSONObject) player).get("name").toString());
					json.put("age", ((JSONObject) player).get("age").toString());
					json.put("type", ((JSONObject) player).get("type").toString());
					json.put("noOfMatches", ((JSONObject) player).get("noOfMatches").toString());
					json.put("goalsScored", ((JSONObject) player).get("goalsScored").toString());
					mockMvc.perform(post("/admin/players/register/" + (i + 3)).header("JWT", adminToken)
							.contentType(MediaType.APPLICATION_JSON).content(json.toJSONString()))
							.andExpect(status().isCreated())
							.andExpect(jsonPath("$.['Belongs to']").value(randData.get("teams").get(i).get("name")))
							.andExpect(jsonPath("$.Name").value(((JSONObject) player).get("name")))
							.andExpect(jsonPath("$.['Number of matches']")
									.value(Integer.parseInt(((JSONObject) player).get("noOfMatches").toString())))
							.andExpect(jsonPath("$.Type").value(((JSONObject) player).get("type")))
							.andExpect(jsonPath("$.['Goals scored']")
									.value(Integer.parseInt(((JSONObject) player).get("goalsScored").toString())));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test09_viewTeamsFromTeam() {
		try {
			MvcResult result = mockMvc.perform(get("/teams/view").header("JWT", teamToken)).andExpect(status().isOk())
					.andReturn();
			String res = result.getResponse().getContentAsString();
			for (int i = 1; i < randData.size(); i++) {
				assert (res.contains(randData.get("teams").get(i).get("name").toString()));
				assert (res.contains(randData.get("teams").get(i).get("country").toString()));
				assert (res.contains(randData.get("teams").get(i).get("coach").toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test10_elevenTeam() {
		try {
			MvcResult res = mockMvc.perform(get("/teams/eleven").header("JWT", teamToken)).andExpect(status().isOk())
					.andReturn();
			String result = res.getResponse().getContentAsString();
			assert (result.contains("Forwarder"));
			assert (result.contains("Fast Footers"));
			assert (result.contains("Jeje"));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test11_updateTeam() {
		try {
			JSONObject json = new JSONObject();
			json.put("name", randData.get("teams").get(9).get("name").toString());
			json.put("country", randData.get("teams").get(9).get("country").toString());
			json.put("coach", randData.get("teams").get(9).get("coach").toString());
			mockMvc.perform(put("/teams/update").header("JWT", teamToken).contentType(MediaType.APPLICATION_JSON)
					.content(json.toJSONString())).andExpect(status().isOk())
					.andExpect(jsonPath("$.Name", is("Fast Footers")))
					.andExpect(jsonPath("$.Coach", is(randData.get("teams").get(9).get("coach"))));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test12_updateAndViewPlayer() {
		try {
			JSONObject json = (JSONObject) ((JSONArray) randData.get("teams").get(9).get("players")).get(1);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("name", json.get("name").toString());
			jsonObj.put("age", json.get("age").toString());
			jsonObj.put("type", json.get("type").toString());
			jsonObj.put("noOfMatches", json.get("noOfMatches").toString());
			jsonObj.put("goalsScored", json.get("goalsScored").toString());
			mockMvc.perform(put("/admin/players/update/3").header("JWT", adminToken)
					.contentType(MediaType.APPLICATION_JSON).content(jsonObj.toJSONString())).andExpect(status().isOk())
					.andExpect(jsonPath("$.Name", is(json.get("name")))).andExpect(jsonPath("$.Id", is(3)));
			mockMvc.perform(get("/admin/players/view/3").header("JWT", adminToken)).andExpect(status().isOk())
					.andExpect(jsonPath("$.Name", is(json.get("name")))).andExpect(jsonPath("$.Id", is(3)))
					.andExpect(jsonPath("$.Type", is(json.get("type"))));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test13_deletePlayer() {
		try {
			mockMvc.perform(delete("/admin/players/delete/3").header("JWT", adminToken)).andExpect(status().isOk())
					.andExpect(jsonPath("$.message").value("Player deleted Successfully"));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test14_deletePlayer() {
		try {
			MvcResult res = mockMvc.perform(get("/mapping/view").header("JWT", adminToken)).andExpect(status().isOk())
					.andReturn();
			String result = res.getResponse().getContentAsString();
			assert(result.contains("Category"));
			assert(result.contains("Semi-Final 1"));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test15_deleteAllFromTeam() {
		try {
			mockMvc.perform(delete("/admin/players/deleteAll/2").header("JWT", adminToken))
					.andExpect(status().isOk());
			String res = mockMvc.perform(get("/admin/teams/eleven/2").header("JWT", adminToken)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
			assertEquals(0, ((JSONArray)((JSONObject)new JSONParser().parse(res)).get("team11s")).size());
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

}
