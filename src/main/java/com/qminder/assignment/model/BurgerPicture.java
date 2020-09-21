package com.qminder.assignment.model;

import java.time.LocalDate;

public class BurgerPicture implements Comparable<BurgerPicture>{
	private String link;
	private LocalDate posted;
	public void setLink(String link) {
		this.link = link;
	}
	public void setPosted(LocalDate posted) {
		this.posted = posted;
	}
	public String getLink() {
		return link;
	}
	public LocalDate getPosted() {
		return posted;
	}
	@Override
	public int compareTo(BurgerPicture o) {
		// TODO Auto-generated method stub
		return o.getPosted().compareTo(this.posted);
	}
	
}
