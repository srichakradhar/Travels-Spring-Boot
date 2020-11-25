package com.fresco.tournament.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Player {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Size(min = 4)
	@NotBlank(message = "Name is mandatory")
	private String name;

	@NotBlank(message = "Age is mandatory")
	private Integer age;

	@NotBlank(message = "Number of matches is mandatory")
	private Integer noOfMatches;

	@NotBlank(message = "Goals scored is mandatory")
	private Integer goalsScored;

	@NotBlank(message = "Type is mandatory")
	private String type;

	@NotBlank(message = "Type is mandatory")
	private Boolean inEleven;

	@ManyToOne
	@JoinColumn(name = "players")
	private Team belongsTo;

	public Player() {
		super();
	}

	public Player(String name, Integer age, Integer noOfMatches, Integer goalsScored, String type, Boolean inEleven,
			Team belongsTo) {
		super();
		this.name = name;
		this.age = age;
		this.noOfMatches = noOfMatches;
		this.goalsScored = goalsScored;
		this.type = type;
		this.inEleven = inEleven;
		this.belongsTo = belongsTo;
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getNoOfMatches() {
		return noOfMatches;
	}

	public void setNoOfMatches(Integer noOfMatches) {
		this.noOfMatches = noOfMatches;
	}

	public Integer getGoalsScored() {
		return goalsScored;
	}

	public void setGoalsScored(Integer goalsScored) {
		this.goalsScored = goalsScored;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Team getBelongsTo() {
		return belongsTo;
	}

	public void setBelongsTo(Team belongsTo) {
		this.belongsTo = belongsTo;
	}

	public Boolean getInEleven() {
		return inEleven;
	}

	public void setInEleven(Boolean inEleven) {
		this.inEleven = inEleven;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", age=" + age + ", noOfMatches=" + noOfMatches
				+ ", goalsScored=" + goalsScored + ", type=" + type + ", inEleven=" + inEleven + ", belongsTo="
				+ belongsTo + "]";
	}

}
