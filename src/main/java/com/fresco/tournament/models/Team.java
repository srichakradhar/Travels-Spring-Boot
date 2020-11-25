package com.fresco.tournament.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Team {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Size(min = 3)
	@Pattern(regexp = "[A-Za-z ]+")
	@NotBlank(message = "Name is mandatory")
	private String name;

	@NotBlank(message = "Password is mandatory")
	private String password;

	@NotBlank(message = "Country is mandatory")
	@Column(unique = true)
	@Size(min = 3)
	@Pattern(regexp = "[A-Za-z ]+")
	private String country;

	@NotBlank(message = "Coach is mandatory")
	@Size(min = 3)
	@Pattern(regexp = "[A-Za-z ]+")
	private String coach;

	@OneToMany(mappedBy = "belongsTo")
	private List<Player> players;

	public Team() {
		super();
	}

	public Team(String name, String password, String country, String coach, List<Player> players) {
		super();
		this.name = name;
		this.password = password;
		this.country = country;
		this.coach = coach;
		this.players = players;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCoach() {
		return coach;
	}

	public void setCoach(String coach) {
		this.coach = coach;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	@Override
	public String toString() {
		return "Team [id=" + id + ", name=" + name + ", password=" + password + ", country=" + country + ", coach="
				+ coach + ", players=" + players + "]";
	}

}
