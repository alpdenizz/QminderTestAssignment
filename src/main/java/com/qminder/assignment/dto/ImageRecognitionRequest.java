package com.qminder.assignment.dto;

import java.util.List;

public class ImageRecognitionRequest {
	
	private List<String> urls;
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
	public List<String> getUrls() {
		return urls;
	}

}
