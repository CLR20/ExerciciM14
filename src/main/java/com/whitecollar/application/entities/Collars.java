package com.whitecollar.application.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "collars")
public class Collars {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	@Column(nullable=true)
	private String creator;
	private Double price;
	private String entryDate;
	@ManyToOne
	private Shops shop;
	
	public Collars() {}
	
	public Collars(String n, String c, double p, String d, Shops s) {
		name = n;
		creator = c;
		price = p;
		entryDate = d;		
		shop = s;		
	}
	
	public void setId(long i) {
		id = i;
	}
	public long getId() {
		return id;
	}
	
	public void setName(String n) {
		name = n;
	}
	public String getName() {
		return name;
	}
	
	public void setCreator(String c) {
		creator = c;
	}
	public String getCreator() {
		return creator;
	}
	
	public void setPrice(double p) {
		price = p;
	}
	public Double getPrice() {
		return price;
	}
	
	public void setEntryDate(String d) {
		entryDate = d;
	}
	public String getEntryDate() {
		return entryDate;
	}
	
	public void setShop(Shops s) {
		this.shop = s;
	}	
	public Shops getShop() {
		return shop;
	}
	
	public long getShopId() {
		long shopLong = shop.getId();
		return shopLong;
	}
	
	@Override
	public String toString() {
		return "\"id\": " + id + ", \"name\": " + name + ", \"creator\": " + creator
				+ ", \"price\": " + price + ", \"entry date\": " + entryDate 
				+ ", \"shop\": " + shop.getName() + "\n";
	}

}
