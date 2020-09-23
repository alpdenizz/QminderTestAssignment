package com.qminder.assignment.dto;

/**
 * 
 * Wrapper class to handle the response from Foursquare Explore API
 *
 */
public class BurgerJointDTO {
	private String id;
	private String name;
	private Location location;
	
	public void setId(String id) {
		this.id = id;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public Location getLocation() {
		return location;
	}
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("BurgerJointDTO {").append("id=").append(this.id).append(", ")
		.append("name=").append(this.name).append(", ")
		.append("location=").append(this.location.getAddress()).append("}");
		return sb.toString();
	}
}
