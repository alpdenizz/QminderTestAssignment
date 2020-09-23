package com.qminder.assignment.model;

/**
 * Model class for a burger joint. <br>
 * It includes: name, address and imageURL for the latest burger.
 *
 */
public class BurgerJoint implements Comparable<BurgerJoint> {
	
	private String name;
	private String address;
	private String imageUrl;
	
	public BurgerJoint() {}
	
	public void setAddress(String address) {
		this.address = address;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public String getName() {
		return name;
	}
	
	public BurgerJoint(String name, String address, String imageUrl) {
		this.address = address;
		this.name = name;
		this.imageUrl = imageUrl;
	}
	
	/**
	 * This is implemented to show the joints with picture upper.
	 * If no picture, then the ones with address to upper.
	 */
	@Override
	public int compareTo(BurgerJoint o) {
		// TODO Auto-generated method stub
		int byUrl = o.getImageUrl().compareTo(this.imageUrl);
		if(byUrl == 0) {
			if(this.address == null) return 1;
			if(o.getAddress() == null) return -1;
			return o.getAddress().compareTo(this.address);
		}
		return byUrl;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder("BurgerJoint{");
		sb.append("name: ").append(this.name).append(", ")
		.append("address: ").append(this.address).append(", ")
		.append("image: ").append(this.imageUrl).append("}");
		return sb.toString();
	}
	
	

}
