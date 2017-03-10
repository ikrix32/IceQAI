package org.neo4j.northwind.model;

import java.util.HashSet;
import java.util.Set;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * Entity class to represent a Customer entity within the Northwind example dataset.
 * 
 * @author Axel Morgner
 */
@NodeEntity
public class Customer {
	
	@GraphId
	public Long id;
	
	public String contactTitle;
	public String contactName;
	public String address;
	public String city;
	public String postalCode;
	public String country;
	public String region;
	public String phone;
	public String fax;
	public String customerID;
	
	@Relationship(type = "PURCHASED", direction = Relationship.OUTGOING)
	public Set<Order> orders;

	public Customer() {
		orders = new HashSet<>();
	}

	public Customer(Long id, String contactTitle, String contactName, String address, String city, String postalCode, String country, String region, String phone, String fax, String customerID) {
		this.id = id;
		this.contactTitle = contactTitle;
		this.contactName = contactName;
		this.address = address;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
		this.region = region;
		this.phone = phone;
		this.fax = fax;
		this.customerID = customerID;
	}
	
}
