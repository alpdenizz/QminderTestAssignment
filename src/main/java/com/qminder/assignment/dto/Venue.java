package com.qminder.assignment.dto;

/**
 * Wrapper class to handle response from Foursquare Explore API
 *
 */
public class Venue {
	private BurgerJointDTO venue;
	public void setVenue(BurgerJointDTO venue) {
		this.venue = venue;
	}
	public BurgerJointDTO getVenue() {
		return venue;
	}
}
