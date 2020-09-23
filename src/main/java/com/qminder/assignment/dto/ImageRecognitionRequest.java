package com.qminder.assignment.dto;

import java.util.List;

/**
 * 
 * Wrapper class to handle the request to Image Recognition API
 *
 */
public class ImageRecognitionRequest {
	
	private List<String> urls;
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
	public List<String> getUrls() {
		return urls;
	}

}
