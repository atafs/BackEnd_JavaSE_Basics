package main.com.markit.booking.dao;

public class Room {
	
	//attributes
	private Integer number;
	private String type;
	private double price;

	//constructor
	public Room(Integer number, String type, double price) {
		super();
		this.number = number;
		this.type = type;
		this.price = price;
	}
	
	public Room(Integer number) {
		this.number = number;
	}


	//getters and setters
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	//toString
	@Override
	public String toString() {
		return "Room Number: " + this.number + ", Room Type: " + this.type + ", Room Price: " + this.price + "\n";
	}
}
