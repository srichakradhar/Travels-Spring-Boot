package com.fresco.tournament;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

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

import com.fresco.tournament.config.JwtUtil;

@TestMethodOrder(Alphanumeric.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
class NewFeatureTests {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	JwtUtil jwtUtil;
	static String viewerToken, viewerName, viewerPassword;

	String generateString() {
		Random random = new Random();
		StringBuilder randStr = new StringBuilder();
		while (randStr.length() < 14)
			randStr.append("ABCDEFGHIJKLMNOPQRST1234567890".charAt(random.nextInt(26)));
		return randStr.toString();
	}

	@Test
	void test01_loginViewerWithoutRegister() {
		try {
			viewerName = generateString();
			viewerPassword = generateString();
			JSONObject json = new JSONObject();
			json.put("name", viewerName);
			json.put("password", viewerPassword);
			mockMvc.perform(post("/viewer-login").contentType(MediaType.APPLICATION_JSON).content(json.toJSONString()))
					.andExpect(status().is4xxClientError())
					.andExpect(jsonPath("$.message").value("Username and Password is wrong"));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test02_registerViewer() {
		try {
			JSONObject json = new JSONObject();
			json.put("name", viewerName);
			json.put("password", viewerPassword);
			mockMvc.perform(
					post("/viewer-register").contentType(MediaType.APPLICATION_JSON).content(json.toJSONString()))
					.andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.id").value(1))
					.andExpect(jsonPath("$.name").value(viewerName));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test03_loginViewer() {
		try {
			JSONObject json = new JSONObject();
			json.put("name", viewerName);
			json.put("password", viewerPassword);
			MvcResult result = mockMvc
					.perform(post("/viewer-login").contentType(MediaType.APPLICATION_JSON).content(json.toJSONString()))
					.andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Login successful")).andReturn();
			viewerToken = ((JSONObject) new JSONParser().parse(result.getResponse().getContentAsString())).get("token")
					.toString();
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test04_getMappings() {
		try {
			mockMvc.perform(get("/viewer-matches").header("JWT", viewerToken)).andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(7)));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test05_getMappingsWithInvalidToken() {
		try {
			String dummyToken = jwtUtil.generateToken(generateString());
			mockMvc.perform(get("/viewer-matches").header("JWT", dummyToken)).andExpect(status().is4xxClientError())
					.andExpect(jsonPath("$.error").value("Please authenticate"));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test06_makeBooking() {
		try {
			mockMvc.perform(post("/viewer-make-booking/1").header("JWT", viewerToken)).andExpect(status().isOk())
					.andExpect(jsonPath("$.bookingId").value(1)).andExpect(jsonPath("$.viewerId").value(1))
					.andExpect(jsonPath("$.matchId").value(1));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	void test07_getBookings() {
		try {
			mockMvc.perform(get("/viewer-bookings").header("JWT", viewerToken)).andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].bookingId").value(1))
					.andExpect(jsonPath("$[0].matchId").value(1));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	//// ------------Ignore below test case----------------////
	@Test
	void test08_checkingPreviousTests() {
		try {
			String content = new String(
					Files.readAllBytes(Paths.get("target/surefire-reports/com.fresco.tournament.CompletedTests.txt")));
			assert (content.contains("Tests run: 15, Failures: 0, Errors: 0, Skipped: 0"));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}
	//////////////////////////////////////////////////////////

}
