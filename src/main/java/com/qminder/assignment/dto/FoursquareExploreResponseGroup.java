package com.qminder.assignment.dto;

import java.util.List;

public class FoursquareExploreResponseGroup {
	private List<Venue> items;
	public void setItems(List<Venue> items) {
		this.items = items;
	}
	public List<Venue> getItems() {
		return items;
	}
}
