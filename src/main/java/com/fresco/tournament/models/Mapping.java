package com.fresco.tournament.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Mapping {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	@Size(min = 4)
	@NotBlank(message = "Name is mandatory")
	private String name;
	@NotBlank(message = "Category is mandatory")
	private String category;

	public Mapping() {
		super();
	}

	public Mapping(String name, String category) {
		super();
		this.name = name;
		this.category = category;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Mapping [id=" + id + ", name=" + name + ", category=" + category + "]";
	}
}
