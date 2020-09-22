package com.qminder.assignment.dto;

public class ImageRecognitionResponse {
	private String urlWithBurger;
	public void setUrlWithBurger(String urlWithBurger) {
		this.urlWithBurger = urlWithBurger;
	}
	public String getUrlWithBurger() {
		System.out.println("[+] Photo url: "+urlWithBurger);
		return urlWithBurger;
	}
}
