package com.qminder.assignment.dto;

/**
 * 
 * Wrapper class to handle response from Foursquare Explore API
 *
 */
public class Location {
	private String address;
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
}
