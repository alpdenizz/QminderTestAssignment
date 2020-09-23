package com.qminder.assignment.dto;

/**
 * 
 * Wrapper class to handle the response from Foursquare Explore API
 *
 */
public class FoursquareExplore {
	
	private FoursquareExploreResponse response;
	
	public FoursquareExploreResponse getResponse() {
		return response;
	}
	public void setResponse(FoursquareExploreResponse response) {
		this.response = response;
	}

}
