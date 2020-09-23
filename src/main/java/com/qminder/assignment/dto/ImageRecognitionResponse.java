package com.qminder.assignment.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper class to handle the response from Image Recognition API
 *
 */
public class ImageRecognitionResponse {
	
	private static final Logger logger = LoggerFactory.getLogger(ImageRecognitionResponse.class);
	
	private String urlWithBurger;
	public void setUrlWithBurger(String urlWithBurger) {
		this.urlWithBurger = urlWithBurger;
	}
	public String getUrlWithBurger() {
		logger.info("[+] Photo url: "+urlWithBurger);
		return urlWithBurger;
	}
}
