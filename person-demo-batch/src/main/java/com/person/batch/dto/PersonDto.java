package com.person.batch.dto;

public class PersonDto {

	private long id;

	private String firstName;

	private String lastName;

	// private Gender gender;
	private char gender;

	private int age;

	private String fullName;

	public PersonDto(long id, String firstName, String lastName, char gender, int age) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.age = age;
	}

	public PersonDto() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the gender
	 */
	public char getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(char gender) {
		this.gender = gender;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName = this.firstName + " " + this.lastName;
	}

	/**
	 * @param fullName the fullName to set
	 *//*
		 * public void setFullName(String fullName) { this.fullName = fullName; }
		 */

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PersonDto [id=");
		builder.append(id);
		builder.append(", firstName=");
		builder.append(firstName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", age=");
		builder.append(age);
		builder.append(", fullName=");
		builder.append(fullName);
		builder.append("]");
		return builder.toString();
	}
}
