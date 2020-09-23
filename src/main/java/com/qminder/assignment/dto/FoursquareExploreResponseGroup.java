package com.qminder.assignment.dto;

import java.util.List;

/**
 * 
 * Wrapper class to handle the response from Foursquare Explore API
 *
 */
public class FoursquareExploreResponseGroup {
	private List<Venue> items;
	public void setItems(List<Venue> items) {
		this.items = items;
	}
	public List<Venue> getItems() {
		return items;
	}
}
