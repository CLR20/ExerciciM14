package com.whitecollar.application.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "shops")
public class Shops {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	private int capacity;
	
	@OneToMany(mappedBy= "shop", cascade=CascadeType.REMOVE)
	List<Collars> collars;
	
	public Shops() {}
	
	public Shops(String n, int c) {		
		name = n;
		capacity = c;
	}
	
	public Shops(Shops s) {
		name = s.getName();
		capacity = s.getCapacity();
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getCapacity() {
		return capacity;
	}
	
	public Collars getCollar(Collars collar) {
		return collar;
	}
	
	@Override
	public String toString() {
		return "\"id\": " + id + ", \"name\": " + name + ", \"capacity\": " + capacity;
	}
}
