package com.example.demo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private Integer age;

	public User(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}
}
