package com.qminder.assignment.dto;

import java.util.List;

/**
 * 
 * Wrapper class to handle the response from Foursquare Explore API
 *
 */
public class FoursquareExploreResponse {
	
	private int totalResults;
	private List<FoursquareExploreResponseGroup> groups;
	
	public void setGroups(List<FoursquareExploreResponseGroup> groups) {
		this.groups = groups;
	}
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}
	public List<FoursquareExploreResponseGroup> getGroups() {
		return groups;
	}
	public int getTotalResults() {
		return totalResults;
	}
}
