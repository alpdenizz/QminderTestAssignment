package com.qminder.assignment.model;

import java.time.LocalDate;

/**
 * Model class for a burger picture. <br>
 * It includes: link and posted date.
 *
 */
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
	
	/**
	 * This is implemented to sort the pictures where the first element is the most recent posted,
	 * whereas the last element is the oldest picture.
	 */
	@Override
	public int compareTo(BurgerPicture o) {
		// TODO Auto-generated method stub
		return o.getPosted().compareTo(this.posted);
	}
	
}
